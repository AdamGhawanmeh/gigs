package com.schoolproject.gigs;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class deckLayoutAdapter extends RecyclerView.Adapter<deckLayoutAdapter.MyViewHolder>{
    String TAG = "User Data From Firestore";
    Context context;
    private static ArrayList <deck> deckLayoutArrayList;

    public deckLayoutAdapter(Context context, ArrayList<deck> deckLayoutArrayList) {
        this.context = context;
        this.deckLayoutArrayList = deckLayoutArrayList;
    }

    @NonNull
    @Override
    public deckLayoutAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.deck_layout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder (@NonNull deckLayoutAdapter.MyViewHolder holder, int position) {
        deck dk = deckLayoutArrayList.get(position);
        holder.title.setText(dk.getName());
    }

    @Override
    public int getItemCount() {
        return deckLayoutArrayList.size();
    }

    public String deckName(int position) {
        deck dk = deckLayoutArrayList.get(position);
        return dk.getName();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }
}
