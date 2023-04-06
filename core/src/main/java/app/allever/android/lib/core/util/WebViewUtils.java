package app.allever.android.lib.core.util;

import android.os.Build;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewUtils {

    //    private static final String WEBVIEW_CONTENT = "<html><head></head><body style=\"text-align:justify;margin:0;\">%s</body></html>";
    private static final String WEBVIEW_CONTENT = "<html><head></head><body style=\"margin:0;\">%s</body></html>";

    public static void loadData(WebView webView, String content) {
        if (webView == null) {
            return;
        }

        webView.setScrollContainer(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//支持js
        webSettings.setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//设置允许JS弹窗
        webSettings.setAllowFileAccess(true);//设置可以访问文件
        webSettings.supportMultipleWindows();//多窗口
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        //webview的缓存模式
//        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);
        webSettings.setNeedInitialFocus(true);

        webView.setLayerType(View.LAYER_TYPE_NONE, null);//关闭硬件加速即可，也不能使用软件加速

        //设置自适应屏幕，两者合用
        //webSettings.setUseWideViewPort(true);//将图片调整到适合webview的大小
        //webSettings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小

        webSettings.setSupportZoom(false);  //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(false);//设置内置的缩放控件。
        webSettings.setDisplayZoomControls(false);//隐藏原生的缩放控件
        //webSettings.setTextZoom(100);

        webView.setBackgroundColor(0);

//        webView.loadData(String.format(WEBVIEW_CONTENT, content), "text/html", "utf-8");
        webView.loadData(getWebViewContent(content), "text/html;charset=UTF-8", "utf-8");
    }

    /**
     * 设置富文本适配手机屏幕
     */
    private static String getWebViewContent(String content) {
//        *{margin:0px;}
        return "<html>\n" +
                "    <head>\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "        <style>img{max-width: 100%; width:100%; height:auto;}</style>\n" +
                "    </head>\n" +
                "    <body>" + content.trim() + " </body></html>";
    }


}
