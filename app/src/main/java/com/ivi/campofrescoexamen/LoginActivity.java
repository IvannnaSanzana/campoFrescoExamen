package com.ivi.campofrescoexamen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        EditText inputUser = (EditText) findViewById(R.id.textUser);
        EditText inputPassword = (EditText) findViewById(R.id.textPass);
        Button btnIngresar = findViewById(R.id.btnLogin);
        Button btnRegistrar = findViewById(R.id.btnRegister);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mAuth.signOut();
                String user = inputUser.getText().toString().trim();
                String pass = inputPassword.getText().toString().trim();

                if (user.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Por favor, ingrese sus credenciales", Toast.LENGTH_SHORT).show();
                } else {
                    iniciarSesion(user, pass);
                }
                inputUser.setText("");
                inputPassword.setText("");
            }
        });
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
            finish();
        }
    }

    public void iniciarSesion(String correo, String clave){
        mAuth.signInWithEmailAndPassword(correo, clave).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful() && hayConexion(LoginActivity.this)){
                    finish();
                    startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                    Toast.makeText(LoginActivity.this, "Bienvenido!", Toast.LENGTH_SHORT).show();
                }else if (!hayConexion(LoginActivity.this)){
                    mensajeConexion(LoginActivity.this);
                }else{
                    Toast.makeText(LoginActivity.this, "Error al iniciar sesión.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Error al iniciar sesión.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static boolean hayConexion(Context context){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if(netinfo != null && netinfo.isConnectedOrConnecting()){
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if(mobile != null && mobile.isConnectedOrConnecting() ||
                    wifi != null && wifi.isConnectedOrConnecting()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    public static void mensajeConexion(Context context){
        ImageView image = new ImageView(context);
        image.setImageResource(R.mipmap.ic_launcher);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setTitle("No hay acceso a Internet");
        alertBuilder.setMessage("Porfavor conéctate a Internet para continuar.");
        alertBuilder.setIcon(R.mipmap.ic_launcher);

        alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = alertBuilder.show();
        TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
        dialog.setView(image);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

    }
}