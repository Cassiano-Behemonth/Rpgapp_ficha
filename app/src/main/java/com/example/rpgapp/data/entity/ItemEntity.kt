// Arquivo: app/src/main/java/com/example/rpgapp/data/entity/ItemEntity.kt
package com.example.rpgapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "itens")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fichaId: Long = 0,
    val nome: String,
    val quantidade: String = "1",
    val descricao: String = ""
)