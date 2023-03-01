package com.schoolproject.gigs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

public class StudiedAllFragment extends Fragment implements View.OnClickListener {

  private Button again,finish;

  private FirebaseFirestore db = FirebaseFirestore.getInstance();
  private FirebaseAuth mAuth = FirebaseAuth.getInstance();
  private FirebaseUser user = mAuth.getCurrentUser();

  private String Title;
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_studied_all, container, false);
    Intent intent = getActivity().getIntent();
    Bundle extras = intent.getExtras();
    if (extras != null) {
      Title = extras.getString("title");
    }    again= view.findViewById(R.id.again);
    finish = view.findViewById(R.id.Finish);
    again.setOnClickListener(this);
    finish.setOnClickListener(this);
    return view;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case (R.id.again):
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
                        cardData.put("study",0);
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
                      getActivity().recreate();
                      } else {
                      Log.d("Firestore", "Error getting document: ", task.getException());
                    }
                }});
        break;
      case (R.id.Finish):
        getActivity().onBackPressed();
    }
  }
}