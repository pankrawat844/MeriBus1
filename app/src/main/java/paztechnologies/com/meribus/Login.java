package paztechnologies.com.meribus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;

import paztechnologies.com.meribus.Parser.WebServiceCall;

/**
 * Created by Admin on 3/16/2017.
 */

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    Button login;
    EditText username, password;
    AwesomeValidation mAwesomeValidation;
    ImageView google_plus;
    SignInButton google_plus_btn;
    String strResponce;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
//        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
//        builder.appendPath("time");
//        ContentUris.appendId(builder, 12);
//        Intent intent = new Intent(Intent.ACTION_VIEW)
//                .setData(builder.build());
//        startActivity(intent);
        init();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAwesomeValidation.validate()) {
                    Connectivity connectivity = new Connectivity();
                    if (connectivity.isNetworkAvilable(Login.this)) {
                        new Call_Service().execute();
                    } else {
                        Toast.makeText(Login.this, "Internet is not Connected,Please Try Again ", 3).show();
                    }

                }
            }
        });

        google_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }
    private void init(){
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";

        login=(Button)findViewById(R.id.login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        google_plus_btn = (SignInButton) findViewById(R.id.google_plus_btn);
        google_plus = (ImageView) findViewById(R.id.google_plus);


         mAwesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);
        mAwesomeValidation.setColor(R.color.apptheme);
        // mAwesomeValidation.addValidation(this, R.id.username, regexPassword, R.string.username_error);
        //  mAwesomeValidation.addValidation(this, R.id.password,  regexPassword, R.string.password_error);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        google_plus_btn.setSize(SignInButton.SIZE_WIDE);
        google_plus_btn.setScopes(gso.getScopeArray());
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn() {
        //Creating an intent
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        //Starting intent for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If signin
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        //If the login succeed
        if (result.isSuccess()) {
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(this, "Login" + acct.getDisplayName() + acct.getEmail(), Toast.LENGTH_LONG).show();

            //Displaying name and email
            //   textViewName.setText(acct.getDisplayName());
            // textViewEmail.setText(acct.getEmail());

            //Initializing image loader
//            imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext())
//                    .getImageLoader();
//
//            imageLoader.get(acct.getPhotoUrl().toString(),
//                    ImageLoader.getImageListener(profilePhoto,
//                            R.mipmap.ic_launcher,
//                            R.mipmap.ic_launcher));

            //Loading image
            // profilePhoto.setImageUrl(acct.getPhotoUrl().toString(), imageLoader);

        } else {
            //If login fails
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }

    private void savePropData() {
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "getCustomerLoginDetails");

            PropertyInfo Orderid = new PropertyInfo();
            Orderid.setType(android.R.string.class);
            Orderid.setName("_emailAddress");
            Orderid.setValue(username.getText().toString());
            request.addProperty(Orderid);

            PropertyInfo aa123 = new PropertyInfo();
            aa123.setType(android.R.string.class);
            aa123.setName("_password");
            aa123.setValue(password.getText().toString());
            request.addProperty(aa123);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport =
                    new HttpTransportSE("http://sales.meribus.com/Service1.svc", 5000);
            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/IService1/getCustomerLoginDetails", envelope);
            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();

            strResponce = soapPrimitive.toString();
            Log.e("TAG", "Soap primitive1" + strResponce);
        } catch (SocketTimeoutException e) {

        } catch (Exception e) {
            Log.e("TAG", "Soap Exception" + e.toString());
        }
    }

    private class Call_Service extends AsyncTask<String[], Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(Login.this);
        private Login activity;
        private String soapAction;
        private String methodName;
        private String paramsName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading....");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String[]... params) {
            savePropData();
            return strResponce;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.e("resulttttt",s);
            progressDialog.dismiss();

            try {
                if (s.contains("[")) {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONArray arr = jsonArray.getJSONArray(0);
                    for (int i = 0; i < arr.length(); i++) {
                        SharedPreferences sharedPreferences = getSharedPreferences("app", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        JSONObject jsonObject = arr.getJSONObject(i);

                        editor.putBoolean("islogin", true);
                        editor.putString("name", jsonObject.getString("name"));
                        editor.putString("password", jsonObject.getString("password"));
                        editor.putString("email", jsonObject.getString("emailid"));
                        editor.putString("phoneno", jsonObject.getString("phonenumber"));
                        editor.putString("gender", jsonObject.getString("gender"));
                        editor.commit();
                        Intent home = new Intent(Login.this, Home.class);
                        startActivity(home);
                        finish();
                    }
                    // Toast.makeText(Login.this, s, 3).show();
                } else {
                    Toast.makeText(Login.this, "Emailid And Password is Not Correct, Try Again", 3).show();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }
}
