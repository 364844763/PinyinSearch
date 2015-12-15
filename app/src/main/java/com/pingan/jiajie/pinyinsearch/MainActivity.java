package com.pingan.jiajie.pinyinsearch;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity {
    private EditText mSearchTv;
    private ListView mListLv;
    private Button mSearchBtn;
    private List<Contact> mList;
    private SearchAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initData();
        setListener();
    }

    private void setListener() {
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key= mSearchTv.getText().toString();
                if (key==null||key.isEmpty()){
                    mAdapter.setData(mList);
                    return;
                }

                ArrayList<Contact> contactList=new ArrayList<Contact>();
                contactList=PinyinUtil.search(key, (ArrayList<Contact>) mList,contactList);
                mAdapter.setData(contactList);

            }
        });
    }

    private void initData() {
        mList=new ArrayList<>();
        for (int i = 0; i <20; i++) {
            String str="";
            Contact contact=new Contact();
            RandomHan han = new RandomHan();
            if (i%2==0){
                for (int j = 0; j <3; j++) {
                    str+=han.getRandomHan();
                }
                contact.setNumber("美女" + i);
            }else {
                for (int j = 0; j <2; j++) {
                    str+=han.getRandomHan();
                }
                contact.setNumber("帅哥"+i);
            }
            contact.setName(str);
            mList.add(contact);

        }
        mAdapter=new SearchAdapter(mList,this);
        mListLv.setAdapter(mAdapter);
    }

    private void init() {
        mSearchTv = (EditText) findViewById(R.id.search_et);
        mListLv = (ListView) findViewById(R.id.search_lv);
        mSearchBtn = (Button) findViewById(R.id.search_button);
    }
    class RandomHan {
        private Random ran = new Random();
        private final static int delta = 0x9fa5 - 0x4e00 + 1;

        public char getRandomHan() {
            return (char)(0x4e00 + ran.nextInt(delta));
        }
    }
    private Handler mHandler;
    private void LightTaskManager() {
        HandlerThread workerThread = new HandlerThread("LightTaskThread");
        workerThread.start();
        mHandler = new Handler(workerThread.getLooper());
    }
}
