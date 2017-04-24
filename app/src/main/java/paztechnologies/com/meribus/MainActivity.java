package paztechnologies.com.meribus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
        TextView signin,register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        SharedPreferences sharedPreferences = getSharedPreferences("app", 0);
        if (sharedPreferences.getBoolean("islogin", false)) {
            Intent home = new Intent(MainActivity.this, Home.class);
            startActivity(home);
            finish();
        }
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login= new Intent(MainActivity.this, Login.class);
                startActivity(login);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login= new Intent(MainActivity.this, Signup.class);
                startActivity(login);
            }
        });
    }

    private void init(){
        signin=(TextView)findViewById(R.id.signin);
        register=(TextView)findViewById(R.id.register);

        SharedPreferences sp = getSharedPreferences("pref", 0);
        Toast.makeText(this, sp.getString("token", "0"), 3).show();
    }
}
