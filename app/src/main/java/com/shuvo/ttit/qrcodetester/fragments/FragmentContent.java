package com.shuvo.ttit.qrcodetester.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shuvo.ttit.qrcodetester.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentContent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentContent extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String KEY_TITLE = "Content";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentContent() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentContent newInstance(String param1) {
        FragmentContent fragment = new FragmentContent();
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = getArguments().getString(KEY_TITLE);
        ((TextView) view.findViewById(R.id.title)).setText(title);
    }
}