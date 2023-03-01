package com.schoolproject.gigs;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class stackAdapter extends BaseAdapter {

  // on below line we have created variables
  // for our array list and context.
  private ArrayList<flashcardWid> flashcardArrayList;
  private Context context;

  // on below line we have created constructor for our variables.
  public stackAdapter(ArrayList<flashcardWid> flashcardArrayList, Context context) {
    this.flashcardArrayList = flashcardArrayList;
    this.context = context;
  }

  @Override
  public int getCount() {
    // in get count method we are returning the size of our array list.
    return flashcardArrayList.size();
  }

  @Override
  public Object getItem(int position) {
    // in get item method we are returning the item from our array list.
    return flashcardArrayList.get(position);
  }

  @Override
  public long getItemId(int position) {
    // in get item id we are returning the position.
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // in get view method we are inflating our layout on below line.
    View v = convertView;
    if (v == null) {
      // on below line we are inflating our layout.
      v = LayoutInflater.from(parent.getContext()).inflate(R.layout.flashcard, parent, false);
    }
    // on below line we are initializing our variables and setting data to our variables.
    ((TextView) v.findViewById(R.id.frontText)).setText(flashcardArrayList.get(position).getFrontText());
    ((TextView) v.findViewById(R.id.backText)).setText(flashcardArrayList.get(position).getBackText());

    v.findViewById(R.id.cardView).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        flipCard(position,v.findViewById(R.id.backLayout),v.findViewById(R.id.frontLayout),v.findViewById(R.id.cardView),v.findViewById(R.id.backText),v.findViewById(R.id.frontText));
      }
    });
    return v;
  }

  private void flipCard(int position, RelativeLayout backLayout, RelativeLayout frontLayout, CardView cardView, TextView backText, TextView frontText) {
    if (flashcardArrayList.get(position).isShowingBack()) {
      // Flip to show the front of the card
      ObjectAnimator flip = ObjectAnimator.ofFloat(cardView,"rotationX",180f, 0f);
      ObjectAnimator textFlipOut = ObjectAnimator.ofFloat(backText, "rotationX", -180f, 0f);
      ObjectAnimator textFlipIn = ObjectAnimator.ofFloat(frontText, "rotationX", 180f, 0f);
      AnimatorSet animatorSet = new AnimatorSet();
      animatorSet.playTogether(flip, textFlipOut, textFlipIn);
      animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
      animatorSet.setDuration(600);
      animatorSet.start();
      flashcardArrayList.get(position).setShowingBack(false);
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
      flashcardArrayList.get(position).setShowingBack(true);
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          backLayout.setVisibility(View.VISIBLE);
          frontLayout.setVisibility(View.GONE);
        }
      }, 300);
    }
  }

}