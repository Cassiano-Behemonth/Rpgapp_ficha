package com.example.rpgapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "magias_fantasia")
data class MagiaFantasiaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fichaId: Long = 0,

    // ========== DADOS BÁSICOS ==========
    val nome: String,
    val escola: String = "",
    val circulo: Int = 1,

    // ========== EXECUÇÃO ==========
    val execucao: String = "",
    val alcance: String = "",
    val area: String = "",
    val duracao: String = "",

    // ========== RESISTÊNCIA & CD ==========
    val resistencia: String = "",
    val atributoChave: String = "INT",

    // ========== DESCRIÇÃO ==========
    val efeito: String = "",

    // ========== COMPONENTES ==========
    val componentes: String = "",

    // ========== ROLAGENS ==========
    val acerto: String = "",
    val dano: String = ""
) {
    fun calcularCD(modAtributo: Int): Int {
        return 10 + circulo + modAtributo
    }

    fun formatarCD(modAtributo: Int): String {
        return "CD ${calcularCD(modAtributo)}"
    }

    fun getEmojiEscola(): String {
        return when (escola.lowercase()) {
            "abjuração", "abjuracao" -> "🛡️"
            "convocação", "convocacao" -> "🌀"
            "adivinhação", "adivinhacao" -> "🔮"
            "encantamento" -> "💫"
            "evocação", "evocacao" -> "⚡"
            "ilusão", "ilusao" -> "✨"
            "necromancia" -> "💀"
            "transmutação", "transmutacao" -> "🔄"
            "universal" -> "🌟"
            else -> "📜"
        }
    }

    fun formatarCirculo(): String {
        return when (circulo) {
            0 -> "Truque"
            1 -> "1º Círculo"
            2 -> "2º Círculo"
            3 -> "3º Círculo"
            else -> "${circulo}º Círculo"
        }
    }

    fun getInfoResumo(): String {
        val infos = mutableListOf<String>()

        if (escola.isNotBlank()) infos.add(escola)
        if (execucao.isNotBlank()) infos.add(execucao)
        if (alcance.isNotBlank()) infos.add(alcance)

        return infos.joinToString(" • ")
    }

    fun temResistencia(): Boolean {
        return resistencia.isNotBlank() &&
                !resistencia.equals("nenhuma", ignoreCase = true) &&
                !resistencia.equals("não", ignoreCase = true)
    }

    fun formatarResistencia(): String {
        return if (temResistencia()) {
            "Resist: $resistencia"
        } else {
            "Sem resistência"
        }
    }

    fun formatarComponentes(): String {
        if (componentes.isBlank()) return "Nenhum"
        return componentes
    }
}
