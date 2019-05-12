package id.ac.umn.keburusarjanainc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import id.ac.umn.keburusarjanainc.model.ArticlesList;
import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever;

public class QRCodeScanner extends AppCompatActivity implements BarcodeRetriever{
//    private WebView myWebView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);

        BarcodeCapture barcodeCapture = (BarcodeCapture) getSupportFragmentManager().findFragmentById(R.id.barcode);
        barcodeCapture.setRetrieval(this);
    }

    @Override
    public void onPermissionRequestDenied() {

    }

    @Override
    public void onRetrievedFailed(String reason) {

    }

    @Override
    public void onRetrieved(final Barcode barcode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                setContentView(R.layout.activity_result_scan);
//
//                myWebView2 = (WebView)findViewById(R.id.webView2);
//                WebSettings webSettings = myWebView2.getSettings();
//                myWebView2.setWebViewClient(new WebViewClient());
//                myWebView2.loadUrl(barcode.displayValue);

                String scannedLink = barcode.displayValue;
                String[] strings = scannedLink.split("/", 0);
                String mustHaveString = "ultimagz.com";
                if(strings[2].equals(mustHaveString)){
                    if(strings.length>4 ){
                        String slug = strings[strings.length -1];
                        loadArticle("?slug="+slug);
                    }
                    else{
                        showErrorToastMessage();
                    }
                }
                else{
                    showErrorToastMessage();
                }
            }
        });
    }

    @Override
    public void onRetrievedMultiple(Barcode closetToClick, List<BarcodeGraphic> barcode) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    private void showErrorToastMessage(){
        Toast.makeText(getApplicationContext(), "Wrong QR Code. Try again or Scan Another one", Toast.LENGTH_LONG).show();
    }

    private void loadArticle(String URL_PARAM){

        final String URL_DATA = "http://ultimagz.com/wp-json/wp/v2/posts";

        Log.d("URL CodeScanned", "URL yang akan ditarik : " + URL_DATA + URL_PARAM);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA + URL_PARAM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);

                        JSONObject jo = array.getJSONObject(0);
                        JSONObject jo_title = jo.getJSONObject("title");
                        JSONObject jo_image = jo.getJSONObject("better_featured_image");
                        JSONObject jo_content = jo.getJSONObject("content");

                        ArticlesList articles = new ArticlesList(jo_title.getString("rendered"), jo_image.getString("source_url"), jo_content.getString("rendered"), jo.getString("date"));

                    final String KEY_TITLE = "title";
                    final String KEY_IMAGE = "image";
                    final String KEY_CONTENT = "content";
                    final String KEY_DATE = "date";
                    Intent skipIntent = new Intent(getApplicationContext(), ArticleActivity.class);
                    skipIntent.putExtra(KEY_TITLE, articles.getArticle_title());
                    skipIntent.putExtra(KEY_IMAGE, articles.getArticle_image());
                    skipIntent.putExtra(KEY_CONTENT, articles.getArticle_content());
                    skipIntent.putExtra(KEY_DATE, articles.getArticle_date());
                    startActivity(skipIntent);

                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}