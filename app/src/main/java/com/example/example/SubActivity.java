package com.example.example;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.TypedArrayUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SubActivity extends AppCompatActivity {
    private TextView titleTv, contentsTv;
    private Button addBtn, logoutBtn;
    private ListView listview;
    private DiaryAdapter adapter;
    private ArrayList<Diary> array;
    private String getTitle, getContents, newtitle="", newcontents="";
    private String[] titleList, contentsList, Firsttitle, Firstcontents;
    private ArrayList<String> titlearry, contentsarry, Firstarrytitle, Firstarrycontents;

    private static final int CALL_ADDACTIVITY = 0;
    private static final int UPDATE_ADDACTIVITY =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        //MainActivity에서 보낸 인텐트 받기
        Intent intent = getIntent();
        //Toast.makeText(this, "로그인 완료", Toast.LENGTH_SHORT).show();

        array = new ArrayList<>();
        titlearry = new ArrayList<>();
        contentsarry = new ArrayList<>();
        Firstarrytitle = new ArrayList<>();
        Firstarrycontents = new ArrayList<>();

        titleTv = findViewById(R.id.textView1);
        contentsTv = findViewById(R.id.textView2);
        addBtn = findViewById(R.id.Button1);
        logoutBtn = findViewById(R.id.logout);
        listview = findViewById(R.id.ListView1);

        SharedPreferences sp = getSharedPreferences("myFile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();


        //처음엔 값이 없으므로 null 값 준다
        getTitle = sp.getString("title", null);
        getContents = sp.getString("contents", null);

        Log.v("FirstHI", getTitle+"");
        Log.v("FirstHI", getContents+"");
        //Log.v("first_title", getTitle+"");
        //Log.v("first_contents", getContents+"");

        //sharedPreference에 아무것도 없을 때, 그냥 화면 출력하기
        if(getTitle == null && getContents == null){
            adapter = new DiaryAdapter(this,R.layout.list_layout,array);
            listview.setAdapter(adapter);
        }

        //sharedPreference에 내용이 있을 때, 리스트 출력하기
        else if (getTitle != null && getContents != null){

            // title 목록 배열 안에 넣기
            Firsttitle= getTitle.split("/");
            for(int i=0; i<Firsttitle.length; i++){
                Firstarrytitle.add(Firsttitle[i]);
                Log.v("titlelist", Firsttitle[i]);
            }

            //contents 목록 배열 안에 넣기
            Firstcontents = getContents.split("/");
            for(int i=0; i<Firstcontents.length; i++){
                Firstarrycontents.add(Firstcontents[i]);
                Log.v("contentslist", Firstcontents[i]);
            }

            //다이어리 배열에 제목, 본문 할당하기
            for(int i=0; i<Firstarrytitle.size(); i++){
                array.add(new Diary(Firsttitle[i], Firstcontents[i]));
            }

            adapter = new DiaryAdapter(this,R.layout.list_layout,array);
            listview.setAdapter(adapter);
        }


        //Log.v("shared_Title", getTitle);
        //Log.v("shared_Contents", getContents);
        //array.add(new Diary(getTitle, getContents));

        //다이어리 배열내용을 listview에 할당하고, adapter에 적용
        //adapter = new DiaryAdapter(this,R.layout.list_layout,array);
        //listview.setAdapter(adapter);

        //아이템 한번 눌렀을 때, 수정 화면으로 전환
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SubActivity.this, AddActivity.class);
                intent.putExtra("index", i);
                intent.putExtra("diaryitem", array.get(i));

                startActivityForResult(intent, UPDATE_ADDACTIVITY);
            }
        });

        //아이템 길게 눌렀을 때, 삭제
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences sp = getSharedPreferences("myFile", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                getTitle = sp.getString("title", null);
                getContents = sp.getString("contents", null);

                Log.v("kjw_getTitle", getTitle+"");
                Log.v("kjw_getContents", getContents+"");

                // title 목록 배열 안에 넣기

                titleList= getTitle.split("/");
                titlearry.clear();
                Log.d("kjw_TitleInitial", String.valueOf(titlearry));
                for(int j=0; j<titleList.length; j++){
                    titlearry.add(titleList[j]);
                }
                Log.d("kjw_titleArry", String.valueOf(titlearry));


                //contents 목록 배열 안에 넣기
                contentsList = getContents.split("/");
                contentsarry.clear();
                Log.d("kjw_ContentsInitial", String.valueOf(contentsarry));
                for(int k=0; k<titleList.length; k++){
                    contentsarry.add(contentsList[k]);
                }
                Log.d("kjw_contentsArry", String.valueOf(contentsarry));

                // Listview 항목 지우기
                array.remove(i);
                // 제목 배열 지우기
                titlearry.remove(i);
                Log.d("kjw_rmvTitle", String.valueOf(titlearry));
                //본문 배열 지우기
                contentsarry.remove(i);
                Log.d("kjw_rmvContents", String.valueOf(contentsarry));

                //

                if ( titlearry.isEmpty() && contentsarry.isEmpty() ){
                    editor.putString("title", null);
                    editor.putString("contents", null);
                }
                else {
                    for (int index = 0; index < titlearry.size(); index++) {
                        newtitle += titlearry.get(index) + "/";
                    }
                    Log.d("kjw_newTitle", newtitle);
                    for (int index = 0; index < contentsarry.size(); index++) {
                        newcontents += contentsarry.get(index) + "/";
                    }
                    Log.d("kjw_newContents", newcontents);


                    String deltitle = newtitle.substring(0, newtitle.length() - 1);
                    String delcontents = newcontents.substring(0, newcontents.length() - 1);
                    editor.putString("title", deltitle);
                    editor.putString("contents", delcontents);
                }
                editor.commit();


                //Log.v("kjw_AFTcommitTitle", sp.getString("title", null));
                //Log.v("kjw_AFTcommitContents",  sp.getString("contents" , null));

                newtitle = "";
                newcontents = "";

                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    //등록 버튼 - 수정화면으로 전환
    public void onAddButtonClicked(View view) {
        Intent intent = new Intent(SubActivity.this, AddActivity.class);
        startActivityForResult(intent, CALL_ADDACTIVITY);
    }

    //로그아웃 버튼 - 로그인 sharedReference 내용 지우고, 목록화면 꺼짐
    public void onLogoutButtonClicked(View view) {
        Intent intent = new Intent(SubActivity.this, MainActivity.class);
        startActivity(intent);
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = auto.edit();
        //auto에 들어있는 모든 정보 기기에서 지우기
        editor.clear();
        editor.commit();
        Toast.makeText(SubActivity.this, "로그아웃.", Toast.LENGTH_SHORT).show();
        finish();
    }

    //수정 화면->목록 화면 돌아올 때, 전달받는 intent 값
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 코드 같으면 데이터 받아서 화면에 뿌리기
        switch (requestCode) {
            case CALL_ADDACTIVITY:
                if (resultCode == RESULT_OK) {
                    String title = data.getStringExtra("title");
                    String contents = data.getStringExtra("contents");


                    array.add(new Diary(title, contents));
                    adapter.notifyDataSetChanged();
                }
                break;

            case UPDATE_ADDACTIVITY:
                if(resultCode == RESULT_OK){
                    int index = data.getIntExtra("index", -1);
                    //index번째 항목 선택했을 때, index번째 항목 내용 바뀜
                    if(index >=0) {
                        Diary dia = (Diary) data.getSerializableExtra("diaryitem");
                        Diary diary = array.get(index);

                        diary.setTitle(dia.getTitle());
                        diary.setContents(dia.getContents());
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }



}