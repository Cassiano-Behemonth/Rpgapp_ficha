package com.example.rpgapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "itens_fantasia")
data class ItemFantasiaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fichaId: Long = 0,

    val nome: String,
    val quantidade: String = "1",
    val descricao: String = "",
    val slots: Int = 1,
    val bonusDefesa: Int = 0, // General defense bonus from item
    val bonusFortitude: Int = 0,
    val bonusReflexos: Int = 0,
    val bonusVontade: Int = 0,
    val bonusAtributo: String = "",
    val tipo: String = "Geral",
    val acerto: String = "",
    val dano: String = ""
) {
    fun slotsTotal(): Int {
        val qtd = quantidade.toIntOrNull() ?: 1
        return qtd * slots
    }

    fun temBonusDefesa(): Boolean = bonusDefesa != 0
    fun temBonusFortitude(): Boolean = bonusFortitude != 0
    fun temBonusReflexos(): Boolean = bonusReflexos != 0
    fun temBonusVontade(): Boolean = bonusVontade != 0
    fun temBonusAtributo(): Boolean = bonusAtributo.isNotBlank()

    fun parseBonusAtributo(): Pair<String, Int>? {
        if (!temBonusAtributo()) return null
        val regex = Regex("(FOR|DES|CON|INT|SAB|CAR)([+\\-])(\\d+)")
        val match = regex.find(bonusAtributo) ?: return null
        val atributo = match.groupValues[1]
        val sinal = match.groupValues[2]
        val valor = match.groupValues[3].toIntOrNull() ?: 0
        val valorFinal = if (sinal == "-") -valor else valor
        return Pair(atributo, valorFinal)
    }

    fun formatarBonusAtributo(): String {
        val bonus = parseBonusAtributo() ?: return ""
        val (attr, valor) = bonus
        val sinal = if (valor >= 0) "+" else ""
        return "$attr $sinal$valor"
    }

    fun formatarBonusDefesa(): String {
        if (!temBonusDefesa()) return ""
        val sinal = if (bonusDefesa >= 0) "+" else ""
        return "Defesa $sinal$bonusDefesa"
    }

    fun formatarBonusFortitude(): String {
        if (!temBonusFortitude()) return ""
        val sinal = if (bonusFortitude >= 0) "+" else ""
        return "Fort $sinal$bonusFortitude"
    }

    fun formatarBonusReflexos(): String {
        if (!temBonusReflexos()) return ""
        val sinal = if (bonusReflexos >= 0) "+" else ""
        return "Ref $sinal$bonusReflexos"
    }

    fun formatarBonusVontade(): String {
        if (!temBonusVontade()) return ""
        val sinal = if (bonusVontade >= 0) "+" else ""
        return "Vont $sinal$bonusVontade"
    }

    fun formatarTodosBonus(): String {
        val bonus = mutableListOf<String>()
        if (temBonusDefesa()) bonus.add(formatarBonusDefesa())
        if (temBonusFortitude()) bonus.add(formatarBonusFortitude())
        if (temBonusReflexos()) bonus.add(formatarBonusReflexos())
        if (temBonusVontade()) bonus.add(formatarBonusVontade())
        if (temBonusAtributo()) bonus.add(formatarBonusAtributo())
        return bonus.joinToString(", ")
    }
}
