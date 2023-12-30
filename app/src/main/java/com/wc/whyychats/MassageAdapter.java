package com.wc.whyychats;

import static androidx.recyclerview.widget.RecyclerView.*;
import static com.wc.whyychats.Chatting.rImage;
import static com.wc.whyychats.Chatting.sImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MassageAdapter extends RecyclerView.Adapter{
    Context context;
    ArrayList<Messages>messagesArrayList;
    int ITEM_SEND=1;
    int ITEM_RECEIVE=2;

    public MassageAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==ITEM_SEND){
            View view= LayoutInflater.from(context).inflate(R.layout.sender_item,parent,false);
            return new SenderViewHolder(view);
        }else {
            View view= LayoutInflater.from(context).inflate(R.layout.receiver_item,parent,false);
            return new ReceiverViewHolder(view);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Messages messages=messagesArrayList.get(position);

            if (holder.getClass()==SenderViewHolder.class){
                SenderViewHolder viewHolder=(SenderViewHolder) holder;
                viewHolder.textmessage.setText(messages.getMessage());

                Picasso.get().load(sImage).into(viewHolder.circleImageView);

            }else {
                ReceiverViewHolder viewHolder=(ReceiverViewHolder) holder;
                viewHolder.textmessage.setText(messages.getMessage());

                Picasso.get().load(rImage).into(viewHolder.circleImageView);


            }
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages=messagesArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId()))
        {
            return ITEM_SEND;
        }else {
            return ITEM_RECEIVE;
        }

    }

    class SenderViewHolder extends ViewHolder {
        CircleImageView circleImageView;
        TextView textmessage;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView=itemView.findViewById(R.id.profile_image);
            textmessage=itemView.findViewById(R.id.textMessages);
        }
    }
class ReceiverViewHolder extends ViewHolder {
    CircleImageView circleImageView;
    TextView textmessage;
     public ReceiverViewHolder(@NonNull View itemView) {
        super(itemView);
         circleImageView=itemView.findViewById(R.id.profile_image);
         textmessage=itemView.findViewById(R.id.textMessages);
    }
}

}
