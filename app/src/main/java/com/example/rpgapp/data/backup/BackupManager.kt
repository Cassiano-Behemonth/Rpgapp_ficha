package com.example.rpgapp.data.backup

import com.example.rpgapp.data.AppDatabase
import com.example.rpgapp.data.entity.*
import com.example.rpgapp.ui.screens.GameMode
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import androidx.room.withTransaction

class BackupManager(private val db: AppDatabase) {

    private val jsonConfig = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    suspend fun exportModeData(mode: GameMode): String {
        return when (mode) {
            GameMode.INVESTIGACAO_HORROR -> {
                val ficha = db.fichaDao().getFichaOnce() ?: return ""
                val pericias = db.periciaDao().getPericiasOnce(ficha.id)
                val itens = db.itemDao().getItensOnce(ficha.id)
                
                val backup = FichaBackupWrapper(
                    modo = "HORROR",
                    dataHorror = HorrorBackup(
                        ficha = ficha.toDTO(),
                        pericias = pericias.map { it.toDTO() },
                        itens = itens.map { it.toDTO() }
                    )
                )
                jsonConfig.encodeToString(backup)
            }
            GameMode.VELHO_OESTE -> {
                val ficha = db.fichaVelhoOesteDao().getFichaOnce() ?: return ""
                val id = ficha.id
                val backup = FichaBackupWrapper(
                    modo = "WEST",
                    dataWest = WestBackup(
                        ficha = ficha.toDTO(),
                        antecedentes = db.antecedenteVelhoOesteDao().getAntecedentesOnce(id).map { it.toDTO() },
                        habilidades = db.habilidadeVelhoOesteDao().getHabilidadesOnce(id).map { it.toDTO() },
                        itens = db.itemVelhoOesteDao().getItensOnce(id).map { it.toDTO() }
                    )
                )
                jsonConfig.encodeToString(backup)
            }
            GameMode.ASSIMILACAO -> {
                val ficha = db.fichaAssimilacaoDao().getFichaOnce() ?: return ""
                val id = ficha.id
                val backup = FichaBackupWrapper(
                    modo = "ASSIMILACAO",
                    dataAssimilacao = AssimilacaoBackup(
                        ficha = ficha.toDTO(),
                        assimilacoes = db.assimilacaoDao().getAssimilacoesOnce(id).map { it.toDTO() },
                        itens = db.itemAssimilacaoDao().getItensOnce(id).map { it.toDTO() },
                        caracteristicas = db.caracteristicaAssimilacaoDao().getCaracteristicasOnce(id).map { it.toDTO() }
                    )
                )
                jsonConfig.encodeToString(backup)
            }
            GameMode.FANTASIA -> {
                val ficha = db.fichaFantasiaDao().getFichaOnce() ?: return ""
                val id = ficha.id
                val backup = FichaBackupWrapper(
                    modo = "FANTASIA",
                    dataFantasia = FantasiaBackup(
                        ficha = ficha.toDTO(),
                        pericias = db.periciaFantasiaDao().getPericiasOnce(id).map { it.toDTO() },
                        itens = db.itemFantasiaDao().getItensOnce(id).map { it.toDTO() },
                        habilidades = db.habilidadeFantasiaDao().getHabilidadesOnce(id).map { it.toDTO() },
                        magias = db.magiaFantasiaDao().getMagiasOnce(id).map { it.toDTO() }
                    )
                )
                jsonConfig.encodeToString(backup)
            }
        }
    }

