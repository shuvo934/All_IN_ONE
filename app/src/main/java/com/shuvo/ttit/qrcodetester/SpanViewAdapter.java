package com.shuvo.ttit.qrcodetester;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SpanViewAdapter extends RecyclerView.Adapter<SpanViewAdapter.SVHolder> {

    private ArrayList<String> arrayList;

    public SpanViewAdapter(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public SVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == 2) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_2,parent,false);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_1,parent,false);
        }
        return new SVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SVHolder holder, int position) {

        holder.textView.setText(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {

//        if ((position+1) % 5 * 2 == 0) {
//            return 2;
//        }
//        else {
//            return 1;
//        }

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

    public class SVHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public SVHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.view_text);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println(arrayList.get(getAdapterPosition()));
                }
            });
        }
    }
}
