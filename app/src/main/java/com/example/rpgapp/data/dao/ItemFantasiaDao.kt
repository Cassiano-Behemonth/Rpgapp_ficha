package com.example.rpgapp.data.dao

import androidx.room.*
import com.example.rpgapp.data.entity.ItemFantasiaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemFantasiaDao {
    @Query("SELECT * FROM itens_fantasia WHERE fichaId = :fichaId ORDER BY nome")
    fun getItensFromFicha(fichaId: Long): Flow<List<ItemFantasiaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemFantasiaEntity): Long

    @Update
    suspend fun updateItem(item: ItemFantasiaEntity)

    @Delete
    suspend fun deleteItem(item: ItemFantasiaEntity)

    @Query("DELETE FROM itens_fantasia WHERE fichaId = :fichaId")
    suspend fun deleteAllFromFicha(fichaId: Long)
}
