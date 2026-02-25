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

    // Flow reativo para a UI
    val ficha: StateFlow<FichaAssimilacaoEntity?> = fichaDao.getFicha()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _fichaId = MutableStateFlow(0L)
    val fichaId: StateFlow<Long> = _fichaId

    // ── Listas vinculadas à ficha ────────────────────────────
    val assimilacoes: StateFlow<List<AssimilacaoEntity>> = fichaId
        .flatMapLatest { id ->
            if (id > 0) assimilacaoDao.getAssimilacoesFromFicha(id)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val itens: StateFlow<List<ItemAssimilacaoEntity>> = fichaId
        .flatMapLatest { id ->
            if (id > 0) itemDao.getItensFromFicha(id)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val caracteristicas: StateFlow<List<CaracteristicaAssimilacaoEntity>> = fichaId
        .flatMapLatest { id ->
            if (id > 0) caracteristicaDao.getCaracteristicasFromFicha(id)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalPontosCaracteristicas: StateFlow<Int> = fichaId
        .flatMapLatest { id ->
            if (id > 0) caracteristicaDao.getTotalPontos(id).map { it ?: 0 }
            else flowOf(0)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val slotsUsados: StateFlow<Int> = itens
        .map { lista -> lista.sumOf { it.slotsTotal() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // ── CORREÇÃO PRINCIPAL ───────────────────────────────────
    // Todas as funções de update leem do banco diretamente via getFichaOnce()
    // ao invés de usar ficha.value (cache do StateFlow).
    //
    // O bug: ficha é StateFlow com SharingStarted.Lazily. Quando ninguém
    // coleta ativamente, o cache pode ficar desatualizado. Ao fazer
    // fichaAtual.copy(maxNivel6 = 10) sobre um cache com maxNivel6 = 5,
    // o resultado é salvar 5 de volta — sobrescrevendo o que o jogador digitou.
    // getFichaOnce() lê o valor real do SQLite, sem cache.
    private suspend fun fichaAtual(): FichaAssimilacaoEntity? = fichaDao.getFichaOnce()

    // ── Init: cria ficha se não existir ─────────────────────
    init {
        viewModelScope.launch {
            val existente = fichaDao.getFichaOnce()
            if (existente != null) {
                _fichaId.value = existente.id
            } else {
                val novoId = fichaDao.insertFicha(FichaAssimilacaoEntity())
                _fichaId.value = novoId
            }
        }
    }

    // ── Nome ─────────────────────────────────────────────────
    fun salvarNome(nome: String) {
        viewModelScope.launch {
            val f = fichaAtual() ?: return@launch
            fichaDao.updateFicha(f.copy(nome = nome))
        }
    }

    // ── Saúde ────────────────────────────────────────────────
    fun atualizarSaude(
        pontosNivel6: Int, pontosNivel5: Int, pontosNivel4: Int,
        pontosNivel3: Int, pontosNivel2: Int, pontosNivel1: Int
    ) {
        viewModelScope.launch {
            val f = fichaAtual() ?: return@launch
            fichaDao.updateFicha(
                f.copy(
                    pontosNivel6 = pontosNivel6, pontosNivel5 = pontosNivel5,
                    pontosNivel4 = pontosNivel4, pontosNivel3 = pontosNivel3,
                    pontosNivel2 = pontosNivel2, pontosNivel1 = pontosNivel1
                )
            )
        }
    }

    fun atualizarMaxSaude(
        maxNivel6: Int, maxNivel5: Int, maxNivel4: Int,
        maxNivel3: Int, maxNivel2: Int, maxNivel1: Int
    ) {
        viewModelScope.launch {
            val f = fichaAtual() ?: return@launch
            fichaDao.updateFicha(
                f.copy(
                    maxNivel6 = maxNivel6, maxNivel5 = maxNivel5,
                    maxNivel4 = maxNivel4, maxNivel3 = maxNivel3,
                    maxNivel2 = maxNivel2, maxNivel1 = maxNivel1
                )
            )
        }
    }

    // ── Atualiza max E pontos numa única transação atômica ───
    // IMPORTANTE: use esta função no onMaxChange da UI.
    // Chamar atualizarMaxSaude() + atualizarSaude() separados causa race condition:
    // a segunda coroutine lê o banco antes da primeira terminar de escrever,
    // pega o valor antigo, e sobrescreve o max que você acabou de salvar.
    fun atualizarMaxESaude(novoMax: Int) {
        viewModelScope.launch {
            val f = fichaAtual() ?: return@launch
            fichaDao.updateFicha(
                f.copy(
                    maxNivel6 = novoMax, maxNivel5 = novoMax,
                    maxNivel4 = novoMax, maxNivel3 = novoMax,
                    maxNivel2 = novoMax, maxNivel1 = novoMax,
                    pontosNivel6 = novoMax, pontosNivel5 = novoMax,
                    pontosNivel4 = novoMax, pontosNivel3 = novoMax,
                    pontosNivel2 = novoMax, pontosNivel1 = novoMax
                )
            )
        }
    }

    fun atualizarSaudeNivel(nivel: Int, pontos: Int) {
        viewModelScope.launch {
            val f = fichaAtual() ?: return@launch
            fichaDao.updateFicha(
                when (nivel) {
                    6 -> f.copy(pontosNivel6 = pontos)
                    5 -> f.copy(pontosNivel5 = pontos)
                    4 -> f.copy(pontosNivel4 = pontos)
                    3 -> f.copy(pontosNivel3 = pontos)
                    2 -> f.copy(pontosNivel2 = pontos)
                    1 -> f.copy(pontosNivel1 = pontos)
                    else -> f
                }
            )
        }
    }

    // ── Cabo de Guerra ───────────────────────────────────────
    fun atualizarNivelDeterminacao(nivel: Int) {
        viewModelScope.launch {
            val f = fichaAtual() ?: return@launch
            val nivelValido = nivel.coerceIn(0, 10)
            val nivelAssim = 10 - nivelValido
            fichaDao.updateFicha(
                f.copy(
                    nivelDeterminacao  = nivelValido,
                    pontosDeterminacao = f.pontosDeterminacao.coerceAtMost(nivelValido),
                    pontosAssimilacao  = f.pontosAssimilacao.coerceAtMost(nivelAssim)
                )
            )
        }
    }

    fun atualizarPontosDeterminacao(pontos: Int) {
        viewModelScope.launch {
            val f = fichaAtual() ?: return@launch
            fichaDao.updateFicha(f.copy(pontosDeterminacao = pontos.coerceIn(0, f.nivelDeterminacao)))
        }
    }

    fun atualizarPontosAssimilacao(pontos: Int) {
        viewModelScope.launch {
            val f = fichaAtual() ?: return@launch
            fichaDao.updateFicha(f.copy(pontosAssimilacao = pontos.coerceIn(0, f.nivelAssimilacao)))
        }
    }

    // ── Aptidões ─────────────────────────────────────────────
    fun salvarAptidoes(
        influencia: Int, percepcao: Int, potencia: Int,
        reacao: Int, resolucao: Int, sagacidade: Int,
        biologia: Int, erudicao: Int, engenharia: Int,
        geografia: Int, medicina: Int, seguranca: Int,
        armas: Int, atletismo: Int, expressao: Int,
        furtividade: Int, manufaturas: Int, sobrevivencia: Int
    ) {
        viewModelScope.launch {
            val f = fichaAtual() ?: return@launch
            fichaDao.updateFicha(
                f.copy(
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
            val f = fichaAtual() ?: return@launch
            fichaDao.updateFicha(
                f.copy(
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
                AssimilacaoEntity(fichaId = fichaId.value, nome = nome, tipo = tipo, descricao = descricao)
            )
        }
    }

    fun atualizarAssimilacao(assimilacao: AssimilacaoEntity) {
        viewModelScope.launch { assimilacaoDao.updateAssimilacao(assimilacao) }
    }

    fun deletarAssimilacao(assimilacao: AssimilacaoEntity) {
        viewModelScope.launch { assimilacaoDao.deleteAssimilacao(assimilacao) }
    }

    // ── Inventário ───────────────────────────────────────────
    fun adicionarItem(nome: String, quantidade: String, descricao: String, slots: Int) {
        viewModelScope.launch {
            itemDao.insertItem(
                ItemAssimilacaoEntity(fichaId = fichaId.value, nome = nome, quantidade = quantidade, descricao = descricao, slots = slots)
            )
        }
    }

    fun atualizarItem(item: ItemAssimilacaoEntity) {
        viewModelScope.launch { itemDao.updateItem(item) }
    }

    fun deletarItem(item: ItemAssimilacaoEntity) {
        viewModelScope.launch { itemDao.deleteItem(item) }
    }

    // ── Características ──────────────────────────────────────
    fun adicionarCaracteristica(nome: String, custo: Int, requisitos: String, descricao: String) {
        viewModelScope.launch {
            caracteristicaDao.insertCaracteristica(
                CaracteristicaAssimilacaoEntity(fichaId = fichaId.value, nome = nome, custo = custo, requisitos = requisitos, descricao = descricao)
            )
        }
    }

    fun atualizarCaracteristica(caracteristica: CaracteristicaAssimilacaoEntity) {
        viewModelScope.launch { caracteristicaDao.updateCaracteristica(caracteristica) }
    }

    fun deletarCaracteristica(caracteristica: CaracteristicaAssimilacaoEntity) {
        viewModelScope.launch { caracteristicaDao.deleteCaracteristica(caracteristica) }
    }
}