    suspend fun importData(jsonString: String): GameMode? {
        val wrapper = try {
            jsonConfig.decodeFromString<FichaBackupWrapper>(jsonString)
        } catch (e: Exception) {
            return null
        }

        return when (wrapper.modo) {
            "HORROR" -> {
                val data = wrapper.dataHorror ?: return null
                db.withTransaction {
                    db.fichaDao().deleteAll()
                    val newFichaId = db.fichaDao().insertFicha(data.ficha.toEntity())
                    data.pericias.forEach { db.periciaDao().insertPericia(it.toEntity(newFichaId)) }
                    data.itens.forEach { db.itemDao().insertItem(it.toEntity(newFichaId)) }
                }
                GameMode.INVESTIGACAO_HORROR
            }
            "WEST" -> {
                val data = wrapper.dataWest ?: return null
                db.withTransaction {
                    db.fichaVelhoOesteDao().deleteAll()
                    val newFichaId = db.fichaVelhoOesteDao().insertFicha(data.ficha.toEntity())
                    data.antecedentes.forEach { db.antecedenteVelhoOesteDao().insertAntecedente(it.toEntity(newFichaId)) }
                    data.habilidades.forEach { db.habilidadeVelhoOesteDao().insertHabilidade(it.toEntity(newFichaId)) }
                    data.itens.forEach { db.itemVelhoOesteDao().insertItem(it.toEntity(newFichaId)) }
                }
                GameMode.VELHO_OESTE
            }
            "ASSIMILACAO" -> {
                val data = wrapper.dataAssimilacao ?: return null
                db.withTransaction {
                    db.fichaAssimilacaoDao().deleteAll()
                    val newFichaId = db.fichaAssimilacaoDao().insertFicha(data.ficha.toEntity())
                    data.assimilacoes.forEach { db.assimilacaoDao().insertAssimilacao(it.toEntity(newFichaId)) }
                    data.itens.forEach { db.itemAssimilacaoDao().insertItem(it.toEntity(newFichaId)) }
                    data.caracteristicas.forEach { db.caracteristicaAssimilacaoDao().insertCaracteristica(it.toEntity(newFichaId)) }
                }
                GameMode.ASSIMILACAO
            }
            "FANTASIA" -> {
                val data = wrapper.dataFantasia ?: return null
                db.withTransaction {
                    db.fichaFantasiaDao().deleteAll()
                    val newFichaId = db.fichaFantasiaDao().insertFicha(data.ficha.toEntity())
                    data.pericias.forEach { db.periciaFantasiaDao().insertPericia(it.toEntity(newFichaId)) }
                    data.itens.forEach { db.itemFantasiaDao().insertItem(it.toEntity(newFichaId)) }
                    data.habilidades.forEach { db.habilidadeFantasiaDao().insertHabilidade(it.toEntity(newFichaId)) }
                    data.magias.forEach { db.magiaFantasiaDao().insertMagia(it.toEntity(newFichaId)) }
                }
                GameMode.FANTASIA
            }
            else -> null
        }
    }

    // --- MAPPERS HORROR ---
    private fun FichaEntity.toDTO() = FichaDTO(forca, agilidade, presenca, nex, vidaAtual, vidaMax, sanidadeAtual, sanidadeMax, nome, jogador, origem, classe, trilha, patente, aparencia, personalidade, historia, anotacoes)
    private fun FichaDTO.toEntity() = FichaEntity(forca = forca, agilidade = agilidade, presenca = presenca, nex = nex, vidaAtual = vidaAtual, vidaMax = vidaMax, sanidadeAtual = sanidadeAtual, sanidadeMax = sanidadeMax, nome = nome, jogador = jogador, origem = origem, classe = classe, trilha = trilha, patente = patente, aparencia = aparencia, personalidade = personalidade, historia = historia, anotacoes = anotacoes)
    private fun PericiaEntity.toDTO() = PericiaDTO(nome, atributo, treino, vantagem, desvantagem)
    private fun PericiaDTO.toEntity(fichaId: Long) = PericiaEntity(fichaId = fichaId, nome = nome, atributo = atributo, treino = treino, vantagem = vantagem, desvantagem = desvantagem)
    private fun ItemEntity.toDTO() = ItemDTO(nome, quantidade, descricao)
    private fun ItemDTO.toEntity(fichaId: Long) = ItemEntity(fichaId = fichaId, nome = nome, quantidade = quantidade, descricao = descricao)

