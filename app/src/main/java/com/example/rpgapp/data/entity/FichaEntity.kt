package com.example.rpgapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fichas")
data class FichaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val forca: Int = 0,
    val agilidade: Int = 0,
    val presenca: Int = 0,
    val nex: Int = 5,
    val vidaAtual: Int = 0,
    val vidaMax: Int = 0,
    val sanidadeAtual: Int = 0,
    val sanidadeMax: Int = 0,
    val nome: String = "",
    val jogador: String = "",
    val origem: String = "",
    val classe: String = "",
    val trilha: String = "",
    val patente: String = "",
    val aparencia: String = "",
    val personalidade: String = "",
    val historia: String = "",
    val anotacoes: String = ""
)