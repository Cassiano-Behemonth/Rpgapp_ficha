package com.example.rpgapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpgapp.data.AppDatabase
import com.example.rpgapp.data.entity.FichaVelhoOesteEntity
import com.example.rpgapp.data.entity.PericiaVelhoOesteEntity
import com.example.rpgapp.data.entity.ItemVelhoOesteEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FichaVelhoOesteViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val fichaDao = database.fichaVelhoOesteDao()
    private val periciaDao = database.periciaVelhoOesteDao()
    private val itemDao = database.itemVelhoOesteDao()

    val ficha: StateFlow<FichaVelhoOesteEntity?> = fichaDao.getFicha()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val _fichaId = MutableStateFlow(0L)
    val fichaId: StateFlow<Long> = _fichaId

    val pericias: StateFlow<List<PericiaVelhoOesteEntity>> = fichaId
        .flatMapLatest { id ->
            if (id > 0) periciaDao.getPericiasFromFicha(id)
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
                    // Cria ficha inicial
                    val novaFichaId = fichaDao.insertFicha(FichaVelhoOesteEntity())
                    _fichaId.value = novaFichaId
                }
            }
        }
    }

    fun salvarFicha(
        pontaria: String,
        vigor: String,
        esperteza: String,
        carisma: String,
        reflexos: String,
        vidaAtual: String,
        vidaMax: String,
        municao: String,
        dinheiro: String
    ) {
        viewModelScope.launch {
            val fichaAtual = ficha.value ?: FichaVelhoOesteEntity()
            fichaDao.updateFicha(
                fichaAtual.copy(
                    pontaria = pontaria.toIntOrNull() ?: 0,
                    vigor = vigor.toIntOrNull() ?: 0,
                    esperteza = esperteza.toIntOrNull() ?: 0,
                    carisma = carisma.toIntOrNull() ?: 0,
                    reflexos = reflexos.toIntOrNull() ?: 0,
                    vidaAtual = vidaAtual.toIntOrNull() ?: 0,
                    vidaMax = vidaMax.toIntOrNull() ?: 0,
                    municao = municao.toIntOrNull() ?: 0,
                    dinheiro = dinheiro
                )
            )
        }
    }

    fun salvarFichaCompleta(
        nome: String,
        pontaria: String,
        vigor: String,
        esperteza: String,
        carisma: String,
        reflexos: String,
        vidaAtual: String,
        vidaMax: String,
        municao: String,
        dinheiro: String
    ) {
        viewModelScope.launch {
            val fichaAtual = ficha.value ?: FichaVelhoOesteEntity()
            fichaDao.updateFicha(
                fichaAtual.copy(
                    nome = nome,
                    pontaria = pontaria.toIntOrNull() ?: 0,
                    vigor = vigor.toIntOrNull() ?: 0,
                    esperteza = esperteza.toIntOrNull() ?: 0,
                    carisma = carisma.toIntOrNull() ?: 0,
                    reflexos = reflexos.toIntOrNull() ?: 0,
                    vidaAtual = vidaAtual.toIntOrNull() ?: 0,
                    vidaMax = vidaMax.toIntOrNull() ?: 0,
                    municao = municao.toIntOrNull() ?: 0,
                    dinheiro = dinheiro
                )
            )
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

    fun adicionarPericia(nome: String, atributo: String) {
        viewModelScope.launch {
            periciaDao.insertPericia(
                PericiaVelhoOesteEntity(
                    fichaId = fichaId.value,
                    nome = nome,
                    atributo = atributo
                )
            )
        }
    }

    fun atualizarPericia(pericia: PericiaVelhoOesteEntity) {
        viewModelScope.launch {
            periciaDao.updatePericia(pericia)
        }
    }

    fun deletarPericia(pericia: PericiaVelhoOesteEntity) {
        viewModelScope.launch {
            periciaDao.deletePericia(pericia)
        }
    }

    fun adicionarItem(nome: String, tipo: String, quantidade: String, descricao: String) {
        viewModelScope.launch {
            itemDao.insertItem(
                ItemVelhoOesteEntity(
                    fichaId = fichaId.value,
                    nome = nome,
                    tipo = tipo,
                    quantidade = quantidade,
                    descricao = descricao
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