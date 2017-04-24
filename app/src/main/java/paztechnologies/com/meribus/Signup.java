package paztechnologies.com.meribus;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;

public class Signup extends AppCompatActivity {
    AwesomeValidation mAwesomeValidation;
    Button register;
    String strResponce;
    EditText username, email, phoneno, password;
    RadioGroup gender;
    String gender_string = "male";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        init();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     mAwesomeValidation.validate();
                Connectivity connectivity = new Connectivity();

                if (connectivity.isNetworkAvilable(Signup.this)) {

                    new Call_Service().execute();
                } else {
                    //If login fails
                    Toast.makeText(Signup.this, "Internet is not Connected,Please Try Again", Toast.LENGTH_LONG).show();
                }
            }
        });
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton male = (RadioButton) findViewById(R.id.male);
                if (male.isChecked()) {
                    gender_string = "male";
                } else {
                    gender_string = "female";
                }
            }
        });
    }

    private void init() {
        register = (Button) findViewById(R.id.update_profile);
        mAwesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        mAwesomeValidation.addValidation(this, R.id.activity_register_username, "^[A-Za-z\\\\s]{1,}[\\\\.]{0,1}[A-Za-z\\\\s]{0,}$", R.string.username_error);
        mAwesomeValidation.addValidation(this, R.id.activity_register_email, "[a-zA-Z\\s]+", R.string.email_error);
        mAwesomeValidation.addValidation(this, R.id.activity_register_email, Patterns.EMAIL_ADDRESS, R.string.email_error1);

        mAwesomeValidation.addValidation(this, R.id.activity_phone_no, "^[2-9]{2}[0-9]{8}$", R.string.phno_error);
        mAwesomeValidation.addValidation(this, R.id.activity_register_password, "[a-zA-Z\\s]+", R.string.password_error);
//        mAwesomeValidation.addValidation(this, R.id.activity_residence_add, "[a-zA-Z\\s]+", R.string.address_error);
//        mAwesomeValidation.addValidation(this, R.id.activity_office_add, "[a-zA-Z\\s]+", R.string.address_error);

        username = (EditText) findViewById(R.id.activity_register_username);
        email = (EditText) findViewById(R.id.activity_register_email);
        password = (EditText) findViewById(R.id.activity_register_password);
        phoneno = (EditText) findViewById(R.id.activity_register_password);
        gender = (RadioGroup) findViewById(R.id.radio_grp);
    }

    private void savePropData() {
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "insertNewRegistration");

            PropertyInfo username = new PropertyInfo();
            username.setType(android.R.string.class);
            username.setName("_name");
            username.setValue(this.username.getText().toString());
            request.addProperty(username);
            PropertyInfo Orderid = new PropertyInfo();
            Orderid.setType(android.R.string.class);
            Orderid.setName("_email");
            Orderid.setValue(email.getText().toString());
            request.addProperty(Orderid);

            PropertyInfo aa123 = new PropertyInfo();
            aa123.setType(android.R.string.class);
            aa123.setName("_password");
            aa123.setValue(password.getText().toString());
            request.addProperty(aa123);



            PropertyInfo phone = new PropertyInfo();
            phone.setType(android.R.string.class);
            phone.setName("_phonenumber");
            phone.setValue(phoneno.getText().toString());
            request.addProperty(phone);

            PropertyInfo gender = new PropertyInfo();
            gender.setType(android.R.string.class);
            gender.setName("_gender");
            gender.setValue(gender_string);
            request.addProperty(gender);

            PropertyInfo refer = new PropertyInfo();
            refer.setType(android.R.string.class);
            refer.setName("referralcode");
            refer.setValue(this.phoneno.getText().toString());
            request.addProperty(refer);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport =
                    new HttpTransportSE("http://sales.meribus.com/Service1.svc", 5000);
            androidHttpTransport.debug = true;

            androidHttpTransport.call("http://tempuri.org/IService1/insertNewRegistration", envelope);
            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();

            strResponce = soapPrimitive.toString();
            Log.e("TAG", "Soap primitive1" + strResponce);
        } catch (SocketTimeoutException e) {

        } catch (Exception e) {
            Log.e("TAG", "Soap Exception" + e.toString());
        }
    }

    private class Call_Service extends AsyncTask<String[], Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(Signup.this);
        private Login activity;
        private String soapAction;
        private String methodName;
        private String paramsName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading....");
            progressDialog.setCancelable(false);
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
                if (s.contains("Data Insert Succesfully")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
                    dialog.setMessage("Register Succesfully, Please Login.");
                    dialog.setTitle("Register");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Signup.this, Login.class);
                            startActivity(intent);
                            dialog.dismiss();
                            finish();
                        }
                    });
                    dialog.show();

                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
                    dialog.setMessage("Something Went Wrong, Please Try Again.");
                    dialog.setTitle("Register");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                        }
                    });
                    dialog.show();
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }
}
