package com.example.myapplication.scroll_conflict.my_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.scroll_conflict.CreateData;

public class EFragment extends Fragment {

    public EFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.example_fragment_listview_01, container, false);
        ListView listView = view.findViewById(R.id.id_fragment_listView001);
        listView.setAdapter(new ArrayAdapter<>(getActivity(),
                R.layout.example_scroll_conflict_item, R.id.id_text_view_001,
                CreateData.getStringList01()));
        return view;
    }
}
