package com.vimal.margh.database

import android.content.Context
import androidx.lifecycle.LiveData
import com.vimal.margh.model.ModelCategory
import com.vimal.margh.model.ModelMeal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class Repository(context: Context?) {

    private val roomDao: RoomDao
    private val favoriteLiveData: LiveData<List<ModelMeal>>
    private val sliderList: List<ModelMeal>
    private val categoryList: List<ModelCategory>

    init {
        val database = RoomDB.getDatabase(context!!)
        roomDao = database.getDao()
        favoriteLiveData = roomDao.getAllFavorite()
        sliderList = roomDao.getAllSlider()
        categoryList = roomDao.getAllCategories()
    }

    fun allFavorite(): LiveData<List<ModelMeal>> {
        return favoriteLiveData
    }

    fun deleteFavorite(model: ModelMeal?) {
        object : Thread(Runnable { roomDao.deleteFavorite(model!!) }) {}.start()
    }

    fun insertFavorite(model: ModelMeal?) {
        object : Thread(Runnable { roomDao.insertFavorite(model!!) }) {
        }.start()
    }


    fun isFavorite(id: Int): Boolean {
        return runBlocking {
            withContext(Dispatchers.IO) {
                roomDao.isFavorite(id)
            }
        }
    }


    fun allSlider(): List<ModelMeal> {
        return sliderList
    }

    fun insertSlider(model: List<ModelMeal>) {
        object : Thread(Runnable { roomDao.insertAllSlider(model) }) {
        }.start()
    }


    fun allCategory(): List<ModelCategory> {
        return categoryList
    }

    fun insertCategory(model: List<ModelCategory>) {
        object : Thread(Runnable { roomDao.insertAllCategories(model) }) {
        }.start()
    }


    companion object {
        private var repository: Repository? = null
        fun getInstance(context: Context?): Repository? {
            if (repository == null) {
                repository = Repository(context)
            }
            return repository
        }
    }
}