package com.yiftah.jobtimer;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    final static String TAG = "com.yiftah.jobtimer";
    Handler moneyHandler = new Handler () {
        @Override
        public void handleMessage(Message msg) {
            String startTime = handler.getStartTime();
            if (startTime.length() > 0) {
                Date startDate = new Date ();
                startDate.setYear(Integer.parseInt(startTime.substring(0,4)) - 1900);
                startDate.setMonth(Integer.parseInt(startTime.substring(5, 7)) - 1);
                startDate.setDate(Integer.parseInt(startTime.substring(8, 10)));
                startDate.setHours(Integer.parseInt(startTime.substring(11, 13)));
                startDate.setMinutes(Integer.parseInt(startTime.substring(14)));
                long timeInMillis = System.currentTimeMillis() - startDate.getTime();
                int minutes = (int) (timeInMillis / 60000);
                int workMoney = handler.getMoneyPerHour() * minutes / 60;
                money.setText(("רווח: " + (handler.getMoney() + workMoney) + " ₪"));
            }
            else {
                money.setText(("רווח: " + handler.getMoney() + " ₪"));
            }
        }
    };
    TextView money = null;
    EditText moneyPerHour = null;
    Button work = null;
    EditText addMoney = null;
    boolean working = false;
    MyDBHandler handler = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        money = (TextView) findViewById (R.id.money);
        moneyPerHour = (EditText) findViewById (R.id.perhourEdit);
        work = (Button) findViewById (R.id.work);
        addMoney = (EditText) findViewById (R.id.addMoneyEdit);
        handler = new MyDBHandler (this);
        working = handler.getStartTime().length() > 0;
        if (working) {
            work.setText("הפסק לעבוד");
            Thread thread = new Thread () {
                public void run () {
                    while (working) {
                        moneyHandler.sendEmptyMessage(0);
                        try {
                            Thread.sleep(60000);
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            thread.start();
        }
        money.setText(("רווח: " + handler.getMoney() + "₪"));
        moneyPerHour.setText("" + handler.getMoneyPerHour());
    }
    public void work (View view) {
        if (working) {
            working = false;
            work.setText("התחל לעבוד");
            String startTime = handler.getStartTime();
            if (startTime != "") {
                Date startDate = new Date ();
                startDate.setYear(Integer.parseInt(startTime.substring(0,4))-1900);
                startDate.setMonth(Integer.parseInt(startTime.substring(5, 7))-1);
                startDate.setDate(Integer.parseInt(startTime.substring(8, 10)));
                startDate.setHours(Integer.parseInt(startTime.substring(11, 13)));
                startDate.setMinutes(Integer.parseInt(startTime.substring(14)));
                long timeInMillis = System.currentTimeMillis() - startDate.getTime();
                int minutes = (int) (timeInMillis / 60000);
                int workMoney = handler.getMoneyPerHour() * minutes / 60;
                handler.setMoney(handler.getMoney() + workMoney);
                handler.setStartTime("");
                moneyHandler.sendEmptyMessage(0);
            }
            handler.setStartTime("");
        }
        else {
            working = true;
            work.setText("הפסק לעבוד");
            String nowTime = "";
            Date now = new Date ();
            nowTime += (now.getYear() + 1900) + "-";
            if (("" + (now.getMonth() + 1)).length() == 1)
                nowTime += "0";
            nowTime += (now.getMonth() + 1) + "-";
            if (("" + now.getDate()).length() == 1)
                nowTime += "0";
            nowTime += now.getDate() + " ";
            if (("" + now.getHours()).length() == 1)
                nowTime += "0";
            nowTime += now.getHours() + ":";
            if (("" + now.getMinutes()).length() == 1)
                nowTime += "0";
            nowTime += now.getMinutes();
            handler.setMoneyPerHour(Integer.parseInt(moneyPerHour.getText().toString()));
            handler.setStartTime(nowTime);
            Log.i(TAG, nowTime);
            Thread thread = new Thread () {
                public void run () {
                    while (working) {
                        moneyHandler.sendEmptyMessage(0);
                        try {
                            Thread.sleep(60000);
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            thread.start();
        }
    }
    public void addMoney (View view) {
        int moneyToAdd = Integer.parseInt(addMoney.getText().toString());
        if (moneyToAdd != 0) {
            handler.setMoney(handler.getMoney() + moneyToAdd);
            moneyHandler.sendEmptyMessage(0);
        }
    }
    public void removeMoney (View view) {
        int moneyToAdd = Integer.parseInt(addMoney.getText().toString());
        if (moneyToAdd != 0) {
            handler.setMoney(handler.getMoney() - moneyToAdd);
            moneyHandler.sendEmptyMessage(0);
        }
    }

}
