package com.schoolproject.gigs;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class searchFragment extends Fragment {
  private SearchView mSearchBox;
  private FirebaseFirestore mFirestore;
  private CollectionReference mPublicDecksRef;
  private RecyclerView recyclerView;
  private ArrayList <deck> deckLayoutArrayList;
  private ArrayList <String> deckFullIDArrayList = new ArrayList<>();
  private deckLayoutAdapter deckLayoutAdapter;
  private GestureDetector mGestureDetector;


  public searchFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view  = inflater.inflate(R.layout.fragment_search, container, false);

    recyclerView = view.findViewById(R.id.search_results);
    recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    deckLayoutArrayList = new ArrayList<>();
    deckLayoutAdapter = new deckLayoutAdapter(this.getActivity(), deckLayoutArrayList);
    recyclerView.setAdapter(deckLayoutAdapter);

    // Initialize Firestore
    mFirestore = FirebaseFirestore.getInstance();
    mPublicDecksRef = mFirestore.collection("public decks");


    // Set up the SearchView
    mSearchBox = view.findViewById(R.id.search_box);
    mSearchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        searchPublicDecks(query);
        return true;
      }
      @Override
      public boolean onQueryTextChange(String newText) {
        return false;
      }
    });

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
          String title= deckFullIDArrayList.get(position);
          Intent intent = new Intent(getActivity(),DeckDetailsPublic.class);
          intent.putExtra("Title",title);
          startActivity(intent);
          return true;
        }
        return false;
      }
    });
    return view;
  }

  private void searchPublicDecks(String searchTerm) {
    mPublicDecksRef.whereGreaterThanOrEqualTo(FieldPath.documentId(), searchTerm)
            .whereLessThan(FieldPath.documentId(), searchTerm + "z")
            .get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
              @Override
              public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                deckLayoutArrayList.clear();
                deckFullIDArrayList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                  deckFullIDArrayList.add(documentSnapshot.getId());
                  String modifiedName = removeAfterAtSymbol(documentSnapshot.getId());
                  deck deckName=new deck(modifiedName);
                  deckLayoutArrayList.add(deckName);
                }
                deckLayoutAdapter.notifyDataSetChanged();

              }
            });
  }
  public static String removeAfterAtSymbol(String inputString) {
    int atIndex = inputString.indexOf('@');
    if (atIndex != -1) {
      return inputString.substring(0, atIndex);
    } else {
      return inputString;
    }
  }
}