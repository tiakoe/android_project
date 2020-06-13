package com.a.navigation_module.ui.my;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.a.navigation_module.R;

public  class MyFragment extends Fragment {
    private MyViewModel myViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
          myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);

        View root = inflater.inflate(R.layout.fragment_my, container, false);

        final TextView textView = root.findViewById(R.id.text_my);

        myViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
}
