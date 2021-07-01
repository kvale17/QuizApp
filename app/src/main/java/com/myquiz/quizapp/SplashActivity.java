package com.myquiz.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private TextView splashTitle;

    public static List<SubjectModel> subList = new ArrayList<>();
    public static int selected_sub_index = 0;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashTitle = findViewById(R.id.splashTitle);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.title_anim);
        splashTitle.setAnimation(anim);

        firestore = FirebaseFirestore.getInstance();

        new Thread(){
            public void run(){

                loadData();
            }
        }.start();


    }

    private void loadData() {
        subList.clear();

        firestore.collection("QUIZ").document("Subjects").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                        long count = (long) documentSnapshot.get("COUNT");

                        for (int i = 1; i <= count; i++) {
                            String subName = documentSnapshot.getString("SUB" + i + "_NAME");
                            String subID = documentSnapshot.getString("SUB" + i + "_ID");
                            subList.add(new SubjectModel(subID,subName));
                        }
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        SplashActivity.this.finish();
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SplashActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}