package com.example.timerdems;


import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * @author xiandanin
 * created 2018/11/9 17:50
 */
public class RecyclerViewActivity extends AppCompatActivity {
    private CountDownTimerSupport mTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);

        long maxDuration = 0;

        List<TimeInfo> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            final TimeInfo info = new TimeInfo();
            final int duration = new Random().nextInt(60) * 1000;
            if (duration > maxDuration) {
                maxDuration = duration;
            }
            info.setDuration(duration);
            info.setRemainingTime(duration);
            info.setState(TimerState.START);
            data.add(info);
        }


        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);

        rv.setLayoutManager(new LinearLayoutManager(this));
        final ExampleAdapter adapter = new ExampleAdapter(data);
        rv.setAdapter(adapter);

        mTimer = new CountDownTimerSupport(maxDuration, 1000);
        mTimer.setOnCountDownTimerListener(new OnCountDownTimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("CountDownTimerSupport", "onTick : " + millisUntilFinished + "ms");
                for (TimeInfo info : adapter.getData()) {
                    if (info.getState() == TimerState.START) {
                        long newTime = info.getRemainingTime() - 1000;
                        info.setRemainingTime(Math.max(0, newTime));
                        info.setState(newTime <= 0 ? TimerState.FINISH : TimerState.START);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFinish() {
                Log.d("CountDownTimerSupport", "onFinish");
            }

            @Override
            public void onCancel() {
                Log.d("CountDownTimerSupport", "onCancel");
            }
        });
        mTimer.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mTimer.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTimer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.stop();
    }
}
