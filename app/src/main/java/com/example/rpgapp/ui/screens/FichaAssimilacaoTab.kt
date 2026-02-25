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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rpgapp.viewmodel.FichaAssimilacaoViewModel
import kotlinx.coroutines.delay

// ── Cores narrativas da Saúde ────────────────────────────────
val corSaude6 = Color(0xFF43A047) // Intacto     — verde
val corSaude5 = Color(0xFF7CB342) // Arranhado   — verde amarelado
val corSaude4 = Color(0xFFFDD835) // Ferido      — amarelo
val corSaude3 = Color(0xFFFB8C00) // Machucado   — laranja
val corSaude2 = Color(0xFFE53935) // Crítico     — vermelho
val corSaude1 = Color(0xFF6A1B9A) // Limiar      — roxo escuro
val corMorto  = Color(0xFF212121) // Morto       — preto

@Composable
fun FichaAssimilacaoTab(
    viewModel: FichaAssimilacaoViewModel,
    onThemeChange: () -> Unit,
    onModeChange: () -> Unit
) {
    val ficha by viewModel.ficha.collectAsState()

    // ── Estado local do nome ─────────────────────────────────
    var nome by remember { mutableStateOf("") }

    LaunchedEffect(ficha) {
        ficha?.let { nome = it.nome }
    }

    // Salvamento automático do nome
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
                    placeholder = { Text("Nome do personagem") }
                )
            }
        }

        // ── Saúde ─────────────────────────────────────────────
        ficha?.let { f ->
            SaudeNarrativaCard(
                pontosNivel6 = f.pontosNivel6, maxNivel6 = f.maxNivel6,
                pontosNivel5 = f.pontosNivel5, maxNivel5 = f.maxNivel5,
                pontosNivel4 = f.pontosNivel4, maxNivel4 = f.maxNivel4,
                pontosNivel3 = f.pontosNivel3, maxNivel3 = f.maxNivel3,
                pontosNivel2 = f.pontosNivel2, maxNivel2 = f.maxNivel2,
                pontosNivel1 = f.pontosNivel1, maxNivel1 = f.maxNivel1,
                nivelAtual = f.nivelSaudeAtual,
                condicao = f.condicaoNarrativa,
                onPontosChange = { n6, n5, n4, n3, n2, n1 ->
                    viewModel.atualizarSaude(n6, n5, n4, n3, n2, n1)
                },
                onMaxChange = { n6, n5, n4, n3, n2, n1 ->
                    viewModel.atualizarMaxSaude(n6, n5, n4, n3, n2, n1)
                }
            )
        }

        // ── Cabo de Guerra ────────────────────────────────────
        ficha?.let { f ->
            CaboDeGuerraCard(
                nivelDeterminacao = f.nivelDeterminacao,
                nivelAssimilacao = f.nivelAssimilacao,
                pontosDeterminacao = f.pontosDeterminacao,
                pontosAssimilacao = f.pontosAssimilacao,
                onNivelDetChange = { viewModel.atualizarNivelDeterminacao(it) },
                onPontosDetChange = { viewModel.atualizarPontosDeterminacao(it) },
                onPontosAssimChange = { viewModel.atualizarPontosAssimilacao(it) }
            )
        }

        // ── Botões de navegação ───────────────────────────────
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
// SAÚDE NARRATIVA
// ─────────────────────────────────────────────────────────────
@Composable
fun SaudeNarrativaCard(
    pontosNivel6: Int, maxNivel6: Int,
    pontosNivel5: Int, maxNivel5: Int,
    pontosNivel4: Int, maxNivel4: Int,
    pontosNivel3: Int, maxNivel3: Int,
    pontosNivel2: Int, maxNivel2: Int,
    pontosNivel1: Int, maxNivel1: Int,
    nivelAtual: Int,
    condicao: String,
    onPontosChange: (Int, Int, Int, Int, Int, Int) -> Unit,
    onMaxChange: (Int, Int, Int, Int, Int, Int) -> Unit
) {
    // Cor animada baseada na condição atual
    val corAtual by animateColorAsState(
        targetValue = when (nivelAtual) {
            6 -> corSaude6
            5 -> corSaude5
            4 -> corSaude4
            3 -> corSaude3
            2 -> corSaude2
            1 -> corSaude1
            else -> corMorto
        },
        animationSpec = tween(600),
        label = "corSaude"
    )

    var showMaxDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Header com condição atual
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

                // Badge da condição atual
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(corAtual)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (nivelAtual == 0) "☠️ MORTO" else condicao.uppercase(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 6 barras de saúde sequenciais
            val niveis = listOf(
                NivelSaude(6, "Intacto",   corSaude6, pontosNivel6, maxNivel6),
                NivelSaude(5, "Arranhado", corSaude5, pontosNivel5, maxNivel5),
                NivelSaude(4, "Ferido",    corSaude4, pontosNivel4, maxNivel4),
                NivelSaude(3, "Machucado", corSaude3, pontosNivel3, maxNivel3),
                NivelSaude(2, "Crítico",   corSaude2, pontosNivel2, maxNivel2),
                NivelSaude(1, "Limiar",    corSaude1, pontosNivel1, maxNivel1),
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                niveis.forEach { nivel ->
                    val isAtivo = nivel.numero == nivelAtual
                    val isDesbloqueado = nivel.numero >= nivelAtual || nivelAtual == 0

                    BarraSaude(
                        nivel = nivel,
                        isAtivo = isAtivo,
                        isDesbloqueado = isDesbloqueado,
                        onDano = {
                            // Reduz 1 ponto do nível atual
                            val novosPontos = (nivel.pontos - 1).coerceAtLeast(0)
                            emitirMudancaSaude(
                                nivelAlterado = nivel.numero,
                                novosPontos = novosPontos,
                                p6 = pontosNivel6, p5 = pontosNivel5,
                                p4 = pontosNivel4, p3 = pontosNivel3,
                                p2 = pontosNivel2, p1 = pontosNivel1,
                                onChange = onPontosChange
                            )
                        },
                        onCura = {
                            // Aumenta 1 ponto do nível atual (até o máximo)
                            val novosPontos = (nivel.pontos + 1).coerceAtMost(nivel.max)
                            emitirMudancaSaude(
                                nivelAlterado = nivel.numero,
                                novosPontos = novosPontos,
                                p6 = pontosNivel6, p5 = pontosNivel5,
                                p4 = pontosNivel4, p3 = pontosNivel3,
                                p2 = pontosNivel2, p1 = pontosNivel1,
                                onChange = onPontosChange
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botão para editar máximos
            TextButton(
                onClick = { showMaxDialog = true },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    "✏️ Editar máximos",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    if (showMaxDialog) {
        MaxSaudeDialog(
            maxNivel6 = maxNivel6, maxNivel5 = maxNivel5,
            maxNivel4 = maxNivel4, maxNivel3 = maxNivel3,
            maxNivel2 = maxNivel2, maxNivel1 = maxNivel1,
            onDismiss = { showMaxDialog = false },
            onConfirm = { n6, n5, n4, n3, n2, n1 ->
                onMaxChange(n6, n5, n4, n3, n2, n1)
                showMaxDialog = false
            }
        )
    }
}

data class NivelSaude(
    val numero: Int,
    val nome: String,
    val cor: Color,
    val pontos: Int,
    val max: Int
)

@Composable
fun BarraSaude(
    nivel: NivelSaude,
    isAtivo: Boolean,
    isDesbloqueado: Boolean,
    onDano: () -> Unit,
    onCura: () -> Unit
) {
    val progresso by animateFloatAsState(
        targetValue = if (nivel.max > 0) nivel.pontos.toFloat() / nivel.max.toFloat() else 0f,
        animationSpec = tween(400),
        label = "progresso_${nivel.numero}"
    )

    val alpha = if (isDesbloqueado) 1f else 0.3f

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Label do nível
        Text(
            text = nivel.nome,
            fontSize = 11.sp,
            fontWeight = if (isAtivo) FontWeight.Bold else FontWeight.Normal,
            color = if (isAtivo) nivel.cor
            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha),
            modifier = Modifier.width(72.dp)
        )

        // Barra de progresso
        Box(
            modifier = Modifier
                .weight(1f)
                .height(14.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progresso)
                    .clip(RoundedCornerShape(7.dp))
                    .background(nivel.cor.copy(alpha = alpha))
            )
        }

        // Contador
        Text(
            text = "${nivel.pontos}/${nivel.max}",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isAtivo) nivel.cor
            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha),
            modifier = Modifier.width(36.dp)
        )

        // Botões - e + (só ativos no nível atual ou desbloqueados)
        if (isDesbloqueado) {
            IconButton(
                onClick = onDano,
                modifier = Modifier.size(28.dp),
                enabled = nivel.pontos > 0
            ) {
                Text(
                    "−",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (nivel.pontos > 0) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
            }
            IconButton(
                onClick = onCura,
                modifier = Modifier.size(28.dp),
                enabled = nivel.pontos < nivel.max
            ) {
                Text(
                    "+",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (nivel.pontos < nivel.max) nivel.cor
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
            }
        } else {
            Spacer(modifier = Modifier.width(56.dp))
        }
    }
}

// Helper para emitir mudança mantendo os outros níveis intactos
fun emitirMudancaSaude(
    nivelAlterado: Int,
    novosPontos: Int,
    p6: Int, p5: Int, p4: Int, p3: Int, p2: Int, p1: Int,
    onChange: (Int, Int, Int, Int, Int, Int) -> Unit
) {
    onChange(
        if (nivelAlterado == 6) novosPontos else p6,
        if (nivelAlterado == 5) novosPontos else p5,
        if (nivelAlterado == 4) novosPontos else p4,
        if (nivelAlterado == 3) novosPontos else p3,
        if (nivelAlterado == 2) novosPontos else p2,
        if (nivelAlterado == 1) novosPontos else p1
    )
}

@Composable
fun MaxSaudeDialog(
    maxNivel6: Int, maxNivel5: Int, maxNivel4: Int,
    maxNivel3: Int, maxNivel2: Int, maxNivel1: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int, Int, Int, Int, Int) -> Unit
) {
    var v6 by remember { mutableStateOf(maxNivel6.toString()) }
    var v5 by remember { mutableStateOf(maxNivel5.toString()) }
    var v4 by remember { mutableStateOf(maxNivel4.toString()) }
    var v3 by remember { mutableStateOf(maxNivel3.toString()) }
    var v2 by remember { mutableStateOf(maxNivel2.toString()) }
    var v1 by remember { mutableStateOf(maxNivel1.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Pontos Máximos por Nível", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Define quantos pontos cada nível de saúde possui.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                listOf(
                    Triple("Intacto (6)", corSaude6, v6) to { it: String -> v6 = it },
                    Triple("Arranhado (5)", corSaude5, v5) to { it: String -> v5 = it },
                    Triple("Ferido (4)", corSaude4, v4) to { it: String -> v4 = it },
                    Triple("Machucado (3)", corSaude3, v3) to { it: String -> v3 = it },
                    Triple("Crítico (2)", corSaude2, v2) to { it: String -> v2 = it },
                    Triple("Limiar (1)", corSaude1, v1) to { it: String -> v1 = it },
                ).forEach { (info, onChange) ->
                    val (label, cor, valor) = info
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(cor)
                        )
                        Text(
                            label,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = valor,
                            onValueChange = { if (it.length <= 2) onChange(it) },
                            modifier = Modifier.width(64.dp),
                            singleLine = true
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(
                    v6.toIntOrNull() ?: 5, v5.toIntOrNull() ?: 5,
                    v4.toIntOrNull() ?: 5, v3.toIntOrNull() ?: 5,
                    v2.toIntOrNull() ?: 5, v1.toIntOrNull() ?: 5
                )
            }) {
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

            // ── Níveis lado a lado ────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Determinação
                ContadorCaboDeGuerra(
                    label = "Determinação",
                    emoji = "🛡️",
                    cor = Color(0xFF1565C0),
                    nivel = nivelDeterminacao,
                    pontos = pontosDeterminacao,
                    onNivelMenos = { if (nivelDeterminacao > 0) onNivelDetChange(nivelDeterminacao - 1) },
                    onNivelMais = { if (nivelDeterminacao < 10) onNivelDetChange(nivelDeterminacao + 1) },
                    onPontosMenos = { if (pontosDeterminacao > 0) onPontosDetChange(pontosDeterminacao - 1) },
                    onPontosMais = { if (pontosDeterminacao < nivelDeterminacao) onPontosDetChange(pontosDeterminacao + 1) },
                    modifier = Modifier.weight(1f)
                )

                // Símbolo central
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(top = 24.dp)
                ) {
                    Text("🧬", fontSize = 28.sp)
                    Text(
                        "=10",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Assimilação
                ContadorCaboDeGuerra(
                    label = "Assimilação",
                    emoji = "🦠",
                    cor = Color(0xFF6A1B9A),
                    nivel = nivelAssimilacao,
                    pontos = pontosAssimilacao,
                    onNivelMenos = { if (nivelDeterminacao < 10) onNivelDetChange(nivelDeterminacao + 1) },
                    onNivelMais = { if (nivelDeterminacao > 0) onNivelDetChange(nivelDeterminacao - 1) },
                    onPontosMenos = { if (pontosAssimilacao > 0) onPontosAssimChange(pontosAssimilacao - 1) },
                    onPontosMais = { if (pontosAssimilacao < nivelAssimilacao) onPontosAssimChange(pontosAssimilacao + 1) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Barra visual do cabo de guerra
            Text(
                "Nível",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF6A1B9A)) // fundo = assimilação
            ) {
                val propDet = nivelDeterminacao / 10f
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(propDet)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF1565C0)) // frente = determinação
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "D $nivelDeterminacao",
                    fontSize = 11.sp,
                    color = Color(0xFF1565C0),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "E $nivelAssimilacao",
                    fontSize = 11.sp,
                    color = Color(0xFF6A1B9A),
                    fontWeight = FontWeight.Bold
                )
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
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Label
        Text(
            "$emoji $label",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = cor
        )

        // Nível
        Text(
            "Nível",
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            IconButton(onClick = onNivelMenos, modifier = Modifier.size(32.dp)) {
                Text("−", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = cor)
            }
            Text(
                nivel.toString(),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = cor
            )
            IconButton(onClick = onNivelMais, modifier = Modifier.size(32.dp)) {
                Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = cor)
            }
        }

        Divider(color = cor.copy(alpha = 0.3f))

        // Pontos
        Text(
            "Pontos",
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            IconButton(onClick = onPontosMenos, modifier = Modifier.size(32.dp)) {
                Text("−", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = cor)
            }
            Text(
                "$pontos/$nivel",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = cor
            )
            IconButton(onClick = onPontosMais, modifier = Modifier.size(32.dp)) {
                Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = cor)
            }
        }
    }
}