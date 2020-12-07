package com.keysoc.myapplication.Base

import com.keysoc.myapplication.Event.DialogEvent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


open class BaseActivity : AppCompatActivity() {
    @Override
    override fun onStart(){
        super.onStart()
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

}