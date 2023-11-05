package com.example.minegameproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TableLayout table;
        table = (TableLayout) findViewById(R.id.tableLayout); //xml에 만들어져있던 TableLayout과 연결

        Button[][] buttons = new Button[9][9]; // 9x9 버튼 배열 동적 생성

        for(int i = 0; i < 9; i++) {
            TableRow tableRow = new TableRow(this);
            table.addView(tableRow); // 9개의 TableRow를 동적 생성해서 TableLayout에 삽입
            for(int j = 0; j < 9; j++) {
                buttons[i][j] = new Button(this); // 각 버튼 배열에 버튼 삽입
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        1.0f);
                buttons[i][j].setLayoutParams(layoutParams);
                tableRow.addView(buttons[i][j]);
            }
        }
        public class BlockButton extends Button {
            int x, y; // 버튼 위치
            boolean mine; // 지뢰인지 아닌지
            boolean flag; // 깃발이 꽂혔는지
            int neighborMines; // 블록 주변의 지뢰 수
            static int flags; // 깃발이 꽂힌 블록 수
            static int blocks; // 남은 블록 수
            public void toggleFlag() { // 깃발 꽂기 or 해제 메소드
                if()
            }
            public boolean breakBlock() { // 블록을 여는 메소드
                setClickable(false);
                blocks--;
                if(mine) {
                    setText("M");
                    return true;
                }
                else {
                    setText(neighborMines);
                    setBackgroundColor(Color.WHITE);
                    return false;
                }
            }
            public BlockButton(Context context, int x, int y) {
                super(context);
            }

        }
    }
}




