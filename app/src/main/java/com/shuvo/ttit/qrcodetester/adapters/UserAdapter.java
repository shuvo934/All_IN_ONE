package com.shuvo.ttit.qrcodetester.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.ttit.qrcodetester.NavigationUserList;
import com.shuvo.ttit.qrcodetester.R;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    private final ArrayList<NavigationUserList> navigationUserLists;
    private final Context context;
    private final ClickedItem myClickedItem;

    public UserAdapter(ArrayList<NavigationUserList> navigationUserLists, Context context, ClickedItem myClickedItem) {
        this.navigationUserLists = navigationUserLists;
        this.context = context;
        this.myClickedItem = myClickedItem;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item_layout,parent,false);
        return new UserHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {

        NavigationUserList navigationUserList = navigationUserLists.get(position);

        holder.voucherName.setText(navigationUserList.getName());

    }

    @Override
    public int getItemCount() {
        return navigationUserLists.size();
    }

    public static class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView voucherName;
        public RecyclerView detailsView;
        ClickedItem mClickedItem;

        public UserHolder(@NonNull View itemView,ClickedItem ci) {
            super(itemView);

            voucherName = itemView.findViewById(R.id.user_list_item);

            this.mClickedItem = ci;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
        }
    }

    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }
}
