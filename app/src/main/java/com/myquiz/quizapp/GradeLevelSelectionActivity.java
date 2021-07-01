package com.myquiz.quizapp;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static com.myquiz.quizapp.SplashActivity.selected_sub_index;
import static com.myquiz.quizapp.SplashActivity.subList;

public class GradeLevelSelectionActivity extends AppCompatActivity {

    private GridView grade_grid;
    private FirebaseFirestore firestore;
    private Dialog loadingDialog;

    public static List<SetModel> gradesIDs = new ArrayList<SetModel>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_level_selection);

        Toolbar toolbar = findViewById(R.id.grade_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(subList.get(selected_sub_index).getName());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        grade_grid = findViewById(R.id.grade_grid_view);

        loadingDialog = new Dialog(GradeLevelSelectionActivity.this);
        loadingDialog.setContentView(R.layout.loading_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        firestore = FirebaseFirestore.getInstance();

        loadSets();


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            GradeLevelSelectionActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadSets() {
        gradesIDs.clear();

        loadingDialog.show();

        firestore.collection("QUIZ").document(subList.get(selected_sub_index).getId())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                long noOfGrades = (long) documentSnapshot.get("GRADES");


                if (noOfGrades != 0) {
                    for (int i = 1; i <= noOfGrades; i++) {
                        String gradeName = documentSnapshot.getString("GRADE" + i + "_NAME");
                        gradesIDs.add(new SetModel(documentSnapshot.getString("GRADE" + i + "_ID"), gradeName));
                    }
                }

                GradeAdapter adapter = new GradeAdapter(gradesIDs.size());
                grade_grid.setAdapter(adapter);

                loadingDialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(GradeLevelSelectionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }
}