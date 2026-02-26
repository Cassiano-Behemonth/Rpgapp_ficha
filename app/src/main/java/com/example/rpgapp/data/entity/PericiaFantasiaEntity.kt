package com.example.rpgapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pericias_fantasia")
data class PericiaFantasiaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fichaId: Long = 0,

    // ========== DADOS DA PERÍCIA ==========
    val nome: String,
    val atributo: String, // FOR, DES, CON, INT, SAB, CAR

    // ========== MODIFICADORES ==========
    val treinada: Boolean = false,
    val vantagem: Boolean = false,
    val desvantagem: Boolean = false,
    val bonus: Int = 0
) {
    // ========== CÁLCULO DO MODIFICADOR ==========
    /**
     * Calcula o modificador total da perícia
     * @param modAtributo: modificador do atributo associado
     * @param nivel: nível total do personagem
     * @return modificador total
     */
    fun calcularModificador(modAtributo: Int, nivel: Int): Int {
        val modBase = if (treinada) {
            modAtributo + nivel + bonus
        } else {
            modAtributo + bonus
        }
        return modBase
    }

    // ========== FORMATAÇÃO ==========
    fun formatarModificador(modAtributo: Int, nivel: Int): String {
        val mod = calcularModificador(modAtributo, nivel)
        return if (mod >= 0) "+$mod" else "$mod"
    }

    // ========== VALIDAÇÃO ==========
    /**
     * Vantagem e desvantagem não podem estar ativas ao mesmo tempo
     */
    fun isValid(): Boolean {
        return !(vantagem && desvantagem)
    }

    // ========== STATUS ==========
    fun getStatusRolagem(): String {
        return when {
            vantagem -> "Vantagem"
            desvantagem -> "Desvantagem"
            else -> "Normal"
        }
    }
}
