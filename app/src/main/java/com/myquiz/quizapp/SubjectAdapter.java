package com.myquiz.quizapp;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Random;

public class SubjectAdapter extends BaseAdapter {

    private List<SubjectModel> subList;
    private StorageReference mStorageRef;

    public SubjectAdapter(List<SubjectModel> subList) {
        this.subList = subList;

    }

    @Override
    public int getCount() {
        return subList.size();
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

        mStorageRef = FirebaseStorage.getInstance().getReference();
        final View view;

        if(convertView == null)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_item_layout,parent, false);
        }
        else
        {
            view = convertView;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SplashActivity.selected_sub_index = position;
                Intent intent = new Intent(parent.getContext(),GradeLevelSelectionActivity.class);
                parent.getContext().startActivity(intent);

            }
        });

        ((TextView) view.findViewById(R.id.subName)).setText(subList.get(position).getName());
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(255),rnd.nextInt(255),rnd.nextInt(255));
        view.setBackgroundColor(color);

        mStorageRef.child("IMAGES").child(subList.get(position).getName() + ".png")
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        GlideApp.with(parent.getContext()).load(uri).into((ImageView)view.findViewById(R.id.SubjectIcon));

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(parent.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        return view;
    }
}
