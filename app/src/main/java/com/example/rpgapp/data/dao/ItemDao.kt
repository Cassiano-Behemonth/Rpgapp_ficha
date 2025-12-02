// Arquivo: app/src/main/java/com/example/rpgapp/data/dao/ItemDao.kt
package com.example.rpgapp.data.dao

import androidx.room.*
import com.example.rpgapp.data.entity.ItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM itens WHERE fichaId = :fichaId ORDER BY nome")
    fun getItensFromFicha(fichaId: Long): Flow<List<ItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemEntity): Long

    @Update
    suspend fun updateItem(item: ItemEntity)

    @Delete
    suspend fun deleteItem(item: ItemEntity)

    @Query("DELETE FROM itens WHERE fichaId = :fichaId")
    suspend fun deleteAllFromFicha(fichaId: Long)
}