package com.example.minegameproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private BlockButton[][] buttons; // buttons 배열 변수를 전역 변수로 선언
    private long startTime;
    private boolean timerRunning = false;
    private Handler handler;
    private TextView timerTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();
        timerTextView = findViewById(R.id.textViewTime);
        TableLayout table = (TableLayout) findViewById(R.id.tableLayout); //xml에 만들어져있던 TableLayout과 연결
        buttons = new BlockButton[9][9]; // 9x9 버튼 배열 동적 생성

        for(int i = 0; i < 9; i++) {
            TableRow tableRow = new TableRow(this);
            table.addView(tableRow); // 9개의 TableRow를 동적 생성해서 TableLayout에 삽입
            tableRow.setBackgroundColor(Color.rgb(129, 193, 71));
            for(int j = 0; j < 9; j++) {
                buttons[i][j] = new BlockButton(this, i, j); // 각 버튼 배열에 버튼 삽입
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        1.0f);
                buttons[i][j].setLayoutParams(layoutParams);
                tableRow.addView(buttons[i][j]);
            }
        }

        int totalMines = 10; // 총 지뢰 개수 10개
        Random random = new Random();
        while(totalMines > 0) { // 지뢰 10개 배치
            int randomX = random.nextInt(9); // 9는 X 좌표의 최대 범위
            int randomY = random.nextInt(9); // 9는 Y 좌표의 최대 범위
            if(!buttons[randomX][randomY].mine) { // 랜덤으로 선택된 버튼에 지뢰가 없으면
                buttons[randomX][randomY].mine = true; // 지뢰 배치
                totalMines--;

                if((randomX == 0) && (randomY == 0)) { // (0, 0)일때
                    for(int i = 0; i <= 1; i++) {
                        for(int j = 0; j <= 1; j++) {
                            buttons[randomX + i][randomY + j].neighborMines++;
                        }
                    }
                    buttons[randomX][randomY].neighborMines--;
                } else if((randomX == 0) && (randomY == 8)) { // (0, 8)일때
                    for(int i = 0; i <= 1; i++) {
                        for(int j = -1; j <= 0; j++) {
                            buttons[randomX + i][randomY + j].neighborMines++;
                        }
                    }
                    buttons[randomX][randomY].neighborMines--;
                } else if((randomX == 8) && (randomY == 0)) { // (8, 0)일때
                    for(int i = -1; i <= 0; i++) {
                        for(int j = 0; j <= 1; j++) {
                            buttons[randomX + i][randomY + j].neighborMines++;
                        }
                    }
                    buttons[randomX][randomY].neighborMines--;
                } else if((randomX == 8) && (randomY == 8)) { // (8, 8)일때
                    for(int i = -1; i <= 0; i++) {
                        for(int j = -1; j <= 0; j++) {
                            buttons[randomX + i][randomY + j].neighborMines++;
                        }
                    }
                    buttons[randomX][randomY].neighborMines--;
                } else if((randomX == 0) && (randomY >= 1 && randomY <= 7)) { // (0, 1~7)일때
                    for(int i = 0; i <= 1; i++) {
                        for(int j = -1; j <= 1; j++) {
                            buttons[randomX + i][randomY + j].neighborMines++;
                        }
                    }
                    buttons[randomX][randomY].neighborMines--;
                } else if((randomX == 8) && (randomY >= 1 && randomY <= 7)) { // (8, 1~7)일때
                    for(int i = -1; i <= 0; i++) {
                        for(int j = -1; j <= 1; j++) {
                            buttons[randomX + i][randomY + j].neighborMines++;
                        }
                    }
                    buttons[randomX][randomY].neighborMines--;
                } else if((randomX >= 1 && randomX <= 7) && (randomY == 0)) { // (1~7, 0)일때
                    for(int i = -1; i <= 1; i++) {
                        for(int j = 0; j <= 1; j++) {
                            buttons[randomX + i][randomY + j].neighborMines++;
                        }
                    }
                    buttons[randomX][randomY].neighborMines--;
                } else if((randomX >= 1 && randomX <= 7) && (randomY == 8)) { // (1~7, 8)일때
                    for(int i = -1; i <= 1; i++) {
                        for(int j = -1; j <= 0; j++) {
                            buttons[randomX + i][randomY + j].neighborMines++;
                        }
                    }
                    buttons[randomX][randomY].neighborMines--;
                } else if((randomX >= 1 && randomX <= 7) && (randomY >= 1 && randomY <= 7)) { // 주변 블록이 8개인 경우
                    for(int i = -1; i <= 1; i++) {
                        for(int j = -1; j <= 1; j++) {
                            buttons[randomX + i][randomY + j].neighborMines++;
                        }
                    }
                    buttons[randomX][randomY].neighborMines--;
                }
            }
        }

        // 토글버튼 체크 상태에 따라서 블록 온클릭 리스너 다르게 작동
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                int finalI = i;
                int finalJ = j;
                buttons[i][j].setOnClickListener(new View.OnClickListener() { // 각 버튼들의 클릭리스너 지정
                    @Override
                    public void onClick(View view) {
                        if(toggleButton.isChecked()) { // Flag 모드일 때
                            ((BlockButton)view).toggleFlag(); // toggleFlag() 실행
                            TextView textView = findViewById(R.id.textViewMines);
                            textView.setText("Mines: " + String.valueOf(10 - BlockButton.flags)); // 남은 지뢰수 반영
                            if(checkNoRemainBlocks()) { // 만약에 남은 블록이 없다면
                                showWinAlertDialog();
                                stopTimer();
                                timerRunning = false;
                                setToggleButtonUnClickable();
                                setAllButtonUnClickable();
                            }
                        } else if(!toggleButton.isChecked()) { // Uncover 모드일 때
                            if(buttons[finalI][finalJ].flag == false) { // 깃발이 안 꽂혀 있는 버튼일 경우만 Uncover 가능
                                if(!timerRunning) { // 첫 버튼 클릭 시 타이머 실행
                                    startTime = SystemClock.elapsedRealtime();
                                    startTimer();
                                    timerRunning = true;
                                }
                                breakBlock(finalI, finalJ);
                            }
                        }
                    }
                });
            }
        }
    }
    private void breakBlock(int x, int y) { // 버튼을 여는 메소드
        BlockButton clickedButton = buttons[x][y];
        clickedButton.setClickable(false); // 버튼 안 눌리게 설정
        BlockButton.blocks--; // 전체 버튼 수 차감

        if(clickedButton.mine) { // 버튼을 클릭했는데 지뢰가 있으면
            clickedButton.setText("\uD83D\uDCA3"); // 지뢰 이모티콘으로 표시
            showLoseAlertDialog();
            setAllButtonUnClickable();
            stopTimer();
            timerRunning = false;
            setToggleButtonUnClickable();
        } else if(BlockButton.blocks == 0) { // 더 이상 남은 버튼이 없으면
            stopTimer();
            timerRunning = false;
            setToggleButtonUnClickable();
            setAllButtonUnClickable();
            showWinAlertDialog();
        } else { // 버튼을 클릭했는데 지뢰가 없으면 주변 지뢰 수로 표시
            if(clickedButton.neighborMines == 0) { // 클릭한 버튼의 주변 지뢰 수가 0이면
                openSurroundingBlocks(x, y); // 주변 버튼도 오픈
            } else { // 클릭한 버튼의 주변 지뢰 수가 0이 아니면
                clickedButton.setText(String.valueOf(clickedButton.neighborMines)); // 클릭한 버튼에 주변 지뢰 수 표시
                clickedButton.setTypeface(null, Typeface.BOLD);
                if(clickedButton.neighborMines == 1) {
                    clickedButton.setTextColor(Color.BLUE);
                } else if(clickedButton.neighborMines == 2) {
                    clickedButton.setTextColor(Color.RED);
                } else if(clickedButton.neighborMines == 3) {
                    clickedButton.setTextColor(Color.YELLOW);
                } else if(clickedButton.neighborMines == 4) {
                    clickedButton.setTextColor(Color.MAGENTA);
                } else if(clickedButton.neighborMines == 5) {
                    clickedButton.setTextColor(Color.GREEN);
                } else if(clickedButton.neighborMines == 6) {
                    clickedButton.setTextColor(Color.CYAN);
                } else if(clickedButton.neighborMines == 7) {
                    clickedButton.setTextColor(Color.DKGRAY);
                } else if(clickedButton.neighborMines == 8) {
                    clickedButton.setTextColor(Color.WHITE);
                }
            }
            clickedButton.setBackgroundColor(Color.rgb(192, 192, 192));
        }
    }
    private void setToggleButtonUnClickable() { // 특정 버튼 Unclickable 지정
        ToggleButton toggleButton;
        toggleButton = findViewById(R.id.toggleButton);
        toggleButton.setClickable(false);
    }
    private void openSurroundingBlocks(int x, int y) { // 주변 버튼 열기
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newX = x + i;
                int newY = y + j;
                if (checkValidBlock(newX, newY) && buttons[newX][newY].isClickable()) { // 해당 버튼의 좌표가 유효한 범위 내에 있고, 클릭 되지 않은 상태면
                    if(buttons[newX][newY].neighborMines != 0) { // 해당 버튼의 주변 지뢰 수가 0이 아닐때
                        breakBlock(newX, newY); // 해당 버튼만 열기
                    } else if(buttons[newX][newY].neighborMines == 0) { // 해당 버튼의 주변 지뢰 수가 0일때
                        breakBlock(newX, newY); // 해당 버튼 열고
                        openSurroundingBlocks(newX, newY); // 재귀 호출로 주변 버튼 열기
                    }
                }
            }
        }
    }
    private boolean checkValidBlock(int x, int y) { // 버튼의 (x, y)좌표의 범위는 (0~8, 0~8)이어야 함
        return x >= 0 && x < 9 && y >= 0 && y < 9;
    }
    private void showWinAlertDialog() { // 승리 AlertDialog
        long elapsedTime = SystemClock.elapsedRealtime() - startTime;
        int minutes = (int) (elapsedTime / 60000);
        int seconds = (int) (elapsedTime % 60000 / 1000);
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("10개의 지뢰를 모두 피했습니다! \uD83E\uDD29\nClear Time - " + timeFormatted)
                .setTitle("Congratulations!")
                .setPositiveButton("재도전", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                })
                .setNegativeButton("게임종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showLoseAlertDialog() { // 패배 AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this) // 게임 종료를 알리는 AlertDialog
                .setMessage("지뢰를 눌렀습니다! \uD83D\uDE2D")
                .setTitle("Game Over!")
                .setPositiveButton("재도전", new DialogInterface.OnClickListener() { // 앱 재시작
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                })
                .setNeutralButton("모든 지뢰 보기", new DialogInterface.OnClickListener() { // 앱 종료
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showAllMine();
                    }
                })
                .setNegativeButton("게임종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showAllMine() { // 모든 지뢰 위치 보여주기
        for(int a = 0; a < 9; a++) {
            for(int b = 0; b < 9; b++) {
                if(buttons[a][b].mine == true) {
                    buttons[a][b].setText("\uD83D\uDCA3");
                }
            }
        }
        makeRestartButton();
    }
    private void setAllButtonUnClickable() { // 모든 버튼 클릭 안되게
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                buttons[i][j].setClickable(false);
            }
        }
    }
    private void startTimer() { // 타이머 시작 후 동적으로 타이머 갱신
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                long elapsedTime = SystemClock.elapsedRealtime() - startTime;
                int minutes = (int) (elapsedTime / 60000);
                int seconds = (int) (elapsedTime % 60000 / 1000);
                String timeFormatted = String.format("%02d:%02d", minutes, seconds);
                timerTextView.setText("Time: " + timeFormatted);
                startTimer(); // 자기 자신을 재귀호출하여 일정 시간마다 업데이트
            }
        }, 1000); // 1초마다 업데이트
    }
    private void stopTimer() { // 타이머 종료
        handler.removeCallbacksAndMessages(null);
    }
    private boolean checkNoRemainBlocks() { // 남은 버튼 아예 없는지 확인
        if(BlockButton.blocks == 0) {
            return true;
        } else {
            return false;
        }
    }
    private void makeRestartButton() { // 재시작 버튼 동적 생성 함수
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
        Button restartButton = new Button(this);
        restartButton.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        restartButton.setText("재도전");
        restartButton.setTypeface(null, Typeface.BOLD);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.topToBottom = findViewById(R.id.toggleButton).getId(); // 토글 버튼의 아래에 위치하도록 설정
        restartButton.setLayoutParams(layoutParams);

        constraintLayout.addView(restartButton);
    }
}