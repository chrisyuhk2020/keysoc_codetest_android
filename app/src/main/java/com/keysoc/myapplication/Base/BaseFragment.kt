package com.keysoc.myapplication.Base

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.greenrobot.eventbus.EventBus

open class BaseFragment : Fragment() {
    lateinit var view : ViewGroup


    @Override
    override fun onStart(){
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop(){
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}