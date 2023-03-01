package com.schoolproject.gigs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class decks extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private DeckAdapter adapter;
    private List<deck> deckList;
    private ImageView left,right,top,bottom,add;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userID = mAuth.getCurrentUser().getUid();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decks);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        recyclerView = findViewById(R.id.decks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        deckList = new ArrayList<>();
        left=findViewById(R.id.left);
        right=findViewById(R.id.right);
        top=findViewById(R.id.top);
        bottom=findViewById(R.id.bottom);
        left.setOnClickListener(this);
        right.setOnClickListener(this);
        top.setOnClickListener(this);
        bottom.setOnClickListener(this);

        adapter = new DeckAdapter(deckList);
        recyclerView.setAdapter(adapter);

        db.collection("users").document(userID)
                .collection("Decks")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            deck deckName=new deck(documentSnapshot.getId());
                            deckList.add(deckName);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });


        add=findViewById(R.id.add_deck);
        add.setOnClickListener(this);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_deck:
                Intent intent = new Intent(this,addDeck.class);
                startActivity(intent);
            default:
                finish();
                break;
        }
    }
}