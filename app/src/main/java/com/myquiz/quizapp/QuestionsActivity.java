package com.myquiz.quizapp;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import static com.myquiz.quizapp.GradeLevelSelectionActivity.gradesIDs;
import static com.myquiz.quizapp.SplashActivity.selected_sub_index;
import static com.myquiz.quizapp.SplashActivity.subList;

public class QuestionsActivity extends AppCompatActivity {

    private TextView question;
    private TextView count;
    private Button Option1;
    private Button Option2;
    private Button Option3;
    private Button Option4;
    private Button btnSubmit;
    private List<Question> questionList;
    private int questionNum;
    private int selectedOption;
    private int score;
    private FirebaseFirestore firestore;
    private int GradeNo;
    private Dialog loadingDialog;

    View buttonClicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        selectedOption = 0;
        question = findViewById(R.id.questionText);
        count = findViewById(R.id.questionNum);
        Option1 = findViewById(R.id.Option1);
        Option2 = findViewById(R.id.Option2);
        Option3 = findViewById(R.id.Option3);
        Option4 = findViewById(R.id.Option4);
        btnSubmit = findViewById(R.id.btnSubmit);

        loadingDialog = new Dialog(QuestionsActivity.this);
        loadingDialog.setContentView(R.layout.loading_bar);
        loadingDialog.setCancelable(false);
        Objects.requireNonNull(loadingDialog.getWindow()).setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        questionList = new ArrayList<>();
        GradeNo = getIntent().getIntExtra("GRADENO",1);
        firestore = FirebaseFirestore.getInstance();

        Option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBtnColors();
                Option1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                selectedOption = 1;
                buttonClicked = v;
            }
        });
        Option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBtnColors();
                Option2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                selectedOption = 2;
                buttonClicked = v;
            }
        });
        Option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBtnColors();
                Option3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                selectedOption = 3;
                buttonClicked = v;
            }
        });
        Option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBtnColors();
                Option4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                selectedOption = 4;
                buttonClicked = v;
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedOption ==0)
                {
                    Toast.makeText(QuestionsActivity.this, "Select an answer", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    v.setClickable(false);
                    Option1.setClickable(false);
                    Option2.setClickable(false);
                    Option3.setClickable(false);
                    Option4.setClickable(false);
                    checkAnswer(selectedOption, buttonClicked);
                }
            }
        });



        getQuestionList();
        score = 0;


    }

    public void getQuestionList()
    {

        questionList.clear();
        loadingDialog.show();

        firestore.collection("QUIZ").document(subList.get(selected_sub_index).getId())
                .collection(gradesIDs.get(GradeNo).getId()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                        for(QueryDocumentSnapshot doc : queryDocumentSnapshots)
                        {
                            docList.put(doc.getId(),doc);
                        }

                        QueryDocumentSnapshot quesListDoc = docList.get("QUESTIONS_LIST");

                        String count = quesListDoc.getString("COUNT");

                        for(int i = 0; i<Integer.valueOf(count);i++)
                        {

                            String questID = quesListDoc.getString("Q" + (i + 1) + "_ID");

                            QueryDocumentSnapshot quesDoc = docList.get(questID);

                            questionList.add(new Question(
                                    quesDoc.getString("QUESTION"),
                                    quesDoc.getString("A"),
                                    quesDoc.getString("B"),
                                    quesDoc.getString("C"),
                                    quesDoc.getString("D"),
                                    Integer.valueOf(quesDoc.getString("ANSWER"))
                            ));

                        }


                        setQuestion();

                        loadingDialog.dismiss();


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuestionsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });


    }


    private void setQuestion()
    {
        question.setText(questionList.get(0).getQuestion());
        Option1.setText(questionList.get(0).getOptionA());
        Option2.setText(questionList.get(0).getOptionB());
        Option3.setText(questionList.get(0).getOptionC());
        Option4.setText(questionList.get(0).getOptionD());

        count.setText(1 + "/" + questionList.size());

        questionNum = 0;
    }



    private void checkAnswer(int selectedOption, View view)
    {

            if (selectedOption == questionList.get(questionNum).getCorrectAns())
            {
                //Right Answer

                score++;
                view.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));


            } else

                {
                //Wrong Answer

                    view.setBackgroundTintList(ColorStateList.valueOf(Color.RED));

                    switch (questionList.get(questionNum).getCorrectAns())
                    {
                        case 1:
                            Option1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                            break;
                        case 2:
                            Option2.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                            break;
                        case 3:
                            Option3.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                            break;
                        case 4:
                            Option4.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                            break;
                        default:
                            break;
                    }
            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    changeQuestion();
                }
            }, 3000);


    }

    @SuppressLint("SetTextI18n")
    private void changeQuestion()
    {

        if(questionNum<(questionList.size()-1))
        {
            //change question

            questionNum++;

            changeAnim(question, 0, 0);
            changeAnim(Option1, 0,1);
            changeAnim(Option2, 0,2);
            changeAnim(Option3, 0,3);
            changeAnim(Option4, 0,4);

            count.setText((questionNum + 1) + "/" + questionList.size());


        }
        else
        {
            //Go to score activity
            Intent intent = new Intent(QuestionsActivity.this, ScoreActivity.class);
            intent.putExtra("SCORE", score + "/" + questionList.size());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        selectedOption = 0;
        btnSubmit.setClickable(true);
        Option1.setClickable(true);
        Option2.setClickable(true);
        Option3.setClickable(true);
        Option4.setClickable(true);


    }

    private void changeAnim(final View view, final int value, final int viewNum)
    {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                       if(value == 0)
                       {
                           switch (viewNum)
                           {
                               case 0:
                                   ((TextView)view).setText((questionList.get(questionNum).getQuestion()));
                                   break;
                               case 1:
                                   ((Button)view).setText(questionList.get(questionNum).getOptionA());
                                   break;
                               case 2:
                                   ((Button)view).setText(questionList.get(questionNum).getOptionB());
                                   break;
                               case 3:
                                   ((Button)view).setText(questionList.get(questionNum).getOptionC());
                                   break;
                               case 4:
                                   ((Button)view).setText(questionList.get(questionNum).getOptionD());
                                   break;
                               default:
                                   break;
                           }

                           if(viewNum != 0)
                           {
                               view.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                           }

                           changeAnim(view,1,viewNum);
                       }

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    private void resetBtnColors()
    {
        Option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        Option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        Option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        Option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
    }

}