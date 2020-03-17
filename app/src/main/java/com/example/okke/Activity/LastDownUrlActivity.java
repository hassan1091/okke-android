package com.example.okke.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.okke.Adapters.LR_Adapter;
import com.example.okke.R;
import com.example.okke.data.ExtraContext;
import com.example.okke.listener.OnItemClickListener;

import java.util.ArrayList;

public class LastDownUrlActivity extends AppCompatActivity {
    private ArrayList mLRdownList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re2s);
        mLRdownList = new ArrayList<String>();
        mLRdownList.add(getIntent().getStringExtra(ExtraContext.LRList));

        LR_Adapter lr_adapter = new LR_Adapter(mLRdownList, new OnItemClickListener() {
            @Override
            public void OnItemClickListener(int position) {
               Intent intent = new Intent();
                intent.putExtra(ExtraContext.THIS_URL, mLRdownList.get(position).toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(lr_adapter);


    }
}
