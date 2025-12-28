package com.example.rpgapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "itens_velho_oeste")
data class ItemVelhoOesteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fichaId: Long = 0,
    val nome: String,
    val tipo: String = "Item", // Arma, Item, Equipamento, Cavalo, etc.
    val quantidade: String = "1",

    // Campo de peso em kg
    val peso: Int = 1, // Peso unitário em kg

    val descricao: String = "",

    // Campo de combate
    val dano: String = ""  // Ex: "1d6", "2d8+3", "1d10"
) {
    /**
     * Calcula peso total (quantidade × peso unitário)
     */
    fun pesoTotal(): Int {
        val qtd = quantidade.toIntOrNull() ?: 1
        return qtd * peso
    }
}