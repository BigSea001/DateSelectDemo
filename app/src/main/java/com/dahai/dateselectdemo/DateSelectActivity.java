package com.dahai.dateselectdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 描述：时间段选择
 * 时间：2018/1/27/027
 * 作者：xjh
 */
public class DateSelectActivity extends FragmentActivity implements View.OnClickListener {

    public static final int RERSULT_CODE = 200;
    public static final String START = "START";
    public static final String END = "END";
    public static final String DAY_NUMBER = "DAY_NUMBER";
    private List<MonthBean> monthTimeEntities;
    private List<DayBean> currList;
    private List<DayBean> nextList;
    private RecyclerView mRecyclerView;
    private ImageView toolbar_iv_back;
    private TextView toolbar_iv_title;
    private TextView toolbar_tv_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dateselect);

        initView();
        initToolbar();
        initData();
        initRecyclerView();
    }

    private void initToolbar() {
        toolbar_iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar_iv_title.setText("入住离店日期");
        toolbar_tv_right.setVisibility(View.VISIBLE);
        toolbar_tv_right.setText("确定");
        toolbar_tv_right.setOnClickListener(this);
    }

    private void initData() {
        monthTimeEntities = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        int currYear = c.get(Calendar.YEAR);
        int currMonth = c.get(Calendar.MONTH) + 1;
        monthTimeEntities.add(new MonthBean(currYear, currMonth));

        c.add(Calendar.MONTH, 1);  // 得到下一个月
        int nextYear = c.get(Calendar.YEAR);
        int nextMonth = c.get(Calendar.MONTH) + 1;
        monthTimeEntities.add(new MonthBean(nextYear, nextMonth));

        handleData();
    }

    private void handleData() {
        currList = new ArrayList<>();
        nextList = new ArrayList<>();

        for (int i = 0; i < monthTimeEntities.size(); i++) {
            if (i == 0) {
                MonthBean entity = monthTimeEntities.get(i);
                int month = entity.getMonth();
                int year = entity.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month - 1, 1);  // 设置这个月的第一天
                int currYear = calendar.get(Calendar.YEAR);
                int currMonth = calendar.get(Calendar.MONTH);
                int day_of_week = calendar.get(Calendar.DAY_OF_WEEK); // 获取当前这天的星期
                // 获取第一天的前面空了几个星期
                int offset = day_of_week - 1;
                // 获取当月最后一天
                calendar.add(Calendar.MONTH, 1); // 下一个月
                calendar.add(Calendar.DATE, -1); // 减一天
                int totalDays = calendar.get(Calendar.DATE);  // 获取当月的天数
                for (int i1 = 0; i1 < offset; i1++) {
                    String jr = DateUtils.getJR(0, 0);
                    currList.add(new DayBean(0, 0, 0, jr, false, false));
                }
                for (int i1 = 0; i1 < totalDays; i1++) {
                    String jr = DateUtils.getJR(currMonth + 1, i1 + 1);
                    currList.add(new DayBean(currYear, currMonth + 1, (i1 + 1), jr, false, false));
                }
            } else if (i == 1) {
                MonthBean entity = monthTimeEntities.get(i);
                int month = entity.getMonth();
                int year = entity.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month - 1, 1);  // 设置这个月的第一天
                int currYear = calendar.get(Calendar.YEAR);
                int currMonth = calendar.get(Calendar.MONTH);
                int day_of_week = calendar.get(Calendar.DAY_OF_WEEK); // 获取当前这天的星期
                // 获取第一天的前面空了几个星期
                int offset = day_of_week - 1;
                // 获取当月最后一天
                calendar.add(Calendar.MONTH, 1); // 下一个月
                calendar.add(Calendar.DATE, -1); // 减一天
                int totalDays = calendar.get(Calendar.DATE);  // 获取当月的天数
                for (int i1 = 0; i1 < offset; i1++) {
                    String jr = DateUtils.getJR(0, 0);
                    nextList.add(new DayBean(0, 0, 0, jr, false, false));
                }
                for (int i1 = 0; i1 < totalDays; i1++) {
                    String jr = DateUtils.getJR(currMonth + 1, i1 + 1);
                    nextList.add(new DayBean(currYear, currMonth + 1, (i1 + 1), jr, false, false));
                }
            }
        }
    }

    private void initRecyclerView() {
        final MonthDateSelectAdapter adapter = new MonthDateSelectAdapter(this, monthTimeEntities, currList, nextList);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setListener(new MonthDateSelectAdapter.onDateSelectListener() {
            @Override
            public void onSelect(int pos, int month) {
                // 判断当前有多少个选中的
                int num = 0;
                for (DayBean entity : currList) {
                    if (entity.isSelect()) {
                        num++;
                    }
                }
                for (DayBean entity : nextList) {
                    if (entity.isSelect()) {
                        num++;
                    }
                }
                // 如果选了2个全部重置
                if (num > 1) {
                    for (DayBean entity : currList) {
                        entity.setSelect(false);
                        entity.setMiddle(false);
                    }
                    for (DayBean entity : nextList) {
                        entity.setSelect(false);
                        entity.setMiddle(false);
                    }

                    // 设置当前选中
                    if (month == 0) {
                        currList.get(pos).setSelect(true);
                    }
                    if (month == 1) {
                        nextList.get(pos).setSelect(true);
                    }
                } else if (num == 1) {
                    int selectMonth = -1;
                    int prePos = 0;
                    for (int i = 0; i < currList.size(); i++) {
                        DayBean entity = currList.get(i);
                        if (entity.isSelect()) {
                            selectMonth = 0;
                            prePos = i;
                        }
                    }
                    for (int i = 0; i < nextList.size(); i++) {
                        DayBean entity = nextList.get(i);
                        if (entity.isSelect()) {
                            selectMonth = 1;
                            prePos = i;
                        }
                    }

                    if (selectMonth == 0) {
                        if (month == 0) {
                            if (prePos >= pos) {  // 全部置空
                                for (DayBean entity : currList) {
                                    entity.setSelect(false);
                                    entity.setMiddle(false);
                                }
                                for (DayBean entity : nextList) {
                                    entity.setSelect(false);
                                    entity.setMiddle(false);
                                }
                                currList.get(pos).setSelect(true);
                            } else {
                                for (int i = 0; i < currList.size(); i++) {
                                    if (i > prePos && i < pos) {
                                        currList.get(i).setSelect(false);
                                        currList.get(i).setMiddle(true);
                                    }
                                }
                                currList.get(pos).setSelect(true);
                            }
                        } else if (month == 1) {
                            for (int i = 0; i < currList.size(); i++) {
                                if (i > prePos) {
                                    currList.get(i).setSelect(false);
                                    currList.get(i).setMiddle(true);
                                }
                            }
                            for (int i = 0; i < nextList.size(); i++) {
                                if (i < pos) {
                                    nextList.get(i).setMiddle(true);
                                    nextList.get(i).setSelect(false);
                                }
                            }
                            nextList.get(pos).setSelect(true);
                            nextList.get(pos).setMiddle(false);
                        }
                    } else if (selectMonth == 1) {
                        if (month == 0) {
                            for (DayBean entity : currList) {
                                entity.setSelect(false);
                                entity.setMiddle(false);
                            }
                            for (DayBean entity : nextList) {
                                entity.setSelect(false);
                                entity.setMiddle(false);
                            }
                            currList.get(pos).setSelect(true);
                        } else if (month == 1) {
                            if (prePos >= pos) {  // 全部置空
                                for (DayBean entity : currList) {
                                    entity.setSelect(false);
                                    entity.setMiddle(false);
                                }
                                for (DayBean entity : nextList) {
                                    entity.setSelect(false);
                                    entity.setMiddle(false);
                                }
                                nextList.get(pos).setSelect(true);
                            } else {
                                for (int i = 0; i < nextList.size(); i++) {
                                    if (i > prePos && i < pos) {
                                        nextList.get(i).setSelect(false);
                                        nextList.get(i).setMiddle(true);
                                    }
                                }
                                nextList.get(pos).setSelect(true);
                            }
                        }
                    }
                } else if (num == 0) {
                    if (month == 0) {
                        currList.get(pos).setSelect(true);
                    } else if (month == 1) {
                        nextList.get(pos).setSelect(true);
                    }
                }
                adapter.notifySelectChanage();
            }
        });
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        toolbar_iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        toolbar_iv_title = (TextView) findViewById(R.id.toolbar_iv_title);
        toolbar_tv_right = (TextView) findViewById(R.id.toolbar_tv_right);
    }

    public void sure() {
        int num = 0;
        for (DayBean entity : currList) {
            if (entity.isSelect()) {
                num++;
            }
        }
        for (DayBean entity : nextList) {
            if (entity.isSelect()) {
                num++;
            }
        }
        if (num < 2) {
            Toast.makeText(this, "选择", Toast.LENGTH_SHORT).show();
        } else if (num == 2) {
            List<DayBean> selectDays = new ArrayList<>();
            List<DayBean> all = new ArrayList<>();
            all.addAll(currList);
            all.addAll(nextList);
            for (int i = 0; i < all.size(); i++) {
                DayBean entity = all.get(i);
                if ((entity.isSelect() || entity.isMiddle()) && entity.getDay() != 0) {
                    selectDays.add(entity);
                }
            }
            String text = "天数：" + selectDays.size() + " | " + selectDays.get(0).getYear() + "年" + (selectDays.get(0).getMonth()) + "月" + selectDays.get(0).getDay() + "日"
                    + "===>" + selectDays.get(selectDays.size() - 1).getYear() + "年" + (selectDays.get(selectDays.size() - 1).getMonth()) + "月" + selectDays.get(selectDays.size() - 1).getDay() + "日";
            Log.e("HHH", "sure: 选中 " + text );
            Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(START,selectDays.get(0).getMonth()+"."+selectDays.get(0).getDay());
            bundle.putString(END,selectDays.get(selectDays.size() - 1).getMonth()+"."+selectDays.get(selectDays.size() - 1).getDay());
            bundle.putInt(DAY_NUMBER,selectDays.size());
            data.putExtras(bundle);
            setResult(RERSULT_CODE, data);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.toolbar_tv_right:
                sure();
                break;
        }
    }
}
