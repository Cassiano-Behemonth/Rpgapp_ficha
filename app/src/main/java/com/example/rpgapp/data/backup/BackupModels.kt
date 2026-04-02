@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)
package com.example.rpgapp.data.backup

import kotlinx.serialization.Serializable
import kotlinx.serialization.InternalSerializationApi

@Serializable
data class FichaBackupWrapper(
    val version: Int = 1,
    val modo: String, // "HORROR", "WEST", "ASSIMILACAO", "FANTASIA"
    val dataHorror: HorrorBackup? = null,
    val dataWest: WestBackup? = null,
    val dataAssimilacao: AssimilacaoBackup? = null,
    val dataFantasia: FantasiaBackup? = null
)

// --- MODO HORROR ---
@Serializable
data class HorrorBackup(
    val ficha: FichaDTO,
    val pericias: List<PericiaDTO>,
    val itens: List<ItemDTO>
)

@Serializable
data class FichaDTO(
    val forca: Int, val agilidade: Int, val presenca: Int, val nex: Int,
    val vidaAtual: Int, val vidaMax: Int, val sanidadeAtual: Int, val sanidadeMax: Int,
    val nome: String, val jogador: String, val origem: String, val classe: String,
    val trilha: String, val patente: String, val aparencia: String,
    val personalidade: String, val historia: String, val anotacoes: String
)

@Serializable
data class PericiaDTO(val nome: String, val atributo: String, val treino: Boolean = false, val vantagem: Boolean = false, val desvantagem: Boolean = false)

@Serializable
data class ItemDTO(val nome: String, val quantidade: String, val descricao: String)

// --- MODO VELHO OESTE ---
@Serializable
data class WestBackup(
    val ficha: FichaWestDTO,
    val antecedentes: List<AntecedenteWestDTO>,
    val habilidades: List<HabilidadeWestDTO>,
    val itens: List<ItemWestDTO>
)

@Serializable
data class FichaWestDTO(
    val fisico: Int, val velocidade: Int, val intelecto: Int, val coragem: Int, val defesa: Int,
    val vidaAtual: Int, val dorAtual: Int, val dinheiro: String, val nome: String,
    val jogador: String, val arquetipo: String, val origem: String, val reputacao: String,
    val aparencia: String, val personalidade: String, val historia: String, val anotacoes: String,
    val selosMorteBonus: Int = 0, val pesoBonus: Int = 0
)

@Serializable
data class AntecedenteWestDTO(val nome: String, val pontos: Int)

@Serializable
data class HabilidadeWestDTO(val nome: String, val descricao: String, val danoOuDado: String)

@Serializable
data class ItemWestDTO(val nome: String, val tipo: String, val quantidade: String, val descricao: String, val peso: Int, val dano: String)

// --- MODO ASSIMILAÇÃO ---
@Serializable
data class AssimilacaoBackup(
    val ficha: FichaAssimilacaoDTO,
    val assimilacoes: List<AssimilacaoDTO>,
    val itens: List<ItemAssimilacaoDTO>,
    val caracteristicas: List<CaracteristicaAssimilacaoDTO>
)

@Serializable
data class FichaAssimilacaoDTO(
    val nome: String, val jogador: String,
    val pontosNivel6: Int, val pontosNivel5: Int, val pontosNivel4: Int,
    val pontosNivel3: Int, val pontosNivel2: Int, val pontosNivel1: Int,
    val maxNivel6: Int, val maxNivel5: Int, val maxNivel4: Int,
    val maxNivel3: Int, val maxNivel2: Int, val maxNivel1: Int,
    val nivelDeterminacao: Int, val pontosDeterminacao: Int, val pontosAssimilacao: Int,
    val influencia: Int, val percepcao: Int, val potencia: Int,
    val reacao: Int, val resolucao: Int, val sagacidade: Int,
    val biologia: Int, val erudicao: Int, val engenharia: Int,
    val geografia: Int, val medicina: Int, val seguranca: Int,
    val armas: Int, val atletismo: Int, val expressao: Int,
    val furtividade: Int, val manufaturas: Int, val sobrevivencia: Int,
    val eventoMarcante: String, val ocupacao: String,
    val proposito1: String, val proposito2: String, val propositoColetivo: String,
    val aparencia: String, val personalidade: String, val historia: String, val anotacoes: String
)

@Serializable
data class AssimilacaoDTO(val nome: String, val tipo: String, val descricao: String)

@Serializable
data class ItemAssimilacaoDTO(val nome: String, val quantidade: String, val descricao: String, val slots: Int)

@Serializable
data class CaracteristicaAssimilacaoDTO(val nome: String, val custo: Int, val requisitos: String, val descricao: String)

// --- MODO FANTASIA ---
@Serializable
data class FantasiaBackup(
    val ficha: FichaFantasiaDTO,
    val pericias: List<PericiaFantasiaDTO>,
    val itens: List<ItemFantasiaDTO>,
    val habilidades: List<HabilidadeFantasiaDTO>,
    val magias: List<MagiaFantasiaDTO>
)

@Serializable
data class FichaFantasiaDTO(
    val forca: Int, val destreza: Int, val constituicao: Int,
    val inteligencia: Int, val sabedoria: Int, val carisma: Int,
    val vidaAtual: Int, val vidaMax: Int, val manaAtual: Int, val manaMax: Int,
    val nivel: Int, val xp: Int, val bonusArmadura: Int, val bonusEscudo: Int,
    val outrosBonusDefesa: Int, val bonusFortitude: Int, val bonusReflexos: Int,
    val bonusVontade: Int, val deslocamento: String, val tamanho: String,
    val penalidadeArmadura: Int, val limiteCargaBonus: Int, val dinheiro: String,
    val nome: String, val jogador: String, val raca: String, val origem: String,
    val divindade: String, val classes: String, val aparencia: String,
    val personalidade: String, val historia: String, val anotacoes: String
)

@Serializable
data class PericiaFantasiaDTO(val nome: String, val atributo: String, val treinada: Boolean, val vantagem: Boolean, val desvantagem: Boolean, val bonus: Int)

@Serializable
data class ItemFantasiaDTO(
    val nome: String, val quantidade: String, val descricao: String, val slots: Int,
    val bonusDefesa: Int, val bonusFortitude: Int, val bonusReflexos: Int,
    val bonusVontade: Int, val bonusAtributo: String, val tipo: String,
    val acerto: String, val dano: String
)

@Serializable
data class HabilidadeFantasiaDTO(
    val nome: String, val categoria: String, val descricao: String, val custoPM: Int,
    val requisitos: String, val acao: String, val alcance: String, val duracao: String,
    val acerto: String, val dano: String
)

@Serializable
data class MagiaFantasiaDTO(
    val nome: String, val escola: String, val circulo: Int, val custoPM: Int,
    val execucao: String, val alcance: String, val area: String, val duracao: String,
    val resistencia: String, val atributoChave: String, val efeito: String,
    val componentes: String, val acerto: String, val dano: String
)
