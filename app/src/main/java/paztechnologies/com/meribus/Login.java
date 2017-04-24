package paztechnologies.com.meribus;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

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
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        init();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAwesomeValidation.validate()) {
                    new Call_Service(Login.this, Constant.LOGIN_SOAP_ACTION, Constant.LOGIN_METHOD).execute(new String[]{"_emailAddress", "_password"}, new String[]{username.getText().toString(), password.getText().toString()});

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


    private class Call_Service extends AsyncTask<String[], Void, String> {
        private Login activity;
        private String soapAction;
        private String methodName;
        private String paramsName;

        public Call_Service(Login activity, String soapAction, String methodName) {
            this.activity = activity;
            this.methodName = methodName;
            this.soapAction = soapAction;

        }

        @Override
        protected String doInBackground(String[]... params) {
            SoapObject request = new SoapObject(Constant.NAME_SPACE, methodName);
            //add properties for soap object
            request.addProperty(params[0][0], params[1][0]);
            request.addProperty(params[0][1], params[1][1]);

            //request to server and get Soap Primitive response
            //  Log.d("result",WebServiceCall.callWSThreadSoapPrimitive(Constant.URL, soapAction, request));
            return WebServiceCall.callWSThreadSoapPrimitive(Constant.URL, soapAction, request);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.e("resulttttt",s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                Toast.makeText(Login.this, s, 3).show();
                Intent home = new Intent(Login.this, Home.class);
                startActivity(home);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
