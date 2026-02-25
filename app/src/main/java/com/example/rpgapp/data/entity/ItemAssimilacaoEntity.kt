package com.example.rpgapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "itens_assimilacao")
data class ItemAssimilacaoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fichaId: Long = 0,

    // Nome do item — ex: "Faca enferrujada", "Cantil"
    val nome: String = "",

    // Quantidade — ex: "1", "3", "x2"
    val quantidade: String = "1",

    // Descrição livre
    val descricao: String = "",

    // Slots ocupados por este item (padrão 1)
    val slots: Int = 1
) {
    // Slots totais ocupados (quantidade × slots unitários)
    fun slotsTotal(): Int {
        val qtd = quantidade.toIntOrNull() ?: 1
        return qtd * slots
    }
}