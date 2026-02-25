package com.example.rpgapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fichas_assimilacao")
data class FichaAssimilacaoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // ── Identidade ──────────────────────────────────────────
    val nome: String = "",
    val jogador: String = "",

    // ── Saúde (6 níveis, pontos manuais por nível) ──────────
    // Pontos atuais de cada nível (quando chega a 0 passa pro próximo)
    val pontosNivel6: Int = 5, // Intacto     (começa aqui)
    val pontosNivel5: Int = 5, // Arranhado
    val pontosNivel4: Int = 5, // Ferido
    val pontosNivel3: Int = 5, // Machucado
    val pontosNivel2: Int = 5, // Crítico
    val pontosNivel1: Int = 5, // Limiar

    // Pontos máximos de cada nível (definidos manualmente pelo jogador)
    val maxNivel6: Int = 5,
    val maxNivel5: Int = 5,
    val maxNivel4: Int = 5,
    val maxNivel3: Int = 5,
    val maxNivel2: Int = 5,
    val maxNivel1: Int = 5,

    // ── Cabo de Guerra ───────────────────────────────────────
    // Nível: soma sempre = 10
    val nivelDeterminacao: Int = 9, // D (padrão: começa em 9)
    // nivelAssimilacao é calculado: 10 - nivelDeterminacao

    // Pontos: recurso gasto/recuperado durante o jogo
    val pontosDeterminacao: Int = 9, // d (máximo = nivelDeterminacao)
    val pontosAssimilacao: Int = 1,  // e (máximo = nivelAssimilacao)

    // ── Aptidões — Instintos (1-5) ───────────────────────────
    val influencia: Int = 1,
    val percepcao: Int = 1,
    val potencia: Int = 1,
    val reacao: Int = 1,
    val resolucao: Int = 1,
    val sagacidade: Int = 1,

    // ── Aptidões — Conhecimentos (0-5) ───────────────────────
    val biologia: Int = 0,
    val erudicao: Int = 0,
    val engenharia: Int = 0,
    val geografia: Int = 0,
    val medicina: Int = 0,
    val seguranca: Int = 0,

    // ── Aptidões — Práticas (0-5) ────────────────────────────
    val armas: Int = 0,
    val atletismo: Int = 0,
    val expressao: Int = 0,
    val furtividade: Int = 0,
    val manufaturas: Int = 0,
    val sobrevivencia: Int = 0,

    // ── Origens ──────────────────────────────────────────────
    val eventoMarcante: String = "",
    val ocupacao: String = "",

    // ── Propósitos ───────────────────────────────────────────
    val proposito1: String = "",
    val proposito2: String = "",
    val propositoColetivo: String = "",

    // ── Descrição ────────────────────────────────────────────
    val aparencia: String = "",
    val personalidade: String = "",
    val historia: String = "",
    val anotacoes: String = ""
) {
    // Nível de Assimilação calculado automaticamente
    val nivelAssimilacao: Int
        get() = 10 - nivelDeterminacao

    // Nível atual de saúde (qual barra está ativa)
    // Retorna 6 (Intacto) até 1 (Limiar), ou 0 se morto
    val nivelSaudeAtual: Int
        get() = when {
            pontosNivel6 > 0 -> 6
            pontosNivel5 > 0 -> 5
            pontosNivel4 > 0 -> 4
            pontosNivel3 > 0 -> 3
            pontosNivel2 > 0 -> 2
            pontosNivel1 > 0 -> 1
            else -> 0
        }

    // Descrição narrativa da condição atual
    val condicaoNarrativa: String
        get() = when (nivelSaudeAtual) {
            6 -> "Intacto"
            5 -> "Arranhado"
            4 -> "Ferido"
            3 -> "Machucado"
            2 -> "Crítico"
            1 -> "Limiar"
            else -> "Morto"
        }
}