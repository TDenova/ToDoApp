package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class EditTodo extends AppCompatActivity {
    protected String ID;
    protected String userId;
    protected EditText editTodo;
    protected Button btnSave, btnDelete, btnDone;
    protected boolean isDone;

    protected void CloseActivity() {
        finishActivity(200);
        Intent intent = new Intent(EditTodo.this, TodoList.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);

        editTodo = findViewById(R.id.editTodo);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        btnDone = findViewById(R.id.btnDone);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            editTodo.setText(bundle.getString("todoText"));
            userId = bundle.getString("userId");
            try {
                SQLiteDatabase db = null;
                db = SQLiteDatabase.openOrCreateDatabase(
                        getFilesDir().getPath() + "/" + "todoDb.db",
                        null
                );

                ArrayList<String> todoItems = new ArrayList<String>();
                String queryTodo = "SELECT * FROM TODO WHERE user_id = ? and todo_text = ? ;";
                String todoText = editTodo.getText().toString();
                Cursor cursorTodo = db.rawQuery(queryTodo, new String[]{userId, todoText});

                while (cursorTodo.moveToNext()) {
                    isDone = cursorTodo.getString(cursorTodo.getColumnIndex("is_done")).equals("1");
                    ID = cursorTodo.getString(cursorTodo.getColumnIndex("ID"));
                }
            } catch (Exception err) {

            }
            if(isDone) {
                btnDone.setEnabled(false);
            }
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = null;
                try {
                    db = SQLiteDatabase.openOrCreateDatabase(
                            getFilesDir().getPath() + "/" + "todoDb.db",
                            null
                    );
                    String todoText = editTodo.getText().toString();
                    if(!todoText.isEmpty()) {
                        String query = "UPDATE TODO SET todo_text =? " ;
                        query+= "WHERE ID=?; " ;

                        db.execSQL(query, new Object[]{todoText, ID});
                        Toast.makeText(getApplicationContext(),
                                "Successful update!",
                                Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Unsuccessful update!",
                                Toast.LENGTH_LONG).show();
                    }

                } catch (Exception error) {
                    Toast.makeText(getApplicationContext(),
                            error.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();
                } finally {
                    if(db != null) {
                        db.close();
                        db = null;
                    }
                }
                CloseActivity();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = null;
                try {
                    db = SQLiteDatabase.openOrCreateDatabase(
                            getFilesDir().getPath() + "/" + "todoDb.db",
                            null
                    );
                    String todoText = editTodo.getText().toString();
                    if(!todoText.isEmpty()) {
                        String query = "UPDATE TODO SET is_done = ? " ;
                        query+= "WHERE ID=?; " ;

                        db.execSQL(query, new Object[]{true, ID});
                        Toast.makeText(getApplicationContext(),
                                "Successful update!",
                                Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Unsuccessful update!",
                                Toast.LENGTH_LONG).show();
                    }

                } catch (Exception error) {
                    Toast.makeText(getApplicationContext(),
                            error.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();
                } finally {
                    if(db != null) {
                        db.close();
                        db = null;
                    }
                }
                CloseActivity();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = null;
                try {
                    db = SQLiteDatabase.openOrCreateDatabase(
                            getFilesDir().getPath() + "/" + "todoDb.db",
                            null
                    );
                    String todoText = editTodo.getText().toString();
                    if(!todoText.isEmpty()) {
                        String query = "DELETE FROM TODO " ;
                        query+= "WHERE ID=?; " ;

                        db.execSQL(query, new Object[]{ID});
                        Toast.makeText(getApplicationContext(),
                                "Successful delete!",
                                Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Unsuccessful delete!",
                                Toast.LENGTH_LONG).show();
                    }

                } catch (Exception error) {
                    Toast.makeText(getApplicationContext(),
                            error.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();
                } finally {
                    if(db != null) {
                        db.close();
                        db = null;
                    }
                }
                CloseActivity();
            }
        });
    }
}