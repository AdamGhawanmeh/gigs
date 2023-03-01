package com.schoolproject.gigs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class fragmentWriteAnsTest extends Fragment {
  private TextView Term;
  private String rightAns;
  private EditText givenAnswer;
  private boolean Answer = false;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_write_ans, container, false);
    Term=view.findViewById(R.id.term);
    givenAnswer = view.findViewById(R.id.Answer);
    Intent intent = getActivity().getIntent();
    Bundle extras = intent.getExtras();
    rightAns = getArguments().getString("right answer",null);
    Term.setText(getArguments().getString("term",null));

    view.findViewById(R.id.submit_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String userAnswer = givenAnswer.getText().toString().trim();
        if(userAnswer.equalsIgnoreCase(rightAns)) {
          Answer=true;
            showPopupMessage("Good Job!", "You have provided the right answer.");
          }
        else {
          Answer=false;
          showPopupMessage("Incorrect Answer","The correct answer was:" + rightAns);
        }}});
    return view;
  }

  private void showPopupMessage(String title, String message) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    builder.setTitle(title);
    builder.setMessage(message);
    builder.setCancelable(false);

    builder.setPositiveButton(
            "OK",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                notifyAns();
                dialog.cancel();
              }
            });

    AlertDialog alert = builder.create();
    alert.show();
  }

  private void notifyAns() {
    // Notify the Test activity of the user's answer
    ((Test) getActivity()).onAnswer(Answer);
  }
}