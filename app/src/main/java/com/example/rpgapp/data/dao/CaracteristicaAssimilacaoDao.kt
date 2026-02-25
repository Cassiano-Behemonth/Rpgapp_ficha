package com.example.rpgapp.data.dao

import androidx.room.*
import com.example.rpgapp.data.entity.CaracteristicaAssimilacaoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CaracteristicaAssimilacaoDao {

    @Query("SELECT * FROM caracteristicas_assimilacao WHERE fichaId = :fichaId ORDER BY custo DESC, nome")
    fun getCaracteristicasFromFicha(fichaId: Long): Flow<List<CaracteristicaAssimilacaoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCaracteristica(caracteristica: CaracteristicaAssimilacaoEntity): Long

    @Update
    suspend fun updateCaracteristica(caracteristica: CaracteristicaAssimilacaoEntity)

    @Delete
    suspend fun deleteCaracteristica(caracteristica: CaracteristicaAssimilacaoEntity)

    @Query("DELETE FROM caracteristicas_assimilacao WHERE fichaId = :fichaId")
    suspend fun deleteAllFromFicha(fichaId: Long)

    @Query("SELECT SUM(custo) FROM caracteristicas_assimilacao WHERE fichaId = :fichaId")
    fun getTotalPontos(fichaId: Long): Flow<Int?>
}