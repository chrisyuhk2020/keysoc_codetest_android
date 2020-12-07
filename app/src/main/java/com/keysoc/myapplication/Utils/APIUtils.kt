package com.keysoc.myapplication.Utils

import com.keysoc.myapplication.Entity.ResultData
import com.keysoc.myapplication.Event.DialogEvent
import com.keysoc.myapplication.Event.ResponseEvent
import android.content.Context
import android.util.Log
import com.github.kittinunf.fuel.core.Parameters
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.moshi.moshiDeserializerOf
import com.keysoc.myapplication.R
import org.greenrobot.eventbus.EventBus
import java.lang.Exception

object APIUtils {
    fun <T : Any> get(url: String, parameters: Parameters? = null, context : Context, clazz: Class<T>) {
        if(url == null)
            return

        try {
            url.httpGet(parameters)

                .responseObject(moshiDeserializerOf(clazz))
                { _, response, result ->
                    when (result) {

                    is com.github.kittinunf.result.Result.Failure -> {
                        Log.i("failure", "failure");

                        val ex = result.getException()
                        println(ex)
                        EventBus.getDefault().post(DialogEvent(context.getString(R.string.app_name), ex.toString()))
                    }
                    is com.github.kittinunf.result.Result.Success -> {
                        Log.i("success", "success");

                        val data = result.get()
                        println(data)

                        EventBus.getDefault().post(
                            ResponseEvent(
                                url,
                                (data)
                            )
                        )
                    }

                }
            }
        } catch (e : Exception) {
            EventBus.getDefault().post(DialogEvent(context.getString(R.string.app_name), e.toString()))
        }
    }
}