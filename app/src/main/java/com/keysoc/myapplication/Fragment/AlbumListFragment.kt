package com.keysoc.myapplication.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import com.keysoc.myapplication.Base.BaseFragment
import com.keysoc.myapplication.Entity.ResultData
import com.keysoc.myapplication.Entity.ResultDataItem
import com.keysoc.myapplication.Event.ResponseEvent
import com.keysoc.myapplication.R
import com.keysoc.myapplication.Utils.APIUtils
import com.keysoc.myapplication.databinding.FragmentAlbumListingBinding
import com.keysoc.myapplication.databinding.LayoutAlbumItemBinding
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class AlbumListFragment : BaseFragment() {
    lateinit var binding : FragmentAlbumListingBinding;

    @Override
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = LayoutInflater.from(activity).inflate(R.layout.fragment_album_listing, null) as android.view.ViewGroup
        binding = FragmentAlbumListingBinding.bind(view);

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
        if(binding.listview.adapter == null)
            binding.listview.adapter = AlbumListAdapter( (event!!.response as ResultData), activity as Context)
        else
            (binding.listview.adapter as AlbumListAdapter).notifyDataSetChanged()
    }

    class AlbumListAdapter(var resultData : ResultData, var context : Context) : BaseAdapter() {

        override fun getView(position: Int, view: View?, root: ViewGroup?): ViewGroup? {
            var reference : ViewGroup? =  null
            lateinit var binding : LayoutAlbumItemBinding;

            if(view == null) {
                reference = LayoutInflater.from(context).inflate(R.layout.layout_album_item, null) as ViewGroup
            } else {
                reference = view as ViewGroup?
            }
            binding = LayoutAlbumItemBinding.bind(reference!!)

            binding.title!!.setText(getItem(position).collectionName)
            binding.artistName!!.setText(getItem(position).artistName)
            //binding.collectionName!!.setText(getItem(position).collectionName)
            binding.trackCount!!.setText(getItem(position).trackCount.toString())
            binding.releaseDate!!.setText(getItem(position).releaseDate)

            return reference
        }

        override fun getItem(position: Int): ResultDataItem {
           return resultData.results!![position]
        }

        override fun getItemId(position: Int): Long {
            return resultData.results!![position].hashCode().toLong()
        }

        override fun getCount(): Int {
            return resultData.results!!.size
        }
    }
}