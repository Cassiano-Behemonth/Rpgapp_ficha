package com.example.rpgapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "antecedentes_velho_oeste")
data class AntecedenteVelhoOesteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fichaId: Long = 0,
    val nome: String,
    val pontos: Int = 0 // Quantidade de pontos investidos (0-3 ou 0-5, por exemplo)
)