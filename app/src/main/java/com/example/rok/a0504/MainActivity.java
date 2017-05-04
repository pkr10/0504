package com.example.rok.a0504;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText e1;
    WebView w1;
    Animation a1;
    LinearLayout l1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1 = (EditText)findViewById(R.id.editText);
        w1 = (WebView)findViewById(R.id.webview);
        w1.addJavascriptInterface(new Javascriptmethod(),"MyApp");
        l1 = (LinearLayout)findViewById(R.id.linear);
        final ProgressDialog dialog;
        dialog = new ProgressDialog(this);
        w1.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                dialog.setMessage("Loading");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                e1.setText(url);
            }
        });
        w1.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress>=100)dialog.dismiss();
            }


            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                result.confirm();
                return super.onJsAlert(view, url, message, result);
            }
        });


        w1.loadUrl("http://blog.naver.com/pkr10");
        a1 = AnimationUtils.loadAnimation(this,R.anim.translate_top);
        a1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                l1.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        WebSettings webSettings = w1.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"즐겨찾기");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==1){
            w1.loadUrl("file:///android_asset/www/index.html");
            l1.setAnimation(a1);

            a1.start();
        }
        return super.onOptionsItemSelected(item);
    }
    Handler handler = new Handler();

    class Javascriptmethod{

        @JavascriptInterface//이렇게 해준 애만 웹페이지에 호출가능
        public void displayToast(){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder =  new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("그림변경").setMessage("그림을 변경하시겠습니까").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            w1.loadUrl("javascript:changeImage()");
                        }
                    }).setNegativeButton("cancel",null).show();
                }
            });


        }

    }

    public void onmyclick(View view) {
        switch(view.getId()){
            case R.id.button2:
                w1.loadUrl("javascript:changeImage()");
                break;
        }
    }
}
