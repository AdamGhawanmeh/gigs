package com.schoolproject.gigs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class fragmentWriteAns extends Fragment {
    private TextView Term;
    private String cardID,Title;
    private String rightAns;
    private EditText givenAnswer;
    private boolean wrongAns = false;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private Integer score;


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_write_ans, container, false);
    Term=view.findViewById(R.id.term);
    givenAnswer = view.findViewById(R.id.Answer);
    Intent intent = getActivity().getIntent();
    Bundle extras = intent.getExtras();
    if (extras != null) {
      Title = extras.getString("title");
    }

    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Write Answer", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();

    rightAns = sharedPreferences.getString("right answer",null);
    Term.setText(sharedPreferences.getString("term",null));
    cardID = sharedPreferences.getString("card ID",null);

    view.findViewById(R.id.submit_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String userAnswer = givenAnswer.getText().toString().trim();
        if(userAnswer.equalsIgnoreCase(rightAns)) {
            db.collection("users")
                    .document(user.getUid())
                    .collection("Decks")
                    .document(Title)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                      @Override
                      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                          showPopupMessage("Good Job!", "You have provided the right answer.");
                          DocumentSnapshot document = task.getResult();
                          Map<String, Object> data = document.getData();
                          for (Map.Entry<String, Object> entry : data.entrySet()) {
                            String key = entry.getKey();
                            Map<String, Object> cardData = (Map<String, Object>) entry.getValue();
                            Long study = (Long) cardData.get("study");
                            if (key.equals(cardID) && wrongAns == false) {
                              cardData.put("study", study + 1);
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
                          }
                        } else {
                          Log.d("Firestore", "Error getting document: ", task.getException());
                        }
                      }
                    });
          }
        else {
showPopupMessage("Incorrect Answer","The correct answer is:" + rightAns+
        "\nPlease try again.");
          wrongAns=true;
        }
      }
    });
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
                dialog.cancel();
                getActivity().recreate();
              }
            });

    AlertDialog alert = builder.create();
    alert.show();
  }
}