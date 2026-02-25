package com.example.rpgapp.data.dao

import androidx.room.*
import com.example.rpgapp.data.entity.ItemAssimilacaoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemAssimilacaoDao {

    @Query("SELECT * FROM itens_assimilacao WHERE fichaId = :fichaId ORDER BY nome")
    fun getItensFromFicha(fichaId: Long): Flow<List<ItemAssimilacaoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemAssimilacaoEntity): Long

    @Update
    suspend fun updateItem(item: ItemAssimilacaoEntity)

    @Delete
    suspend fun deleteItem(item: ItemAssimilacaoEntity)

    @Query("DELETE FROM itens_assimilacao WHERE fichaId = :fichaId")
    suspend fun deleteAllFromFicha(fichaId: Long)
}