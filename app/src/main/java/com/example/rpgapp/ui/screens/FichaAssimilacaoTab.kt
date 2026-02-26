package com.example.rpgapp.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rpgapp.ui.theme.AppTextFieldDefaults
import com.example.rpgapp.viewmodel.FichaAssimilacaoViewModel
import kotlinx.coroutines.delay

// ── Condições de saúde ───────────────────────────────────────
data class CondicaoSaude(
    val nivel: Int,
    val nome: String,
    val cor: Color
)

val condicoesSaude = listOf(
    CondicaoSaude(6, "SAUDÁVEL",      Color(0xFF43A047)),
    CondicaoSaude(5, "ESCORIAÇÃO",    Color(0xFF7CB342)),
    CondicaoSaude(4, "LACERAÇÃO",     Color(0xFFFDD835)),
    CondicaoSaude(3, "FERIMENTOS",    Color(0xFFFB8C00)),
    CondicaoSaude(2, "DEBILITAÇÃO",   Color(0xFFE53935)),
    CondicaoSaude(1, "INCAPACITAÇÃO", Color(0xFF6A1B9A)),
)

@Composable
fun FichaAssimilacaoTab(
    viewModel: FichaAssimilacaoViewModel,
    onThemeChange: () -> Unit,
    onModeChange: () -> Unit
) {
    val ficha by viewModel.ficha.collectAsState()
    var nome by remember { mutableStateOf("") }

    LaunchedEffect(ficha) {
        ficha?.let { nome = it.nome }
    }

    LaunchedEffect(nome) {
        delay(1000)
        viewModel.salvarNome(nome)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // ── Nome ─────────────────────────────────────────────
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "▸ INFECTADO",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Nome do personagem") },
                    colors = AppTextFieldDefaults.colors()
                )
            }
        }

        // ── Saúde ─────────────────────────────────────────────
        ficha?.let { f ->
            val nivelAtivo = f.nivelSaudeAtual

            // ── CORREÇÃO: pontos e máximo lidos corretamente por nível ──
            val maxPontosNivel = f.maxNivel6  // todos os níveis têm o mesmo máximo

            val pontosAtivo = when (nivelAtivo) {
                6 -> f.pontosNivel6
                5 -> f.pontosNivel5
                4 -> f.pontosNivel4
                3 -> f.pontosNivel3
                2 -> f.pontosNivel2
                1 -> f.pontosNivel1
                else -> 0
            }

            SaudeProgressivaCard(
                nivelAtivo    = nivelAtivo,
                pontosAtivo   = pontosAtivo,
                maxPontos     = maxPontosNivel,
                totalPontos   = f.pontosNivel6 + f.pontosNivel5 + f.pontosNivel4 +
                        f.pontosNivel3 + f.pontosNivel2 + f.pontosNivel1,
                totalMax      = maxPontosNivel * 6,
                onDano = {
                    if (pontosAtivo > 0) {
                        viewModel.atualizarSaudeNivel(nivelAtivo, pontosAtivo - 1)
                    }
                },
                onCura = {
                    when {
                        nivelAtivo == 0 && nivelAtivo < 6 -> {
                            viewModel.atualizarSaudeNivel(1, 1)
                        }
                        pontosAtivo < maxPontosNivel -> {
                            viewModel.atualizarSaudeNivel(nivelAtivo, pontosAtivo + 1)
                        }
                        nivelAtivo < 6 -> {
                            viewModel.atualizarSaudeNivel(nivelAtivo + 1, 1)
                        }
                    }
                },
                onDanoNivel = {
                    // Remove o nível inteiro: zera pontos do nível atual e desce
                    when {
                        nivelAtivo > 1 -> {
                            // Zera o nível atual e desce para o anterior com 0 pontos
                            viewModel.atualizarSaudeNivel(nivelAtivo, 0)
                        }
                        nivelAtivo == 1 -> {
                            // Nível 1 com dano total → vai para morto (nivel 0)
                            viewModel.atualizarSaudeNivel(1, 0)
                        }
                        // nivelAtivo == 0 → já morto, não faz nada
                    }
                },
                onCuraNivel = {
                    // Restaura nível inteiro: enche o próximo nível completamente
                    when {
                        nivelAtivo == 0 -> {
                            // Morto → restaura nível 1 completo
                            viewModel.atualizarSaudeNivel(1, maxPontosNivel)
                        }
                        nivelAtivo < 6 -> {
                            // Enche o nível atual e sobe para o próximo completo
                            viewModel.atualizarSaudeNivel(nivelAtivo + 1, maxPontosNivel)
                        }
                        nivelAtivo == 6 -> {
                            // Já no máximo, enche o nível 6 completamente
                            viewModel.atualizarSaudeNivel(6, maxPontosNivel)
                        }
                    }
                },
                onMaxChange = { novoMax ->
                    viewModel.atualizarMaxESaude(novoMax)
                }
            )
        }

        // ── Cabo de Guerra ────────────────────────────────────
        ficha?.let { f ->
            CaboDeGuerraCard(
                nivelDeterminacao = f.nivelDeterminacao,
                nivelAssimilacao  = f.nivelAssimilacao,
                pontosDeterminacao = f.pontosDeterminacao,
                pontosAssimilacao  = f.pontosAssimilacao,
                onNivelDetChange   = { viewModel.atualizarNivelDeterminacao(it) },
                onPontosDetChange  = { viewModel.atualizarPontosDeterminacao(it) },
                onPontosAssimChange = { viewModel.atualizarPontosAssimilacao(it) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onThemeChange,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("🎨 TROCAR TEMA", fontWeight = FontWeight.Bold)
        }

        OutlinedButton(
            onClick = onModeChange,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("🔄 TROCAR MODO DE JOGO", fontWeight = FontWeight.Bold)
        }
    }
}

