package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScoreBoard extends AppCompatActivity {
    DBHelper db;
    List<Player> data = new ArrayList<>();
    ListView lstPlayers;
    EditText edtId,edtName,edtScore;
    Button btback;
    String nameOne,nameTwo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        db = new DBHelper(this);
        Hooks();
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            nameOne = arguments.getString("nameOne");
            nameTwo = arguments.getString("nameTwo");
        }

        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ScoreBoard.this,GamePan.class);
                i.putExtra("nameOne",nameOne);
                i.putExtra("nameTwo",nameTwo);
                startActivity(i);
            }
        });
        refreshData();
        int size = data.size();

    }
    private void refreshData()
    {
        data = db.getAllPlayers();
        Collections.sort(data, new AgeComparator());
        PlayerAdapter adapter = new PlayerAdapter(ScoreBoard.this,data,edtId,edtName,edtScore);
        lstPlayers.setAdapter(adapter);
    }
    private  void Hooks(){
        lstPlayers = (ListView) findViewById(R.id.lstPlayers);

        btback = findViewById(R.id.btnback);
    }
}
class AgeComparator implements Comparator<Player> {
    @Override
    public int compare(Player a, Player b) {
        return a.getScore() > b.getScore() ? -1 : a.getScore() == b.getScore() ? 0 : 1;
    }
}