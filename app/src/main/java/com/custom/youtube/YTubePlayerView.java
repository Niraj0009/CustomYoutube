package com.custom.youtube;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Iterator;

public class YTubePlayerView extends WebView {
    public Activity activity;
    private ArrayList<String> classes = new ArrayList<>();
    public Handler handler = new Handler();
    private WebView webView;

    public YTubePlayerView(Context context) {
        super(context);
        initView(context);
    }

    public YTubePlayerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.webView = new WebView(context.getApplicationContext());
        initView(context);
    }

    public void setInstanseOfActivity(Activity activity2) {
        this.activity = activity2;
        MyStorage.getInstance().storage.put("myActivity", this.activity);
    }

    private void initView(Context context) {
        initialList();
        hideSomeSectionOfBlog(this);
        LayoutInflater.from(context);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setUseWideViewPort(true);
        getSettings().setLoadWithOverviewMode(true);
        setWebChromeClient(new MyChrome());
        getSettings().setUserAgentString("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:50.0) Gecko/20100101 Firefox/50.0");
        getSettings().setDisplayZoomControls(true);
        //  getSettings().setAllowContentAccess(false);
        getSettings().setMediaPlaybackRequiresUserGesture(false);
        getSettings().setDomStorageEnabled(true);
        getSettings().setLoadsImagesAutomatically(true);
        getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        getSettings().setAllowContentAccess(true);
        getSettings().setAllowFileAccess(true);
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false; // allow loading inside WebView
            }

            public void onPageCommitVisible(WebView webView, String str) {
                super.onPageCommitVisible(webView, str);
            }

            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
            }

            @Override
            public void onPageFinished(WebView webView, String url) {
                super.onPageFinished(webView, url);
                tryAutoPlay(webView);
                YTubePlayerView.this.hideSomeSectionOfBlog(webView);
                YTubePlayerView.this.scheduleHideContent(webView);
            }

            public void onReceivedError(WebView webView, int i, String str, String str2) {
                webView.getSettings();
                webView.loadData("Please try after some time.", "text/html", "UTF-8");
            }

            public boolean onRenderProcessGone(WebView webView, RenderProcessGoneDetail renderProcessGoneDetail) {
                return super.onRenderProcessGone(webView, renderProcessGoneDetail);
            }


        });

        setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View view) {
                return true;
            }
        });
    }

    private void tryAutoPlay(WebView webView) {
        webView.loadUrl(
                "javascript:(function() {" +
                        "  var btn = document.getElementsByClassName('ytp-play-button ytp-button')[0];" +
                        "  if(btn && btn.getAttribute('aria-label').indexOf('Play') !== -1) {" +
                        "    btn.click();" +
                        "  } else {" +
                        "    setTimeout(arguments.callee, 500);" +
                        "  }" +
                        "})();"
        );
    }

    private void initialList() {
        this.classes.add("ytp-chrome-top-buttons");
        this.classes.add("ytp-title");
        this.classes.add("ytp-youtube-button ytp-button yt-uix-sessionlink");
        this.classes.add("ytp-button ytp-endscreen-next");
        this.classes.add("ytp-button ytp-endscreen-previous");
        this.classes.add("ytp-show-cards-title");
        this.classes.add("ytp-endscreen-content");
        this.classes.add("ytp-chrome-top");
        this.classes.add("ytp-share-button");
        this.classes.add("ytp-watch-later-button");
        this.classes.add("ytp-pause-overlay");
    }

    public void onConfigurationChanged(Configuration configuration) {
        try {
            super.onConfigurationChanged(configuration);
            if (configuration.orientation == 1) {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                hideFullScreen();
            } else if (configuration.orientation == 2) {
                goFullScreenVideo();
            }
        } catch (Exception unused) {
            unused.printStackTrace();
        }
    }

    public void onMeasure(int i, int i2) {
        int i3;
        if (getLayoutParams().height == -2) {
            if (Build.VERSION.SDK_INT > 21) {
                i3 = MeasureSpec.makeMeasureSpec((MeasureSpec.getSize(i) * 9) / 24, MeasureSpec.EXACTLY);
            } else {
                i3 = MeasureSpec.makeMeasureSpec((MeasureSpec.getSize(i) * 9) / 16, MeasureSpec.EXACTLY);
            }
            super.onMeasure(i, i3);
            return;
        }
        super.onMeasure(i, i2);
    }

    public void hideSomeSectionOfBlog(final WebView webView2) {
        try {
            Iterator<String> it = this.classes.iterator();
            while (it.hasNext()) {
                String next = it.next();
                hideElementByClassName(webView2, next);
                removeElementByClassName(webView2, next);
            }
            hideContextMenu(webView2);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    webView2.loadUrl("javascript:(function() { document.getElementsByClassName('ytp-button ytp-settings-button')[0].style.display='inline'; })()");
                    webView2.loadUrl("javascript:(function() { document.getElementsByClassName('ytp-fullscreen-button ytp-button')[0].style.display='inline'; })()");
                    webView2.loadUrl("javascript:(function() { document.getElementsByClassName('annotation annotation-type-custom iv-branding')[0].style.display='none'; })()");
                }
            }, 1000);
        } catch (Exception unused) {
            unused.printStackTrace();
        }
    }

    private void hideElementByClassName(WebView webView2, String str) {
        webView2.loadUrl("javascript:(function() { document.getElementsByClassName('" + str + "')[0].style.display='none'; })()");
    }

    private void removeElementByClassName(WebView webView2, String str) {
        webView2.loadUrl("javascript:(function() {  var elements = document.getElementsByClassName('" + str + "');    while(elements.length > 0){        elements[0].parentNode.removeChild(elements[0]);    } })()");
    }

    public void scheduleHideContent(final WebView webView2) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                YTubePlayerView.this.hideSomeSectionOfBlog(webView2);
                YTubePlayerView.this.handler.postDelayed(this,2000L);
            }
        }, 2000L);
    }

    public  void goFullScreenVideo() {
        Activity activity2 = (Activity) MyStorage.getInstance().storage.get("myActivity");
        this.activity = activity2;
        activity2.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        hideSomeSectionOfBlog(this.webView);
    }

    public void hideFullScreen() {
        Activity activity2 = (Activity) MyStorage.getInstance().storage.get("myActivity");
        this.activity = activity2;

        final float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) (250 * scale + 0.5f);
        boolean xlarge = ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        if (large) {
            pixels = (int) (350 * scale + 0.5f);
        } else if (xlarge) {
            pixels = (int) (350 * scale + 0.5f);
        }
        hideSomeSectionOfBlog(this.webView);
        // setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,pixels));
        setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pixels));

    }

    private void hideContextMenu(WebView webView2) {
        webView2.loadUrl("javascript:(function() { var css = document.createElement('style');  css.type = 'text/css'; var styles = '.ytp-contextmenu { width: 0px !important}';if (css.styleSheet) css.styleSheet.cssText = styles; else css.appendChild(document.createTextNode(styles));document.getElementsByTagName('head')[0].appendChild(css); })()");
    }

    /* renamed from: co.exam.study.trend1.yt.YTubePlayerView$MyChrome */
    private class MyChrome extends WebChromeClient {
        private View mCustomView;
        private CustomViewCallback mCustomViewCallback;
        private int mOriginalSystemUiVisibility;

        MyChrome() {
        }

        public Bitmap getDefaultVideoPoster() {
            if (this.mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(YTubePlayerView.this.activity.getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout) YTubePlayerView.this.activity.getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            YTubePlayerView.this.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            this.mCustomViewCallback = null;
            YTubePlayerView.this.hideFullScreen();
        }

        public void onShowCustomView(View view, CustomViewCallback customViewCallback) {
            YTubePlayerView.this.activity = (Activity) MyStorage.getInstance().storage.get("myActivity");
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = view;
            view.setLongClickable(true);
            view.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    return true;
                }
            });
            FrameLayout frameLayout = (FrameLayout) view;
            int childCount = frameLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = frameLayout.getChildAt(i);
                if (childAt != null) {
                    childAt.setLongClickable(true);
                    childAt.setOnLongClickListener(new OnLongClickListener() {
                        public boolean onLongClick(View view) {
                            return true;
                        }
                    });
                }
            }
            this.mOriginalSystemUiVisibility = YTubePlayerView.this.activity.getWindow().getDecorView().getSystemUiVisibility();
            this.mCustomViewCallback = customViewCallback;
            ((FrameLayout) YTubePlayerView.this.activity.getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            YTubePlayerView.this.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            YTubePlayerView.this.goFullScreenVideo();
        }
    }
}