// ─────────────────────────────────────────────────────────────
// SAÚDE PROGRESSIVA — BARRA ÚNICA
// ─────────────────────────────────────────────────────────────
@Composable
fun SaudeProgressivaCard(
    nivelAtivo: Int,
    pontosAtivo: Int,
    maxPontos: Int,
    totalPontos: Int,
    totalMax: Int,
    onDano: () -> Unit,
    onCura: () -> Unit,
    onDanoNivel: () -> Unit,
    onCuraNivel: () -> Unit,
    onMaxChange: (Int) -> Unit
) {
    val condicaoAtual = condicoesSaude.find { it.nivel == nivelAtivo }
        ?: condicoesSaude.last()

    val corAtual by animateColorAsState(
        targetValue = if (nivelAtivo == 0) Color(0xFF212121) else condicaoAtual.cor,
        animationSpec = tween(600),
        label = "corSaude"
    )

    // ── CORREÇÃO: progresso calculado com maxPontos real ──
    val progresso by animateFloatAsState(
        targetValue = if (maxPontos > 0) pontosAtivo.toFloat() / maxPontos.toFloat() else 0f,
        animationSpec = tween(400),
        label = "progressoSaude"
    )

    var showMaxDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Header com badge de condição
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "▸ SAÚDE",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(corAtual)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (nivelAtivo == 0) "☠️ MORTO" else condicaoAtual.nome,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Barra animada
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progresso)
                        .clip(RoundedCornerShape(16.dp))
                        .background(corAtual)
                )
                // ── CORREÇÃO: exibe maxPontos real na label ──
                Text(
                    "$pontosAtivo / $maxPontos",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Linha única: << | − DANO | + CURA | >>
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // << remove nível inteiro
                IconButton(onClick = onDanoNivel) {
                    Text(
                        "«",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                FilledTonalButton(
                    onClick = onDano,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.15f),
                        contentColor   = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("− DANO", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(12.dp))
                // Quando morto (nível 0), botão de cura fica roxo; senão, sempre verde
                val corCura = if (nivelAtivo == 0) Color(0xFF7B1FA2) else Color(0xFF43A047)
                FilledTonalButton(
                    onClick = onCura,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = corCura.copy(alpha = 0.15f),
                        contentColor   = corCura
                    )
                ) {
                    Text("+ CURA", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(4.dp))
                // >> restaura nível inteiro
                IconButton(onClick = onCuraNivel) {
                    Text(
                        "»",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF43A047)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Trilha de condições
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                condicoesSaude.reversed().forEach { cond ->
                    val isAtivo = cond.nivel == nivelAtivo
                    val isAtras = cond.nivel < nivelAtivo
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(if (isAtivo) 8.dp else 5.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    when {
                                        isAtivo -> cond.cor
                                        isAtras -> cond.cor.copy(alpha = 0.25f)
                                        else    -> cond.cor.copy(alpha = 0.55f)
                                    }
                                )
                        )
                        Text(
                            cond.nome.take(4),
                            fontSize = 7.sp,
                            fontWeight = if (isAtivo) FontWeight.Bold else FontWeight.Normal,
                            color = if (isAtivo) cond.cor
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(10.dp))

            // Total e editar máximo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Total de vida",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    // ── CORREÇÃO: totalMax calculado com o máximo real ──
                    Text(
                        "$totalPontos / $totalMax",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                TextButton(onClick = { showMaxDialog = true }) {
                    Text(
                        "✏️ Editar máximo",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }

    if (showMaxDialog) {
        MaxSaudeUnicoDialog(
            maxAtual  = maxPontos,
            onDismiss = { showMaxDialog = false },
            onConfirm = { novoMax ->
                onMaxChange(novoMax)
                showMaxDialog = false
            }
        )
    }
}

// ─────────────────────────────────────────────────────────────
// DIALOG — EDITAR MÁXIMO
// ─────────────────────────────────────────────────────────────
@Composable
fun MaxSaudeUnicoDialog(
    maxAtual: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var valor by remember { mutableStateOf(maxAtual.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Pontos por nível de saúde", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "O mesmo valor se aplica a todos os 6 níveis de saúde.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                OutlinedTextField(
                    value = valor,
                    onValueChange = { if (it.length <= 2) valor = it },
                    label = { Text("Pontos por nível") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = AppTextFieldDefaults.colors()
                )
                Text(
                    "Total: ${(valor.toIntOrNull() ?: 0) * 6} pts  (${valor} × 6 níveis)",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    condicoesSaude.forEach { cond ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(cond.cor)
                            )
                            Text(
                                cond.nome,
                                fontSize = 11.sp,
                                modifier = Modifier.weight(1f),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                "${valor.toIntOrNull() ?: 0} pts",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = cond.cor
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(valor.toIntOrNull() ?: maxAtual) }) {
                Text("Salvar", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

// ─────────────────────────────────────────────────────────────
// CABO DE GUERRA
// ─────────────────────────────────────────────────────────────
@Composable
fun CaboDeGuerraCard(
    nivelDeterminacao: Int,
    nivelAssimilacao: Int,
    pontosDeterminacao: Int,
    pontosAssimilacao: Int,
    onNivelDetChange: (Int) -> Unit,
    onPontosDetChange: (Int) -> Unit,
    onPontosAssimChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "▸ 🧬 CABO DE GUERRA",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ContadorCaboDeGuerra(
                    label = "Determinação",
                    emoji = "🛡️",
                    cor   = Color(0xFF1565C0),
                    nivel = nivelDeterminacao,
                    pontos = pontosDeterminacao,
                    onNivelMenos  = { if (nivelDeterminacao > 0)  onNivelDetChange(nivelDeterminacao - 1) },
                    onNivelMais   = { if (nivelDeterminacao < 10) onNivelDetChange(nivelDeterminacao + 1) },
                    onPontosMenos = { if (pontosDeterminacao > 0) onPontosDetChange(pontosDeterminacao - 1) },
                    onPontosMais  = { if (pontosDeterminacao < nivelDeterminacao) onPontosDetChange(pontosDeterminacao + 1) },
                    modifier = Modifier.weight(1f)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    Text("🧬", fontSize = 26.sp)
                    Text("=10", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                ContadorCaboDeGuerra(
                    label = "Assimilação",
                    emoji = "🦠",
                    cor   = Color(0xFF6A1B9A),
                    nivel = nivelAssimilacao,
                    pontos = pontosAssimilacao,
                    onNivelMenos  = { if (nivelDeterminacao < 10) onNivelDetChange(nivelDeterminacao + 1) },
                    onNivelMais   = { if (nivelDeterminacao > 0)  onNivelDetChange(nivelDeterminacao - 1) },
                    onPontosMenos = { if (pontosAssimilacao > 0)  onPontosAssimChange(pontosAssimilacao - 1) },
                    onPontosMais  = { if (pontosAssimilacao < nivelAssimilacao) onPontosAssimChange(pontosAssimilacao + 1) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Nível",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF6A1B9A))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(nivelDeterminacao / 10f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF1565C0))
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("D $nivelDeterminacao", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1565C0))
                Text("E $nivelAssimilacao",  fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6A1B9A))
            }
        }
    }
}

@Composable
fun ContadorCaboDeGuerra(
    label: String,
    emoji: String,
    cor: Color,
    nivel: Int,
    pontos: Int,
    onNivelMenos: () -> Unit,
    onNivelMais: () -> Unit,
    onPontosMenos: () -> Unit,
    onPontosMais: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text("$emoji $label", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = cor)

        Text("Nível", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            IconButton(onClick = onNivelMenos, modifier = Modifier.size(32.dp)) {
                Text("−", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = cor)
            }
            Text(nivel.toString(), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = cor)
            IconButton(onClick = onNivelMais, modifier = Modifier.size(32.dp)) {
                Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = cor)
            }
        }

        HorizontalDivider(color = cor.copy(alpha = 0.3f))

        Text("Pontos", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            IconButton(onClick = onPontosMenos, modifier = Modifier.size(32.dp)) {
                Text("−", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = cor)
            }
            Text("$pontos/$nivel", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = cor)
            IconButton(onClick = onPontosMais, modifier = Modifier.size(32.dp)) {
                Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = cor)
            }
        }
    }
}