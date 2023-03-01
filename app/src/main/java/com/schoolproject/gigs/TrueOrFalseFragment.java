package com.schoolproject.gigs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Random;

public class TrueOrFalseFragment extends Fragment implements View.OnClickListener {

  private TextView term,definition;
  private Button True,False;
  private flashcard card;
  private boolean answer = false;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_true_or_false, container, false);

    // Get the question and the score from the bundle
    card = getArguments().getParcelable("flashcard");
    String randomAnswer = getArguments().getString("random answer");

    term = view.findViewById(R.id.term);
    definition = view.findViewById(R.id.definition);
    True = view.findViewById(R.id.True);
    False = view.findViewById(R.id.False);

    term.setText(card.getFrontText());

    switch (new Random().nextInt(2)){
      case 0:
        definition.setText(card.getBackText());
        break;
      case 1:
        definition.setText(randomAnswer);
        break;
    }

    // Set click listeners for the true and false buttons
    True.setOnClickListener(this);
    False.setOnClickListener(this);

    return view;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.True:
        if (definition.getText().equals(card.getBackText())){
        answer = true;
          showPopupMessage("Good job!");
        }
        else {
          answer=false;
          showPopupMessage("Incorrect! The correct answer is: " + card.getBackText());
        }
        break;
      case R.id.False:
        if (!definition.getText().equals(card.getBackText())){
        answer = true;
          showPopupMessage("Good job!");}
        else {
          answer=false;
          showPopupMessage("Incorrect! The correct answer is: " + card.getBackText());
        }
        break;
    }
  }
  private void showPopupMessage(String message) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    builder.setMessage(message);
    builder.setCancelable(false);

    builder.setPositiveButton(
            "OK",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                notifyAns();
              }
            });

    AlertDialog alert = builder.create();
    alert.show();
  }

  private void notifyAns() {
    // Notify the Test activity of the user's answer
    ((Test) getActivity()).onAnswer(answer);
  }
}



//public class TrueOrFalseFragment extends Fragment implements View.OnClickListener {
//
//
//  @Override
//  public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                           Bundle savedInstanceState) {
//    // Inflate the layout for this fragment
//    View view = inflater.inflate(R.layout.fragment_true_or_false, container, false);
//    term = view.findViewById(R.id.term);
//    definition = view.findViewById(R.id.definition);
//    True = view.findViewById(R.id.True);
//    False = view.findViewById(R.id.False);
//
//    SharedPreferences prefToF = getActivity().getSharedPreferences("Write Answer", Context.MODE_PRIVATE);
//    SharedPreferences.Editor editorToF = prefToF.edit();
//    if(prefToF.getString("scoreStr",null)!=null){
//      score = Integer.parseInt(prefToF.getString("scoreStr",null));}
//    term.setText(prefToF.getString("term",null));
//    definition.setText(prefToF.getString("random answer",null));
//    rightDefinition = prefToF.getString("definition",null);
//
//    True.setOnClickListener(this);
//    False.setOnClickListener(this);
//
//    SharedPreferences sharedPrefnum= getActivity().getSharedPreferences("numStudied", Context.MODE_PRIVATE);
//    SharedPreferences.Editor editorNum = sharedPrefnum.edit();
//    editorNum.putInt("answered",sharedPrefnum.getInt("answered",0)+1);
//    editorNum.apply();
//    return view;
//  }
//
//  @Override
//  public void onClick(View v) {
//    SharedPreferences prefToF = getActivity().getSharedPreferences("Write Answer", Context.MODE_PRIVATE);
//    SharedPreferences.Editor editorToF = prefToF.edit();
//    if(v.getId()==R.id.True&&rightDefinition.equals(definition.getText())){
//      editorToF.putInt("score",score+1);
//      getActivity().recreate();
//    }
//    else if (v.getId()==R.id.False&&!(rightDefinition.equals(definition.getText()))){
//      editorToF.putInt("score",score+1);
//      getActivity().recreate();
//    }
//    else {
//      getActivity().recreate();
//    }
//    editorToF.apply();
//  }
//}