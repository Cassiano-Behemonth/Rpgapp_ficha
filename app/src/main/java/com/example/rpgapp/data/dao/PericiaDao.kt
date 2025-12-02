// Arquivo: app/src/main/java/com/example/rpgapp/data/dao/PericiaDao.kt
package com.example.rpgapp.data.dao

import androidx.room.*
import com.example.rpgapp.data.entity.PericiaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PericiaDao {
    @Query("SELECT * FROM pericias WHERE fichaId = :fichaId ORDER BY nome")
    fun getPericiasFromFicha(fichaId: Long): Flow<List<PericiaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPericia(pericia: PericiaEntity): Long

    @Update
    suspend fun updatePericia(pericia: PericiaEntity)

    @Delete
    suspend fun deletePericia(pericia: PericiaEntity)

    @Query("DELETE FROM pericias WHERE fichaId = :fichaId")
    suspend fun deleteAllFromFicha(fichaId: Long)
}