package com.wc.whyychats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    FirebaseAuth auth;

    RecyclerView userRecycle;
    FirebaseDatabase database;
    UserAdapter adapter;
    ArrayList<User> userArrayList;
    TextView logout;
    ImageView imgSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        userArrayList = new ArrayList<>();
        DatabaseReference reference = database.getReference().child("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userArrayList.clear();
                adapter.notifyDataSetChanged();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    userArrayList.add(user);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        logout = findViewById(R.id.logout);
        imgSetting=findViewById(R.id.setting);
        userRecycle = findViewById(R.id.userRecycle);
        userRecycle.setLayoutManager(new LinearLayoutManager(this));

        adapter = new UserAdapter(Home.this, userArrayList);
        userRecycle.setAdapter(adapter);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(Home.this, R.style.Dialoge);
                dialog.setContentView(R.layout.dialog_layout);

                TextView yes_btn, no_btn;
                yes_btn = dialog.findViewById(R.id.yes_btn);
                no_btn = dialog.findViewById(R.id.no_btn);

                yes_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(Home.this, Registration.class));
                    }
                });
                no_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,SettingActivity.class));
            }
        });
    }
}
