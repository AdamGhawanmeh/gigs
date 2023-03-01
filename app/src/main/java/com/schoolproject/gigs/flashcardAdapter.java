package com.schoolproject.gigs;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class flashcardAdapter extends RecyclerView.Adapter<flashcardAdapter.CardViewHolder> implements View.OnClickListener {

    private List<flashcardWid> cards;


    public flashcardAdapter(List<flashcardWid> cards) {
        this.cards = cards;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flashcard, parent, false);
        return new CardViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final CardViewHolder holder, int position) {
        flashcardWid card = cards.get(position);
        card.setShowingBack(false);
        holder.frontText.setText(card.getFrontText());
        holder.backText.setText(card.getBackText());
        holder.frontLayout.setVisibility(View.VISIBLE);
        holder.backLayout.setVisibility(View.GONE);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.cardView.getContext(), editCard.class);
                intent.putExtra("cardID",cards.get(holder.getAdapterPosition()).getID());
                intent.putExtra("term",cards.get(holder.getAdapterPosition()).getFrontText());
                intent.putExtra("definition",cards.get(holder.getAdapterPosition()).getBackText());
                holder.cardView.getContext().startActivity(intent);
                notifyDataSetChanged();
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard(holder.getAdapterPosition(),holder.backLayout,holder.frontLayout,holder.cardView,holder.backText,holder.frontText);
            }
        });


    }


    private void flipCard(int position,RelativeLayout backLayout,RelativeLayout frontLayout,CardView cardView,TextView backText,TextView frontText) {
        if (cards.get(position).isShowingBack()) {
            // Flip to show the front of the card
            ObjectAnimator flip = ObjectAnimator.ofFloat(cardView,"rotationX",180f, 0f);
            ObjectAnimator textFlipOut = ObjectAnimator.ofFloat(backText, "rotationX", -180f, 0f);
            ObjectAnimator textFlipIn = ObjectAnimator.ofFloat(frontText, "rotationX", 180f, 0f);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(flip, textFlipOut, textFlipIn);
            animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
            animatorSet.setDuration(600);
            animatorSet.start();
            cards.get(position).setShowingBack(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backLayout.setVisibility(View.GONE);
                    frontLayout.setVisibility(View.VISIBLE);
                }
            }, 300);

        } else {
            // Flip to show the back of the card
            ObjectAnimator flip = ObjectAnimator.ofFloat(cardView,"rotationX",180f, 0f);
            ObjectAnimator textFlipOut = ObjectAnimator.ofFloat(frontText, "rotationX", -180f, 0f);
            ObjectAnimator textFlipIn = ObjectAnimator.ofFloat(backText, "rotationX", 180f, 0f);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(flip, textFlipOut, textFlipIn);
            animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
            animatorSet.setDuration(600);
            animatorSet.start();
            cards.get(position).setShowingBack(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
            backLayout.setVisibility(View.VISIBLE);
            frontLayout.setVisibility(View.GONE);
                }
            }, 300);
        }
    }


    @Override
    public int getItemCount() {
        return cards.size();
    }

    @Override
    public void onClick(View view) {

    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private RelativeLayout frontLayout, backLayout;
        private TextView frontText, backText;
        private Button edit;

        public CardViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            frontLayout = itemView.findViewById(R.id.frontLayout);
            backLayout = itemView.findViewById(R.id.backLayout);
            frontText = itemView.findViewById(R.id.frontText);
            backText = itemView.findViewById(R.id.backText);
            edit = itemView.findViewById(R.id.edit);
        }
    }
}