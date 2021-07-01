package com.myquiz.quizapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import static com.myquiz.quizapp.GradeLevelSelectionActivity.gradesIDs;

public class GradeAdapter extends BaseAdapter {

    private int numOfGrades;

    public GradeAdapter(int numOfGrades)
    {
        this.numOfGrades = numOfGrades;
    }

    @Override
    public int getCount()
    {
        return numOfGrades;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View view;

        if(convertView == null)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grade_selection_layout,parent,false);
        }
        else
        {
          view = convertView;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), QuestionsActivity.class);
                intent.putExtra("GRADENO",position );
                parent.getContext().startActivity(intent);
            }
        });

        ((TextView) view.findViewById(R.id.gradeName)).setText(gradesIDs.get(position).getName());

        return view;
    }
}
