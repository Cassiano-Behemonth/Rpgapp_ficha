package com.example.rpgapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fichas_fantasia")
data class FichaFantasiaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // ========== ATRIBUTOS (Tormenta 20) ==========
    val forca: Int = 10,
    val destreza: Int = 10,
    val constituicao: Int = 10,
    val inteligencia: Int = 10,
    val sabedoria: Int = 10,
    val carisma: Int = 10,

    // ========== RECURSOS ==========
    val vidaAtual: Int = 0,
    val vidaMax: Int = 0,
    val manaAtual: Int = 0,
    val manaMax: Int = 0,

    // ========== PROGRESSÃO ==========
    val nivel: Int = 1,
    val xp: Int = 0,

    // ========== DEFESAS (bônus adicionais) ==========
    val bonusDefesa: Int = 0,
    val bonusFortitude: Int = 0,
    val bonusReflexos: Int = 0,
    val bonusVontade: Int = 0,

    // ========== OUTROS ==========
    val deslocamento: String = "9m",
    val tamanho: String = "Médio",
    val penalidade_armadura: Int = 0,
    val limiteCargaBonus: Int = 0,
    val dinheiro: String = "0", // Tibares (T$)

    // ========== INFORMAÇÕES DO PERSONAGEM ==========
    val nome: String = "",
    val jogador: String = "",
    val raca: String = "",
    val origem: String = "",
    val divindade: String = "",
    val classes: String = "", // Ex: "Guerreiro 5, Paladino 3"

    // ========== DESCRITIVOS ==========
    val aparencia: String = "",
    val personalidade: String = "",
    val historia: String = "",
    val anotacoes: String = ""
) {
    // ========== CÁLCULOS AUTOMÁTICOS ==========

    // Modificadores de atributos (Tormenta20)
    fun modForca(): Int = calcularModificador(forca)
    fun modDestreza(): Int = calcularModificador(destreza)
    fun modConstituicao(): Int = calcularModificador(constituicao)
    fun modInteligencia(): Int = calcularModificador(inteligencia)
    fun modSabedoria(): Int = calcularModificador(sabedoria)
    fun modCarisma(): Int = calcularModificador(carisma)

    private fun calcularModificador(valorAtributo: Int): Int {
        return when (valorAtributo) {
            in 1..3 -> -2
            in 4..7 -> -1
            in 8..13 -> 0
            in 14..17 -> 1
            in 18..19 -> 2
            in 20..21 -> 3
            in 22..23 -> 4
            else -> if (valorAtributo >= 24) (valorAtributo - 20) / 2 + 3 else -2
        }
    }

    // Defesas (10 + mod + bônus)
    fun defesa(): Int = 10 + modDestreza() + bonusDefesa
    fun fortitude(): Int = 10 + modConstituicao() + bonusFortitude
    fun reflexos(): Int = 10 + modDestreza() + bonusReflexos
    fun vontade(): Int = 10 + modSabedoria() + bonusVontade

    // Detalhamento simplificado da defesa (apenas bônus)
    fun defesaBonusIcon(): String {
        return if (bonusDefesa > 0) "🛡️ +$bonusDefesa" else ""
    }

    fun fortitudeBonusIcon(): String {
        return if (bonusFortitude > 0) "💪 +$bonusFortitude" else ""
    }

    fun reflexosBonusIcon(): String {
        return if (bonusReflexos > 0) "⚡ +$bonusReflexos" else ""
    }

    fun vontadeBonusIcon(): String {
        return if (bonusVontade > 0) "🧠 +$bonusVontade" else ""
    }

    // Limite de carga (FOR + bônus)
    fun limiteCarga(): Int = forca + limiteCargaBonus

    // Formatar modificador com sinal (+/-)
    fun formatarMod(valor: Int): String {
        return if (valor >= 0) "+$valor" else "$valor"
    }
}
