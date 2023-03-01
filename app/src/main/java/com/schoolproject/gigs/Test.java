package com.schoolproject.gigs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Test extends AppCompatActivity {

  private List<flashcard> flashcardArrayList;
  private int currentQuestionIndex = 0;
  private int score;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test);

    // Get the flashcard arraylist and the initial score from the intent
    Parcelable[] parcelables = getIntent().getParcelableArrayExtra("flashcards");
    flashcard[] flashcardArray = new flashcard[parcelables.length];

    for (int i = 0; i < parcelables.length; i++) {
      flashcardArray[i] = (flashcard) parcelables[i];
    }
      flashcardArrayList = Arrays.asList(flashcardArray);
    score = getIntent().getIntExtra("score", 0);

    // Display the first question fragment
    getRandomQuestionFragment();
  }

//  private void displayQuestionFragment() {
    // Get a random question fragment
//    Fragment questionFragment = getRandomQuestionFragment();
//    // Pass the current flashcard and the score to the fragment
//    Bundle bundle = new Bundle();
//
//    bundle.putParcelable("flashcard", flashcardArrayList.get(currentQuestionIndex));
//    bundle.putInt("score", score);
//    questionFragment.setArguments(bundle);
//
//    Toast.makeText(this, "fjerio", Toast.LENGTH_SHORT).show();
    // Replace the current fragment with the new question fragment
//    getSupportFragmentManager().beginTransaction()
//            .replace(R.id.container, questionFragment)
//            .commit();
//  }

  private void getRandomQuestionFragment() {
    int random = new Random().nextInt(3);
    switch (random) {
      case 0:
        Bundle bundleWrite = new Bundle();

        bundleWrite.putString("term",flashcardArrayList.get(currentQuestionIndex).getFrontText());
        bundleWrite.putString("right answer", flashcardArrayList.get(currentQuestionIndex).getBackText());
        Fragment WriteAnswer= new fragmentWriteAnsTest();
        WriteAnswer.setArguments(bundleWrite);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,WriteAnswer)
                .commit();
        break;
      case 1:
        // Pass the current flashcard and the score to the fragment
        Bundle bundle4 = new Bundle();
        ArrayList<String> randomAnswers = new ArrayList<>();
          while (randomAnswers.size() <3) {
            String randomAnswer = flashcardArrayList.get(new Random().nextInt(flashcardArrayList.size() - 1) + 1).getBackText().toString();
            if (!randomAnswers.contains(randomAnswer)&&!randomAnswer.equals(flashcardArrayList.get(currentQuestionIndex).getBackText())) {
              randomAnswers.add(randomAnswer);
            }
          }
          randomAnswers.add(flashcardArrayList.get(currentQuestionIndex).getBackText());
          Collections.shuffle(randomAnswers);
          for (int j = 0; j < 4; j++) {
            bundle4.putString("answer" + (j + 1), randomAnswers.get(j));
          }

        bundle4.putParcelable("flashcard", flashcardArrayList.get(currentQuestionIndex));
//        String randomAnswer = flashcardArrayList.get(new Random().nextInt(flashcardArrayList.size())).getBackText();
        bundle4.putStringArrayList("random answers", randomAnswers);
        Fragment SelectFourTest = new SelectFourTest();
        SelectFourTest.setArguments(bundle4);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, SelectFourTest)
                .commit();
        break;
      case 2:
        // Pass the current flashcard and the score to the fragment
        Bundle bundle = new Bundle();

        bundle.putParcelable("flashcard", flashcardArrayList.get(currentQuestionIndex));
        String randomAnswer = flashcardArrayList.get(new Random().nextInt(flashcardArrayList.size())).getBackText();
        Toast.makeText(this, randomAnswer, Toast.LENGTH_SHORT).show();
        bundle.putString("random answer", randomAnswer);
        Fragment TrueOrFalse = new TrueOrFalseFragment();
        TrueOrFalse.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, TrueOrFalse)
                .commit();
        break;
    }
  }

  // This method is called from the question fragments when the user answers the question
  public void onAnswer(boolean correct) {
    if (correct) {
      score++;
    }
    currentQuestionIndex++;
    if (currentQuestionIndex < flashcardArrayList.size()) {
      // Display the next question fragment
      getRandomQuestionFragment();
    } else {
      Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
      // All questions have been answered, display the score activity
      Intent intent = new Intent(this, TestScore.class);
      intent.putExtra("score", score+"");
      intent.putExtra("number of questions",flashcardArrayList.size()+"");
      startActivity(intent);
      finish();
    }
  }
}