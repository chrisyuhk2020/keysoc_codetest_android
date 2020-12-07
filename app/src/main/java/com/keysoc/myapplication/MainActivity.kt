package com.keysoc.myapplication

import com.keysoc.myapplication.Base.BaseActivity
import com.keysoc.myapplication.Event.ResponseEvent
import com.keysoc.myapplication.Utils.APIUtils
import com.keysoc.myapplication.Entity.ResultData
import android.os.Bundle
import android.util.Log
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        /*APIUtils.get(
            "https://itunes.apple.com/search",
            listOf("term" to "jack+johnson", "entity" to "album"),
            this, ResultData::class.java
        )*/
    }

}