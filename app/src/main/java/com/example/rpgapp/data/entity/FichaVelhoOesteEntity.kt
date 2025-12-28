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
    val vidaAtual: Int = 0, // Quantidade de círculos preenchidos (começa vazio)

    // Sistema de dor (0-6 pontos fixos)
    val dorAtual: Int = 0, // Quantidade de pontos de dor marcados

    // NOVO: Bônus extra de selos de morte (adicionado manualmente)
    val selosMorteBonus: Int = 0,

    // NOVO: Bônus extra de peso (itens mágicos, etc)
    val pesoBonus: Int = 0,

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
    /**
     * MUDANÇA: Vida máxima agora inclui bônus manual
     *
     * 6 base + Físico + Bônus manual
     *
     * Exemplo:
     * - Físico 2, bônus 0 = 6 + 2 = 8 selos
     * - Físico 2, bônus 3 = 6 + 2 + 3 = 11 selos
     */
    val vidaMaxima: Int
        get() = 6 + fisico + selosMorteBonus

    /**
     * NOVO: Sistema de peso da mochila
     *
     * 15 kg base + Físico + Bônus extra
     *
     * Exemplo:
     * - Físico 2, bônus 0 = 15 + 2 = 17 kg
     * - Físico 2, bônus 5 = 15 + 2 + 5 = 22 kg
     */
    val pesoMaximo: Int
        get() = 15 + fisico + pesoBonus
}