package com.example.rpgapp.data.dao

import androidx.room.*
import com.example.rpgapp.data.entity.FichaAssimilacaoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FichaAssimilacaoDao {

    @Query("SELECT * FROM fichas_assimilacao LIMIT 1")
    fun getFicha(): Flow<FichaAssimilacaoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFicha(ficha: FichaAssimilacaoEntity): Long

    @Update
    suspend fun updateFicha(ficha: FichaAssimilacaoEntity)

    @Query("DELETE FROM fichas_assimilacao")
    suspend fun deleteAll()
}