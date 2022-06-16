package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    EditText emailPlayer1, passwordPlayer1, emailPlayer2, passwordPlayer2;
    TextView tvSignUp,tvPlayer1,tvPlayer2;
    DBHelper db;
    Button buttonSignInPlayer1,buttonSignInPlayer2,buttonIsReady1,buttonIsReady2,
                                     buttonPlay, btnPLayAsGuest1,btnPlayAsGuest2;

    boolean player1isReady = false,player2isReady = false;
    private static int AUTH_REQUEST_CODE = 1345;
    private FirebaseAuth mFireBaseAuth;
    private FirebaseAuth.AuthStateListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFireBaseAuth = FirebaseAuth.getInstance();
        db = new DBHelper(this);
        Hooks();

        btnPLayAsGuest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player1isReady = true;
                buttonIsReady1.setText("X");
                buttonIsReady1.setTextColor(Color.parseColor("#FFC34A"));
            }
        });
        btnPlayAsGuest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player2isReady = true;
                buttonIsReady2.setText("O");
                buttonIsReady2.setTextColor(Color.parseColor("#70FFEA"));
            }
        });

        buttonSignInPlayer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailPlayer1.getText().toString();
                String pass = passwordPlayer1.getText().toString();
                if(email.isEmpty()) {
                    emailPlayer1.setError("Please enter e-mail");
                    emailPlayer1.requestFocus();
                }
                if(pass.isEmpty()) {
                    passwordPlayer1.setError("Please enter password");
                    passwordPlayer1.requestFocus();
                }
                if(!(email.isEmpty() && pass.isEmpty())) {
                    mFireBaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(MainActivity.this,"Login error",Toast.LENGTH_SHORT).show();
                            }else{
                                tvPlayer1.setText(email);
                                player1isReady = true;
                                buttonIsReady1.setText("X");
                                buttonIsReady1.setTextColor(Color.parseColor("#FFC34A"));
                                if(db.isPlayerExists(email) == false){
                                    int id = db.getAllPlayers().size();
                                    id +=1;
                                    Player player = new Player(id,email,0);
                                    db.addPlayer(player);
                                }else {
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this,"ERROR OCCURED",Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonSignInPlayer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailPlayer2.getText().toString();
                String pass = passwordPlayer2.getText().toString();
                int id = db.getAllPlayers().size();
                id +=1;
                if(email.isEmpty()) {
                    emailPlayer2.setError("Please enter e-mail");
                    emailPlayer2.requestFocus();
                }
                if(pass.isEmpty()) {
                    passwordPlayer2.setError("Please enter password");
                    passwordPlayer2.requestFocus();
                }
                if(!(email.isEmpty() && pass.isEmpty())) {
                    mFireBaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(MainActivity.this,"Login error",Toast.LENGTH_SHORT).show();
                            }else{
                                tvPlayer2.setText(email);
                                player2isReady = true;
                                buttonIsReady2.setText("O");
                                buttonIsReady2.setTextColor(Color.parseColor("#70FFEA"));
                                if(db.isPlayerExists(email) == false){
                                    int id = db.getAllPlayers().size();
                                    id +=1;
                                    Log.d("PLAYER?","DOES NOT EXISTS");
                                    Player player = new Player(id,email,0);
                                    db.addPlayer(player);
                                }else{
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this,"ERROR OCCURED",Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog1 = new Dialog(MainActivity.this,R.style.Dialoge);
                Window window = dialog1.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.FILL_PARENT,
                        WindowManager.LayoutParams.FILL_PARENT);
                window.setBackgroundDrawable(new ColorDrawable());
                dialog1.setTitle(null);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                dialog1.setContentView(R.layout.activity_sign_up);
                dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                dialog1.show();
                Button btnSignUp = (Button)dialog1.findViewById(R.id.btnSignUpPlayer);
                EditText etMail = dialog1.findViewById(R.id.etMailPlayer);
                EditText etPassword = dialog1.findViewById(R.id.etPasswordPlayer);
                btnSignUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = etMail.getText().toString();
                        String pass = etPassword.getText().toString();
                        int score = 0;
                        if(email.isEmpty()) {
                            etMail.setError("Please enter e-mail");
                            etMail.requestFocus();
                        }
                        if(pass.isEmpty()) {
                            etPassword.setError("Please enter password");
                            etPassword.requestFocus();
                        }
                        if(!(email.isEmpty() && pass.isEmpty())) {
                            mFireBaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this,"Sign Up unsuccesful,please try again",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this,"Sign Up succesful!",Toast.LENGTH_SHORT).show();
                                        dialog1.cancel();

                                    }
                                }
                            });
                        } else {
                            Toast.makeText(MainActivity.this,"ERROR OCCURED",Toast.LENGTH_SHORT).show();
                        }
                    }

                });
                Button btnBack = (Button)dialog1.findViewById(R.id.buttonBack);
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       dialog1.cancel();
                    }
                });
            }
        });
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email1 = tvPlayer1.getText().toString();
                String pass1 = passwordPlayer1.getText().toString();
                String email2 = tvPlayer2.getText().toString();
                String pass2 = passwordPlayer2.getText().toString();
                if(player1isReady & player2isReady){
                    Intent i = new Intent(MainActivity.this,GamePan.class);
                    i.putExtra("nameOne",email1);
                    i.putExtra("nameTwo",email2);
                    startActivity(i);
                    db.close();
                }
            }
        });
    }
    private void Hooks() {
        tvPlayer1 = findViewById(R.id.TVplayer1);
        tvPlayer2 = findViewById(R.id.TVplayer2);
        emailPlayer1 = findViewById(R.id.etMailPlayer1);
        emailPlayer2 = findViewById(R.id.etMailPlayer2);
        passwordPlayer1 = findViewById(R.id.etPasswordPlayer1);
        passwordPlayer2 = findViewById(R.id.etPasswordPlayer2);
        tvSignUp = findViewById(R.id.tvSignUp);
        buttonSignInPlayer1 = findViewById(R.id.btnSignInPlayer1);
        buttonSignInPlayer2 = findViewById(R.id.btnSignInPlayer2);
        buttonIsReady1 = findViewById(R.id.btnPlayer1isOkey);
        buttonIsReady2 = findViewById(R.id.btnPlayer2isOkey);
        btnPLayAsGuest1 = findViewById(R.id.btnPlayAsAGuestPlayer1);
        btnPlayAsGuest2 = findViewById(R.id.btnPlayAsAGuestPlayer2);
        buttonPlay = findViewById(R.id.btnPlayGame);
        tvSignUp = findViewById(R.id.tvSignUp);
    }
}