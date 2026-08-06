package com.miclima.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CiudadDao {

    @Query("SELECT * FROM ciudades ORDER BY agregadaEn ASC")
    fun todas(): Flow<List<CiudadEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(ciudad: CiudadEntity)

    @Query("DELETE FROM ciudades WHERE id = :id")
    suspend fun eliminar(id: Long)
}

@Dao
interface ClimaCacheDao {

    @Query("SELECT * FROM clima_cache")
    fun todos(): Flow<List<ClimaCacheEntity>>

    @Query("SELECT * FROM clima_cache WHERE clave = :clave")
    suspend fun porClave(clave: String): ClimaCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(cache: ClimaCacheEntity)
}
