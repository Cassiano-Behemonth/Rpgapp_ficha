package com.example.rpgapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fichas_velho_oeste")
data class FichaVelhoOesteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Atributos principais
    val pontaria: Int = 0,
    val vigor: Int = 0,
    val esperteza: Int = 0,
    val carisma: Int = 0,
    val reflexos: Int = 0,

    // Recursos
    val vidaAtual: Int = 0,
    val vidaMax: Int = 0,
    val municao: Int = 0,
    val dinheiro: String = "0",

    // Informações do personagem
    val nome: String = "",
    val jogador: String = "",
    val arquetipo: String = "", // Ex: Pistoleiro, Xerife, Fora da Lei, Jogador, etc.
    val origem: String = "",
    val reputacao: String = "", // Ex: Herói, Neutro, Bandido

    // Descrição
    val aparencia: String = "",
    val personalidade: String = "",
    val historia: String = "",
    val anotacoes: String = ""
)