package com.example.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class PlayerAdapter extends BaseAdapter {
    Activity activity;
    List<Player> lstPlayers;
    LayoutInflater inflater;
    EditText edtId,edtName, edtScore;

    public PlayerAdapter() {
    }
    public PlayerAdapter(Activity activity, List<Player> lstPlayers,EditText edtId, EditText edtName, EditText edtScore) {
        this.activity = activity;
        this.lstPlayers = lstPlayers;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.edtId = edtId;
        this.edtName = edtName;
        this.edtScore = edtScore;

    }
    @Override
    public int getCount() {
        return lstPlayers.size();
    }

    @Override
    public Object getItem(int i) {
        return lstPlayers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return lstPlayers.get(i).getId();
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView;
        rowView = inflater.inflate(R.layout.row,null);
        final TextView txtId,txtName,txtScore;
        //txtId = (TextView)rowView.findViewById(R.id.txtPlace);
        txtName = (TextView)rowView.findViewById(R.id.txtViewUserNameRow);
        txtScore = (TextView)rowView.findViewById(R.id.txtViewScore);

       // txtId.setText(""+lstPlayers.get(i).getId());
        txtName.setText(""+lstPlayers.get(i).getName());
        txtScore.setText(""+lstPlayers.get(i).getScore());

      //  String message = txtId.getText().toString();
       // Log.d("id",message);

        return rowView;
    }
}
