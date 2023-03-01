package com.schoolproject.gigs;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeckAdapter extends RecyclerView.Adapter<DeckAdapter.DeckViewHolder> {
    private List<deck> decks;

    public DeckAdapter(List<deck> decks) {
        this.decks = decks;
    }

    @NonNull
    @Override
    public DeckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deck, parent, false);
        return new DeckViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeckViewHolder holder, int position) {
        deck Deck = decks.get(position);
        holder.textViewDeck.setText(Deck.getName());
    }

    @Override
    public int getItemCount() {
        return decks.size();
    }

    class DeckViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewDeck;

        public DeckViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDeck = itemView.findViewById(R.id.deck_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(itemView.getContext(),newFlashCard.class);
            SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("Title", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Title",textViewDeck.getText().toString());
            editor.apply();
            itemView.getContext().startActivity(intent);
            ((Activity) itemView.getContext()).finish();
        }
    }
}
