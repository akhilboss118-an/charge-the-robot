package com.example.chargetherobot

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.webkit.ConsoleMessage
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.window.OnBackInvokedCallback
import android.window.OnBackInvokedDispatcher
import androidx.annotation.RequiresApi
import androidx.webkit.WebViewAssetLoader

class MainActivity : Activity() {

    private var webView: WebView? = null
    private var backCallback: Any? = null

    companion object {
        private const val TAG = "ChargeTheRobot"
        private const val APPASSETS_URL = "https://appassets.androidplatform.net/assets/charge-the-robot.html"
        private const val FILE_URL = "file:///android_asset/charge-the-robot.html"
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            // Enable hardware acceleration flag on window for max 60fps GPU performance
            window.setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
            )

            // 1. Build and install WebView content
            val web = buildWebView()
            setContentView(web)

            // 2. Set immersive fullscreen after decor view is attached
            setImmersiveFullscreen()

            // 3. Register safe back handler
            registerBackHandler()

            // 4. Load game content
            if (savedInstanceState != null) {
                web.restoreState(savedInstanceState)
            } else {
                web.loadUrl(APPASSETS_URL)
            }
        } catch (t: Throwable) {
            Log.e(TAG, "Fatal error initializing MainActivity", t)
            showFallbackScreen(t)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun buildWebView(): WebView {
        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(this))
            .build()

        return WebView(this).apply {
            webView = this
            setBackgroundColor(0xFF0A0A0C.toInt())
            scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            isVerticalScrollBarEnabled = false
            isHorizontalScrollBarEnabled = false
            overScrollMode = View.OVER_SCROLL_NEVER

            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                databaseEnabled = true
                mediaPlaybackRequiresUserGesture = false
                allowFileAccess = true
                allowContentAccess = true
                useWideViewPort = true
                loadWithOverviewMode = true
                setSupportZoom(false)
                displayZoomControls = false
                builtInZoomControls = false
                cacheMode = WebSettings.LOAD_NO_CACHE
                try {
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                } catch (_: Throwable) {}
            }

            webChromeClient = object : WebChromeClient() {
                override fun onConsoleMessage(message: ConsoleMessage): Boolean {
                    Log.d(TAG, "[JS] ${message.message()} (line ${message.lineNumber()})")
                    return true
                }
            }

            webViewClient = object : WebViewClient() {
                override fun shouldInterceptRequest(
                    view: WebView,
                    request: WebResourceRequest
                ): WebResourceResponse? {
                    return try {
                        assetLoader.shouldInterceptRequest(request.url)
                    } catch (e: Throwable) {
                        Log.w(TAG, "AssetLoader interception error", e)
                        null
                    }
                }

                override fun onReceivedError(
                    view: WebView?,
                    errorCode: Int,
                    description: String?,
                    failingUrl: String?
                ) {
                    Log.w(TAG, "WebView error ($errorCode): $description at $failingUrl")
                    // Fallback to direct asset file URL if appassets scheme has any issue
                    if (failingUrl?.startsWith("https://appassets.androidplatform.net") == true) {
                        view?.loadUrl(FILE_URL)
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onRenderProcessGone(view: WebView, detail: RenderProcessGoneDetail): Boolean {
                    Log.e(TAG, "Renderer gone didCrash=${detail.didCrash()}")
                    try {
                        view.visibility = View.GONE
                        val fresh = buildWebView()
                        setContentView(fresh)
                        fresh.loadUrl(APPASSETS_URL)
                    } catch (t: Throwable) {
                        Log.e(TAG, "Failed recreating WebView after renderer loss", t)
                    }
                    return true
                }
            }
        }
    }

    private fun registerBackHandler() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val callback = OnBackInvokedCallback { handleBack() }
                backCallback = callback
                val priority = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT
                } else {
                    0
                }
                onBackInvokedDispatcher.registerOnBackInvokedCallback(priority, callback)
            }
        } catch (t: Throwable) {
            Log.w(TAG, "back callback unavailable", t)
        }
    }

    private fun unregisterBackHandler() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && backCallback != null) {
                (backCallback as? OnBackInvokedCallback)?.let {
                    onBackInvokedDispatcher.unregisterOnBackInvokedCallback(it)
                }
                backCallback = null
            }
        } catch (_: Throwable) {}
    }

    private fun handleBack() {
        try {
            val wv = webView
            if (wv != null && wv.canGoBack()) {
                wv.goBack()
            } else {
                finish()
            }
        } catch (_: Throwable) {
            finish()
        }
    }

    @Deprecated("Only dispatched below API 33")
    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            handleBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun setImmersiveFullscreen() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                window.attributes = window.attributes.apply {
                    layoutInDisplayCutoutMode =
                        WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.setDecorFitsSystemWindows(false)
                window.insetsController?.let { controller ->
                    controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                    controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                )
                @Suppress("DEPRECATION")
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
        } catch (t: Throwable) {
            Log.w(TAG, "Error applying immersive fullscreen", t)
        }
    }

    private fun showFallbackScreen(t: Throwable) {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(0xFF071521.toInt())
            setPadding(40, 40, 40, 40)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val title = TextView(this).apply {
            text = "⚡ Charge the Robot"
            textSize = 22f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }

        val details = TextView(this).apply {
            text = "Starting game...\n${t.localizedMessage ?: t.javaClass.simpleName}"
            textSize = 14f
            setTextColor(0xFF91EFFF.toInt())
            gravity = Gravity.CENTER
            setPadding(0, 20, 0, 30)
        }

        val retryBtn = Button(this).apply {
            text = "Retry Launch"
            setOnClickListener {
                recreate()
            }
        }

        root.addView(title)
        root.addView(details)
        root.addView(retryBtn)
        setContentView(root)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        try {
            webView?.saveState(outState)
        } catch (_: Throwable) {}
    }

    override fun onResume() {
        super.onResume()
        setImmersiveFullscreen()
        try {
            webView?.onResume()
        } catch (_: Throwable) {}
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            setImmersiveFullscreen()
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            webView?.onPause()
        } catch (_: Throwable) {}
    }

    override fun onDestroy() {
        unregisterBackHandler()
        try {
            webView?.let { wv ->
                wv.stopLoading()
                wv.destroy()
            }
            webView = null
        } catch (_: Throwable) {}
        super.onDestroy()
    }
}