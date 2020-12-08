package com.keysoc.myapplication.Base

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.keysoc.myapplication.Event.DialogEvent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.facebook.drawee.backends.pipeline.Fresco
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

open class BaseActivity : AppCompatActivity() {

    @Override
    override fun onStart(){
        super.onStart()
        Fresco.initialize(this);

        EventBus.getDefault().register(this)
    }

    override fun onStop(){
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event: DialogEvent?) {

        AlertDialog.Builder(this)
            .setTitle(event!!.title)
            .setMessage(event!!.message)
            .setPositiveButton(android.R.string.ok, null)
            .show();
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}