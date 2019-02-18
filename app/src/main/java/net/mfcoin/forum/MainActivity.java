package net.mfcoin.forum;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.SslErrorHandler;
import android.net.http.SslError;

public class MainActivity extends AppCompatActivity {

    WebView vw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    public void onBackPressed() {
        vw.loadUrl("javascript: windowClose();");
        MainActivity.this.finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        //vw.loadUrl("javascript: windowClose();");
        MainActivity.this.finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        //vw.loadUrl("javascript: windowOpen();");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        vw.loadUrl("javascript: windowClose();");
        MainActivity.this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (vw.canGoBack()) {
                vw.goBack();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("JavascriptInterface")
    private void init() {
        vw = (WebView) findViewById(R.id.webview);
        //** Отключили вертикальную прокрутку **/
        vw.setVerticalScrollBarEnabled(false);
        //** Отключили горизонтальную прокрутку **/
        vw.setHorizontalScrollBarEnabled(false);
        //** Включили JavaScript **/
        vw.getSettings().setJavaScriptEnabled(true);
        //** Включили localStorage и т. п. **/
        vw.getSettings().setDomStorageEnabled(true);
        //** Отключили зум, т .к. нормальные приложения подобным функционалом не обладают **/
        vw.getSettings().setSupportZoom(false);
        //** Отключили поддержку вкладок, т .к. пользователь должен сидеть в SPA приложении **/
        vw.getSettings().setSupportMultipleWindows(false);
        //** Отключение контекстных меню по долгому клику **/
        vw.setLongClickable(false);
        //** в JavaScript'е создается объект API. Это будет наш мост в мир Java. **/
        vw.addJavascriptInterface(new WebAppInterface(this), "API");
        //** открываем локальный ресурс **/
        vw.loadUrl("https://forum.mfcoin.net");

        vw.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
            @TargetApi(Build.VERSION_CODES.N)
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return false;
            }

            @Override public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
                if (error.toString() == "piglet")
                    handler.cancel();
                else
                    handler.proceed();
            }
        });

        vw.setWebChromeClient(new WebChromeClient());
    }
}
