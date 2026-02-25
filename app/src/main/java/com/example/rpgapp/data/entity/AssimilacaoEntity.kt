package com.example.rpgapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assimilacoes")
data class AssimilacaoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fichaId: Long = 0,

    // Nome da mutação — ex: "Fotossíntese", "Visão Noturna"
    val nome: String = "",

    // Tipo da mutação
    // "Evolutiva" | "Adaptativa" | "Inoportuna" | "Singular"
    val tipo: String = "Evolutiva",

    // Descrição do efeito narrativo/mecânico
    val descricao: String = ""
)