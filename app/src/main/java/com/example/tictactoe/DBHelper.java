package com.example.tictactoe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper  extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FOR_ANDRDSTD";


    private static final String TABLE_NAME = "Players";
    private static final String Key_Name = "Name";
    private static final String Key_Id = "Id";
    private static final String Score = "Score";

    public DBHelper(@Nullable Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME + "(" +
                Key_Id + " INTEGER PRIMARY KEY," + Key_Name + " TEXT," +
                Score +" INTEGER" + ")";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean isPlayerExists(String name)
    {
        Log.d("debug","debug");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{Key_Id,Key_Name,Score},Key_Name+ " =?",
                new String[]{String.valueOf(name)},null,null,null,null);

        if( (cursor != null) && (cursor.moveToFirst()) ){
            Log.d("messege",cursor.getInt(0)+"/"+ cursor.getString(1)+"/"+cursor.getString(2));
            return true;
        }
        return false;
    }

    public Player getPlayer(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{Key_Id,Key_Name,Score},Key_Id+ " =?",
                new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor != null)
            cursor.moveToFirst();
        return new Player(cursor.getInt(0),cursor.getString(1),cursor.getInt(2));
    }

    public void addPlayer(Player contact)
    {
        Log.d("success","player added");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Key_Id, contact.getId());
        values.put(Key_Name, contact.getName());
        values.put(Score, contact.getScore());
        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public int updatePlayer(Player player)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Key_Name, player.getName());
        values.put(Score, player.getScore());
        return db.update(TABLE_NAME,values, Key_Name + " =?",new String[]{String.valueOf(player.getName())});
    }

    public int getId(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = db.query(TABLE_NAME, new String[]{Key_Id,Key_Name,Score},Key_Name+ " =?",
                new String[]{String.valueOf(name)},null,null,null,null);
        if(cursor != null) {
            cursor.moveToFirst();
            int id = cursor.getInt(0);
            return id;
        }
        else{
        }
        return 0;
    }


    public List<Player> getAllPlayers() {

        List<Player> lstPlayers = new ArrayList<>();
        String selectQuerry = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuerry, null);

        if (cursor.moveToFirst()) {
            do {
                Player player = new Player();
                player.setId(cursor.getInt(0));
                player.setName(cursor.getString(1));
                player.setScore(cursor.getInt(2));
                lstPlayers.add(player);
            }
            while (cursor.moveToNext());
        }
        return lstPlayers;
    }

}
