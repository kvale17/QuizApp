package com.myquiz.quizapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.myquiz.quizapp.SplashActivity.subList;

public class SubjectSelectionActivity extends AppCompatActivity {

    private GridView subGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_selection);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Subjects");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        subGrid = findViewById(R.id.subGridView);

        SubjectAdapter adapter = new SubjectAdapter(subList);
        subGrid.setAdapter(adapter);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            SubjectSelectionActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}