    // --- MAPPERS WEST ---
    private fun FichaVelhoOesteEntity.toDTO() = FichaWestDTO(fisico, velocidade, intelecto, coragem, defesa, vidaAtual, dorAtual, dinheiro, nome, jogador, arquetipo, origem, reputacao, aparencia, personalidade, historia, anotacoes, selosMorteBonus, pesoBonus)
    private fun FichaWestDTO.toEntity() = FichaVelhoOesteEntity(fisico = fisico, velocidade = velocidade, intelecto = intelecto, coragem = coragem, defesa = defesa, vidaAtual = vidaAtual, dorAtual = dorAtual, dinheiro = dinheiro, nome = nome, jogador = jogador, arquetipo = arquetipo, origem = origem, reputacao = reputacao, aparencia = aparencia, personalidade = personalidade, historia = historia, anotacoes = anotacoes, selosMorteBonus = selosMorteBonus, pesoBonus = pesoBonus)
    private fun AntecedenteVelhoOesteEntity.toDTO() = AntecedenteWestDTO(nome, pontos)
    private fun AntecedenteWestDTO.toEntity(fichaId: Long) = AntecedenteVelhoOesteEntity(fichaId = fichaId, nome = nome, pontos = pontos)
    private fun HabilidadeVelhoOesteEntity.toDTO() = HabilidadeWestDTO(nome, descricao, danoOuDado)
    private fun HabilidadeWestDTO.toEntity(fichaId: Long) = HabilidadeVelhoOesteEntity(fichaId = fichaId, nome = nome, descricao = descricao, danoOuDado = danoOuDado)
    private fun ItemVelhoOesteEntity.toDTO() = ItemWestDTO(nome, tipo, quantidade, descricao, peso, dano)
    private fun ItemWestDTO.toEntity(fichaId: Long) = ItemVelhoOesteEntity(fichaId = fichaId, nome = nome, tipo = tipo, quantidade = quantidade, descricao = descricao, peso = peso, dano = dano)

    // --- MAPPERS ASSIMILACAO ---
    private fun FichaAssimilacaoEntity.toDTO() = FichaAssimilacaoDTO(nome, jogador, pontosNivel6, pontosNivel5, pontosNivel4, pontosNivel3, pontosNivel2, pontosNivel1, maxNivel6, maxNivel5, maxNivel4, maxNivel3, maxNivel2, maxNivel1, nivelDeterminacao, pontosDeterminacao, pontosAssimilacao, influencia, percepcao, potencia, reacao, resolucao, sagacidade, biologia, erudicao, engenharia, geografia, medicina, seguranca, armas, atletismo, expressao, furtividade, manufaturas, sobrevivencia, eventoMarcante, ocupacao, proposito1, proposito2, propositoColetivo, aparencia, personalidade, historia, anotacoes)
    private fun FichaAssimilacaoDTO.toEntity() = FichaAssimilacaoEntity(nome = nome, jogador = jogador, pontosNivel6 = pontosNivel6, pontosNivel5 = pontosNivel5, pontosNivel4 = pontosNivel4, pontosNivel3 = pontosNivel3, pontosNivel2 = pontosNivel2, pontosNivel1 = pontosNivel1, maxNivel6 = maxNivel6, maxNivel5 = maxNivel5, maxNivel4 = maxNivel4, maxNivel3 = maxNivel3, maxNivel2 = maxNivel2, maxNivel1 = maxNivel1, nivelDeterminacao = nivelDeterminacao, pontosDeterminacao = pontosDeterminacao, pontosAssimilacao = pontosAssimilacao, influencia = influencia, percepcao = percepcao, potencia = potencia, reacao = reacao, resolucao = resolucao, sagacidade = sagacidade, biologia = biologia, erudicao = erudicao, engenharia = engenharia, geografia = geografia, medicina = medicina, seguranca = seguranca, armas = armas, atletismo = atletismo, expressao = expressao, furtividade = furtividade, manufaturas = manufaturas, sobrevivencia = sobrevivencia, eventoMarcante = eventoMarcante, ocupacao = ocupacao, proposito1 = proposito1, proposito2 = proposito2, propositoColetivo = propositoColetivo, aparencia = aparencia, personalidade = personalidade, historia = historia, anotacoes = anotacoes)
    private fun AssimilacaoEntity.toDTO() = AssimilacaoDTO(nome, tipo, descricao)
    private fun AssimilacaoDTO.toEntity(fichaId: Long) = AssimilacaoEntity(fichaId = fichaId, nome = nome, tipo = tipo, descricao = descricao)
    private fun ItemAssimilacaoEntity.toDTO() = ItemAssimilacaoDTO(nome, quantidade, descricao, slots)
    private fun ItemAssimilacaoDTO.toEntity(fichaId: Long) = ItemAssimilacaoEntity(fichaId = fichaId, nome = nome, quantidade = quantidade, descricao = descricao, slots = slots)
    private fun CaracteristicaAssimilacaoEntity.toDTO() = CaracteristicaAssimilacaoDTO(nome, custo, requisitos, descricao)
    private fun CaracteristicaAssimilacaoDTO.toEntity(fichaId: Long) = CaracteristicaAssimilacaoEntity(fichaId = fichaId, nome = nome, custo = custo, requisitos = requisitos, descricao = descricao)

