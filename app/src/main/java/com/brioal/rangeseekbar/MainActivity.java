package com.brioal.rangeseekbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.brioal.rangeseek.entity.RangeEntity;
import com.brioal.rangeseek.interfaces.OnRangeChangedListener;
import com.brioal.rangeseek.view.RangeBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RangeBar mRangeBar;
    TextView mTvMin;
    TextView mTvMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRangeBar = (RangeBar) findViewById(R.id.main_container);
        mTvMin = (TextView) findViewById(R.id.tv_min);
        mTvMax = (TextView) findViewById(R.id.tv_max);
        final List<RangeEntity> list = new ArrayList<>();
        list.add(new RangeEntity("15℃", 15));
        list.add(new RangeEntity("18℃", 18));
        list.add(new RangeEntity("21℃", 21));
        list.add(new RangeEntity("24℃", 24));
        list.add(new RangeEntity("27℃", 27));
        list.add(new RangeEntity("30℃", 30));
        mRangeBar.setValues(list);
        mRangeBar.addOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void selected(int startIndex, int endIndex) {
                mTvMin.setText(list.get(startIndex).getValue() + "");
                mTvMax.setText(list.get(endIndex).getValue() + "");
            }
        });
    }
}
