package com.example.minegameproject;

import android.content.Context;
import android.widget.TableLayout;

public class BlockButton extends androidx.appcompat.widget.AppCompatButton {
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
        blocks = 81;
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams( // 버튼의 레이아웃 파라미터를 설정
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        setLayoutParams(layoutParams);
    }
    public void toggleFlag() { // 깃발 꽂기 or 해제 메소드
        if(!flag && flags < 10) { // 깃발이 없고, 꽂을 수 있는 깃발이 남아 있을때
            flag = true;
            setText("\uD83D\uDEA9"); // 깃발 이모티콘으로 표시
            flags++;
            blocks--;
        } else if(flag) { // 깃발이 꽂혀 있을때
            flag = false;
            setText(" "); // 공백 문자로 표시
            flags--;
            blocks++;
        }
    }
}