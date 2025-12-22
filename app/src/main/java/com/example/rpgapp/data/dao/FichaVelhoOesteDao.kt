package com.example.rpgapp.data.dao

import androidx.room.*
import com.example.rpgapp.data.entity.FichaVelhoOesteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FichaVelhoOesteDao {
    @Query("SELECT * FROM fichas_velho_oeste LIMIT 1")
    fun getFicha(): Flow<FichaVelhoOesteEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFicha(ficha: FichaVelhoOesteEntity): Long

    @Update
    suspend fun updateFicha(ficha: FichaVelhoOesteEntity)

    @Query("DELETE FROM fichas_velho_oeste")
    suspend fun deleteAll()
}