package com.example.rpgapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fichas_velho_oeste")
data class FichaVelhoOesteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Atributos principais
    val fisico: Int = 0,
    val velocidade: Int = 0,
    val intelecto: Int = 0,
    val coragem: Int = 0,
    val defesa: Int = 0,

    // Sistema de vida (círculos preenchidos)
    val vidaAtual: Int = 6, // Quantidade de círculos preenchidos

    // Sistema de dor (0-6 pontos fixos)
    val dorAtual: Int = 0, // Quantidade de pontos de dor marcados

    // Dinheiro
    val dinheiro: String = "",

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
) {
    // Vida máxima = 6 base + pontos em Físico
    val vidaMaxima: Int
        get() = 6 + fisico
}