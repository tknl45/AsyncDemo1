package com.chtti.asyncdemo1;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private class DemoAsyncTask extends AsyncTask<Integer, Integer, Date>{

        //這是在另一個執行緒，不行用主執行緒‘
        @Override
        protected Date doInBackground(Integer... colors) {
            //把背景顏色換
            for (Integer color : colors){
                if (isCancelled()){
                    break;
                }
                try {
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                //point1
                publishProgress(color);
            }

            //point2
            return new Date();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            //point1
            background.setBackgroundColor(values[0]);

            textView1.setText((new Date()).toString());
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Date date) {
            //point2
            textView1.setText(date.toString());
        }

        @Override
        protected void onCancelled(Date date) {
            textView1.setBackgroundColor(Color.BLACK);
            textView1.setTextColor(Color.WHITE);
            textView1.setText("CANCELED AT:" + date.toString());
        }
    }

    private View background;
    private TextView textView1;
    private static final Integer[] COLORS = {Color.RED, Color.YELLOW, Color.GREEN, Color.GRAY, Color.BLACK};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        DemoAsyncTask asyncTask = new DemoAsyncTask();
        background = findViewById(android.R.id.content);
        textView1 = findViewById(R.id.textview1);
        findViewById(R.id.button1).setOnClickListener((v) -> asyncTask.execute(COLORS));

        findViewById(R.id.button2).setOnClickListener((v) -> asyncTask.cancel(true));
    }
}
