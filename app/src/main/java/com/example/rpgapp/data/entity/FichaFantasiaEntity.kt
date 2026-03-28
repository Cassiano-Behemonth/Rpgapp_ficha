package com.example.rpgapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fichas_fantasia")
data class FichaFantasiaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // ========== ATRIBUTOS (Tormenta 20) ==========
    val forca: Int = 0,
    val destreza: Int = 0,
    val constituicao: Int = 0,
    val inteligencia: Int = 0,
    val sabedoria: Int = 0,
    val carisma: Int = 0,

    // ========== RECURSOS ==========
    val vidaAtual: Int = 0,
    val vidaMax: Int = 0,
    val manaAtual: Int = 0,
    val manaMax: Int = 0,

    // ========== PROGRESSÃO ==========
    val nivel: Int = 1,
    val xp: Int = 0,

    // ========== DEFESAS (bônus adicionais) ==========
    val bonusArmadura: Int = 0,
    val bonusEscudo: Int = 0,
    val outrosBonusDefesa: Int = 0,
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

    // Modificadores de atributos (Tormenta20 JdA)
    fun modForca(): Int = forca
    fun modDestreza(): Int = destreza
    fun modConstituicao(): Int = constituicao
    fun modInteligencia(): Int = inteligencia
    fun modSabedoria(): Int = sabedoria
    fun modCarisma(): Int = carisma

    // Defesas (10 + metade do nível + mod + bônus)
    fun defesa(): Int = 10 + (nivel / 2) + modDestreza() + bonusArmadura + bonusEscudo + outrosBonusDefesa
    fun fortitude(): Int = 10 + (nivel / 2) + modConstituicao() + bonusFortitude
    fun reflexos(): Int = 10 + (nivel / 2) + modDestreza() + bonusReflexos
    fun vontade(): Int = 10 + (nivel / 2) + modSabedoria() + bonusVontade

    // Detalhamento simplificado da defesa (apenas bônus)
    fun defesaBonusIcon(): String {
        val totalDefesaExtra = bonusArmadura + bonusEscudo + outrosBonusDefesa
        return if (totalDefesaExtra > 0) "🛡️ +$totalDefesaExtra" else ""
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

    // Limite de carga normal (10 + (FOR*2 se FOR > 0 else FOR) + bônus)
    fun limiteCarga(): Int {
        val forcaVal = forca
        val spacesDaForca = if (forcaVal > 0) forcaVal * 2 else forcaVal
        return 10 + spacesDaForca + limiteCargaBonus
    }

    // Limite de carga máximo absoluto (o dobro do limite normal)
    fun limiteCargaMaximo(): Int {
        return limiteCarga() * 2
    }

    // Formatar modificador com sinal (+/-)
    fun formatarMod(valor: Int): String {
        return if (valor >= 0) "+$valor" else "$valor"
    }
}
