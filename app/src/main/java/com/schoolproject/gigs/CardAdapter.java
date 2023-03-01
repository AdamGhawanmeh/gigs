package com.schoolproject.gigs;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

  private List<String> mData;

  public CardAdapter(List<String> data) {
    mData = data;
  }

  @NonNull
  @Override
  public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
    return new CardViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
    String text = mData.get(position);
    holder.cardText.setText(text);
    Log.d("CardAdapter", "Data at position " + position + ": " + text);
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public static class CardViewHolder extends RecyclerView.ViewHolder {

    TextView cardText;

    public CardViewHolder(@NonNull View itemView) {
      super(itemView);
      cardText = itemView.findViewById(R.id.card_text);
    }
  }
}