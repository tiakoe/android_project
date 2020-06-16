package com.a.fun_module.customview.view.plan;

import android.os.Bundle;

import com.a.fun_module.R;

import java.util.ArrayList;
import java.util.List;


public class PlanViewActivity extends BaseActivity {

    private static final String TAG = "PlanViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_view);
        PlanView planView = findViewById(R.id.planView);
        planView.setPlanListBeanList(getData());
        planView.setPlanClickListener(new PlanView.OnPlanClickListener() {
            @Override
            public void onClick(String planId, boolean isEllipsis) {
                showToast("PlanId: " + planId + " isEllipsis: " + isEllipsis);
            }
        });
    }

    private List<PlanBean> getData() {
        List<PlanBean> planListBeanList = new ArrayList<>();

        PlanBean bean = new PlanBean();
        bean.setDayIndex(1);
        bean.setPlanId("1");
        bean.setPlanStartTime("12:20");
        bean.setPlanEndTime("16:40");
        bean.setPlanName("晋太元中，武陵人捕鱼为业。缘溪行，忘路之远近。忽逢桃花林，夹岸数百步，中无杂树，芳草鲜美，落英缤纷。渔人甚异之，复前行，欲穷其林");
        bean.setColor("#008577");
        planListBeanList.add(bean);

        bean = new PlanBean();
        bean.setDayIndex(2);
        bean.setPlanId("2");
        bean.setPlanStartTime("05:20");
        bean.setPlanEndTime("05:52");
        bean.setPlanName("晋太元中，武陵人捕鱼为业。缘溪行，忘路之远近。忽逢桃花林，夹岸数百步，中无杂树，芳草鲜美，落英缤纷。渔人甚异之，复前行，欲穷其林");
        bean.setColor("#3b6ff2");
        planListBeanList.add(bean);

        bean = new PlanBean();
        bean.setDayIndex(3);
        bean.setPlanId("3");
        bean.setPlanStartTime("05:20");
        bean.setPlanEndTime("05:25");
        bean.setPlanName("晋太元中，武陵人捕鱼为业。缘溪行，忘路之远近。忽逢桃花林，夹岸数百步，中无杂树，芳草鲜美，落英缤纷。渔人甚异之，复前行，欲穷其林");
        bean.setColor("#F77DA5");
        planListBeanList.add(bean);

        bean = new PlanBean();
        bean.setDayIndex(3);
        bean.setPlanId("3");
        bean.setPlanStartTime("15:20");
        bean.setPlanEndTime("15:52");
        bean.setPlanName("晋太元中，武陵人捕鱼为业。缘溪行，忘路之远近。忽逢桃花林，夹岸数百步，中无杂树，芳草鲜美，落英缤纷。渔人甚异之，复前行，欲穷其林");
        bean.setColor("#f7b364");
        planListBeanList.add(bean);

        bean = new PlanBean();
        bean.setDayIndex(3);
        bean.setPlanId("4");
        bean.setPlanStartTime("10:20");
        bean.setPlanEndTime("12:20");
        bean.setPlanName("晋太元中，武陵人捕鱼为业。缘溪行，忘路之远近。忽逢桃花林，夹岸数百步，中无杂树，芳草鲜美，落英缤纷。渔人甚异之，复前行，欲穷其林");
        bean.setColor("#ed3371");
        planListBeanList.add(bean);

        bean = new PlanBean();
        bean.setDayIndex(4);
        bean.setPlanId("5");
        bean.setPlanStartTime("10:20");
        bean.setPlanEndTime("12:20");
        bean.setPlanName("晋太元中，武陵人捕鱼为业。缘溪行，忘路之远近。忽逢桃花林，夹岸数百步，中无杂树，芳草鲜美，落英缤纷。渔人甚异之，复前行，欲穷其林");
        bean.setColor("#ed3371");
        planListBeanList.add(bean);

        bean = new PlanBean();
        bean.setDayIndex(7);
        bean.setPlanId("6");
        bean.setPlanStartTime("10:20");
        bean.setPlanEndTime("12:20");
        bean.setPlanName("晋太元中，武陵人捕鱼为业。缘溪行，忘路之远近。忽逢桃花林，夹岸数百步，中无杂树，芳草鲜美，落英缤纷。渔人甚异之，复前行，欲穷其林");
        bean.setColor("#ef5a4d");
        planListBeanList.add(bean);

        bean = new PlanBean();
        bean.setDayIndex(5);
        bean.setPlanId("7");
        bean.setPlanStartTime("05:20");
        bean.setPlanEndTime("12:20");
        bean.setPlanName("晋太元中，武陵人捕鱼为业。缘溪行，忘路之远近。忽逢桃花林，夹岸数百步，中无杂树，芳草鲜美，落英缤纷。渔人甚异之，复前行，欲穷其林");
        bean.setColor("#f14bfd");
        planListBeanList.add(bean);

        return planListBeanList;
    }

}
