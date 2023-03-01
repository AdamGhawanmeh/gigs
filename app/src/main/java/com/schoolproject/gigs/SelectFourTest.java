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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.ResourceBundle;

public class SelectFourTest extends Fragment {
    private LinearLayout ans1,ans2,ans3,ans4;
    private TextView term,ans1txt,ans2txt,ans3txt,ans4txt;
    private String rightAns;
    private boolean answerT = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_four_test, container, false);

        term = view.findViewById(R.id.term);
        ans1=view.findViewById(R.id.choice1Card);
        ans2=view.findViewById(R.id.choice2Card);
        ans3=view.findViewById(R.id.choice3Card);
        ans4=view.findViewById(R.id.choice4Card);
        ans1txt=view.findViewById(R.id.choice1);
        ans2txt=view.findViewById(R.id.choice2);
        ans3txt=view.findViewById(R.id.choice3);
        ans4txt=view.findViewById(R.id.choice4);

        ArrayList<String>answers =getArguments().getStringArrayList("random answers");

        String answer1 =answers.get(0);
        String answer2 = answers.get(1);
        String answer3 = answers.get(2);
        String answer4 = answers.get(3);
        flashcard card = getArguments().getParcelable("flashcard");

        rightAns = card.getBackText();
        ans1txt.setText(answer1);
        ans2txt.setText(answer2);
        ans3txt.setText(answer3);
        ans4txt.setText(answer4);
        term.setText(card.getFrontText());



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

        SharedPreferences sharedPrefnum= getActivity().getSharedPreferences("numStudied", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorNum = sharedPrefnum.edit();
        editorNum.putInt("answered",sharedPrefnum.getInt("answered",0)+1);

        return view;
    }

    private void handleAnswer(String answer, LinearLayout cardView) {
        if (answer.equals(rightAns)) {
            cardView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
            answerT = true;
            showPopupMessage("Good job!");
        }

//    else if(answer.equals(rightAns)&&score!=null){
//      SharedPreferences sharedPreferences = getActivity().getSharedPreferences("selectFour", Context.MODE_PRIVATE);
//      SharedPreferences.Editor editor = sharedPreferences.edit();
//      showPopupMessage("Good job!");
//      editor.putInt("score",score+1);
//      getActivity().recreate();
//    }
        else {
            cardView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));
            answerT=false;
            showPopupMessage("Incorrect! The correct answer is: " + rightAns);

//      if (score!=null){
//        getActivity().recreate();
//      }
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
        ((Test) getActivity()).onAnswer(answerT);
    }
}