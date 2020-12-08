package com.keysoc.myapplication.Fragment

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import androidx.browser.customtabs.CustomTabsIntent
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.keysoc.myapplication.Base.BaseFragment
import com.keysoc.myapplication.Entity.ResultData
import com.keysoc.myapplication.Entity.ResultDataItem
import com.keysoc.myapplication.Event.ResponseEvent
import com.keysoc.myapplication.R
import com.keysoc.myapplication.Utils.APIUtils
import com.keysoc.myapplication.databinding.FragmentAlbumListingBinding
import com.keysoc.myapplication.databinding.LayoutAlbumItemBinding
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class AlbumListFragment : BaseFragment() {
    lateinit var binding : FragmentAlbumListingBinding;
    lateinit var resultData: ResultData;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = LayoutInflater.from(activity).inflate(R.layout.fragment_album_listing, null) as android.view.ViewGroup
        binding = FragmentAlbumListingBinding.bind(view);
    }

    @Override
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.listview.adapter = null

                if(tab.position == 0) {
                    setupListView(resultData)
                } else {
                    val mPrefs: SharedPreferences = (context as Activity).getPreferences(MODE_PRIVATE)
                    val gson = Gson()
                    var json = mPrefs.getString("LIST", "[]")
                    val obj: List<ResultDataItem> = gson.fromJson(json, object : TypeToken<List<ResultDataItem?>?>() {}.getType())

                    var resultData = ResultData()
                    resultData.resultCount = obj.size
                    resultData.results = obj

                    Log.i("yoyo", "yoyo " + resultData.results.size)

                    setupListView(resultData)
                }
            }

        })

        binding.filter.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                APIUtils.get(
                    "https://itunes.apple.com/search",
                    listOf("term" to binding.filter.text.toString().replace(" ", "+"), "entity" to "album"),
                    activity, ResultData::class.java
                )
            }

        })
        return view
    }

    override fun onResume() {
        super.onResume()
        APIUtils.get(
                "https://itunes.apple.com/search",
                listOf("term" to binding.filter.text.toString().replace(" ", "+"), "entity" to "album"),
                this!!.requireActivity(), ResultData::class.java
        )
    }

    override fun onPause() {
        super.onPause()
        binding.filter.clearFocus()
        context?.hideKeyboard(binding.filter)
    }

    fun getLocalData() : ResultData{

        val mPrefs: SharedPreferences = (context as Activity).getPreferences(MODE_PRIVATE)
        val gson = Gson()
        var json = mPrefs.getString("LIST", "[]")
        val obj: List<ResultDataItem> = gson.fromJson(json, object : TypeToken<List<ResultDataItem?>?>() {}.getType())

        var resultData = ResultData()
        resultData.resultCount = obj.size
        resultData.results = obj

        return resultData
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event: ResponseEvent?) {
        if(binding.tabLayout.selectedTabPosition == 0)
            resultData = (event!!.response as ResultData)

        if(resultData == null || binding.tabLayout.selectedTabPosition == 0) {
            setupListView((event!!.response as ResultData))
        } else {
            setupListView(getLocalData())
        }
    }

    fun handleNoData(resultData : ResultData) {
        if(resultData.results.size == 0) {
            binding.noData.visibility = View.VISIBLE
        } else {
            binding.noData.visibility = View.GONE
        }
    }

    fun setupListView(resultData: ResultData) {
        if(resultData == null)
            return

        if(binding.tabLayout.selectedTabPosition == 0) {
            handleNoData(resultData)
            if (binding.listview.adapter == null) {
                binding.listview.adapter = AlbumListAdapter(resultData, activity as Context)
            } else {
                (binding.listview.adapter as AlbumListAdapter).resultData = resultData
                (binding.listview.adapter as AlbumListAdapter).notifyDataSetChanged()
            }
        } else {
            handleNoData(resultData)
            if (binding.listview.adapter == null) {
                binding.listview.adapter = AlbumListAdapter(resultData, activity as Context)
            } else {
                (binding.listview.adapter as AlbumListAdapter).resultData = resultData
                (binding.listview.adapter as AlbumListAdapter).notifyDataSetChanged()
            }
        }

        binding.listview.setOnScrollListener(object : AbsListView.OnScrollListener{
            override fun onScroll(p0: AbsListView?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onScrollStateChanged(p0: AbsListView?, p1: Int) {
                context?.hideKeyboard(binding.filter)
            }
        })
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

            binding.title!!.text = (getItem(position).collectionName)
            binding.artistName!!.text = (getItem(position).artistName)
            binding.genre!!.text = (getItem(position).primaryGenreName)
            binding.price!!.setText(getItem(position).collectionPrice.toString() + " " + getItem(position).currency)
            binding.trackCount!!.text = (getItem(position).trackCount.toString())
            binding.releaseDate!!.text =(getItem(position).releaseDate.split("T")[0])

            binding.cover.setImageURI(getItem(position).artworkUrl100)

            val mPrefs: SharedPreferences = (context as Activity).getPreferences(MODE_PRIVATE)
            val gson = Gson()
            var json = mPrefs.getString("LIST", "[]")
            val obj: List<ResultDataItem> = gson.fromJson(json, object : TypeToken<List<ResultDataItem?>?>() {}.getType())

            //for (item in collection) print(item)
            binding.bookmark.setImageResource(R.drawable.notbookmark)

            for(resultDataItem in obj) {
                if(resultDataItem.collectionId.equals((getItem(position).collectionId))) {
                    binding.bookmark.setImageResource(R.drawable.bookmarked)
                }
            }

            binding.bookmark.setOnClickListener{
                val mPrefs: SharedPreferences = (context as Activity).getPreferences(MODE_PRIVATE)
                val gson = Gson()
                var json = mPrefs.getString("LIST", "[]")
                val obj: List<ResultDataItem> = gson.fromJson(json, object : TypeToken<List<ResultDataItem?>?>() {}.getType())

                var exist : Int = -1;
                var pos : Int = 0;
                for(resultDataItem in obj) {
                    if(resultDataItem.collectionId == (getItem(position).collectionId)) {
                        exist = pos;
                        break;
                    }
                    pos++;
                }

                if(exist == -1) {
                    (obj as ArrayList).add(getItem(position))
                } else {
                    (obj as ArrayList).removeAt(exist)
                }

                json = gson.toJson(obj)
                val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
                prefsEditor.putString("LIST", json)
                prefsEditor.commit()

                if(exist >= 0) {
                    var resultData = ResultData()
                    resultData.resultCount = obj.size
                    resultData.results = obj
                    EventBus.getDefault().post(ResponseEvent("", resultData))
                }
                notifyDataSetChanged()
            }

            binding.root.setOnClickListener {
                //Log.i("url", "url " + getItem(position).collectionViewUrl)
                //AlbumDetailsFragment.url = getItem(position).collectionViewUrl
                //Navigation.findNavController(binding.root).navigate(R.id.action_albumListFragment_to_albumDetailsFragment)

                var url = getItem(position).collectionViewUrl
                var builder : CustomTabsIntent.Builder = CustomTabsIntent.Builder();
                var customTabsIntent : CustomTabsIntent = builder!!.build();
                customTabsIntent.intent.setPackage("com.android.chrome");
                customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                customTabsIntent.launchUrl(context, Uri.parse(url));

            }
            return reference
        }

        fun setData(resultData : ResultData) {
            this.resultData = resultData
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