package com.myquiz.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ScoreActivity extends AppCompatActivity {

    private TextView score;
    private Button btnEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        score = findViewById(R.id.score);
        btnEnd = findViewById(R.id.btnEnd);

        String score_str = getIntent().getStringExtra("SCORE");
        score.setText(score_str);

        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
                 ScoreActivity.this.startActivity(intent);
                 ScoreActivity.this.finish();
            }
        });

    }
}