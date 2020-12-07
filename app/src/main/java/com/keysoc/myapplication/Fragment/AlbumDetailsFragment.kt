package com.keysoc.myapplication.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.Nullable
import com.keysoc.myapplication.Base.BaseFragment
import com.keysoc.myapplication.Entity.ResultData
import com.keysoc.myapplication.Event.ResponseEvent
import com.keysoc.myapplication.R
import com.keysoc.myapplication.Utils.APIUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import android.view.ViewGroup as ViewGroup1

class AlbumDetailsFragment : BaseFragment() {
    @Override
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup1?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_album_listing, null) as android.view.ViewGroup

        return view
    }

    override fun onResume() {
        super.onResume()
        APIUtils.get(
                "https://itunes.apple.com/search",
                listOf("term" to "jack+johnson", "entity" to "album"),
                this!!.requireActivity(), ResultData::class.java
        )
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event: ResponseEvent?) {
        Log.i("response", "response " + (event!!.response as ResultData).results.size )
    }
}