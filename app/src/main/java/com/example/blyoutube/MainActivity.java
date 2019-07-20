package com.example.blyoutube;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
            view.evaluateJavascript("document.getElementById('masthead-container').style.display='none';",null);
            view.evaluateJavascript("document.getElementById('page-manager').style.margin='0';",null);


            //I doubt this works as Youtube doesn't load each page regularly therefore if this element isn't present
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
}
