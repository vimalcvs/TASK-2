package com.vimal.margh.util

import android.content.Context
import com.vimal.margh.util.Constant.KEY_ONBOARDING
import com.vimal.margh.util.Constant.SET_ONBOARDING

class SharedPrefer(private val context: Context) {


    fun getOnBoarding(): Boolean {
        return context.getSharedPreferences(SET_ONBOARDING, Context.MODE_PRIVATE)
            .getBoolean(KEY_ONBOARDING, false)
    }

    fun saveOnBoarding(isOnBoarding: Boolean) {
        context.getSharedPreferences(SET_ONBOARDING, Context.MODE_PRIVATE).edit()
            .putBoolean(KEY_ONBOARDING, isOnBoarding).apply()
    }
}