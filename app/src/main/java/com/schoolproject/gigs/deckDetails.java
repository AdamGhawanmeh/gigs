package com.schoolproject.gigs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Map;

public class deckDetails extends AppCompatActivity {


    private LinearLayout study;
    private LinearLayout Test;
    private LinearLayout deleteDeck;
    private String Title;
    private RecyclerView recyclerView;
    ArrayList<flashcardWid> flashcardArrayList;
    flashcardAdapter flashcardAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_details);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Title = extras.getString("Title");
            SharedPreferences sharedPreferences = getSharedPreferences("edit",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("title",Title);
            editor.apply();
        }

        recyclerView= findViewById(R.id.flashcards);
        recyclerView.setHasFixedSize(true);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
//        layoutManager.setOrientation();
        recyclerView.setLayoutManager(layoutManager);

        flashcardArrayList = new ArrayList<flashcardWid>();
        flashcardAdapter = new flashcardAdapter(flashcardArrayList);
        recyclerView.setAdapter(flashcardAdapter);
        EventChangeListener();


        study= findViewById(R.id.Study);
        deleteDeck=findViewById(R.id.delete);
        Test = findViewById(R.id.test);
    }
    private void EventChangeListener() {
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
                            if (document.exists()) {
                                Map<String, Object> data = document.getData();
                                for (Map.Entry<String, Object> entry : data.entrySet()) {
                                    String key = entry.getKey();
                                    Map<String, Object> value = (Map<String, Object>) entry.getValue();
                                    String frontText = (String) value.get("frontText");
                                    String backText = (String) value.get("backText");

                                    flashcardWid card = new flashcardWid(frontText, backText);
                                    card.setID(key);
                                    // Add the card to the adapter
                                    flashcardArrayList.add(card);

                                }

                                study.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent1 = new Intent(deckDetails.this, StudyActivity.class);
                                        intent1.putExtra("title",Title);
                                        startActivity(intent1);
                                    }
                                });
                                Test.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent2 = new Intent(deckDetails.this, TestOptions.class);
                                        intent2.putExtra("max question count", data.size());
                                        intent2.putExtra("title", Title);
                                        startActivity(intent2);
                                    }
                                });
                                deleteDeck.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(deckDetails.this, "test", Toast.LENGTH_SHORT).show();
                                        deletePopUpMessage("Are you sure you want to delete this deck\nthis action can not be undone");
                                    }
                                });
                                flashcardAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
    private void deletePopUpMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(true);


        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference myCollectionRef = db.collection("users")
                                .document(user.getUid())
                                .collection("Decks");

// Delete all documents in the collection
                        myCollectionRef.get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                myCollectionRef.document(Title).delete()
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("delete", "Collection deleted successfully");
                                            Intent intent = new Intent(deckDetails.this,homePage.class);
                                            startActivity(intent);
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.w("delete", "Error deleting documents in collection", e);
                                        });
                            } else {
                                Log.w("delete", "Error getting documents from collection", task.getException());
                            }
                        });
                    }
                });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }}