package com.example.rpgapp.data.dao

import androidx.room.*
import com.example.rpgapp.data.entity.PericiaFantasiaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PericiaFantasiaDao {
    @Query("SELECT * FROM pericias_fantasia WHERE fichaId = :fichaId ORDER BY nome")
    fun getPericiasFromFicha(fichaId: Long): Flow<List<PericiaFantasiaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPericia(pericia: PericiaFantasiaEntity): Long

    @Update
    suspend fun updatePericia(pericia: PericiaFantasiaEntity)

    @Delete
    suspend fun deletePericia(pericia: PericiaFantasiaEntity)

    @Query("DELETE FROM pericias_fantasia WHERE fichaId = :fichaId")
    suspend fun deleteAllFromFicha(fichaId: Long)
}
