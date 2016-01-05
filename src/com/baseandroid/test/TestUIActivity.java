package com.baseandroid.test;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.baseandroid.R;

/**
 * @author Mark
 * @title UI测试类
 * @function 测试Activity Fragment
 */
public class TestUIActivity extends Activity {
    public static final String TYPE_FRAGMENT = "Fragment";
    public static final String TYPE_ACTIVITY = "Activity";
    public static final String EXTRA_FRAGMENT = "fragment";

    private ListView main_list;
    private String[] listStrs = null;
    private List<Class> classList = new ArrayList<Class>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);
        // view init
        main_list = (ListView) findViewById(R.id.main_list);

        initData();

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, R.layout.item_test, R.id.item_tv, listStrs);
        main_list.setAdapter(myAdapter);
        // setListener
        main_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Intent i = new Intent();
                Class itemClass = classList.get(position);

                if (itemClass.getSimpleName().contains(TYPE_FRAGMENT)) {

                    i.setClass(TestUIActivity.this, TestFragmentActivity.class);
                    i.putExtra("Fragment", itemClass);

                } else if (itemClass.getSimpleName().contains(TYPE_ACTIVITY)) {

                    i.setClass(TestUIActivity.this, itemClass);

                }

                startActivity(i);
            }
        });

    }

    private void initData() {
        // add


        listStrs = new String[classList.size()];
        for (int i = 0; i < classList.size(); i++) {
            listStrs[i] = classList.get(i).getSimpleName();
        }
    }

    private void add(Class cl) {
        classList.add(cl);
    }
}
