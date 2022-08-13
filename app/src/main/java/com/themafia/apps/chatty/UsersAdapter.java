package com.themafia.apps.chatty;

import android.content.Context;
import android.content.Intent;
import android.transition.CircularPropagation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    List<String> userListT;
    Context mContext;
    String username;

    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    public UsersAdapter(List<String> userListT, Context context, String username) {
        this.userListT = userListT;
        mContext = context;
        this.username = username;

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mReference.child("Users").child(userListT.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String otherName = snapshot.child("userName").getValue().toString();
                String imageURL = snapshot.child("image").getValue().toString();




                holder.userTextView.setText(otherName);

                if (imageURL.equals("null"))
                {
                    holder.dp.setImageResource(R.drawable.account);
                }
                else
                {
//                    holder.dp.setImageResource(R.drawable.account);
                    Picasso.get().load(imageURL).into(holder.dp);
                }

                holder.mCardView.setOnClickListener(v -> mContext.startActivity(new Intent(mContext , ChatActivity.class).putExtra("UserName" , username).putExtra("OtherName",otherName)));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, "Error " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userListT.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView userTextView;
        CircleImageView dp;
        CardView mCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userTextView = itemView.findViewById(R.id.userTextView);
            dp = itemView.findViewById(R.id.dataPage);
            mCardView = itemView.findViewById(R.id.carView);
        }
    }
}
