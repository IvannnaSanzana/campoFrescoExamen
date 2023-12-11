package com.ivi.campofrescoexamen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText inputUser = (EditText) findViewById(R.id.textUserR);
        EditText inputPassword = (EditText) findViewById(R.id.textPassR);
        EditText inputPasswordConfirm = (EditText) findViewById(R.id.textPassConfirm);
        Button btnRegistrar = findViewById(R.id.btnRegisterR);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = inputUser.getText().toString().trim();
                String pass = inputPassword.getText().toString().trim();
                String passConfirm = inputPasswordConfirm.getText().toString().trim();

                if (user.isEmpty() || pass.isEmpty() || passConfirm.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Por favor, rellene todos los campos.", Toast.LENGTH_SHORT).show();
                } else if (pass.equalsIgnoreCase(passConfirm)){
                    if(pass.length()>=6){
                        registrarUsuario(user,pass);
                    }else {
                        Toast.makeText(RegisterActivity.this, "Contraseña demasiado corta!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden! Vuelva a intentarlo.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void registrarUsuario(String correo, String clave){
        mAuth.createUserWithEmailAndPassword(correo, clave).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful() && LoginActivity.hayConexion(RegisterActivity.this)){
                    finish();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    Toast.makeText(RegisterActivity.this, "Su usuario se ha registrado con éxito!", Toast.LENGTH_SHORT).show();
                }else if (!LoginActivity.hayConexion(RegisterActivity.this)){
                    LoginActivity.mensajeConexion(RegisterActivity.this);
                }else{
                    Toast.makeText(RegisterActivity.this, "Error al registrar usuario.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Error al registrar usuario.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}