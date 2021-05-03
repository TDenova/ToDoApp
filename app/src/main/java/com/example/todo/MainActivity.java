package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {
    protected TextView btnGoToSignUp;
    protected Button btnLogin;
    protected EditText loginUsername, loginPassword;
    protected String usernameMenu;
    protected String userId;
    protected void initDb() throws SQLException {
        SQLiteDatabase db = null;
        db = SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath() + "/" + "todoDb.db",
                null
        );
        String query = "CREATE TABLE if not exists USERS(";
        query += " ID integer primary key AUTOINCREMENT, ";
        query += " username text not null, ";
        query += " password text not null, ";
        query += " unique(username) );";

        db.execSQL(query);

        String todoQuery = "CREATE TABLE if not exists TODO(";
        todoQuery += " ID integer primary key AUTOINCREMENT, ";
        todoQuery += " user_id integer not null, ";
        todoQuery += " is_done boolean not null default false, ";
        todoQuery += " todo_text text not null, ";
        todoQuery += "unique(todo_text, user_id) , ";
        todoQuery += " FOREIGN KEY(user_id) REFERENCES USERS(id) );";

        db.execSQL(todoQuery);
        db.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGoToSignUp = findViewById(R.id.goToSignUp);
        btnLogin = findViewById(R.id.btnLogin);
        loginUsername = findViewById(R.id.editUsernameLogin);
        loginPassword = findViewById(R.id.editPasswordLogin);

        try{
            initDb();
        } catch (Exception err) {
            Toast.makeText(getApplicationContext(),
                    err.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = null;
                try {
                    db = SQLiteDatabase.openOrCreateDatabase(
                            getFilesDir().getPath() + "/" + "todoDb.db",
                            null
                    );
                    String username = loginUsername.getText().toString();
                    String password = loginPassword.getText().toString();

                    String query = "SELECT * FROM USERS WHERE username = ? and password = ? ; ";
                    Cursor cursor = db.rawQuery(query, new String[]{username, password});

                    if ( cursor.getCount() > 0 ) {
                        while(cursor.moveToNext()) {
                            userId = cursor.getString(cursor.getColumnIndex("ID"));
                            Toast.makeText(getApplicationContext(),
                                    "Successful login!",
                                    Toast.LENGTH_LONG).show();
                        }

                        Intent intent = new Intent(MainActivity.this, TodoList.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("username", username);
                        bundle.putString("userId", userId);
                        intent.putExtras(bundle);

                        startActivity(intent, bundle);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Unsuccessful login! Please try again.",
                                Toast.LENGTH_LONG).show();
                    }
                    db.close();
                } catch (Exception err) {
                    Toast.makeText(getApplicationContext(),
                            err.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        btnGoToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SignUp.class);
                startActivity(i);
            }
        });
    }
}