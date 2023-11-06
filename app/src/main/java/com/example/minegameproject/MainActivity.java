package com.example.minegameproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TableLayout table;
        table = (TableLayout) findViewById(R.id.tableLayout); //xml에 만들어져있던 TableLayout과 연결

        BlockButton[][] buttons = new BlockButton[9][9]; // 9x9 버튼 배열 동적 생성

        for (int i = 0; i < 9; i++) {
            TableRow tableRow = new TableRow(this);
            table.addView(tableRow); // 9개의 TableRow를 동적 생성해서 TableLayout에 삽입
            for (int j = 0; j < 9; j++) {
                buttons[i][j] = new BlockButton(this, i, j); // 각 버튼 배열에 버튼 삽입
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        1.0f);
                buttons[i][j].setLayoutParams(layoutParams);
                tableRow.addView(buttons[i][j]);

                // breakBlock 메서드 테스트
//                buttons[i][j].setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        ((BlockButton)view).breakBlock(view);
//                    }
//                });

                // toggleFlag 메서드 테스트
//                buttons[i][j].setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        ((BlockButton)view).toggleFlag();
//                    }
//                });
            }
        }
        int totalMines = 10; // 지뢰 10개
        Random random = new Random();
        while (totalMines > 0) { // 지뢰 10개 배치
            int randomX = random.nextInt(9); // 9는 X 좌표의 최대 범위
            int randomY = random.nextInt(9); // 9는 Y 좌표의 최대 범위
            if (!buttons[randomX][randomY].mine) {
                buttons[randomX][randomY].mine = true;
                totalMines--;
            }
        }


    }
}

class BlockButton extends Button {
    int x, y; // 버튼 위치
    boolean mine; // 지뢰인지 아닌지
    boolean flag; // 깃발이 꽂혔는지
    int neighborMines; // 블록 주변의 지뢰 수
    static int flags; // 깃발이 꽂힌 블록 수
    static int blocks; // 남은 블록 수
    public BlockButton(Context context, int x, int y) { // 생성자
        super(context);
        mine = false;
        flag = false;
        neighborMines = 0;
        flags = 0;
        blocks = 0;
        blocks++;
        // 버튼의 레이아웃 파라미터를 설정
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        setLayoutParams(layoutParams);
    }
    public void toggleFlag() { // 깃발 꽂기 or 해제 메소드
        if(flag == false) {
            flag = true;
            setText("+");
            flags++;
        }
        else if(flag == true) {
            flag = false;
            setText(" ");
            flags--;
        }
    }
    public boolean breakBlock(View view) { // 블록을 여는 메소드
        setClickable(false);
        blocks--;
        if(mine == true) { // 블록을 열었는데 지뢰가 있으면 'M'으로 표시
            setText("M");
            return true;
        }
        else { // 블록을 열었는데 지뢰가 없으면 주변 지뢰 수로 표시
            setText(neighborMines);
            setBackgroundColor(Color.WHITE);
            return false;
        }
    }
}



