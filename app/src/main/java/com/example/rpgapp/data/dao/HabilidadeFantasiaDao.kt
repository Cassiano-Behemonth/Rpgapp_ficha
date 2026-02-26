package com.example.rpgapp.data.dao

import androidx.room.*
import com.example.rpgapp.data.entity.HabilidadeFantasiaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabilidadeFantasiaDao {

    // ========== CONSULTAS ==========

    /**
     * Obtém todas as habilidades de uma ficha, ordenadas por categoria e nome
     */
    @Query("SELECT * FROM habilidades_fantasia WHERE fichaId = :fichaId ORDER BY categoria, nome")
    fun getHabilidadesFromFicha(fichaId: Long): Flow<List<HabilidadeFantasiaEntity>>

    /**
     * Obtém habilidades por categoria específica
     */
    @Query("SELECT * FROM habilidades_fantasia WHERE fichaId = :fichaId AND categoria = :categoria ORDER BY nome")
    fun getHabilidadesPorCategoria(fichaId: Long, categoria: String): Flow<List<HabilidadeFantasiaEntity>>

    /**
     * Obtém apenas poderes (habilidades com custo de PM > 0)
     */
    @Query("SELECT * FROM habilidades_fantasia WHERE fichaId = :fichaId AND custoPM > 0 ORDER BY nome")
    fun getPoderes(fichaId: Long): Flow<List<HabilidadeFantasiaEntity>>

    /**
     * Obtém apenas habilidades passivas (sem custo de PM)
     */
    @Query("SELECT * FROM habilidades_fantasia WHERE fichaId = :fichaId AND custoPM = 0 ORDER BY categoria, nome")
    fun getHabilidadesPassivas(fichaId: Long): Flow<List<HabilidadeFantasiaEntity>>

    /**
     * Busca habilidades por nome
     */
    @Query("SELECT * FROM habilidades_fantasia WHERE fichaId = :fichaId AND nome LIKE '%' || :busca || '%' ORDER BY nome")
    fun buscarHabilidades(fichaId: Long, busca: String): Flow<List<HabilidadeFantasiaEntity>>

    /**
     * Conta total de habilidades
     */
    @Query("SELECT COUNT(*) FROM habilidades_fantasia WHERE fichaId = :fichaId")
    fun contarHabilidades(fichaId: Long): Flow<Int>

    // ========== INSERÇÃO ==========

    /**
     * Insere uma nova habilidade
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabilidade(habilidade: HabilidadeFantasiaEntity): Long

    /**
     * Insere múltiplas habilidades de uma vez
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabilidades(habilidades: List<HabilidadeFantasiaEntity>)

    // ========== ATUALIZAÇÃO ==========

    /**
     * Atualiza uma habilidade existente
     */
    @Update
    suspend fun updateHabilidade(habilidade: HabilidadeFantasiaEntity)

    // ========== EXCLUSÃO ==========

    /**
     * Deleta uma habilidade específica
     */
    @Delete
    suspend fun deleteHabilidade(habilidade: HabilidadeFantasiaEntity)

    /**
     * Deleta todas as habilidades de uma ficha
     */
    @Query("DELETE FROM habilidades_fantasia WHERE fichaId = :fichaId")
    suspend fun deleteAllFromFicha(fichaId: Long)

    /**
     * Deleta todas as habilidades de uma categoria específica
     */
    @Query("DELETE FROM habilidades_fantasia WHERE fichaId = :fichaId AND categoria = :categoria")
    suspend fun deleteCategoria(fichaId: Long, categoria: String)
}
