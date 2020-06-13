package com.a.fun_module.recycle_page;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.a.fun_module.R;

public class RefreshListPage extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refresh_list_activity);

        //initData();

    }
    //
    //    private void initData() {
    //        List<String> date=new ArrayList<>();
    //        for (int i = 0; i < 50; i++) {
    //            date.add("我是数据"+i);
    //        }
    //        recyclerView.setAdapter(new XAdapter<String>(this,date) {
    //            @Override
    //            public BaseHolder<String> initHolder(ViewGroup parent, int viewType) {
    //                return new BaseHolder<String>(context,parent,R.layout.item_single_text){
    //                    @Override
    //                    public void initView(View itemView, int position, String data) {
    //                        super.initView(itemView, position, data);
    //                        TextView tv = itemView.findViewById(R.id.tv_text);
    //                        tv.setText(data);
    //
    //                    }
    //                };
    //            }
    //        });
    //        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
    //    }

}
