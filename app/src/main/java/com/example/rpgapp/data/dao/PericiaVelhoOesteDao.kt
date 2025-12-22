package com.example.rpgapp.data.dao

import androidx.room.*
import com.example.rpgapp.data.entity.PericiaVelhoOesteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PericiaVelhoOesteDao {
    @Query("SELECT * FROM pericias_velho_oeste WHERE fichaId = :fichaId ORDER BY nome")
    fun getPericiasFromFicha(fichaId: Long): Flow<List<PericiaVelhoOesteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPericia(pericia: PericiaVelhoOesteEntity): Long

    @Update
    suspend fun updatePericia(pericia: PericiaVelhoOesteEntity)

    @Delete
    suspend fun deletePericia(pericia: PericiaVelhoOesteEntity)

    @Query("DELETE FROM pericias_velho_oeste WHERE fichaId = :fichaId")
    suspend fun deleteAllFromFicha(fichaId: Long)
}