    // --- MAPPERS FANTASIA ---
    private fun FichaFantasiaEntity.toDTO() = FichaFantasiaDTO(forca, destreza, constituicao, inteligencia, sabedoria, carisma, vidaAtual, vidaMax, manaAtual, manaMax, nivel, xp, bonusArmadura, bonusEscudo, outrosBonusDefesa, bonusFortitude, bonusReflexos, bonusVontade, deslocamento, tamanho, penalidade_armadura, limiteCargaBonus, dinheiro, nome, jogador, raca, origem, divindade, classes, aparencia, personalidade, historia, anotacoes)
    private fun FichaFantasiaDTO.toEntity() = FichaFantasiaEntity(forca = forca, destreza = destreza, constituicao = constituicao, inteligencia = inteligencia, sabedoria = sabedoria, carisma = carisma, vidaAtual = vidaAtual, vidaMax = vidaMax, manaAtual = manaAtual, manaMax = manaMax, nivel = nivel, xp = xp, bonusArmadura = bonusArmadura, bonusEscudo = bonusEscudo, outrosBonusDefesa = outrosBonusDefesa, bonusFortitude = bonusFortitude, bonusReflexos = bonusReflexos, bonusVontade = bonusVontade, deslocamento = deslocamento, tamanho = tamanho, penalidade_armadura = penalidadeArmadura, limiteCargaBonus = limiteCargaBonus, dinheiro = dinheiro, nome = nome, jogador = jogador, raca = raca, origem = origem, divindade = divindade, classes = classes, aparencia = aparencia, personalidade = personalidade, historia = historia, anotacoes = anotacoes)
    private fun PericiaFantasiaEntity.toDTO() = PericiaFantasiaDTO(nome, atributo, treinada, vantagem, desvantagem, bonus)
    private fun PericiaFantasiaDTO.toEntity(fichaId: Long) = PericiaFantasiaEntity(fichaId = fichaId, nome = nome, atributo = atributo, treinada = treinada, vantagem = vantagem, desvantagem = desvantagem, bonus = bonus)
    private fun ItemFantasiaEntity.toDTO() = ItemFantasiaDTO(nome, quantidade, descricao, slots, bonusDefesa, bonusFortitude, bonusReflexos, bonusVontade, bonusAtributo, tipo, acerto, dano)
    private fun ItemFantasiaDTO.toEntity(fichaId: Long) = ItemFantasiaEntity(fichaId = fichaId, nome = nome, quantidade = quantidade, descricao = descricao, slots = slots, bonusDefesa = bonusDefesa, bonusFortitude = bonusFortitude, bonusReflexos = bonusReflexos, bonusVontade = bonusVontade, bonusAtributo = bonusAtributo, tipo = tipo, acerto = acerto, dano = dano)
    private fun HabilidadeFantasiaEntity.toDTO() = HabilidadeFantasiaDTO(nome, categoria, descricao, custoPM, requisitos, acao, alcance, duracao, acerto, dano)
    private fun HabilidadeFantasiaDTO.toEntity(fichaId: Long) = HabilidadeFantasiaEntity(fichaId = fichaId, nome = nome, categoria = categoria, descricao = descricao, custoPM = custoPM, requisitos = requisitos, acao = acao, alcance = alcance, duracao = duracao, acerto = acerto, dano = dano)
    private fun MagiaFantasiaEntity.toDTO() = MagiaFantasiaDTO(nome, escola, circulo, custoPM, execucao, alcance, area, duracao, resistencia, atributoChave, efeito, componentes, acerto, dano)
    private fun MagiaFantasiaDTO.toEntity(fichaId: Long) = MagiaFantasiaEntity(fichaId = fichaId, nome = nome, escola = escola, circulo = circulo, custoPM = custoPM, execucao = execucao, alcance = alcance, area = area, duracao = duracao, resistencia = resistencia, atributoChave = atributoChave, efeito = efeito, componentes = componentes, acerto = acerto, dano = dano)
}
