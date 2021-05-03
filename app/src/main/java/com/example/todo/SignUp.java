package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {
    //MainActivity mActivity = new MainActivity();
    protected Button btnSignUp;
    protected TextView btnGoToLogin;
    protected EditText signUpUsername, signUpPassword;



    protected void CloseThisActivity() {
        finishActivity(200);
        Intent intent = new Intent(SignUp.this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnGoToLogin = findViewById(R.id.goToLogin);
        signUpUsername = findViewById(R.id.editUsernameSignUp);
        signUpPassword = findViewById(R.id.editPasswordSignUp);
        

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = null;
                try {
                    db = SQLiteDatabase.openOrCreateDatabase(
                            getFilesDir().getPath() + "/" + "todoDb.db",
                            null
                    );

                    String username = signUpUsername.getText().toString();
                    String password = signUpPassword.getText().toString();

                    String query = "INSERT INTO USERS (username, password) ";
                    query += "VALUES(?, ?); ";

                    db.execSQL(query, new Object[] {username, password});

                    Toast.makeText(getApplicationContext(),
                            "Successful sign up!",
                            Toast.LENGTH_LONG).show();

                } catch (Exception err){
                    Toast.makeText(getApplicationContext(),
                            err.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();
                } finally {
                    if (db != null) {
                        db.close();
                        db = null;
                    }
                }
                CloseThisActivity();
            }
        });

        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUp.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}