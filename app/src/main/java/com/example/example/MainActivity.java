package com.example.example;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText id, pw;
    Button btn;
    String loginID, loginPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = findViewById(R.id.editText1);
        pw = findViewById(R.id.editText2);
        btn = findViewById(R.id.button1);

        //로그인 정보 저장하기
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        //처음에는 SharedPreferences에 아무런 정보도 없으므로 값을 저장할 키들을 생성한다.
        // getString의 첫 번째 인자는 저장될 키, 두 번쨰 인자는 값입니다.
        // 첨엔 값이 없으므로 키값은 원하는 것으로 하고 값을 null을 줍니다.
        loginID = auto.getString("editText1", null);
        loginPW = auto.getString("editText2", null);

        //자동로그인
        if (loginID != null && loginPW != null) {
            if (loginID.equals("kst") && loginPW.equals("0000")) {
                Toast.makeText(MainActivity.this, loginID + "자동로그인입니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                startActivity(intent);
                finish();
            }
        }  else if (loginID == null && loginPW == null) {
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (id.getText().toString().equals("kst") && pw.getText().toString().equals("0000")) {
                            SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor autoLogin = auto.edit();
                            autoLogin.putString("editText1", id.getText().toString());
                            autoLogin.putString("editText2", pw.getText().toString());
                            autoLogin.commit();
                            Toast.makeText(MainActivity.this, "환영합니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, SubActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "ID/PW 확인하세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }


