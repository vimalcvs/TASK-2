package com.vimal.margh.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vimal.margh.model.ModelCategory
import com.vimal.margh.model.ModelMeal


@Dao
interface RoomDao {

    @Query("Select * from table_meal")
    fun getAllFavorite(): LiveData<List<ModelMeal>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavorite(modelMeal: ModelMeal)

    @Delete
    fun deleteFavorite(modelMeal: ModelMeal)

    @Query("SELECT EXISTS (SELECT 1 FROM table_meal WHERE idMeal = :id)")
    fun isFavorite(id: Int): Boolean





    @Query("SELECT * FROM table_meal")
    fun getAllSlider(): List<ModelMeal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllSlider(meals: List<ModelMeal>)

    @Query("DELETE FROM table_meal")
    fun deleteAllSlider()






    @Query("SELECT * FROM table_category")
    fun getAllCategories(): List<ModelCategory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCategories(categories: List<ModelCategory>)

    @Query("DELETE FROM table_category")
    fun deleteAllCategory()


}