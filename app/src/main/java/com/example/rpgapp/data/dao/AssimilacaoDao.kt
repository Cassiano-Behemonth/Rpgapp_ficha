package com.example.rpgapp.data.dao

import androidx.room.*
import com.example.rpgapp.data.entity.AssimilacaoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AssimilacaoDao {

    @Query("SELECT * FROM assimilacoes WHERE fichaId = :fichaId ORDER BY tipo, nome")
    fun getAssimilacoesFromFicha(fichaId: Long): Flow<List<AssimilacaoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssimilacao(assimilacao: AssimilacaoEntity): Long

    @Update
    suspend fun updateAssimilacao(assimilacao: AssimilacaoEntity)

    @Delete
    suspend fun deleteAssimilacao(assimilacao: AssimilacaoEntity)

    @Query("DELETE FROM assimilacoes WHERE fichaId = :fichaId")
    suspend fun deleteAllFromFicha(fichaId: Long)
}