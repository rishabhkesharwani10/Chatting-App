package com.wc.whyychats;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Viewholder> {
    Context home;
    ArrayList<User> userArrayList;
    public UserAdapter(Home home, ArrayList<User> userArrayList) {
        this.home=home;
        this.userArrayList=userArrayList;

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(home).inflate(R.layout.userdesk,parent,false);
        return new Viewholder(view);

    }
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        User user=userArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getUid())){
            holder.itemView.setVisibility(View.GONE);
        }
        holder.user_name.setText(user.getName());
        holder.user_status.setText(user.getStatus());
        Picasso.get().load(user.getImageUri()).into(holder.user_profile);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(home,Chatting.class);
                intent.putExtra("name",user.getName());
                intent.putExtra("ReceiverImage",user.getImageUri());
                intent.putExtra("uid",user.getUid());
                home.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {

        return userArrayList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {
        CircleImageView user_profile;
        TextView user_name;
        TextView user_status;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            user_profile=itemView.findViewById(R.id.user_image);
            user_name=itemView.findViewById(R.id.user_name);
            user_status=itemView.findViewById(R.id.user_status);
        }
    }
}
