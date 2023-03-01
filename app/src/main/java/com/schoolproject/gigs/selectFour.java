package com.schoolproject.gigs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.ResourceBundle;

public class selectFour extends Fragment {
  private LinearLayout ans1,ans2,ans3,ans4;
  private TextView term,ans1txt,ans2txt,ans3txt,ans4txt;
  private String rightAns, cardID, Title;

  private FirebaseFirestore db = FirebaseFirestore.getInstance();
  private FirebaseAuth mAuth = FirebaseAuth.getInstance();
  private FirebaseUser user = mAuth.getCurrentUser();

  private Integer score=null;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_select_four, container, false);

    Intent intent = getActivity().getIntent();
    Bundle extras = intent.getExtras();
    if (extras != null) {
      Title = extras.getString("title");
    }

    term = view.findViewById(R.id.term);
    ans1=view.findViewById(R.id.choice1Card);
    ans2=view.findViewById(R.id.choice2Card);
    ans3=view.findViewById(R.id.choice3Card);
    ans4=view.findViewById(R.id.choice4Card);
    ans1txt=view.findViewById(R.id.choice1);
    ans2txt=view.findViewById(R.id.choice2);
    ans3txt=view.findViewById(R.id.choice3);
    ans4txt=view.findViewById(R.id.choice4);

    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("selectFour", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    term.setText(sharedPreferences.getString("term",null));
    rightAns = sharedPreferences.getString("right answer", null);
    cardID = sharedPreferences.getString("cardID", null);

    String answer1 = sharedPreferences.getString("answer1", null);
    String answer2 = sharedPreferences.getString("answer2", null);
    String answer3 = sharedPreferences.getString("answer3", null);
    String answer4 = sharedPreferences.getString("answer4",null);

    rightAns = sharedPreferences.getString("right answer", null);
    ans1txt.setText(answer1);
    ans2txt.setText(answer2);
    ans3txt.setText(answer3);
    ans4txt.setText(answer4);


    ans1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        handleAnswer(ans1txt.getText().toString(), ans1);
      }
    });

    ans2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        handleAnswer(ans2txt.getText().toString(), ans2);
      }
    });

    ans3.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        handleAnswer(ans3txt.getText().toString(), ans3);
      }
    });

    ans4.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        handleAnswer(ans4txt.getText().toString(), ans4);
      }
    });
    return view;
  }

  private void handleAnswer(String answer, LinearLayout cardView) {
    if (answer.equals(rightAns)) {
      cardView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
      db.collection("users")
              .document(user.getUid())
              .collection("Decks")
              .document(Title)
              .get()
              .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                  if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Map<String, Object> data = document.getData();
                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                      String key = entry.getKey();
                      Map<String, Object> cardData = (Map<String, Object>) entry.getValue();
                      cardData.put("study",1);
                      if (key.equals(cardID)) {
                        db.collection("users")
                                .document(user.getUid())
                                .collection("Decks")
                                .document(Title)
                                .update(key, cardData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                  @Override
                                  public void onSuccess(Void aVoid) {
                                    Log.d("Firestore", "Card study updated successfully!");
                                  }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                  @Override
                                  public void onFailure(@NonNull Exception e) {
                                    Log.w("Firestore", "Error updating card study", e);
                                  }
                                });
                      }
                    }showPopupMessage("Good job!");
                  } else {
                    Log.d("Firestore", "Error getting document: ", task.getException());
                  }
                }
              });

    }

    else {
      cardView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));
      showPopupMessage("Incorrect! The correct answer is: " + rightAns);

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
                refreshPage();
              }
            });
    AlertDialog alert = builder.create();
    alert.show();
  }

  private void refreshPage() {
      getActivity().recreate();
    }}