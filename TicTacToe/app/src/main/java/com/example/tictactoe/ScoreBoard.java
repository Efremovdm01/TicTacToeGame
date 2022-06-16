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
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            nameOne = arguments.getString("nameOne");
            nameTwo = arguments.getString("nameTwo");
        }

        lstPlayers = (ListView) findViewById(R.id.lstPlayers);

        btback = findViewById(R.id.btnback);

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
        //Player first = data.get(0);
        //Player second = data.get(1);
        int size = data.size();
        Log.d("HMMMMMMM","size" + data.size());
        //Log.d("HMMMMMMM",second.getName());
    }
    private void refreshData()
    {
        data = db.getAllPlayers();
        Collections.sort(data, new ScoreComparator());
        PlayerAdapter adapter = new PlayerAdapter(ScoreBoard.this,data,edtId,edtName,edtScore);
        lstPlayers.setAdapter(adapter);
    }
}
class ScoreComparator implements Comparator<Player> {
    @Override
    public int compare(Player a, Player b) {
        return a.getScore() > b.getScore() ? -1 : a.getScore() == b.getScore() ? 0 : 1;
    }
}