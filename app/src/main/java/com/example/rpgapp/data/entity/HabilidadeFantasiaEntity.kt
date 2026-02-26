package com.example.rpgapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habilidades_fantasia")
data class HabilidadeFantasiaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fichaId: Long = 0,

    // ========== DADOS DA HABILIDADE ==========
    val nome: String,
    val categoria: String,
    val descricao: String = "",

    // ========== CUSTO (para poderes) ==========
    val custoPM: Int = 0,

    // ========== INFORMAÇÕES ADICIONAIS ==========
    val requisitos: String = "",
    val acao: String = "",
    val alcance: String = "",
    val duracao: String = "",

    // ========== ROLAGENS ==========
    val acerto: String = "",
    val dano: String = ""
) {
    fun isPoder(): Boolean = custoPM > 0

    fun formatarCusto(): String {
        return if (isPoder()) {
            "$custoPM PM"
        } else {
            "Passiva"
        }
    }

    fun getEmoji(): String {
        return when (categoria.lowercase()) {
            "raça", "raca" -> "🧬"
            "classe" -> "⚔️"
            "poder" -> "⚡"
            "origem" -> "🌍"
            "geral" -> "📜"
            else -> "✨"
        }
    }

    fun temInfoCombate(): Boolean {
        return acao.isNotBlank() || alcance.isNotBlank() || duracao.isNotBlank()
    }

    fun formatarInfoCombate(): String {
        val infos = mutableListOf<String>()

        if (acao.isNotBlank()) infos.add("Ação: $acao")
        if (alcance.isNotBlank()) infos.add("Alcance: $alcance")
        if (duracao.isNotBlank()) infos.add("Duração: $duracao")

        return infos.joinToString(" • ")
    }
}
