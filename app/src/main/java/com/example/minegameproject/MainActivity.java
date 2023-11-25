package com.example.minegameproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TableLayout table = (TableLayout) findViewById(R.id.tableLayout); //xml에 만들어져있던 TableLayout과 연결
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
                // toggleFlag 메서드 테스트 Code
//                buttons[i][j].setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        ((BlockButton)view).toggleFlag();
//
//                        TextView textView = (TextView) findViewById(R.id.textViewMines);
//                        textView.setText("Mines: " + String.valueOf(10 - BlockButton.flags));
//                    }
//                });

                // breakBlock 메서드 테스트 Code
//                buttons[i][j].setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        ((BlockButton)view).breakBlock(view);
//                    }
//                });
            }
        }

        int totalMines = 10; // 총 지뢰 개수 10개
        Random random = new Random();
        while (totalMines > 0) { // 지뢰 10개 배치
            int randomX = random.nextInt(9); // 9는 X 좌표의 최대 범위
            int randomY = random.nextInt(9); // 9는 Y 좌표의 최대 범위
            if (!buttons[randomX][randomY].mine) { // 랜덤으로 선택된 버튼에 지뢰가 없으면, 지뢰 배치
                buttons[randomX][randomY].mine = true;
                totalMines--;

                if ((randomX == 0) && (randomY == 0)) { // (0, 0)일때
                    for (int i = 0; i <= 1; i++) {
                        for (int j = 0; j <= 1; j++) {
                            buttons[randomX + i][randomY + j].neighborMines++;
                        }
                    }
                    buttons[randomX][randomY].neighborMines--;
                } else if ((randomX == 0) && (randomY == 8)) { // (0, 8)일때
                    for (int i = 0; i <= 1; i++) {
                        for (int j = -1; j <= 0; j++) {
                            buttons[randomX + i][randomY + j].neighborMines++;
                        }
                    }
                    buttons[randomX][randomY].neighborMines--;
                } else if ((randomX == 8) && (randomY == 0)) { // (8, 0)일때
                    for (int i = -1; i <= 0; i++) {
                        for (int j = 0; j <= 1; j++) {
                            buttons[randomX + i][randomY + j].neighborMines++;
                        }
                    }
                    buttons[randomX][randomY].neighborMines--;
                } else if ((randomX == 8) && (randomY == 8)) { // (8, 8)일때
                    for (int i = -1; i <= 0; i++) {
                        for (int j = -1; j <= 0; j++) {
                            buttons[randomX + i][randomY + j].neighborMines++;
                        }
                    }
                    buttons[randomX][randomY].neighborMines--;
                } else if ((randomX == 0) && (randomY >= 1 && randomY <= 7)) { // (0, 1~7)일때
                    for (int i = 0; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            buttons[randomX + i][randomY + j].neighborMines++;
                        }
                    }
                    buttons[randomX][randomY].neighborMines--;
                } else if ((randomX == 8) && (randomY >= 1 && randomY <= 7)) { // (8, 1~7)일때
                    for (int i = -1; i <= 0; i++) {
                        for (int j = -1; j <= 1; j++) {
                            buttons[randomX + i][randomY + j].neighborMines++;
                        }
                    }
                    buttons[randomX][randomY].neighborMines--;
                } else if ((randomX >= 1 && randomX <= 7) && (randomY == 0)) { // (1~7, 0)일때
                    for (int i = -1; i <= 1; i++) {
                        for (int j = 0; j <= 1; j++) {
                            buttons[randomX + i][randomY + j].neighborMines++;
                        }
                    }
                    buttons[randomX][randomY].neighborMines--;
                } else if ((randomX >= 1 && randomX <= 7) && (randomY == 8)) { // (1~7, 8)일때
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 0; j++) {
                            buttons[randomX + i][randomY + j].neighborMines++;
                        }
                    }
                    buttons[randomX][randomY].neighborMines--;
                } else if ((randomX >= 1 && randomX <= 7) && (randomY >= 1 && randomY <= 7)) { // 주변 블록이 8개인 경우
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            buttons[randomX + i][randomY + j].neighborMines++;
                        }
                    }
                    buttons[randomX][randomY].neighborMines--;
                }
            }
        }

        // 토글버튼 체크 상태에 따라서 블록 클릭 리스너 다르게 작동
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int finalI = i;
                int finalJ = j;
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (toggleButton.isChecked()) { // Flag 모드일 때
                            ((BlockButton)view).toggleFlag();

                            TextView textView = findViewById(R.id.textViewMines);
                            textView.setText("Mines: " + String.valueOf(10 - BlockButton.flags));
                        }
                        else { // Uncover 모드일 때
                            if(buttons[finalI][finalJ].flag == false) { // 깃발이 안 꽂혀 있는 자리만 Uncover 가능
                                ((BlockButton)view).breakBlock(view);
                            }
                        }
                    }
                });
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
    boolean isToggle; // 토글버튼이 눌려있는지(깃발 모드인지)
    public BlockButton(Context context, int x, int y) { // 생성자
        super(context);
        mine = false;
        flag = false;
        neighborMines = 0;
        flags = 0;
        blocks = 0;
        blocks++;
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams( // 버튼의 레이아웃 파라미터를 설정
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
            setText(String.valueOf(neighborMines));
            setBackgroundColor(Color.WHITE);
            return false;
        }
    }
}
