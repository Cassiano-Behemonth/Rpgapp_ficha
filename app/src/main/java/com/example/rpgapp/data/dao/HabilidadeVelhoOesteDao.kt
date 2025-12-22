package com.example.rpgapp.data.dao

import androidx.room.*
import com.example.rpgapp.data.entity.HabilidadeVelhoOesteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabilidadeVelhoOesteDao {
    @Query("SELECT * FROM habilidades_velho_oeste WHERE fichaId = :fichaId ORDER BY nome")
    fun getHabilidadesFromFicha(fichaId: Long): Flow<List<HabilidadeVelhoOesteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabilidade(habilidade: HabilidadeVelhoOesteEntity): Long

    @Update
    suspend fun updateHabilidade(habilidade: HabilidadeVelhoOesteEntity)

    @Delete
    suspend fun deleteHabilidade(habilidade: HabilidadeVelhoOesteEntity)

    @Query("DELETE FROM habilidades_velho_oeste WHERE fichaId = :fichaId")
    suspend fun deleteAllFromFicha(fichaId: Long)
}