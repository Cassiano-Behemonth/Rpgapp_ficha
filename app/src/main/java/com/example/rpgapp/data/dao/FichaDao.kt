package com.example.rpgapp.data.dao

import androidx.room.*
import com.example.rpgapp.data.entity.FichaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FichaDao {
    @Query("SELECT * FROM fichas LIMIT 1")
    fun getFicha(): Flow<FichaEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFicha(ficha: FichaEntity): Long

    @Update
    suspend fun updateFicha(ficha: FichaEntity)

    @Query("DELETE FROM fichas")
    suspend fun deleteAll()
}