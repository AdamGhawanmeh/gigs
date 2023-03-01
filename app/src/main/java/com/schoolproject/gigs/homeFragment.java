package com.schoolproject.gigs;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.schoolproject.gigs.R.id;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class homeFragment extends Fragment {



private TextView name;
private ImageView pfp;
private FirebaseAuth mAuth = FirebaseAuth.getInstance();
private FirebaseUser user = mAuth.getCurrentUser();
private RecyclerView recyclerView;
private ArrayList <deck> deckLayoutArrayList;
private deckLayoutAdapter deckLayoutAdapter;
private FirebaseFirestore db = FirebaseFirestore.getInstance();
private GestureDetector mGestureDetector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getContext().getTheme().applyStyle(R.style.Theme_Gigs, true);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        name = (TextView) view.findViewById(R.id.name);
        pfp=(ImageView) view.findViewById(id.pfp);
        // in below two lines we are setting layoutmanager and adapter to our recycler view. 

        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            StorageReference reference = FirebaseStorage.getInstance().getReference().child("pfp/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpeg");
            String ref = "https://firebasestorage.googleapis.com/v0/b/gigs-1663613716509.appspot.com/o/pfp%2F"+FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpeg"+"?alt=media&token=4a057c78-7c73-45ed-bd6b-0cbaea35def6";
            name.setText("Hi " + user.getDisplayName().toString());
            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/gigs-1663613716509.appspot.com/o/pfp%2F"+FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpeg"+"?alt=media&token=4a057c78-7c73-45ed-bd6b-0cbaea35def6").into(pfp);
        }

        recyclerView= view.findViewById(id.MyDecks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        deckLayoutArrayList = new ArrayList<>();
        deckLayoutAdapter = new deckLayoutAdapter(this.getActivity(), deckLayoutArrayList);
        recyclerView.setAdapter(deckLayoutAdapter);

        mGestureDetector = new GestureDetector(this.getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
                recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
                    @Override
                    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                        View child = rv.findChildViewUnder(e.getX(), e.getY());
                        if (child != null && mGestureDetector.onTouchEvent(e)) {
                            int position = rv.getChildAdapterPosition(child);
                            String title= deckLayoutAdapter.deckName(position);
                            Intent intent = new Intent(getActivity(),deckDetails.class);
                            intent.putExtra("Title",title);
                            startActivity(intent);
                            return true;
                        }
                        return false;
                    }
                });
        EventChangeListener();
        return view;
    }


    private void EventChangeListener() {
        db.collection("users").document(user.getUid())
                .collection("Decks")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            deck deckName=new deck(documentSnapshot.getId());
                            deckLayoutArrayList.add(deckName);
                        }
                        deckLayoutAdapter.notifyDataSetChanged();
                    }
                });


    }
}