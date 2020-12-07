package com.example.firebapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginClass extends Activity {

    private EditText etEmail, etPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.login_et_email);
        etPassword = findViewById(R.id.login_et_password);
    }

    public void Button_Login(View view){
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(getBaseContext(),"Faltan campos por acompletar", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(getBaseContext(),"Ingresando al sistema", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginClass.this, MainClass.class);
                        startActivity(intent);
                    }else {
                        Log.w("TYAM","signInWithEmail:failure",task.getException());
                        Toast.makeText(getBaseContext(),"Usuario y/o password incorrecta",Toast.LENGTH_LONG).show();
                    }
                });

    }

    public void Button_New_User(View view){
        Intent intent = new Intent(LoginClass.this, NewUserClass.class);
        startActivity(intent);
    }

}
