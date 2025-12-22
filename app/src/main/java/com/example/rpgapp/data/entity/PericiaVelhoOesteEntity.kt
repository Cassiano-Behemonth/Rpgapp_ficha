package com.example.rpgapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pericias_velho_oeste")
data class PericiaVelhoOesteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fichaId: Long = 0,
    val nome: String,
    val atributo: String, // PON, VIG, ESP, CAR, REF
    val treino: Boolean = false,
    val bonus: Int = 0
)