package com.example.rpgapp.data.dao

import androidx.room.*
import com.example.rpgapp.data.entity.FichaFantasiaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FichaFantasiaDao {
    @Query("SELECT * FROM fichas_fantasia LIMIT 1")
    fun getFicha(): Flow<FichaFantasiaEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFicha(ficha: FichaFantasiaEntity): Long

    @Update
    suspend fun updateFicha(ficha: FichaFantasiaEntity)

    @Query("DELETE FROM fichas_fantasia")
    suspend fun deleteAll()
}
