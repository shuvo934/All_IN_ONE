package com.shuvo.ttit.qrcodetester;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class SpanRecycle extends AppCompatActivity {

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_span_recycle);

        recyclerView = findViewById(R.id.span_test_view);

        ArrayList<String> arrayList = new ArrayList<>();

        for(int i = 0 ; i < 13; i++) {

            arrayList.add("Food Item: "+ i);

        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
//                System.out.println("Result1: "+(position+1) % 5);
//                if ((position+1) % 5 * 2 == 0) {
//                    System.out.println("2 Ken Pai position:: "+(position));
//                    System.out.println("2 Ken Pai:: "+(position+1) % 5 * 2);
//                    return 2;
//                }
//                else {
//                    System.out.println("1 Ken Pai position:: "+(position));
//                    System.out.println("1 Ken Pai:: "+(position+1) % 5 * 2);
//                    return 1;
//                }

                if (arrayList.size() % 2 != 0) {
                    if (position == arrayList.size() -1) {
                        return 2;
                    }
                    else {
                        return 1;
                    }
                } else  {
                    return 1;
                }
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new SpanViewAdapter(arrayList));

    }
}