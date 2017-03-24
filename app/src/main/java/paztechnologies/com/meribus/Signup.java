package paztechnologies.com.meribus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

public class Signup extends AppCompatActivity {
    AwesomeValidation mAwesomeValidation;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        init();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAwesomeValidation.validate();
            }
        });
    }
    private void init(){
            register=(Button)findViewById(R.id.update_profile);
         mAwesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        mAwesomeValidation.addValidation(this, R.id.activity_register_username, "^[A-Za-z\\\\s]{1,}[\\\\.]{0,1}[A-Za-z\\\\s]{0,}$", R.string.username_error);
        mAwesomeValidation.addValidation(this, R.id.activity_register_email, "[a-zA-Z\\s]+", R.string.email_error);
        mAwesomeValidation.addValidation(this, R.id.activity_register_email, Patterns.EMAIL_ADDRESS, R.string.email_error1);

        mAwesomeValidation.addValidation(this, R.id.activity_phone_no, "^[2-9]{2}[0-9]{8}$", R.string.phno_error);
        mAwesomeValidation.addValidation(this, R.id.activity_register_password,  "[a-zA-Z\\s]+", R.string.password_error);
        mAwesomeValidation.addValidation(this, R.id.activity_residence_add, "[a-zA-Z\\s]+", R.string.address_error);
        mAwesomeValidation.addValidation(this, R.id.activity_office_add,  "[a-zA-Z\\s]+", R.string.address_error);

    }
}
