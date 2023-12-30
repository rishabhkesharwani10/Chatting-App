package com.wc.whyychats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chatting extends AppCompatActivity {
    String ReceiverImage, ReceiverUID, ReceiverName,SenderUID;
    CircleImageView profileImage;
    TextView receiverName;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    public static String sImage;
    public static String rImage;

    TextView sendBtn;
    EditText editMessage;

    String senderRoom,receiverRoom;

    RecyclerView messageAdapter;
    ArrayList<Messages>messagesArrayList;
    MassageAdapter adapter;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        ReceiverName = getIntent().getStringExtra("name");
        ReceiverImage = getIntent().getStringExtra("ReceiverImage");
        ReceiverUID = getIntent().getStringExtra("uid");

        messagesArrayList=new ArrayList<>();

        profileImage = findViewById(R.id.user_image);
        receiverName = findViewById(R.id.receiverName);

        messageAdapter=findViewById(R.id.messageAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdapter.setLayoutManager(linearLayoutManager);
        adapter=new MassageAdapter(Chatting.this,messagesArrayList);
        messageAdapter.setAdapter(adapter);


        sendBtn=findViewById(R.id.sendBtn);
        editMessage=findViewById(R.id.editMessage);
        Picasso.get().load(ReceiverImage).into(profileImage);
        receiverName.setText(""+ ReceiverName);

        SenderUID=firebaseAuth.getUid();

        senderRoom=SenderUID+ReceiverUID;
        receiverRoom=ReceiverUID+SenderUID;

        DatabaseReference reference = database.getReference().child("User").child(firebaseAuth.getUid());
        DatabaseReference chatReference = database.getReference().child("chats").child(senderRoom).child("Messages");


        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            messagesArrayList.clear();


                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                 Messages messages=dataSnapshot.getValue(Messages.class);
                 messagesArrayList.add(messages);
                }
                    adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sImage=snapshot.child("imageUri").getValue().toString();
                rImage=ReceiverImage;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String message = editMessage.getText().toString();
                    if (message.isEmpty()) {

                        Toast.makeText(Chatting.this, "Valid Message", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    editMessage.setText("");
                    Date date = new Date();
                    Messages messages = new Messages(message, SenderUID, date.getTime());

                    database = FirebaseDatabase.getInstance();
                    database.getReference().child("chats")
                            .child(senderRoom)
                            .child("Messages")
                            .push()
                            .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    database.getReference().child("chats")
                                            .child(receiverRoom)
                                            .child("Messages")
                                            .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });
                                }
                            });


                }
            });


    }
}