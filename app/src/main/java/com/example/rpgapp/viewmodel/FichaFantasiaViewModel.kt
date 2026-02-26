package com.example.rpgapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpgapp.data.AppDatabase
import com.example.rpgapp.data.entity.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FichaFantasiaViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val fichaDao = database.fichaFantasiaDao()
    private val periciaDao = database.periciaFantasiaDao()
    private val itemDao = database.itemFantasiaDao()
    private val habilidadeDao = database.habilidadeFantasiaDao()
    private val magiaDao = database.magiaFantasiaDao()

    val ficha: StateFlow<FichaFantasiaEntity?> = fichaDao.getFicha()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val _fichaId = MutableStateFlow(0L)
    val fichaId: StateFlow<Long> = _fichaId

    val pericias: StateFlow<List<PericiaFantasiaEntity>> = fichaId
        .flatMapLatest { id ->
            if (id > 0) periciaDao.getPericiasFromFicha(id)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val itens: StateFlow<List<ItemFantasiaEntity>> = fichaId
        .flatMapLatest { id ->
            if (id > 0) itemDao.getItensFromFicha(id)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val habilidades: StateFlow<List<HabilidadeFantasiaEntity>> = fichaId
        .flatMapLatest { id ->
            if (id > 0) habilidadeDao.getHabilidadesFromFicha(id)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val magias: StateFlow<List<MagiaFantasiaEntity>> = fichaId
        .flatMapLatest { id ->
            if (id > 0) magiaDao.getMagiasFromFicha(id)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Calcula bônus totais dos itens automaticamente
    val bonusTotalArmadura: StateFlow<Int> = itens
        .map { lista -> lista.filter { it.tipo.contains("Armadura", ignoreCase = true) }.sumOf { it.bonusDefesa } }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val bonusTotalEscudo: StateFlow<Int> = itens
        .map { lista -> lista.filter { it.tipo.contains("Escudo", ignoreCase = true) }.sumOf { it.bonusDefesa } }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val outrosBonusDefesaTotal: StateFlow<Int> = itens
        .map { lista -> lista.filter { !it.tipo.contains("Armadura", ignoreCase = true) && !it.tipo.contains("Escudo", ignoreCase = true) }.sumOf { it.bonusDefesa } }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val bonusTotalFortitude: StateFlow<Int> = itens
        .map { lista -> lista.sumOf { it.bonusFortitude } }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val bonusTotalReflexos: StateFlow<Int> = itens
        .map { lista -> lista.sumOf { it.bonusReflexos } }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val bonusTotalVontade: StateFlow<Int> = itens
        .map { lista -> lista.sumOf { it.bonusVontade } }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    init {
        viewModelScope.launch {
            ficha.collect { fichaAtual ->
                if (fichaAtual != null) {
                    _fichaId.value = fichaAtual.id
                } else {
                    val novaFichaId = fichaDao.insertFicha(FichaFantasiaEntity())
                    _fichaId.value = novaFichaId
                }
            }
        }
    }

    fun salvarFichaCompleta(
        nome: String,
        forca: String,
        destreza: String,
        constituicao: String,
        inteligencia: String,
        sabedoria: String,
        carisma: String,
        nivel: String,
        xp: String,
        vidaAtual: String,
        vidaMax: String,
        manaAtual: String,
        manaMax: String,
        deslocamento: String,
        tamanho: String,
        dinheiro: String
    ) {
        viewModelScope.launch {
            val fichaAtual = ficha.value ?: FichaFantasiaEntity()
            fichaDao.updateFicha(
                fichaAtual.copy(
                    nome = nome,
                    forca = forca.toIntOrNull() ?: 0,
                    destreza = destreza.toIntOrNull() ?: 0,
                    constituicao = constituicao.toIntOrNull() ?: 0,
                    inteligencia = inteligencia.toIntOrNull() ?: 0,
                    sabedoria = sabedoria.toIntOrNull() ?: 0,
                    carisma = carisma.toIntOrNull() ?: 0,
                    nivel = nivel.toIntOrNull() ?: 1,
                    xp = xp.toIntOrNull() ?: 0,
                    vidaAtual = vidaAtual.toIntOrNull() ?: 0,
                    vidaMax = vidaMax.toIntOrNull() ?: 0,
                    manaAtual = manaAtual.toIntOrNull() ?: 0,
                    manaMax = manaMax.toIntOrNull() ?: 0,
                    deslocamento = deslocamento,
                    tamanho = tamanho,
                    dinheiro = dinheiro
                )
            )
        }
    }

    fun atualizarDefesas(
        bonusArmadura: Int,
        bonusEscudo: Int,
        outrosBonusDefesa: Int,
        bonusFortitude: Int,
        bonusReflexos: Int,
        bonusVontade: Int
    ) {
        viewModelScope.launch {
            val fichaAtual = ficha.value ?: return@launch
            fichaDao.updateFicha(
                fichaAtual.copy(
                    bonusArmadura = bonusArmadura,
                    bonusEscudo = bonusEscudo,
                    outrosBonusDefesa = outrosBonusDefesa,
                    bonusFortitude = bonusFortitude,
                    bonusReflexos = bonusReflexos,
                    bonusVontade = bonusVontade
                )
            )
        }
    }

    fun consumirPM(valor: Int): Boolean {
        val f = ficha.value ?: return false
        if (f.manaAtual < valor) return false
        
        viewModelScope.launch {
            fichaDao.updateFicha(f.copy(manaAtual = f.manaAtual - valor))
        }
        return true
    }

    fun salvarDescricao(
        nome: String,
        jogador: String,
        raca: String,
        origem: String,
        divindade: String,
        classes: String,
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
                    raca = raca,
                    origem = origem,
                    divindade = divindade,
                    classes = classes,
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
                PericiaFantasiaEntity(
                    fichaId = fichaId.value,
                    nome = nome,
                    atributo = atributo
                )
            )
        }
    }

    fun adicionarPericiaPadrao() {
        val periciasPadrao = listOf(
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Acrobacia", atributo = "DES"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Adestramento", atributo = "CAR"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Atletismo", atributo = "FOR"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Atuação", atributo = "CAR"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Cavalgar", atributo = "DES"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Conhecimento", atributo = "INT"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Cura", atributo = "SAB"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Diplomacia", atributo = "CAR"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Enganação", atributo = "CAR"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Furtividade", atributo = "DES"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Guerra", atributo = "INT"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Iniciativa", atributo = "DES"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Intimidação", atributo = "CAR"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Intuição", atributo = "SAB"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Investigação", atributo = "INT"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Jogatina", atributo = "CAR"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Ladinagem", atributo = "DES"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Luta", atributo = "FOR"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Misticismo", atributo = "INT"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Nobreza", atributo = "INT"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Obter Informação", atributo = "CAR"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Ofício", atributo = "INT"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Percepção", atributo = "SAB"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Pontaria", atributo = "DES"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Reflexos", atributo = "DES"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Religião", atributo = "SAB"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Sobrevivência", atributo = "SAB"),
            PericiaFantasiaEntity(fichaId = fichaId.value, nome = "Vontade", atributo = "SAB")
        )

        viewModelScope.launch {
            periciasPadrao.forEach { pericia ->
                periciaDao.insertPericia(pericia)
            }
        }
    }

    fun atualizarPericia(pericia: PericiaFantasiaEntity) {
        viewModelScope.launch {
            periciaDao.updatePericia(pericia)
        }
    }

    fun deletarPericia(pericia: PericiaFantasiaEntity) {
        viewModelScope.launch {
            periciaDao.deletePericia(pericia)
        }
    }

    fun adicionarItem(
        nome: String,
        quantidade: String,
        descricao: String,
        slots: Int,
        bonusDefesa: Int,
        bonusFortitude: Int,
        bonusReflexos: Int,
        bonusVontade: Int,
        bonusAtributo: String,
        tipo: String,
        acerto: String = "",
        dano: String = ""
    ) {
        viewModelScope.launch {
            itemDao.insertItem(
                ItemFantasiaEntity(
                    fichaId = fichaId.value,
                    nome = nome,
                    quantidade = quantidade,
                    descricao = descricao,
                    slots = slots,
                    bonusDefesa = bonusDefesa,
                    bonusFortitude = bonusFortitude,
                    bonusReflexos = bonusReflexos,
                    bonusVontade = bonusVontade,
                    bonusAtributo = bonusAtributo,
                    tipo = tipo,
                    acerto = acerto,
                    dano = dano
                )
            )
        }
    }

    fun atualizarItem(item: ItemFantasiaEntity) {
        viewModelScope.launch {
            itemDao.updateItem(item)
        }
    }

    fun deletarItem(item: ItemFantasiaEntity) {
        viewModelScope.launch {
            itemDao.deleteItem(item)
        }
    }

    fun adicionarHabilidade(
        nome: String,
        categoria: String,
        descricao: String,
        custoPM: Int,
        requisitos: String,
        acao: String,
        alcance: String,
        duracao: String,
        acerto: String,
        dano: String
    ) {
        viewModelScope.launch {
            habilidadeDao.insertHabilidade(
                HabilidadeFantasiaEntity(
                    fichaId = fichaId.value,
                    nome = nome,
                    categoria = categoria,
                    descricao = descricao,
                    custoPM = custoPM,
                    requisitos = requisitos,
                    acao = acao,
                    alcance = alcance,
                    duracao = duracao,
                    acerto = acerto,
                    dano = dano
                )
            )
        }
    }

    fun atualizarHabilidade(habilidade: HabilidadeFantasiaEntity) {
        viewModelScope.launch {
            habilidadeDao.updateHabilidade(habilidade)
        }
    }

    fun deletarHabilidade(habilidade: HabilidadeFantasiaEntity) {
        viewModelScope.launch {
            habilidadeDao.deleteHabilidade(habilidade)
        }
    }

    fun adicionarMagia(
        nome: String,
        escola: String,
        circulo: Int,
        execucao: String,
        alcance: String,
        area: String,
        duracao: String,
        resistencia: String,
        atributoChave: String,
        efeito: String,
        componentes: String,
        acerto: String,
        dano: String,
        custoPM: Int = 1
    ) {
        viewModelScope.launch {
            magiaDao.insertMagia(
                MagiaFantasiaEntity(
                    fichaId = fichaId.value,
                    nome = nome,
                    escola = escola,
                    circulo = circulo,
                    custoPM = custoPM,
                    execucao = execucao,
                    alcance = alcance,
                    area = area,
                    duracao = duracao,
                    resistencia = resistencia,
                    atributoChave = atributoChave,
                    efeito = efeito,
                    componentes = componentes,
                    acerto = acerto,
                    dano = dano
                )
            )
        }
    }

    fun atualizarMagia(magia: MagiaFantasiaEntity) {
        viewModelScope.launch {
            magiaDao.updateMagia(magia)
        }
    }

    fun deletarMagia(magia: MagiaFantasiaEntity) {
        viewModelScope.launch {
            magiaDao.deleteMagia(magia)
        }
    }
}
