package com.wc.whyychats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
public class Login extends AppCompatActivity {
    TextView  text_create;
    EditText email,pass;
    TextView signin_btn;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);


        auth=FirebaseAuth.getInstance();
        text_create=findViewById(R.id.text_create);

        signin_btn=findViewById(R.id.signin_btn);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.pass);
        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Pass=pass.getText().toString();
                String Email=email.getText().toString();
               if (TextUtils.isEmpty(Email) || TextUtils.isEmpty(Pass))
               {
                   Toast.makeText(Login.this,"Enter Valid Data" ,Toast.LENGTH_SHORT).show();

               }
               else {
                   auth.signInWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if(task.isSuccessful()){
                               startActivity(new Intent(Login.this,Home.class));
                           }else {
                               Toast.makeText(Login.this,"Error",Toast.LENGTH_SHORT).show();
                           }
                       }
                   });
               }


            }
        });


        text_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(Login.this, Registration.class));
            }
        });
    }
}
