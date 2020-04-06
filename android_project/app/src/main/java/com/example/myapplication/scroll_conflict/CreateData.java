package com.example.myapplication.scroll_conflict;


import androidx.fragment.app.Fragment;

import com.example.myapplication.scroll_conflict.my_fragments.AFragment;
import com.example.myapplication.scroll_conflict.my_fragments.BFragment;
import com.example.myapplication.scroll_conflict.my_fragments.CFragment;
import com.example.myapplication.scroll_conflict.my_fragments.DFragment;
import com.example.myapplication.scroll_conflict.my_fragments.EFragment;
import com.example.myapplication.scroll_conflict.my_fragments.FFragment;

import java.util.ArrayList;
import java.util.List;

public class CreateData {

    public static List<String> getStringList01() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add("item" + i);
        }
        return list;
    }

    public static List<String> getStringList02() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add("item " + i);
        }
        return list;
    }

    public static List<Fragment> getFragmentList1() {
        List<Fragment> list = new ArrayList<Fragment>();
        list.add(new AFragment());
        list.add(new BFragment());
        return list;
    }

    public static List<Fragment> getFragmentList2() {
        List<Fragment> list = new ArrayList<Fragment>();
        list.add(new CFragment());
        list.add(new DFragment());
        return list;
    }

    public static List<Fragment> getFragmentList4() {
        List<Fragment> list = new ArrayList<Fragment>();
        list.add(new EFragment());
        list.add(new FFragment());
        return list;
    }


}
