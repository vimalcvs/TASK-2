package com.vimal.margh.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vimal.margh.model.ModelDetail
import com.vimal.margh.model.ModelMeal
import com.vimal.margh.network.ApiService
import com.vimal.margh.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService: ApiService by lazy { RetrofitClient.createAPI(application) }

    private val _data = MutableLiveData<Map<String, List<ModelMeal>>>()
    val data: LiveData<Map<String, List<ModelMeal>>> = _data

    private val _detail = MutableLiveData<List<ModelDetail>>()
    val detail: LiveData<List<ModelDetail>> = _detail

    private val _search = MutableLiveData<List<ModelMeal>>()
    val search: LiveData<List<ModelMeal>> = _search


    private val _breakfast = MutableLiveData<List<ModelMeal>>()
    val breakfast: LiveData<List<ModelMeal>> = _breakfast

    private val _pasta = MutableLiveData<List<ModelMeal>>()
    val pasta: LiveData<List<ModelMeal>> = _pasta

    private val _lamb = MutableLiveData<List<ModelMeal>>()
    val lamb: LiveData<List<ModelMeal>> = _lamb

    private val _pork = MutableLiveData<List<ModelMeal>>()
    val pork: LiveData<List<ModelMeal>> = _pork

    private val _side = MutableLiveData<List<ModelMeal>>()
    val side: LiveData<List<ModelMeal>> = _side

    private val _vegetarian = MutableLiveData<List<ModelMeal>>()
    val vegetarian: LiveData<List<ModelMeal>> = _vegetarian


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isNoNetwork = MutableLiveData<Boolean>()
    val isNoNetwork: LiveData<Boolean> = _isNoNetwork

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty


    fun fetchData(category: String?) {
        _isLoading.value = true
        _isNoNetwork.value = false
        _isEmpty.value = false
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getRecipe(category!!)
                if (response.isSuccessful) {
                    val newData = response.body()?.meals ?: emptyList()
                    if (newData.isEmpty()) {
                        _isEmpty.postValue(true)
                    } else {
                        val currentData = _data.value?.toMutableMap() ?: mutableMapOf()
                        currentData[category] = newData
                        _data.postValue(currentData)
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

    fun fetchDetail(detail: String?) {
        _isLoading.value = true
        _isNoNetwork.value = false
        _isEmpty.value = false

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getDetail(detail!!)
                if (response.isSuccessful) {
                    if (response.body()?.meals.isNullOrEmpty()) {
                        _isEmpty.postValue(true)
                    } else {
                        _detail.postValue(response.body()?.meals)
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

    fun fetchSearch(search: String) {
        _isLoading.value = true
        _isNoNetwork.value = false
        _isEmpty.value = false

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getSearch(search)
                if (response.isSuccessful) {
                    if (response.body()?.meals.isNullOrEmpty()) {
                        _isEmpty.postValue(true)
                    } else {
                        _search.postValue(response.body()?.meals)
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


    init {
        fetchBreakfast()
        fetchPasta()
        fetchLamb()
        fetchPork()
        fetchSide()
        fetchVegetarian()
    }

    private fun fetchMeals(mealType: String, liveData: MutableLiveData<List<ModelMeal>>) {
        _isLoading.value = true
        _isNoNetwork.value = false
        _isEmpty.value = false
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getRecipe(mealType)
                if (response.isSuccessful) {
                    val meals = response.body()?.meals
                    if (meals.isNullOrEmpty()) {
                        _isEmpty.postValue(true)
                    } else {
                        liveData.postValue(meals)
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

    fun fetchBreakfast() {
        fetchMeals("Breakfast", _breakfast)
    }

    fun fetchPasta() {
        fetchMeals("Pasta", _pasta)
    }

    fun fetchLamb() {
        fetchMeals("Lamb", _lamb)
    }

    fun fetchPork() {
        fetchMeals("Pork", _pork)
    }

    fun fetchSide() {
        fetchMeals("Side", _side)
    }

    fun fetchVegetarian() {
        fetchMeals("Vegetarian", _vegetarian)
    }
}