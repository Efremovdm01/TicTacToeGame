package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GamePan extends AppCompatActivity implements View.OnClickListener {
    private TextView playerOneScore, playerTwoScore, playerStatus, playerOneName,playerTwoName;
    private Button[] buttons = new Button[9];
    private Button resetGame,btnScoreBoard,btnBackToLogin;
    String nameOne,nameTwo;
    boolean nameOneisGuest,nameTwoisGuest;
    private int playerOneScoreCount, playerTwoScoreCount,roundCount;

    boolean activePlayer;
    DBHelper db;

    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};

    int[][] winningPositions = {
            {0,1,2},{3,4,5},{6,7,8},//rows
            {0,3,6},{1,4,7},{2,5,8},//columns
            {0,4,8},{2,4,6}//cross
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_pan);
        db = new DBHelper(this);
        Hooks();
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            nameOne = arguments.getString("nameOne");
            nameTwo = arguments.getString("nameTwo");
        }
        if(nameOne.equals("Player 1")){
            nameOneisGuest = true;
            //Log.d("nameOne","True");
        }
        if(nameTwo.equals("Player 2")){
            nameOneisGuest = true;
            //Log.d("nameTwo","True");
        }
        playerOneName.setText(nameOne);
        playerTwoName.setText(nameTwo);

        for (int i = 0; i < buttons.length; i++) {
            String buttonID = "btn_" + i;
            int resourceID = getResources().getIdentifier(buttonID, "id",getPackageName());
            buttons[i]=(Button) findViewById(resourceID);
            buttons[i].setOnClickListener(this);
        }

        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GamePan.this,MainActivity.class);
                startActivity(i);
            }
        });
        btnScoreBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playerOneScoreCount = 0;
                playerTwoScoreCount = 0;
                playerStatus.setText("");
                updatePlayerScore();
                Intent i = new Intent(GamePan.this,ScoreBoard.class);
                i.putExtra("nameOne",playerOneName.getText().toString());
                i.putExtra("nameTwo",playerTwoName.getText().toString());
                playAgain();
                startActivity(i);
            }
        });
        roundCount =0;
        playerOneScoreCount = 0;
        playerTwoScoreCount = 0;
        activePlayer = true;
    }
    @Override
    public void onClick(View v) {
        if (!((Button)v).getText().toString().equals("")){
            return;
        }
        String buttonID = v.getResources().getResourceEntryName(v.getId());
        int gameStatePointer = Integer.parseInt(buttonID.substring(buttonID.length() -1, buttonID.length()));

        if(activePlayer){
            ((Button) v).setText("X");
            ((Button) v).setTextColor(Color.parseColor("#FFC34A"));
            gameState[gameStatePointer] = 0;
        } else{
            ((Button) v).setText("O");
            ((Button) v).setTextColor(Color.parseColor("#70FFEA"));
            gameState[gameStatePointer] = 1;
        }
        roundCount++;
        if(roundCount ==9){
            playAgain();
            Log.d("roundcount","STOP");
            playerStatus.setBackgroundColor(getResources().getColor(R.color.white));
            playerStatus.setText("");
            Toast.makeText(GamePan.this, "No Winner!", Toast.LENGTH_SHORT).show();
        }else{
            activePlayer = !activePlayer;
        }
        if(checkWinner()){
            if (!activePlayer){
                playerOneScoreCount++;
                updatePlayerScore();
                Toast.makeText(GamePan.this, nameOne+" Won!", Toast.LENGTH_SHORT).show();
                if(!(nameOneisGuest || nameTwoisGuest)){
                    int id1 = db.getId(playerOneName.getText().toString());
                    int score1 = db.getPlayer(id1).getScore() + playerOneScoreCount;
                    Player player1 = new Player(id1,playerOneName.getText().toString(),score1);
                    playAgain();
                    db.updatePlayer(player1);
                    playerStatus.setText(nameOne + " is Winnig");
                    playerStatus.setBackgroundColor(Color.parseColor("#FFC34A"));
                }else {
                    playAgain();
                    playerStatus.setText(nameOne + " is Winnig");
                    playerStatus.setBackgroundColor(Color.parseColor("#FFC34A"));
                }
            }else {
                playerTwoScoreCount++;
                updatePlayerScore();
                Log.d("roundcount",""+roundCount);
                Toast.makeText(GamePan.this, nameTwo+" Won!", Toast.LENGTH_SHORT).show();
                if(!(nameOneisGuest || nameTwoisGuest)){
                    int id2 = db.getId(playerOneName.getText().toString());
                    int score2 = db.getPlayer(id2).getScore() + playerOneScoreCount;
                    Player player2 = new Player(id2,playerOneName.getText().toString(),score2);
                    playAgain();
                    db.updatePlayer(player2);
                    playerStatus.setText(nameTwo + " is Winnig");
                    playerStatus.setBackgroundColor(Color.parseColor("#70FFEA"));
                }else {
                    playAgain();
                    playerStatus.setText(nameTwo + " is Winnig");
                    playerStatus.setBackgroundColor(Color.parseColor("#70FFEA"));
                }
            }
        }

        resetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAgain();
                playerOneScoreCount = 0;
                playerTwoScoreCount = 0;
                playerStatus.setText("");
                updatePlayerScore();
                playerStatus.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

    }
    private void Hooks() {
        playerOneScore = (TextView) findViewById(R.id.playerOneScore);
        playerTwoScore = (TextView) findViewById(R.id.playerTwoScore);
        playerStatus = (TextView) findViewById(R.id.playerStatus);
        playerOneName = (TextView) findViewById(R.id.TVplayerOne);
        playerTwoName = (TextView) findViewById(R.id.TVplayerTwo);
        resetGame = (Button) findViewById(R.id.resetGame);
        btnScoreBoard = (Button) findViewById(R.id.ScoreBoard);
        btnBackToLogin = (Button) findViewById(R.id.btnBacktoLogin);
    }


    public boolean checkWinner(){
        boolean winnerResult = false;

        for (int [] winningPos : winningPositions) {
            if(gameState[winningPos[0]] == gameState[winningPos[1]]
                    && gameState[winningPos[1]]==gameState[winningPos[2]]
                    && gameState[winningPos[0]]!=2){
                winnerResult = true;
            }
        }
        return winnerResult;
    }

    public void updatePlayerScore(){
        playerOneScore.setText(Integer.toString(playerOneScoreCount));
        playerTwoScore.setText(Integer.toString(playerTwoScoreCount));
    }

    public void playAgain(){
        roundCount=0;
        playerOneScoreCount = 0;
        playerTwoScoreCount = 0;
        activePlayer = true;
        for (int i = 0; i < buttons.length; i++) {
            gameState[i]=2;
            buttons[i].setText("");
        }
    }
}