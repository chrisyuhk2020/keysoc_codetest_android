package com.keysoc.myapplication.Fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.keysoc.myapplication.Base.BaseFragment
import com.keysoc.myapplication.R
import com.keysoc.myapplication.databinding.FragmentAlbumDetailBinding
import android.view.ViewGroup as ViewGroup1


class AlbumDetailsFragment: BaseFragment() {
    lateinit var binding : FragmentAlbumDetailBinding;

    companion object {
        lateinit var url: String
    }

    @Override
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup1?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_album_detail, null) as android.view.ViewGroup
        binding = FragmentAlbumDetailBinding.bind(view)

        binding.webview.loadUrl(url)
        binding.webview.settings.javaScriptEnabled = true;

        binding.webview.settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        binding.webview.settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        binding.webview.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view : WebView, progress : Int){
                Log.i("integer", "integer " + progress);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.progressbar.setProgress(progress, false)
                } else {
                    binding.progressbar.progress = progress
                }
            }
            /*
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }*/
        })

        return view
    }
}