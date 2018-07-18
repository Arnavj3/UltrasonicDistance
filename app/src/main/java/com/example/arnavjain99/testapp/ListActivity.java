package com.example.arnavjain99.testapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;


import android.widget.Toast;
import android.os.Handler;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private RecyclerViewAdapter mAdapter;

    ArrayList<Model> modelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lis);

        findViews();
        setAdapter();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Model model;
                ArrayList<String> distances;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    model = new Model();
                    model.setDate(dataSnapshot1.getKey());
                    distances = new ArrayList<>();
                    for (DataSnapshot valueSnap : dataSnapshot1.getChildren()) {
                        distances.add(String.format("%.2f", valueSnap.child("Distance").getValue()));
                    }

                    model.setDistance(distances);

                    model.setCount(getCount(distances, 4));

                    System.out.println("=======>>>>>>>>" + model.getCount());

                    modelArrayList.add(model);
                }

                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private int getCount(ArrayList<String> arrayList, int minDistance) {
        boolean counterToIncrease = false;
        int counter = 0;

        for(String arrayVal : arrayList) {
            if(Double.parseDouble(arrayVal) > minDistance){
                counterToIncrease = true;
            }

            if(Double.parseDouble(arrayVal) <= minDistance && (counterToIncrease || counter == 0)) {
                counterToIncrease = false;
                counter++;
            }
        }
        return counter;
    }

    private void findViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }


    private void setAdapter() {

        mAdapter = new RecyclerViewAdapter(ListActivity.this, modelArrayList);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setAdapter(mAdapter);


    }
}