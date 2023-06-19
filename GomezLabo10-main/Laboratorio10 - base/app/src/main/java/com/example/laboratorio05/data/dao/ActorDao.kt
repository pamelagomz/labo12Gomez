package com.example.laboratorio05.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.laboratorio05.data.model.ActorModel

@Dao
interface ActorDao {
    // create getAllActors method

    @Query("SELECT * FROM actor_table")
    suspend fun getAllActors(): List<ActorModel>


    // create insertActor method
    @Transaction
    @Insert
    suspend fun insertActor(actor: ActorModel)
}