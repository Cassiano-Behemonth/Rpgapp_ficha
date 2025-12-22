package com.example.rpgapp.data.dao

import androidx.room.*
import com.example.rpgapp.data.entity.AntecedenteVelhoOesteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AntecedenteVelhoOesteDao {
    @Query("SELECT * FROM antecedentes_velho_oeste WHERE fichaId = :fichaId ORDER BY nome")
    fun getAntecedentesFromFicha(fichaId: Long): Flow<List<AntecedenteVelhoOesteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAntecedente(antecedente: AntecedenteVelhoOesteEntity): Long

    @Update
    suspend fun updateAntecedente(antecedente: AntecedenteVelhoOesteEntity)

    @Delete
    suspend fun deleteAntecedente(antecedente: AntecedenteVelhoOesteEntity)

    @Query("DELETE FROM antecedentes_velho_oeste WHERE fichaId = :fichaId")
    suspend fun deleteAllFromFicha(fichaId: Long)
}