package com.vimal.margh.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vimal.margh.database.Repository
import com.vimal.margh.model.ModelCategory
import com.vimal.margh.model.ModelMeal
import com.vimal.margh.network.ApiService
import com.vimal.margh.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService: ApiService by lazy { RetrofitClient.createAPI(application) }
    private val repository: Repository by lazy { Repository.getInstance(application)!! }

    private val _categories = MutableLiveData<List<ModelCategory>>()
    val categories: LiveData<List<ModelCategory>> = _categories

    private val _slider = MutableLiveData<List<ModelMeal>>()
    val slider: LiveData<List<ModelMeal>> = _slider

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isNoNetwork = MutableLiveData<Boolean>()
    val isNoNetwork: LiveData<Boolean> = _isNoNetwork

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty


    init {
        fetchCategories()
        fetchSlider()
    }

    fun fetchCategories() {
        _isLoading.value = true
        _isNoNetwork.value = false
        _isEmpty.value = false

        CoroutineScope(Dispatchers.IO).launch {
            val cachedCategories = repository.allCategory()
            if (cachedCategories.isNotEmpty()) {
                _categories.postValue(cachedCategories)
                _isLoading.postValue(false)
            } else {
                try {
                    val response = apiService.getCategories()
                    if (response.isSuccessful) {
                        val categories = response.body()?.categories
                        if (categories.isNullOrEmpty()) {
                            _isEmpty.postValue(true)
                        } else {
                            repository.insertCategory(categories)
                            _categories.postValue(response.body()?.categories)
                        }
                    } else {
                        _isNoNetwork.postValue(true)
                    }
                } catch (e: Exception) {
                    _isNoNetwork.postValue(true)
                } finally {
                    _isLoading.postValue(false)
                }
            }
        }
    }

    fun fetchSlider() {
        _isLoading.value = true
        _isNoNetwork.value = false
        _isEmpty.value = false

        CoroutineScope(Dispatchers.IO).launch {
            val cachedMeals = repository.allSlider()
            if (cachedMeals.isNotEmpty()) {
                _slider.postValue(cachedMeals)
                _isLoading.postValue(false)
            } else {
                try {
                    val response = apiService.getRecipe("Starter")
                    if (response.isSuccessful) {
                        val meals = response.body()?.meals
                        if (meals.isNullOrEmpty()) {
                            _isEmpty.postValue(true)
                        } else {
                            repository.insertSlider(meals)
                            _slider.postValue(response.body()?.meals)
                        }
                    } else {
                        _isNoNetwork.postValue(true)
                    }
                } catch (e: Exception) {
                    _isNoNetwork.postValue(true)
                } finally {
                    _isLoading.postValue(false)
                }
            }
        }
    }

}