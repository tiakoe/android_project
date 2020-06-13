package com.a.navigation_module.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.a.navigation_module.R;
import com.a.navigation_module.ui.common.Item;
import com.a.navigation_module.ui.common.SharedViewModel;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Item item;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView textView = root.findViewById(R.id.text_home);

        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

//        Fragment之间进行通信；对Dash中的Fragment监听
        SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        model.getSelected().observe(getViewLifecycleOwner(),item -> {
            final TextView show = root.findViewById(R.id.show_home);
            show.setText(item.title);
        });

        return root;
    }
}
