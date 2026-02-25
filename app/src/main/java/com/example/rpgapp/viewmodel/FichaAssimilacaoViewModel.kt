package com.example.rpgapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpgapp.data.AppDatabase
import com.example.rpgapp.data.entity.FichaAssimilacaoEntity
import com.example.rpgapp.data.entity.AssimilacaoEntity
import com.example.rpgapp.data.entity.ItemAssimilacaoEntity
import com.example.rpgapp.data.entity.CaracteristicaAssimilacaoEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FichaAssimilacaoViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val fichaDao = database.fichaAssimilacaoDao()
    private val assimilacaoDao = database.assimilacaoDao()
    private val itemDao = database.itemAssimilacaoDao()
    private val caracteristicaDao = database.caracteristicaAssimilacaoDao()

    // ── Ficha principal ──────────────────────────────────────
    val ficha: StateFlow<FichaAssimilacaoEntity?> = fichaDao.getFicha()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val _fichaId = MutableStateFlow(0L)
    val fichaId: StateFlow<Long> = _fichaId

    // ── Listas vinculadas à ficha ────────────────────────────
    val assimilacoes: StateFlow<List<AssimilacaoEntity>> = fichaId
        .flatMapLatest { id ->
            if (id > 0) assimilacaoDao.getAssimilacoesFromFicha(id)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val itens: StateFlow<List<ItemAssimilacaoEntity>> = fichaId
        .flatMapLatest { id ->
            if (id > 0) itemDao.getItensFromFicha(id)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val caracteristicas: StateFlow<List<CaracteristicaAssimilacaoEntity>> = fichaId
        .flatMapLatest { id ->
            if (id > 0) caracteristicaDao.getCaracteristicasFromFicha(id)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val totalPontosCaracteristicas: StateFlow<Int> = fichaId
        .flatMapLatest { id ->
            if (id > 0) caracteristicaDao.getTotalPontos(id).map { it ?: 0 }
            else flowOf(0)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    // Slots usados no inventário
    val slotsUsados: StateFlow<Int> = itens
        .map { lista -> lista.sumOf { it.slotsTotal() } }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    // ── Init: cria ficha se não existir ─────────────────────
    init {
        viewModelScope.launch {
            ficha.collect { fichaAtual ->
                if (fichaAtual != null) {
                    _fichaId.value = fichaAtual.id
                } else {
                    val novaFichaId = fichaDao.insertFicha(FichaAssimilacaoEntity())
                    _fichaId.value = novaFichaId
                }
            }
        }
    }

    // ── Ficha — Nome ─────────────────────────────────────────
    fun salvarNome(nome: String) {
        viewModelScope.launch {
            val fichaAtual = ficha.value ?: return@launch
            fichaDao.updateFicha(fichaAtual.copy(nome = nome))
        }
    }

    // ── Saúde ────────────────────────────────────────────────
    fun atualizarSaude(
        pontosNivel6: Int, pontosNivel5: Int, pontosNivel4: Int,
        pontosNivel3: Int, pontosNivel2: Int, pontosNivel1: Int
    ) {
        viewModelScope.launch {
            val fichaAtual = ficha.value ?: return@launch
            fichaDao.updateFicha(
                fichaAtual.copy(
                    pontosNivel6 = pontosNivel6,
                    pontosNivel5 = pontosNivel5,
                    pontosNivel4 = pontosNivel4,
                    pontosNivel3 = pontosNivel3,
                    pontosNivel2 = pontosNivel2,
                    pontosNivel1 = pontosNivel1
                )
            )
        }
    }

    fun atualizarMaxSaude(
        maxNivel6: Int, maxNivel5: Int, maxNivel4: Int,
        maxNivel3: Int, maxNivel2: Int, maxNivel1: Int
    ) {
        viewModelScope.launch {
            val fichaAtual = ficha.value ?: return@launch
            fichaDao.updateFicha(
                fichaAtual.copy(
                    maxNivel6 = maxNivel6,
                    maxNivel5 = maxNivel5,
                    maxNivel4 = maxNivel4,
                    maxNivel3 = maxNivel3,
                    maxNivel2 = maxNivel2,
                    maxNivel1 = maxNivel1
                )
            )
        }
    }

    // Atualiza apenas o nível de saúde ativo (helper para barra única)
    fun atualizarSaudeNivel(nivel: Int, pontos: Int) {
        viewModelScope.launch {
            val fichaAtual = ficha.value ?: return@launch
            fichaDao.updateFicha(
                when (nivel) {
                    6 -> fichaAtual.copy(pontosNivel6 = pontos)
                    5 -> fichaAtual.copy(pontosNivel5 = pontos)
                    4 -> fichaAtual.copy(pontosNivel4 = pontos)
                    3 -> fichaAtual.copy(pontosNivel3 = pontos)
                    2 -> fichaAtual.copy(pontosNivel2 = pontos)
                    1 -> fichaAtual.copy(pontosNivel1 = pontos)
                    else -> fichaAtual
                }
            )
        }
    }

    // ── Cabo de Guerra ───────────────────────────────────────
    fun atualizarNivelDeterminacao(nivel: Int) {
        viewModelScope.launch {
            val fichaAtual = ficha.value ?: return@launch
            val nivelValido = nivel.coerceIn(0, 10)
            // Ajusta os pontos para não ultrapassar o novo nível
            val pontosDetAjustados = fichaAtual.pontosDeterminacao.coerceAtMost(nivelValido)
            val nivelAssim = 10 - nivelValido
            val pontosAssimAjustados = fichaAtual.pontosAssimilacao.coerceAtMost(nivelAssim)
            fichaDao.updateFicha(
                fichaAtual.copy(
                    nivelDeterminacao = nivelValido,
                    pontosDeterminacao = pontosDetAjustados,
                    pontosAssimilacao = pontosAssimAjustados
                )
            )
        }
    }

    fun atualizarPontosDeterminacao(pontos: Int) {
        viewModelScope.launch {
            val fichaAtual = ficha.value ?: return@launch
            fichaDao.updateFicha(
                fichaAtual.copy(
                    pontosDeterminacao = pontos.coerceIn(0, fichaAtual.nivelDeterminacao)
                )
            )
        }
    }

    fun atualizarPontosAssimilacao(pontos: Int) {
        viewModelScope.launch {
            val fichaAtual = ficha.value ?: return@launch
            fichaDao.updateFicha(
                fichaAtual.copy(
                    pontosAssimilacao = pontos.coerceIn(0, fichaAtual.nivelAssimilacao)
                )
            )
        }
    }

    // ── Aptidões ─────────────────────────────────────────────
    fun salvarAptidoes(
        // Instintos
        influencia: Int, percepcao: Int, potencia: Int,
        reacao: Int, resolucao: Int, sagacidade: Int,
        // Conhecimentos
        biologia: Int, erudicao: Int, engenharia: Int,
        geografia: Int, medicina: Int, seguranca: Int,
        // Práticas
        armas: Int, atletismo: Int, expressao: Int,
        furtividade: Int, manufaturas: Int, sobrevivencia: Int
    ) {
        viewModelScope.launch {
            val fichaAtual = ficha.value ?: return@launch
            fichaDao.updateFicha(
                fichaAtual.copy(
                    influencia = influencia, percepcao = percepcao,
                    potencia = potencia, reacao = reacao,
                    resolucao = resolucao, sagacidade = sagacidade,
                    biologia = biologia, erudicao = erudicao,
                    engenharia = engenharia, geografia = geografia,
                    medicina = medicina, seguranca = seguranca,
                    armas = armas, atletismo = atletismo,
                    expressao = expressao, furtividade = furtividade,
                    manufaturas = manufaturas, sobrevivencia = sobrevivencia
                )
            )
        }
    }

    // ── Descrição ────────────────────────────────────────────
    fun salvarDescricao(
        nome: String, jogador: String,
        eventoMarcante: String, ocupacao: String,
        proposito1: String, proposito2: String, propositoColetivo: String,
        aparencia: String, personalidade: String,
        historia: String, anotacoes: String
    ) {
        viewModelScope.launch {
            val fichaAtual = ficha.value ?: return@launch
            fichaDao.updateFicha(
                fichaAtual.copy(
                    nome = nome, jogador = jogador,
                    eventoMarcante = eventoMarcante, ocupacao = ocupacao,
                    proposito1 = proposito1, proposito2 = proposito2,
                    propositoColetivo = propositoColetivo,
                    aparencia = aparencia, personalidade = personalidade,
                    historia = historia, anotacoes = anotacoes
                )
            )
        }
    }

    // ── Assimilações ─────────────────────────────────────────
    fun adicionarAssimilacao(nome: String, tipo: String, descricao: String) {
        viewModelScope.launch {
            assimilacaoDao.insertAssimilacao(
                AssimilacaoEntity(
                    fichaId = fichaId.value,
                    nome = nome,
                    tipo = tipo,
                    descricao = descricao
                )
            )
        }
    }

    fun atualizarAssimilacao(assimilacao: AssimilacaoEntity) {
        viewModelScope.launch {
            assimilacaoDao.updateAssimilacao(assimilacao)
        }
    }

    fun deletarAssimilacao(assimilacao: AssimilacaoEntity) {
        viewModelScope.launch {
            assimilacaoDao.deleteAssimilacao(assimilacao)
        }
    }

    // ── Inventário ───────────────────────────────────────────
    fun adicionarItem(nome: String, quantidade: String, descricao: String, slots: Int) {
        viewModelScope.launch {
            itemDao.insertItem(
                ItemAssimilacaoEntity(
                    fichaId = fichaId.value,
                    nome = nome,
                    quantidade = quantidade,
                    descricao = descricao,
                    slots = slots
                )
            )
        }
    }

    fun atualizarItem(item: ItemAssimilacaoEntity) {
        viewModelScope.launch {
            itemDao.updateItem(item)
        }
    }

    fun deletarItem(item: ItemAssimilacaoEntity) {
        viewModelScope.launch {
            itemDao.deleteItem(item)
        }
    }

    // ── Características ──────────────────────────────────────
    fun adicionarCaracteristica(
        nome: String, custo: Int, requisitos: String, descricao: String
    ) {
        viewModelScope.launch {
            caracteristicaDao.insertCaracteristica(
                CaracteristicaAssimilacaoEntity(
                    fichaId = fichaId.value,
                    nome = nome,
                    custo = custo,
                    requisitos = requisitos,
                    descricao = descricao
                )
            )
        }
    }

    fun atualizarCaracteristica(caracteristica: CaracteristicaAssimilacaoEntity) {
        viewModelScope.launch {
            caracteristicaDao.updateCaracteristica(caracteristica)
        }
    }

    fun deletarCaracteristica(caracteristica: CaracteristicaAssimilacaoEntity) {
        viewModelScope.launch {
            caracteristicaDao.deleteCaracteristica(caracteristica)
        }
    }
}