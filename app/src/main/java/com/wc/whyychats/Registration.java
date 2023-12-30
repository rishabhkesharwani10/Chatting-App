package com.wc.whyychats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registration extends AppCompatActivity {
    TextView  signin_btn,signup_btn;
    CircleImageView profile_image;
    EditText reg_name,reg_email,reg_pass,reg_cpass;
    FirebaseAuth auth;
    FirebaseStorage storage;
    String imageURI;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);

        storage =FirebaseStorage.getInstance();
        auth=FirebaseAuth.getInstance();


        database=FirebaseDatabase.getInstance();
        profile_image=findViewById(R.id.profile_image);
        reg_name=findViewById(R.id.reg_name);
        reg_email=findViewById(R.id.reg_email);
        reg_pass=findViewById(R.id.reg_pass);
        reg_cpass=findViewById(R.id.reg_cpass);
        signin_btn=findViewById(R.id.signin_btn);
        signup_btn=findViewById(R.id.signup_btn);
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String name=reg_name.getText().toString();
                String email=reg_email.getText().toString();
                String password=reg_pass.getText().toString();
                String cpass=reg_cpass.getText().toString();
                String status="New user on whYY Chats";


                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
                        TextUtils.isEmpty(password) ||  TextUtils.isEmpty(cpass))
                {
                    progressDialog.dismiss();
            Toast.makeText(Registration.this,"Enter Name",Toast.LENGTH_SHORT).show();
                }else if (!email.matches(emailPattern))
                {
                    progressDialog.dismiss();
                    reg_email.setError("Please Enter Valid Email");
                    Toast.makeText(Registration.this,"Please Enter Valid Email",Toast.LENGTH_SHORT).show();

                } else if (!password.equals(cpass)) {
                    progressDialog.dismiss();
            Toast.makeText(Registration.this,"Password does not match",Toast.LENGTH_SHORT).show();


                } else if (password.length()<5) {
                    progressDialog.dismiss();
                    Toast.makeText(Registration.this,"Enter 5 character password",Toast.LENGTH_SHORT).show();


                }else {
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                DatabaseReference reference=database.getReference().child("User").child(auth.getUid());
                                StorageReference storageReference=storage.getReference().child("Upload").child(auth.getUid());
                                if (imageUri!=null){
                                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageURI=uri.toString();
                                                        User user=new User(auth.getUid(),name,email,imageURI,status);
                                                        reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                           if(task.isSuccessful()){
                                                               progressDialog.dismiss();
                                                               startActivity(new Intent(Registration.this,Home.class));
                                                           }     else {
                                                               Toast.makeText(Registration.this,"Error",Toast.LENGTH_SHORT).show();
                                                           }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }else {
                                    String status="New user on whYY Chats";
                                    imageURI="https://firebasestorage.googleapis.com/v0/b/whyy-chats.appspot.com/o/11.jpg?alt=media&token=0afe8a69-b5f5-492c-baf0-2a78a9fa3953";
                                    User user=new User(auth.getUid(),name,email,imageURI,status);
                                    reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                startActivity(new Intent(Registration.this,Home.class));
                                            }     else {
                                                Toast.makeText(Registration.this,"Error",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                            }else {
                                progressDialog.dismiss();
                                    Toast.makeText(Registration.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }


            }
        });

profile_image.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }
});
         signin_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(Registration.this, Login.class));
        }
    });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(data!=null)
            {
               imageUri=data.getData();
                profile_image.setImageURI(imageUri);
            }
        }

    }
}