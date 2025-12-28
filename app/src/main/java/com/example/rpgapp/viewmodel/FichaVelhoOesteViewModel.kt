package com.example.rpgapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpgapp.data.AppDatabase
import com.example.rpgapp.data.entity.FichaVelhoOesteEntity
import com.example.rpgapp.data.entity.AntecedenteVelhoOesteEntity
import com.example.rpgapp.data.entity.HabilidadeVelhoOesteEntity
import com.example.rpgapp.data.entity.ItemVelhoOesteEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FichaVelhoOesteViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val fichaDao = database.fichaVelhoOesteDao()
    private val antecedenteDao = database.antecedenteVelhoOesteDao()
    private val habilidadeDao = database.habilidadeVelhoOesteDao()
    private val itemDao = database.itemVelhoOesteDao()

    val ficha: StateFlow<FichaVelhoOesteEntity?> = fichaDao.getFicha()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val _fichaId = MutableStateFlow(0L)
    val fichaId: StateFlow<Long> = _fichaId

    val antecedentes: StateFlow<List<AntecedenteVelhoOesteEntity>> = fichaId
        .flatMapLatest { id ->
            if (id > 0) antecedenteDao.getAntecedentesFromFicha(id)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val habilidades: StateFlow<List<HabilidadeVelhoOesteEntity>> = fichaId
        .flatMapLatest { id ->
            if (id > 0) habilidadeDao.getHabilidadesFromFicha(id)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val itens: StateFlow<List<ItemVelhoOesteEntity>> = fichaId
        .flatMapLatest { id ->
            if (id > 0) itemDao.getItensFromFicha(id)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        viewModelScope.launch {
            ficha.collect { fichaAtual ->
                if (fichaAtual != null) {
                    _fichaId.value = fichaAtual.id
                } else {
                    val novaFichaId = fichaDao.insertFicha(FichaVelhoOesteEntity())
                    _fichaId.value = novaFichaId
                }
            }
        }
    }

    fun salvarFicha(
        fisico: String,
        velocidade: String,
        intelecto: String,
        coragem: String,
        vidaAtual: Int,
        dorAtual: Int
    ) {
        viewModelScope.launch {
            val fichaAtual = ficha.value ?: FichaVelhoOesteEntity()
            fichaDao.updateFicha(
                fichaAtual.copy(
                    fisico = fisico.toIntOrNull() ?: 0,
                    velocidade = velocidade.toIntOrNull() ?: 0,
                    intelecto = intelecto.toIntOrNull() ?: 0,
                    coragem = coragem.toIntOrNull() ?: 0,
                    vidaAtual = vidaAtual,
                    dorAtual = dorAtual
                )
            )
        }
    }

    fun salvarFichaCompleta(
        nome: String,
        fisico: String,
        velocidade: String,
        intelecto: String,
        coragem: String,
        defesa: String,
        dinheiro: String,
        vidaAtual: Int,
        dorAtual: Int
    ) {
        viewModelScope.launch {
            val fichaAtual = ficha.value ?: FichaVelhoOesteEntity()
            fichaDao.updateFicha(
                fichaAtual.copy(
                    nome = nome,
                    fisico = fisico.toIntOrNull() ?: 0,
                    velocidade = velocidade.toIntOrNull() ?: 0,
                    intelecto = intelecto.toIntOrNull() ?: 0,
                    coragem = coragem.toIntOrNull() ?: 0,
                    defesa = defesa.toIntOrNull() ?: 0,
                    dinheiro = dinheiro,
                    vidaAtual = vidaAtual,
                    dorAtual = dorAtual
                )
            )
        }
    }

    fun atualizarVida(vidaAtual: Int) {
        viewModelScope.launch {
            val fichaAtual = ficha.value ?: return@launch
            fichaDao.updateFicha(fichaAtual.copy(vidaAtual = vidaAtual))
        }
    }

    fun atualizarDor(dorAtual: Int) {
        viewModelScope.launch {
            val fichaAtual = ficha.value ?: return@launch
            fichaDao.updateFicha(fichaAtual.copy(dorAtual = dorAtual))
        }
    }

    fun atualizarSelosBonus(bonus: Int) {
        viewModelScope.launch {
            val fichaAtual = ficha.value ?: return@launch
            fichaDao.updateFicha(fichaAtual.copy(selosMorteBonus = bonus))
        }
    }

    fun atualizarPesoBonus(bonus: Int) {
        viewModelScope.launch {
            val fichaAtual = ficha.value ?: return@launch
            fichaDao.updateFicha(fichaAtual.copy(pesoBonus = bonus))
        }
    }

    fun salvarDescricao(
        nome: String,
        jogador: String,
        arquetipo: String,
        origem: String,
        reputacao: String,
        aparencia: String,
        personalidade: String,
        historia: String,
        anotacoes: String
    ) {
        viewModelScope.launch {
            val fichaAtual = ficha.value ?: return@launch
            fichaDao.updateFicha(
                fichaAtual.copy(
                    nome = nome,
                    jogador = jogador,
                    arquetipo = arquetipo,
                    origem = origem,
                    reputacao = reputacao,
                    aparencia = aparencia,
                    personalidade = personalidade,
                    historia = historia,
                    anotacoes = anotacoes
                )
            )
        }
    }

    fun adicionarAntecedente(nome: String, pontos: Int = 0) {
        viewModelScope.launch {
            antecedenteDao.insertAntecedente(
                AntecedenteVelhoOesteEntity(
                    fichaId = fichaId.value,
                    nome = nome,
                    pontos = pontos
                )
            )
        }
    }

    fun atualizarAntecedente(antecedente: AntecedenteVelhoOesteEntity) {
        viewModelScope.launch {
            antecedenteDao.updateAntecedente(antecedente)
        }
    }

    fun deletarAntecedente(antecedente: AntecedenteVelhoOesteEntity) {
        viewModelScope.launch {
            antecedenteDao.deleteAntecedente(antecedente)
        }
    }

    fun adicionarHabilidade(nome: String, descricao: String, danoOuDado: String) {
        viewModelScope.launch {
            habilidadeDao.insertHabilidade(
                HabilidadeVelhoOesteEntity(
                    fichaId = fichaId.value,
                    nome = nome,
                    descricao = descricao,
                    danoOuDado = danoOuDado
                )
            )
        }
    }

    fun atualizarHabilidade(habilidade: HabilidadeVelhoOesteEntity) {
        viewModelScope.launch {
            habilidadeDao.updateHabilidade(habilidade)
        }
    }

    fun deletarHabilidade(habilidade: HabilidadeVelhoOesteEntity) {
        viewModelScope.launch {
            habilidadeDao.deleteHabilidade(habilidade)
        }
    }

    /**
     * Adiciona item com suporte a dano
     */
    fun adicionarItem(
        nome: String,
        tipo: String,
        quantidade: String,
        peso: Int,
        descricao: String,
        dano: String
    ) {
        viewModelScope.launch {
            itemDao.insertItem(
                ItemVelhoOesteEntity(
                    fichaId = fichaId.value,
                    nome = nome,
                    tipo = tipo,
                    quantidade = quantidade,
                    peso = peso,
                    descricao = descricao,
                    dano = dano
                )
            )
        }
    }

    fun atualizarItem(item: ItemVelhoOesteEntity) {
        viewModelScope.launch {
            itemDao.updateItem(item)
        }
    }

    fun deletarItem(item: ItemVelhoOesteEntity) {
        viewModelScope.launch {
            itemDao.deleteItem(item)
        }
    }
}