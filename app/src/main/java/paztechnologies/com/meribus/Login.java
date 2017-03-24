package paztechnologies.com.meribus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

/**
 * Created by Admin on 3/16/2017.
 */

public class Login extends Activity {
    Button login;
    AwesomeValidation mAwesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        init();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAwesomeValidation.validate()) {
                    Intent home = new Intent(Login.this, Home.class);
                    startActivity(home);
                }
            }
        });
    }
    private void init(){
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";

        login=(Button)findViewById(R.id.login);

         mAwesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);
        mAwesomeValidation.setColor(R.color.apptheme);
        mAwesomeValidation.addValidation(this, R.id.username, "[a-zA-Z\\s]+", R.string.username_error);
        mAwesomeValidation.addValidation(this, R.id.password,  "[a-zA-Z\\s]+", R.string.password_error);

    }
}
