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

public class NewUserClass  extends Activity {

    private FirebaseAuth mAuth;
    private EditText etEmail, etPassword;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_user_layout);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);

        mAuth = FirebaseAuth.getInstance();
    }

    public void Button_Add_User(View view){
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(getBaseContext(),"Faltan campos por acompletar", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(getBaseContext(),"Registro completado", Toast.LENGTH_LONG).show();
                        finish();
                        //Intent intent = new Intent(NewUserClass.this, LoginClass.class);
                        //startActivity(intent);
                    }else{
                        if(task.getException() != null){
                            Log.e("TYAM",task.getException().getMessage());
                        }
                        Toast.makeText(getBaseContext(),"Registro fallido!",Toast.LENGTH_LONG).show();
                    }
                });
    }


}
