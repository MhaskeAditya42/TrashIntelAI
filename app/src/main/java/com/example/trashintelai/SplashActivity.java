
package com.example.trashintelai;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;
import java.util.Set;


public class SplashActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        auth =FirebaseAuth.getInstance();
        textView=findViewById(R.id.user_details);
        user=auth.getCurrentUser();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                Intent intent;
                if(user == null){
                    intent = new Intent(getApplicationContext(), login.class);
                }else{
                    SharedPreferences pref = getSharedPreferences("pref",MODE_PRIVATE);
                    boolean value =pref.getBoolean("isAdmin",false);
                    if (value){
                        intent = new Intent(getApplicationContext(), AdminActivity.class);
                    }else{
                        intent = new Intent(getApplicationContext(), HomeActivity.class);
                    }
                }
                startActivity(intent);
                finish();

            }
        }, 3000);


    }

}
