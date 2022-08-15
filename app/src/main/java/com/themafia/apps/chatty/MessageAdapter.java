package com.themafia.apps.chatty;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    List<MessageModelClass> list;
    String userName;

    boolean status;
    int send;
    int receive;

    public MessageAdapter(List<MessageModelClass> list, String userName) {
        this.list = list;
        this.userName = userName;

        status = false;
        send = 1;
        receive = 2;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == send)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_send,parent,false);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_recieve,parent,false);

        }

        return new MessageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.textView.setText(list.get(position).getMessage());
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            if (status)
            {
                textView = itemView.findViewById(R.id.textViewSend);
            }
            else
            {
                textView = itemView.findViewById(R.id.textViewReceived);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {


        String l1 = list.get(position).getFrom();
        String l2 = userName;

//        if (l1 == null){
//            Log.d("MAIN"  , "L1 |IS NULL");
//        }else if(l2== null){
//            Log.d("MAIN"  , "L2 |IS NULL");
//        }
        if (l1.equals(l2) ) {
            status = true;
            return send;
        }else {
            status = false;
            return receive;
        }
    }
}
