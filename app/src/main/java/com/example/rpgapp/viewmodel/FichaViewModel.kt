package com.example.rpgapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpgapp.data.AppDatabase
import com.example.rpgapp.data.entity.FichaEntity
import com.example.rpgapp.data.entity.PericiaEntity
import com.example.rpgapp.data.entity.ItemEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FichaViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val fichaDao = database.fichaDao()
    private val periciaDao = database.periciaDao()
    private val itemDao = database.itemDao()

    val ficha: StateFlow<FichaEntity?> = fichaDao.getFicha()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val _fichaId = MutableStateFlow(0L)
    val fichaId: StateFlow<Long> = _fichaId

    val pericias: StateFlow<List<PericiaEntity>> = fichaId
        .flatMapLatest { id ->
            if (id > 0) periciaDao.getPericiasFromFicha(id)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val itens: StateFlow<List<ItemEntity>> = fichaId
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
                    val novaFichaId = fichaDao.insertFicha(FichaEntity())
                    _fichaId.value = novaFichaId
                }
            }
        }
    }

    fun salvarFicha(
        forca: String,
        agilidade: String,
        presenca: String,
        nex: String,
        vidaAtual: String,
        vidaMax: String,
        sanidadeAtual: String,
        sanidadeMax: String
    ) {
        viewModelScope.launch {
            val fichaAtual = ficha.value ?: FichaEntity()
            fichaDao.updateFicha(
                fichaAtual.copy(
                    forca = forca.toIntOrNull() ?: 0,
                    agilidade = agilidade.toIntOrNull() ?: 0,
                    presenca = presenca.toIntOrNull() ?: 0,
                    nex = nex.toIntOrNull() ?: 5,
                    vidaAtual = vidaAtual.toIntOrNull() ?: 0,
                    vidaMax = vidaMax.toIntOrNull() ?: 0,
                    sanidadeAtual = sanidadeAtual.toIntOrNull() ?: 0,
                    sanidadeMax = sanidadeMax.toIntOrNull() ?: 0
                )
            )
        }
    }

    fun salvarDescricao(
        nome: String,
        jogador: String,
        origem: String,
        classe: String,
        trilha: String,
        patente: String,
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
                    origem = origem,
                    classe = classe,
                    trilha = trilha,
                    patente = patente,
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
                PericiaEntity(
                    fichaId = fichaId.value,
                    nome = nome,
                    atributo = atributo
                )
            )
        }
    }

    fun atualizarPericia(pericia: PericiaEntity) {
        viewModelScope.launch {
            periciaDao.updatePericia(pericia)
        }
    }

    fun deletarPericia(pericia: PericiaEntity) {
        viewModelScope.launch {
            periciaDao.deletePericia(pericia)
        }
    }

    fun adicionarItem(nome: String, quantidade: String, descricao: String) {
        viewModelScope.launch {
            itemDao.insertItem(
                ItemEntity(
                    fichaId = fichaId.value,
                    nome = nome,
                    quantidade = quantidade,
                    descricao = descricao
                )
            )
        }
    }

    fun atualizarItem(item: ItemEntity) {
        viewModelScope.launch {
            itemDao.updateItem(item)
        }
    }

    fun deletarItem(item: ItemEntity) {
        viewModelScope.launch {
            itemDao.deleteItem(item)
        }
    }
}