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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rpgapp.viewmodel.FichaAssimilacaoViewModel
import kotlinx.coroutines.delay

// Cores dos símbolos
val corInstinto    = Color(0xFFE53935) // Vermelho — quadrados
val corConhecimento = Color(0xFF1E88E5) // Azul — diamantes
val corPratica     = Color(0xFF43A047) // Verde — diamantes

@Composable
fun AptidoesAssimilacaoScreen(
    viewModel: FichaAssimilacaoViewModel
) {
    val ficha by viewModel.ficha.collectAsState()

    // ── Estado local das aptidões ────────────────────────────
    // Instintos
    var influencia  by remember { mutableStateOf(1) }
    var percepcao   by remember { mutableStateOf(1) }
    var potencia    by remember { mutableStateOf(1) }
    var reacao      by remember { mutableStateOf(1) }
    var resolucao   by remember { mutableStateOf(1) }
    var sagacidade  by remember { mutableStateOf(1) }
    // Conhecimentos
    var biologia    by remember { mutableStateOf(0) }
    var erudicao    by remember { mutableStateOf(0) }
    var engenharia  by remember { mutableStateOf(0) }
    var geografia   by remember { mutableStateOf(0) }
    var medicina    by remember { mutableStateOf(0) }
    var seguranca   by remember { mutableStateOf(0) }
    // Práticas
    var armas         by remember { mutableStateOf(0) }
    var atletismo     by remember { mutableStateOf(0) }
    var expressao     by remember { mutableStateOf(0) }
    var furtividade   by remember { mutableStateOf(0) }
    var manufaturas   by remember { mutableStateOf(0) }
    var sobrevivencia by remember { mutableStateOf(0) }

    LaunchedEffect(ficha) {
        ficha?.let {
            influencia  = it.influencia;  percepcao  = it.percepcao
            potencia    = it.potencia;    reacao     = it.reacao
            resolucao   = it.resolucao;   sagacidade = it.sagacidade
            biologia    = it.biologia;    erudicao   = it.erudicao
            engenharia  = it.engenharia;  geografia  = it.geografia
            medicina    = it.medicina;    seguranca  = it.seguranca
            armas         = it.armas;       atletismo   = it.atletismo
            expressao     = it.expressao;   furtividade = it.furtividade
            manufaturas   = it.manufaturas; sobrevivencia = it.sobrevivencia
        }
    }

    // Salvamento automático
    LaunchedEffect(
        influencia, percepcao, potencia, reacao, resolucao, sagacidade,
        biologia, erudicao, engenharia, geografia, medicina, seguranca,
        armas, atletismo, expressao, furtividade, manufaturas, sobrevivencia
    ) {
        delay(800)
        viewModel.salvarAptidoes(
            influencia, percepcao, potencia, reacao, resolucao, sagacidade,
            biologia, erudicao, engenharia, geografia, medicina, seguranca,
            armas, atletismo, expressao, furtividade, manufaturas, sobrevivencia
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // ── INSTINTOS ─────────────────────────────────────────
        AptidaoSection(
            titulo = "▸ INSTINTOS",
            subtitulo = "d6 — Mínimo 1, Máximo 5",
            cor = corInstinto
        ) {
            // Quadrados ◼ (mínimo 1)
            AptidaoRow("Influência",  influencia,  corInstinto, 1, 5, TipoSimbolo.QUADRADO) { influencia  = it }
            AptidaoRow("Percepção",   percepcao,   corInstinto, 1, 5, TipoSimbolo.QUADRADO) { percepcao   = it }
            AptidaoRow("Potência",    potencia,    corInstinto, 1, 5, TipoSimbolo.QUADRADO) { potencia    = it }
            AptidaoRow("Reação",      reacao,      corInstinto, 1, 5, TipoSimbolo.QUADRADO) { reacao      = it }
            AptidaoRow("Resolução",   resolucao,   corInstinto, 1, 5, TipoSimbolo.QUADRADO) { resolucao   = it }
            AptidaoRow("Sagacidade",  sagacidade,  corInstinto, 1, 5, TipoSimbolo.QUADRADO) { sagacidade  = it }
        }

        // ── CONHECIMENTOS ─────────────────────────────────────
        AptidaoSection(
            titulo = "▸ CONHECIMENTOS",
            subtitulo = "d10 — Mínimo 0, Máximo 5",
            cor = corConhecimento
        ) {
            // Diamantes ◆ azuis (mínimo 0)
            AptidaoRow("Biologia",   biologia,   corConhecimento, 0, 5, TipoSimbolo.DIAMANTE) { biologia   = it }
            AptidaoRow("Erudição",   erudicao,   corConhecimento, 0, 5, TipoSimbolo.DIAMANTE) { erudicao   = it }
            AptidaoRow("Engenharia", engenharia, corConhecimento, 0, 5, TipoSimbolo.DIAMANTE) { engenharia = it }
            AptidaoRow("Geografia",  geografia,  corConhecimento, 0, 5, TipoSimbolo.DIAMANTE) { geografia  = it }
            AptidaoRow("Medicina",   medicina,   corConhecimento, 0, 5, TipoSimbolo.DIAMANTE) { medicina   = it }
            AptidaoRow("Segurança",  seguranca,  corConhecimento, 0, 5, TipoSimbolo.DIAMANTE) { seguranca  = it }
        }

        // ── PRÁTICAS ──────────────────────────────────────────
        AptidaoSection(
            titulo = "▸ PRÁTICAS",
            subtitulo = "d10 — Mínimo 0, Máximo 5",
            cor = corPratica
        ) {
            // Diamantes ◆ verdes (mínimo 0)
            AptidaoRow("Armas",         armas,         corPratica, 0, 5, TipoSimbolo.DIAMANTE) { armas         = it }
            AptidaoRow("Atletismo",     atletismo,     corPratica, 0, 5, TipoSimbolo.DIAMANTE) { atletismo     = it }
            AptidaoRow("Expressão",     expressao,     corPratica, 0, 5, TipoSimbolo.DIAMANTE) { expressao     = it }
            AptidaoRow("Furtividade",   furtividade,   corPratica, 0, 5, TipoSimbolo.DIAMANTE) { furtividade   = it }
            AptidaoRow("Manufaturas",   manufaturas,   corPratica, 0, 5, TipoSimbolo.DIAMANTE) { manufaturas   = it }
            AptidaoRow("Sobrevivência", sobrevivencia, corPratica, 0, 5, TipoSimbolo.DIAMANTE) { sobrevivencia = it }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// SEÇÃO DE APTIDÃO
// ─────────────────────────────────────────────────────────────
@Composable
fun AptidaoSection(
    titulo: String,
    subtitulo: String,
    cor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = cor
            )
            Text(
                subtitulo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            HorizontalDivider(color = cor.copy(alpha = 0.3f))
            content()
        }
    }
}

// ─────────────────────────────────────────────────────────────
// LINHA DE APTIDÃO
// ─────────────────────────────────────────────────────────────
enum class TipoSimbolo { QUADRADO, DIAMANTE }

@Composable
fun AptidaoRow(
    nome: String,
    valor: Int,
    cor: Color,
    min: Int,
    max: Int,
    tipo: TipoSimbolo,
    onValorChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Nome da aptidão
        Text(
            nome,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(100.dp),
            color = MaterialTheme.colorScheme.onSurface
        )

        // Símbolos clicáveis
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 1..max) {
                val preenchido = i <= valor
                SimboloAptidao(
                    preenchido = preenchido,
                    cor = cor,
                    tipo = tipo,
                    onClick = {
                        if (preenchido) {
                            // Clicou num símbolo preenchido:
                            // define o valor para i-1 (remove até esse ponto, pode ser vários)
                            val novoValor = (i - 1).coerceAtLeast(min)
                            if (novoValor != valor) onValorChange(novoValor)
                        } else {
                            // Clicou num símbolo vazio: preenche até esse ponto
                            onValorChange(i)
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Valor numérico
        Text(
            valor.toString(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = cor,
            modifier = Modifier.width(16.dp)
        )
    }
}

// ─────────────────────────────────────────────────────────────
// SÍMBOLO INDIVIDUAL
// ─────────────────────────────────────────────────────────────
@Composable
fun SimboloAptidao(
    preenchido: Boolean,
    cor: Color,
    tipo: TipoSimbolo,
    onClick: () -> Unit
) {
    val size = 20.dp
    val borderWidth = 2.dp

    when (tipo) {
        TipoSimbolo.QUADRADO -> {
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(RoundedCornerShape(3.dp))
                    .background(if (preenchido) cor else Color.Transparent)
                    .border(borderWidth, cor, RoundedCornerShape(3.dp))
                    .clickable(onClick = onClick)
            )
        }
        TipoSimbolo.DIAMANTE -> {
            // Diamante = quadrado rotacionado 45°
            Box(
                modifier = Modifier
                    .size(size)
                    .rotate(45f)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (preenchido) cor else Color.Transparent)
                    .border(borderWidth, cor, RoundedCornerShape(2.dp))
                    .clickable(onClick = onClick)
            )
        }
    }
}