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
    val descricao: String = ""
)