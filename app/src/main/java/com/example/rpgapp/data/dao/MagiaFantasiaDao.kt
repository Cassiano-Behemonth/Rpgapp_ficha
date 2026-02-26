package com.example.rpgapp.data.dao

import androidx.room.*
import com.example.rpgapp.data.entity.MagiaFantasiaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MagiaFantasiaDao {

    // ========== CONSULTAS ==========

    /**
     * Obtém todas as magias de uma ficha, ordenadas por círculo e nome
     */
    @Query("SELECT * FROM magias_fantasia WHERE fichaId = :fichaId ORDER BY circulo, nome")
    fun getMagiasFromFicha(fichaId: Long): Flow<List<MagiaFantasiaEntity>>

    /**
     * Obtém magias por círculo específico
     */
    @Query("SELECT * FROM magias_fantasia WHERE fichaId = :fichaId AND circulo = :circulo ORDER BY nome")
    fun getMagiasPorCirculo(fichaId: Long, circulo: Int): Flow<List<MagiaFantasiaEntity>>

    /**
     * Obtém magias por escola específica
     */
    @Query("SELECT * FROM magias_fantasia WHERE fichaId = :fichaId AND escola = :escola ORDER BY circulo, nome")
    fun getMagiasPorEscola(fichaId: Long, escola: String): Flow<List<MagiaFantasiaEntity>>

    /**
     * Obtém apenas truques (círculo 0)
     */
    @Query("SELECT * FROM magias_fantasia WHERE fichaId = :fichaId AND circulo = 0 ORDER BY nome")
    fun getTruques(fichaId: Long): Flow<List<MagiaFantasiaEntity>>

    /**
     * Obtém magias de círculo 1 ou superior
     */
    @Query("SELECT * FROM magias_fantasia WHERE fichaId = :fichaId AND circulo > 0 ORDER BY circulo, nome")
    fun getMagiasNaoTruques(fichaId: Long): Flow<List<MagiaFantasiaEntity>>

    /**
     * Busca magias por nome
     */
    @Query("SELECT * FROM magias_fantasia WHERE fichaId = :fichaId AND nome LIKE '%' || :busca || '%' ORDER BY circulo, nome")
    fun buscarMagias(fichaId: Long, busca: String): Flow<List<MagiaFantasiaEntity>>

    /**
     * Conta total de magias
     */
    @Query("SELECT COUNT(*) FROM magias_fantasia WHERE fichaId = :fichaId")
    fun contarMagias(fichaId: Long): Flow<Int>

    /**
     * Conta magias por círculo
     */
    @Query("SELECT COUNT(*) FROM magias_fantasia WHERE fichaId = :fichaId AND circulo = :circulo")
    fun contarMagiasPorCirculo(fichaId: Long, circulo: Int): Flow<Int>

    /**
     * Obtém lista de círculos com magias (para filtros)
     */
    @Query("SELECT DISTINCT circulo FROM magias_fantasia WHERE fichaId = :fichaId ORDER BY circulo")
    fun getCirculosComMagias(fichaId: Long): Flow<List<Int>>

    /**
     * Obtém lista de escolas com magias (para filtros)
     */
    @Query("SELECT DISTINCT escola FROM magias_fantasia WHERE fichaId = :fichaId AND escola != '' ORDER BY escola")
    fun getEscolasComMagias(fichaId: Long): Flow<List<String>>

    // ========== INSERÇÃO ==========

    /**
     * Insere uma nova magia
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMagia(magia: MagiaFantasiaEntity): Long

    /**
     * Insere múltiplas magias de uma vez
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMagias(magias: List<MagiaFantasiaEntity>)

    // ========== ATUALIZAÇÃO ==========

    /**
     * Atualiza uma magia existente
     */
    @Update
    suspend fun updateMagia(magia: MagiaFantasiaEntity)

    // ========== EXCLUSÃO ==========

    /**
     * Deleta uma magia específica
     */
    @Delete
    suspend fun deleteMagia(magia: MagiaFantasiaEntity)

    /**
     * Deleta todas as magias de uma ficha
     */
    @Query("DELETE FROM magias_fantasia WHERE fichaId = :fichaId")
    suspend fun deleteAllFromFicha(fichaId: Long)

    /**
     * Deleta todas as magias de um círculo específico
     */
    @Query("DELETE FROM magias_fantasia WHERE fichaId = :fichaId AND circulo = :circulo")
    suspend fun deleteCirculo(fichaId: Long, circulo: Int)

    /**
     * Deleta todas as magias de uma escola específica
     */
    @Query("DELETE FROM magias_fantasia WHERE fichaId = :fichaId AND escola = :escola")
    suspend fun deleteEscola(fichaId: Long, escola: String)
}
