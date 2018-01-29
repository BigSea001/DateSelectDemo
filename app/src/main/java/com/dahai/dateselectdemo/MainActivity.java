package com.dahai.dateselectdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv_select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_select = findViewById(R.id.tv_select);
    }

    public void selectDate(View view) {
        Intent intent = new Intent(this,DateSelectActivity.class);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == DateSelectActivity.RERSULT_CODE) {
            Bundle extras = data.getExtras();
            if (extras!=null) {
                String start = extras.getString(DateSelectActivity.START);
                String end = extras.getString(DateSelectActivity.END);
                int days = extras.getInt(DateSelectActivity.DAY_NUMBER);

                tv_select.setText("开始时间："+start + "   结束时间："+end + "   天数："+days);
            }
        }
    }
}
