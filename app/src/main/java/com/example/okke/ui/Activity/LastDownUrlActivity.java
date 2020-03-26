package com.example.okke.ui.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.okke.Adapters.LR_Adapter;
import com.example.okke.R;
import com.example.okke.data.ExtraContext;
import com.example.okke.data.database.DatabaseForAdapter;
import com.example.okke.data.database.LastUrlList;
import com.example.okke.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class LastDownUrlActivity extends AppCompatActivity {
    private DatabaseForAdapter databaseForAdapter;
    private LR_Adapter lr_adapter;
    private List<LastUrlList> urlLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re2s);
        //استخدام قاعدة البيانات
        databaseForAdapter = DatabaseForAdapter.getsInstance(this);
        //جلب البيانات من قاعدة البيانات
        new Asyn().execute();
        //تعيين recyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //بدء حدث حيما  يتم النقر على الزر
        lr_adapter = new LR_Adapter(urlLists, new OnItemClickListener() {
            @Override
            public void OnItemClickListener(int position, List<LastUrlList> lastUserUrlArray) {
                //اخذ الرابط المختار وارساله الى MainActivity
                Intent intent = new Intent();
                intent.putExtra(ExtraContext.THIS_URL, lastUserUrlArray.get(position).getmLRDownList());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        //تعين adapter الى recyclerView
        recyclerView.setAdapter(lr_adapter);
    }

    // بدء Thread جديد
    public class Asyn extends AsyncTask<Void, Void, List<LastUrlList>> {

        @Override
        protected List<LastUrlList> doInBackground(Void... voids) {
            //اخذ البيانات قاعدة البيانات
            return databaseForAdapter.lastUrlDaw().getLastUrlList();
        }

        @Override
        protected void onPostExecute(List<LastUrlList> lastUrlLists) {
            super.onPostExecute(lastUrlLists);
            //وضع جميع الروابط داخل متغير
            urlLists.addAll(lastUrlLists);
            //فتح Thread للتعامل مع الواجهة
            LastDownUrlActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //تحديث بيانات Adapter
                    lr_adapter.notifyDataSetChanged();
                }
            });
        }
    }
}
