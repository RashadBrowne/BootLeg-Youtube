package com.example.blyoutube;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private WebView Webview;
    String UserAgent = "Mozilla/5.0 (X11; Linux i686; rv:64.0) Gecko/20100101 Firefox/64.0";
    /*
    Fake Browser String so I don't get an outdated version of Youtube
    Note Youtube is fairly complex in terms of html and it's hard to follow what's going on
    This makes editing the website structure a pain to locate and find the ids to change
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Webview = findViewById(R.id.Youtube);
        Webview.setWebChromeClient(new MyChrome());
        Webview.setWebViewClient(new WebClient());//Prevents clicked links from opening in the browser
        Webview.loadUrl("https://www.youtube.com");//Website to load


        WebSettings webSettings = Webview.getSettings();// ¯\_(ツ)_/¯
        webSettings.setJavaScriptEnabled(true);//Enable javascript on the webview
        Webview.getSettings().setUserAgentString(UserAgent);//Use the fake Browser String
    }

    public class WebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {//Override the default function and when the website loads execute some javascript
            view.evaluateJavascript("document.documentElement.style.setProperty('--yt-spec-brand-button-background','#002ccc');",null);
            //view.evaluateJavascript("document.getElementById('masthead-container').style.display='none';",null);
            //view.evaluateJavascript("document.getElementById('page-manager').style.margin='0';",null);


            //This works as Youtube doesn't load each page regularly therefore if this element isn't present
            //on the home page (it isn't) then it doesn't affect the page
            view.evaluateJavascript("document.getElementById('primary').style.padding='0px';",null);
            }
    }

    @Override//Go back on the website
    public void onBackPressed() {
        if(Webview.canGoBack()){
            Webview.goBack();
        }
        else {
            super.onBackPressed();//Or exit app
        }
    }



//Required to make it work with fullscreen https://www.monstertechnocodes.com/2018/07/how-to-enable-fullscreen-mode-in-any.html
private class MyChrome extends WebChromeClient {

    private View mCustomView;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private int mOriginalOrientation;
    private int mOriginalSystemUiVisibility;

    MyChrome() {}

    public Bitmap getDefaultVideoPoster()
    {
        if (mCustomView == null) {
            return null;
        }
        return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
    }

    public void onHideCustomView()
    {
        ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
        this.mCustomView = null;
        getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
        setRequestedOrientation(this.mOriginalOrientation);
        this.mCustomViewCallback.onCustomViewHidden();
        this.mCustomViewCallback = null;
    }

    public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
    {
        if (this.mCustomView != null)
        {
            onHideCustomView();
            return;
        }
        this.mCustomView = paramView;
        this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
        this.mOriginalOrientation = getRequestedOrientation();
        this.mCustomViewCallback = paramCustomViewCallback;
        ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
        getWindow().getDecorView().setSystemUiVisibility(3846);
    }
}
}
