package com.example.cuongphan.multithreadedprogramming_updateuiwithpost;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FlipCoinAvtivity extends Activity {
    private static final int HEADER_TIMEOUT = 10;
    TextView mTextView;
    LinearLayout mLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin);
        mLinearLayout = (LinearLayout)findViewById(R.id.linear_layout);
        mTextView = new TextView(this);
    }

    public void flipCoin(View view){
        mLinearLayout.removeAllViews();
        mLinearLayout.requestLayout();
        ExecutorService taskList = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 5; i++) {
            taskList.execute(new FlipCoin());
        }
        try {
            taskList.shutdown();
            taskList.awaitTermination(HEADER_TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class FlipCoin implements Runnable {

        @Override
        public void run() {
            int coin_sum = 0;
            int coin;
            int max=0;
            Random rd = new Random();
            synchronized (this) {
                for (int i = 0; i < 1000; i++) {
                    coin = rd.nextInt(2);
                    if (coin_sum == (coin_sum + coin)) {
                        coin_sum = 0;
                    } else {
                        coin_sum += coin;
                    }
                    if (coin_sum > max) {
                        max = coin_sum;
                        mLinearLayout.post(new TextviewDisplay(max));
                        try {
                            Thread.sleep((long) 0.01);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private class TextviewDisplay implements Runnable {
        int mMaxCoin;
        public TextviewDisplay(int max) {
            mMaxCoin = max;
        }

        @Override
        public void run() {
            mTextView.setText("Max coin: "+mMaxCoin);
            mLinearLayout.addView(mTextView);
        }
    }
}
