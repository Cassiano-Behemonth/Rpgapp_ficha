package com.example.rpgapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "caracteristicas_assimilacao")
data class CaracteristicaAssimilacaoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fichaId: Long = 0,

    // Nome da característica — ex: "Punhos de Ferro", "Sono Leve"
    val nome: String = "",

    // Custo em pontos (1 a 5)
    val custo: Int = 1,

    // Requisitos — ex: "Potência 2+, Resolução 2+"
    val requisitos: String = "",

    // Descrição do efeito mecânico
    val descricao: String = ""
)