package com.example.rpgapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habilidades_velho_oeste")
data class HabilidadeVelhoOesteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fichaId: Long = 0,
    val nome: String,
    val descricao: String = "",
    val danoOuDado: String = "" // Campo livre para texto como "1d6", "+2", "2d8+3", etc
)