package com.example.rpgapp.data.dao

import androidx.room.*
import com.example.rpgapp.data.entity.FichaAssimilacaoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FichaAssimilacaoDao {

    // Flow reativo para a UI observar
    @Query("SELECT * FROM fichas_assimilacao LIMIT 1")
    fun getFicha(): Flow<FichaAssimilacaoEntity?>

    // Leitura direta (suspend) — usada pelo ViewModel antes de qualquer update
    // Evita o problema de ficha.value estar em cache desatualizado no StateFlow
    @Query("SELECT * FROM fichas_assimilacao LIMIT 1")
    suspend fun getFichaOnce(): FichaAssimilacaoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFicha(ficha: FichaAssimilacaoEntity): Long

    @Update
    suspend fun updateFicha(ficha: FichaAssimilacaoEntity)

    @Query("DELETE FROM fichas_assimilacao")
    suspend fun deleteAll()
}