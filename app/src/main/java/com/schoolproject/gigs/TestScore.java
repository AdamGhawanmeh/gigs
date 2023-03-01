package com.schoolproject.gigs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TestScore extends AppCompatActivity {
  private TextView MSG;
  private String Score,Num;
  private Button finishBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test_score);
    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    Score = extras.getString("score");
    Num = extras.getString("number of questions");
    MSG = findViewById(R.id.scoreMSG);
    finishBtn = findViewById(R.id.FinishBtn);
    MSG.setText("You have scored "+Score+" / "+Num+"!");
    finishBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent1 = new Intent(TestScore.this,homePage.class);
        startActivity(intent1);
      }
    });

  }
}