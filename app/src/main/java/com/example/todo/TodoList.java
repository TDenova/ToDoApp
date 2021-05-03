package com.example.todo;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

public class TodoList extends AppCompatActivity {

    protected String username;
    protected String userId;
    protected Button btnAdd;
    protected EditText enterTodo;
    protected ListView todoList;
    protected String id;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.todomenu, menu);

        MenuItem logout = menu.findItem(R.id.logOut);
        logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(TodoList.this, MainActivity.class);
                startActivity(intent);
                return false;
            }
        });

        return true;
    }
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
    protected void selectDb(String userId) throws SQLException {
        try {
            SQLiteDatabase db = null;
            db = SQLiteDatabase.openOrCreateDatabase(
                    getFilesDir().getPath() + "/" + "todoDb.db",
                    null
            );

            todoList.clearChoices();
            ArrayList<String> todoItems = new ArrayList<String>();
            String queryTodo ="SELECT * FROM TODO WHERE user_id = ? ORDER BY ID DESC ;";

            Cursor cursorTodo = db.rawQuery(queryTodo, new String[]{userId});

            while(cursorTodo.moveToNext()) {
                String todoText = cursorTodo.getString(cursorTodo.getColumnIndex("todo_text"));
                todoItems.add(todoText);
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.activity_list_view,
                    R.id.todoTextView,
                    todoItems
            );

            todoList.setAdapter(arrayAdapter);

            db.execSQL(queryTodo);
            db.close();
        }catch (Exception err) {
            Toast.makeText(getApplicationContext(),
                    err.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
        }

    }
    protected void onUpdate() throws SQLException {

    }

    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            selectDb(data.getStringExtra("userId"));
        } catch (Exception err) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        btnAdd = findViewById(R.id.btnAdd);
        enterTodo = findViewById(R.id.enterTodo);
        todoList = findViewById(R.id.todoList);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            username = bundle.getString("username");
            userId = bundle.getString("userId");
            try{
                initDb();
                selectDb(userId);
            } catch (Exception err) {
                Toast.makeText(getApplicationContext(),
                        err.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }

        todoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = "";
                TextView todoText = view.findViewById(R.id.todoTextView);
                selected = todoText.getText().toString();
                Intent intent = new Intent(TodoList.this, EditTodo.class);
                Bundle bundle = new Bundle();
                bundle.putString("todoText", selected);
                bundle.putString("userId", userId);

                intent.putExtras(bundle);
                startActivityForResult(intent,200, bundle);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = null;
                try {
                    db = SQLiteDatabase.openOrCreateDatabase(
                            getFilesDir().getPath() + "/" + "todoDb.db",
                            null
                    );
                    String todoText = enterTodo.getText().toString();
                    if(!todoText.isEmpty()) {
                        String query = "INSERT INTO TODO (user_id, todo_text)" ;
                        query+= "VALUES(?, ?); " ;

                        db.execSQL(query, new Object[]{userId, todoText});
                        Toast.makeText(getApplicationContext(),
                            "Successful insert!",
                            Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(),
                            "Unsuccessful insert!",
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
                try {
                    selectDb(userId);
                } catch (Exception err) {

                }
            }
        });

    }

}