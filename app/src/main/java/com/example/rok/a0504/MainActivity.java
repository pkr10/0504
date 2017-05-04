package com.example.rok.a0504;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText e1;
    WebView w1;
    Animation a1;
    LinearLayout l1,l2;
    ListView listview;
    ArrayList<String> data= new ArrayList<String>();
    ArrayList<String> data1 = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1 = (EditText)findViewById(R.id.editText);
        w1 = (WebView)findViewById(R.id.webview);
        w1.addJavascriptInterface(new siteAdd(),"MyApp");
        l1 = (LinearLayout)findViewById(R.id.linear);
        l2 = (LinearLayout)findViewById(R.id.linear2);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        listview = (ListView)findViewById(R.id.list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                l2.setVisibility(View.VISIBLE);
                l1.setVisibility(View.VISIBLE);
                listview.setVisibility(View.INVISIBLE);
                w1.loadUrl("http://"+data1.get(position).toString());
            }
        });
        Log.d("데이터",data1.toString());
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("삭제확인").setMessage("정말이에요").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        data.remove(data.get(position));
                        data1.remove(data1.get(position));
                        adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("NO",null).show();

                return false;
            }
        });
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
                l2.setVisibility(View.GONE);

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
        menu.add(0,2,0,"즐겨찾기 추가");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==1){
            l1.setAnimation(a1);
            l1.setVisibility(View.INVISIBLE);

            a1.start();
            listview.setVisibility(View.VISIBLE);
        }
        if(item.getItemId()==2){
            w1.loadUrl("file:///android_asset/www/urladd.html");
            l2.setAnimation(a1);
            a1.start();
    }
        return super.onOptionsItemSelected(item);
    }
    Handler handler = new Handler();

//    class Javascriptmethod{
//        @JavascriptInterface//이렇게 해준 애만 웹페이지에 호출가능
//        public void displayToast(){
//            Toast.makeText(getApplicationContext(),"추가되었습니다",Toast.LENGTH_SHORT).show();
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    AlertDialog.Builder builder =  new AlertDialog.Builder(MainActivity.this);
//                    builder.setTitle("그림변경").setMessage("그림을 변경하시겠습니까").setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            w1.loadUrl("javascript:changeImage()");
//                        }
//                    }).setNegativeButton("cancel",null).show();
//                }
//            });


//        }
//        public void getdata(String value){
//            data.add(value);
//            adapter.notifyDataSetChanged();
//        }

//    }
    class siteAdd {
        @JavascriptInterface
        public void getdata(String value,String value1) {
            data data2 = new data(value,value1);
            data.add(data2.name);
            data1.add(data2.url);
            adapter.notifyDataSetChanged();
        }
    @JavascriptInterface
    public void back(){
        l2.setVisibility(View.VISIBLE);
    }




    }


    public void onmyclick(View view) {
        switch(view.getId()){
            case R.id.button:
                w1.loadUrl("http://"+e1.getText().toString());
                break;
        }
    }
}
