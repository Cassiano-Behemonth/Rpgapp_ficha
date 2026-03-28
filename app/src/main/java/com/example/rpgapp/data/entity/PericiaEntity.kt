// Arquivo: app/src/main/java/com/example/rpgapp/data/entity/PericiaEntity.kt
package com.example.rpgapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pericias")
data class PericiaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fichaId: Long = 0,
    val nome: String,
    val atributo: String,
    val treino: Boolean = false,
    val vantagem: Boolean = false,
    val desvantagem: Boolean = false,
    val bonus: Int = 0
) {
    fun getStatusRolagem(): String {
        return when {
            vantagem -> "Vantagem"
            desvantagem -> "Desvantagem"
            else -> "Normal"
        }
    }
}