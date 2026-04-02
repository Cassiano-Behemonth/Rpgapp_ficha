package com.example.rpgapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.rpgapp.data.backup.BackupManager
import kotlinx.coroutines.launch
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.HorizontalDivider

enum class AppTheme {
    GREEN_BLACK,
    FANTASIA,
    GOLD_BLACK,
    PURPLE_GRAY,
    BLUE_WHITE,
    WILD_WEST,
    ASSIMILACAO,
    BLACK_RED
}

@Composable
fun ThemeSelectorScreen(
    currentTheme: AppTheme,
    currentMode: GameMode? = null,
    backupManager: BackupManager? = null,
    onThemeSelected: (AppTheme) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showImportConfirm by remember { mutableStateOf(false) }
    var pendingJson by remember { mutableStateOf<String?>(null) }
    var importedMode by remember { mutableStateOf<GameMode?>(null) }

    // Launcher para EXPORTAR
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json"),
        onResult = { uri ->
            uri?.let {
                scope.launch {
                    try {
                        val json = backupManager?.exportModeData(currentMode ?: GameMode.INVESTIGACAO_HORROR)
                        if (json != null) {
                            context.contentResolver.openOutputStream(it)?.use { out ->
                                out.write(json.toByteArray())
                            }
                            Toast.makeText(context, "Backup exportado!", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Erro ao exportar", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    )

    // Launcher para IMPORTAR
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                scope.launch {
                    try {
                        val inputStream = context.contentResolver.openInputStream(it)
                        val json = inputStream?.bufferedReader()?.use { reader -> reader.readText() }
                        if (json != null) {
                            // Valida o modo antes de importar
                            val tempManager = backupManager // Local copy
                            if (tempManager != null) {
                                // Título dinâmico baseado no modo
                                pendingJson = json
                                showImportConfirm = true
                            }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Erro ao ler arquivo", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    )

    if (showImportConfirm && pendingJson != null) {
        AlertDialog(
            onDismissRequest = { showImportConfirm = false },
            title = { Text("Restaurar Ficha?") },
            text = { Text("Isso apagará a ficha atual deste modo e a substituirá pelos dados do arquivo. Deseja continuar?") },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            val mode = backupManager?.importData(pendingJson!!)
                            if (mode != null) {
                                Toast.makeText(context, "Ficha de ${mode.name} restaurada!", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(context, "Arquivo inválido ou erro na importação", Toast.LENGTH_SHORT).show()
                            }
                            showImportConfirm = false
                            pendingJson = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Restaurar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showImportConfirm = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Escolha o Tema",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            "Selecione as cores do aplicativo",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        ThemeCard(
            title = "Verde e Preto",
            subtitle = "Tema padrão",
            primaryColor = Color(0xFF4CAF50),
            backgroundColor = Color.Black,
            isSelected = currentTheme == AppTheme.GREEN_BLACK,
            onClick = { onThemeSelected(AppTheme.GREEN_BLACK) }
        )

        ThemeCard(
            title = "🐉 Fantasia",
            subtitle = "Aventuras épicas",
            primaryColor = Color(0xFFE53935),
            backgroundColor = Color.White,
            isSelected = currentTheme == AppTheme.FANTASIA,
            onClick = { onThemeSelected(AppTheme.FANTASIA) }
        )

        ThemeCard(
            title = "Dourado e Preto",
            subtitle = "Elegante",
            primaryColor = Color(0xFFFFC107),
            backgroundColor = Color.Black,
            isSelected = currentTheme == AppTheme.GOLD_BLACK,
            onClick = { onThemeSelected(AppTheme.GOLD_BLACK) }
        )

        ThemeCard(
            title = "Roxo e Preto",
            subtitle = "Misterioso",
            primaryColor = Color(0xFF9C27B0),
            backgroundColor = Color.Black,
            isSelected = currentTheme == AppTheme.PURPLE_GRAY,
            onClick = { onThemeSelected(AppTheme.PURPLE_GRAY) }
        )

        ThemeCard(
            title = "Azul e Branco",
            subtitle = "Limpo e claro",
            primaryColor = Color(0xFF2196F3),
            backgroundColor = Color.White,
            isSelected = currentTheme == AppTheme.BLUE_WHITE,
            onClick = { onThemeSelected(AppTheme.BLUE_WHITE) }
        )

        ThemeCard(
            title = "🔫 Velho Oeste",
            subtitle = "Laranja queimado e dourado",
            primaryColor = Color(0xFFD2691E),
            backgroundColor = Color(0xFFFFF8DC),
            isSelected = currentTheme == AppTheme.WILD_WEST,
            onClick = { onThemeSelected(AppTheme.WILD_WEST) }
        )

        ThemeCard(
            title = "🧬 Assimilação",
            subtitle = "Verde musgo • Azul veia • Vermelho coagulado",
            primaryColor = Color(0xFF6B8F5E),
            backgroundColor = Color(0xFF080A08),
            isSelected = currentTheme == AppTheme.ASSIMILACAO,
            onClick = { onThemeSelected(AppTheme.ASSIMILACAO) }
        )

        ThemeCard(
            title = "🔴 Preto e Vermelho",
            subtitle = "Sombrio e intenso",
            primaryColor = Color(0xFFD32F2F),
            backgroundColor = Color.Black,
            isSelected = currentTheme == AppTheme.BLACK_RED,
            onClick = { onThemeSelected(AppTheme.BLACK_RED) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        Text(
            "Gestão de Dados",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            "Exportar ou importar fichas individualmente",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // CARD EXPORTAR
        BackupActionCard(
            title = "Exportar Ficha",
            subtitle = "Salvar ${currentMode?.name ?: "atual"} em JSON",
            icon = "📤",
            color = MaterialTheme.colorScheme.primary,
            onClick = {
                val fileName = "Ficha_${currentMode?.name ?: "Backup"}.json"
                exportLauncher.launch(fileName)
            }
        )

        // CARD IMPORTAR
        BackupActionCard(
            title = "Importar Ficha",
            subtitle = "Restaurar dados de um arquivo",
            icon = "📥",
            color = MaterialTheme.colorScheme.secondary,
            onClick = {
                importLauncher.launch(arrayOf("application/json"))
            }
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun BackupActionCard(
    title: String,
    subtitle: String,
    icon: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 24.sp, modifier = Modifier.padding(end = 16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun ThemeCard(
    title: String,
    subtitle: String,
    primaryColor: Color,
    backgroundColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(onClick = onClick)
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 2.dp,
                        color = primaryColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (isSelected) {
                    Text(
                        "✓ Selecionado",
                        style = MaterialTheme.typography.labelSmall,
                        color = primaryColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(backgroundColor)
                    .border(
                        width = 2.dp,
                        color = primaryColor,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "A",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )
            }
        }
    }
}