package com.example.blyoutube;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {//I started putting references to websites too late and im too lazy to go back and find all....

    private WebView Webview;
    String UserAgent = "Mozilla/5.0 (X11; Linux i686; rv:64.0) Gecko/20100101 Firefox/64.0";
    /*
    Fake Browser String so I don't get an outdated version of Youtube

    *** Note Youtube is fairly complex in terms of html and it's hard to follow what's going on
    This makes editing the website structure a pain to locate and find the ids to change
    If most of the structure isn't part of the functionality of the web version of youtube im disappointed
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Webview = findViewById(R.id.Youtube);
        Webview.setWebChromeClient(new MyChrome());
        Webview.setWebViewClient(new WebClient());//Prevents clicked links from opening in the browser

        //Improving performance - https://www.youtube.com/watch?v=6FXO4jXiM3M
        //Webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//Makes it so that it using files downloaded before rather than going online
        //Downside is that if you sign in you have to clear the cache sometimes before it registers you logged in
        Webview.getSettings().setAppCacheEnabled(true);
        Webview.getSettings().setDomStorageEnabled(true);//Quote - DOM Storage is a way to store meaningful amounts of client-side data in a persistent and secure manner
        //^^Reference - https://www.viralpatel.net/introduction-html5-domstorage-api-example/
        Webview.getSettings().setUseWideViewPort(false);//Dunno
        Webview.getSettings().setSaveFormData(true);//Remember login information
        Webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);



        Webview.loadUrl("https://www.youtube.com");//Website to load
        WebSettings webSettings = Webview.getSettings();// ¯\_(ツ)_/¯
        webSettings.setJavaScriptEnabled(true);//Enable javascript on the webview
        Webview.getSettings().setUserAgentString(UserAgent);//Use the fake Browser String
    }

    public class WebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {//Taken from - https://www.androidauthority.com/working-with-webview-736873/
            if(Uri.parse(url).getHost().endsWith("youtube.com") || Uri.parse(url).getHost().endsWith("google.com")) {
                return false;//If the URL does contain the string “youtube.com”, then the shouldOverrideUrlLoading method
                //will return ‘false” and the URL will be loaded inside your WebView//
            }
            //If the URL doesn’t contain this string, then it’ll return “true.” At this point, we’ll
            //launch the user’s preferred browser, by firing off an Intent//
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(intent);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {//Override the default function and when the website loads execute some javascript
            view.evaluateJavascript("document.documentElement.style.setProperty('--yt-spec-brand-button-background','#002ccc');",null);
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



//Required to make it work with fullscreen - https://www.monstertechnocodes.com/2018/07/how-to-enable-fullscreen-mode-in-any.html
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
