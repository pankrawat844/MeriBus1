package paztechnologies.com.meribus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.payu.india.Model.PayuConfig;
import com.payu.india.Payu.PayuConstants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Admin on 3/9/2017.
 */

public class Payment_webview extends Activity {
    WebView mWebView;
    Bundle bundle;
    PayuConfig payuConfig;
    String url, merchantResponse, payuReponse, merchantHash;
    boolean isSuccessTransaction;
    int storeOneClickHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_webview);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        bundle = getIntent().getExtras();
        payuConfig = bundle.getParcelable(PayuConstants.PAYU_CONFIG);


        switch (payuConfig.getEnvironment()) {
            case PayuConstants.PRODUCTION_ENV:
                url = PayuConstants.PRODUCTION_PAYMENT_URL;
                break;
            case PayuConstants.MOBILE_STAGING_ENV:
                url = PayuConstants.MOBILE_TEST_PAYMENT_URL;
                break;
            case PayuConstants.STAGING_ENV:
                url = PayuConstants.TEST_PAYMENT_URL;
                break;
//            case PayuConstants.MOBILE_DEV_ENV:
//                url = PayuConstants.MOBILE_DEV_PAYMENT_URL;
//                break;
            default:
                url = PayuConstants.PRODUCTION_PAYMENT_URL;
                break;
        }

        // Setting view port for NB

        // Hiding the overlay


        mWebView.addJavascriptInterface(new Object() {

            /**
             * Call back from surl - sucess transaction
             * with no argument.
             * just send empty string back to calling activity
             */
            @JavascriptInterface
            public void onSuccess() {
                onSuccess("");
            }

            /**
             * call back function from surl - success transaction.
             * keep the data in {@link//PaymentsActivity#merchantResponse}
             * @param result
             */
            @JavascriptInterface
            public void onSuccess(final String result) {
                merchantResponse = result;
            }

            /**
             * Attempt to deprecate surl.
             * Javascript interface call from payu server.
             * Lets keep the data in local variable and pass it to main activity.
             * @param result json data of post param.
             */

            @JavascriptInterface
            public void onPayuSuccess(final String result) {

                isSuccessTransaction = true;
                payuReponse = result;

                if (storeOneClickHash == PayuConstants.STORE_ONE_CLICK_HASH_MOBILE) { // store it only if i need to store it
                    try {
                        JSONObject hashObject = new JSONObject(payuReponse);
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
                callTimer();
            }

            /**
             * Call back from furl - failure transaction
             * with no argument.
             * just send empty string back to calling activity
             */
            @JavascriptInterface
            public void onFailure() {
                onFailure("");
            }

            /**
             * call back function from furl - failure transaction.
             * keep the value in {@link// PaymentsActivity#merchantResponse}
             * @param result
             */
            @JavascriptInterface
            public void onFailure(final String result) {
                merchantResponse = result;
            }

            /**
             * Attempt to deprecate furl.
             * Javascript call from payu server.
             * Lets keep the data in local variable and pass it to calling activity.
             * @param result
             */
            @JavascriptInterface
            public void onPayuFailure(final String result) {
                isSuccessTransaction = false;
                payuReponse = result;
                callTimer();
            }
        }, "PayU");

        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            // flag to tell whether surl or furl loaded.
            private boolean isMerchantUrlStarted;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (isPayuResponseReceived()) // loading either surl or furl.
                    isMerchantUrlStarted = true;

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (isMerchantUrlStarted) { // finishing surl or furl
                    // finish the activity.
                    onMerchantUrlFinished();
                }
            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.postUrl(url, payuConfig.getData().getBytes());
    }


    /**
     * Helper function return the availablity of payu response given by nPayuSuccess(String)
     *
     * @return true or false.
     */
    boolean isPayuResponseReceived() {
        if (null != payuReponse) return true;
        return false;
    }

    /**
     * Just to make sure we finish activity even if the merchant's url got into trouble,
     * should be called from onPayuSuccess(String) or onPayuFailure(String)
     */
    void callTimer() {
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                // tick tick tick tick....
            }

            public void onFinish() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isFinishing()) {
                            onMerchantUrlFinished();
                        }
                    }
                });

            }
        }.start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
            //Lets pass the result back to previous activity
            setResult(resultCode, data);
            Toast.makeText(this, data.toString(), 3).show();
            // finish();
        }
    }

    /**
     * This function takes care of sending the data back to calling activity with the status, merchantResponse, payuresponse.
     */
    public void onMerchantUrlFinished() {
        // finish the activity.
        // finish the activity.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    Intent intent = new Intent();
                    intent.putExtra("result", merchantResponse);
                    intent.putExtra("payu_response", payuReponse);
                    if (storeOneClickHash == PayuConstants.STORE_ONE_CLICK_HASH_SERVER &&
                            null != merchantHash) {
                        intent.putExtra(PayuConstants.MERCHANT_HASH, merchantHash);
                    }
                    if (isSuccessTransaction)
                        setResult(Activity.RESULT_OK, intent);
                    else
                        setResult(Activity.RESULT_CANCELED, intent);

                    finish();
                }
            }
        });
    }
}
