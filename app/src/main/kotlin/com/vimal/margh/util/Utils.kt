@file:Suppress("DEPRECATION")

package com.vimal.margh.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.vimal.margh.BuildConfig
import com.vimal.margh.R
import com.vimal.margh.databinding.LayoutSnackbarBinding

object Utils {

    fun getMultipleColor(position: Int, context: Context?): Int {
        val itemColors = intArrayOf(
            R.color.color_blue,
            R.color.color_red,
            R.color.color_orange,
            R.color.color_green,
            R.color.color_violet
        )
        return ContextCompat.getColor(context!!, itemColors[position % itemColors.size])
    }

    @SuppressLint("RestrictedApi")
    fun customSnackBarWithAction(activity: Activity, view: View, string: String, color: Int) {
        try {
            val snack = Snackbar.make(view, "", Snackbar.LENGTH_LONG)
            val bindings = LayoutSnackbarBinding.inflate(activity.layoutInflater)
            bindings.tvSnack.text = string
            bindings.llLayout.background.setTint(ContextCompat.getColor(activity, color))
            snack.view.setBackgroundColor(Color.TRANSPARENT)
            val snackLayout = snack.view as Snackbar.SnackbarLayout
            snackLayout.addView(bindings.getRoot(), 0)
            snack.show()
        } catch (e: Exception) {
            getErrors(e)
        }
    }


    inline fun <reified T> Intent.getParcelableExtraCompat(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(
            key,
            T::class.java
        )

        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T?
    }

    fun getErrors(e: Exception?) {
        println("TAG :: " + Log.getStackTraceString(e))
    }


    fun openYoutube(context: Context, string: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(string))
            intent.putExtra("force_fullscreen", true)
            intent.setPackage("com.google.android.youtube")
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(string))
                context.startActivity(webIntent)
            }
        } catch (e: Exception) {
            getErrors(e)
        }
    }


    fun shareApp(context: Context) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                context.getString(R.string.share_app_message) + BuildConfig.APPLICATION_ID
            )
            context.startActivity(
                Intent.createChooser(
                    shareIntent,
                    context.getString(R.string.share_app_title)
                )
            )
        } catch (e: Exception) {
            getErrors(e)
        }
    }


    fun getShareUrl(context: Context, url: String?) {
        try {
            if (!url.isNullOrEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                try {
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(context, "Can't open", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Invalid URL", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            getErrors(e)
        }
    }


}
