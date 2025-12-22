package com.example.rpgapp.data.dao

import androidx.room.*
import com.example.rpgapp.data.entity.ItemVelhoOesteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemVelhoOesteDao {
    @Query("SELECT * FROM itens_velho_oeste WHERE fichaId = :fichaId ORDER BY nome")
    fun getItensFromFicha(fichaId: Long): Flow<List<ItemVelhoOesteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemVelhoOesteEntity): Long

    @Update
    suspend fun updateItem(item: ItemVelhoOesteEntity)

    @Delete
    suspend fun deleteItem(item: ItemVelhoOesteEntity)

    @Query("DELETE FROM itens_velho_oeste WHERE fichaId = :fichaId")
    suspend fun deleteAllFromFicha(fichaId: Long)
}