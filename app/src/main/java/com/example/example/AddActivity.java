package com.example.example;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    private EditText et3, et4;
    private Button backbtn, okbtn;
    private int index;
    private String getTitle, getContents, newTitle="", newContents="", saveTitle="", saveContents="";
    private String[] titleList, contentsList;
    private ArrayList<String> titlearry = new ArrayList<>();
    private ArrayList<String> contentsarry = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        et3 = findViewById(R.id.editText3);
        et4 = findViewById(R.id.editText4);
        backbtn = findViewById(R.id.Button3);
        okbtn = findViewById(R.id.Button4);

        //SubActivity에서 인텐트 받음 ( 등록 / 수정 )
        Intent intent = getIntent();

        //새로 등록 시, index 받아온 값 없으므로 -1 지정
        index = intent.getIntExtra("index", -1);

        //수정 시, 몇번 째인지 index값 받아왔으므로 수정화면 editText에 제목, 본문 뿌려짐
        if(index >= 0){
            Diary dia = (Diary)intent.getSerializableExtra("diaryitem");
            et3.setText(dia.getTitle());
            et4.setText(dia.getContents());
        }


        //(등록/수정) 한 값 SubActivity에 넘겨주기
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //제목, 본문에서 적은 값 받아서 Diary에 넣기
                String title = et3.getText().toString();
                String contents = et4.getText().toString();
                Diary diary = new Diary(title, contents);
                Intent data = new Intent();
                //sharedpreference 값 읽어오기
                SharedPreferences sp = getSharedPreferences("myFile", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();

                getTitle = sp.getString("title", null);
                getContents = sp.getString("contents", null);


                if(index <0) {
                    //int index = data.getIntExtra("index", -1);
                    //1. 등록 버튼 눌렀을 때
                    // 1_1. 첫 등록일 때
                    if (getTitle == null && getContents == null) {
                        editor.putString("title", title);
                        editor.putString("contents", contents);
                        editor.commit();
                        Log.v("what_in_title", sp.getString("title", null));
                        Log.v("what_in_contents", sp.getString("contents", null));
                    }

                    //1_2.첫 번째 이후 등록일 때
                    else if (getTitle != null && getContents != null) {
                        //haredReference에 value에 들어갈 String="a/b/c/..." 형식 만들기

                        newTitle = sp.getString("title", null) + "/" + title;
                        newContents = sp.getString("contents", null) + "/" + contents;
                        editor.putString("title", newTitle);
                        editor.putString("contents", newContents);
                        editor.commit();
                        Log.v("what_in_title", sp.getString("title", null));
                        Log.v("what_in_contents", sp.getString("contents", null));
                    }
                }

                //index>=0 일때, 즉 수정화면일 때
                //2. 리스트 눌러서 수정할 때
                if(index >=0) {
                    data.putExtra("index", index);
                    getTitle = sp.getString("title", null);
                    getContents = sp.getString("contents", null);
                    //2_1. 문자열 배열로 자르기
                    titleList = getTitle.split("/");
                    contentsList = getContents.split("/");

                    //2_2. ArrayList 배열에 넣기
                    for (int i = 0; i < titleList.length; i++) {
                        titlearry.add(titleList[i]);
                    }
                    for (int j = 0; j < contentsList.length; j++) {
                        contentsarry.add(contentsList[j]);
                    }

                    //2_3. 수정할 내용으로 해당 index에 넣기
                    titlearry.set(index, title);
                    contentsarry.set(index, contents);
                    for (int i = 0; i < titlearry.size(); i++) {
                        saveTitle += titlearry.get(i) + "/";
                    }
                    for (int j = 0; j < contentsarry.size(); j++) {
                        saveContents += contentsarry.get(j) + "/";
                    }
                    saveTitle = saveTitle.substring(0, saveTitle.length()-1);
                    saveContents = saveContents.substring(0, saveContents.length()-1);
                    //2_4. sharedPreference에 저장
                    editor.putString("title", saveTitle);
                    editor.putString("contents", saveContents);
                    editor.commit();
                    Log.v("what_uptitle", sp.getString("title", null));
                    Log.v("what_upcontents", sp.getString("contents", null));
                }

                // (등록 / 수정) 일때 다시 SubActivity에 본문, 제목 전달
                data.putExtra("diaryitem", diary);
                data.putExtra("title", title);
                data.putExtra("contents", contents);
                setResult(RESULT_OK, data);

                finish();
            }
        });


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

}