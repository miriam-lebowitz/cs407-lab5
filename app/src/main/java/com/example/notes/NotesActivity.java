package com.example.notes;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    TextView welcome;
    public static ArrayList<Note> notes = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.logout) {
            // Erase username from shared preferences.
            Intent intent = new Intent(this, MainActivity.class);
            SharedPreferences sharedPreferences = getSharedPreferences("lab5Username", Context.MODE_PRIVATE);
            sharedPreferences.edit().remove("username").apply();
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.addNote) {
            // Grab Notes Activity
            Intent intent = new Intent(this, EditActivity.class);
            startActivity(intent);
            return true;
        }

        return false;
    }

    public void logout(View view) {
        goToDiffActivity();
    }

    public void goToDiffActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        // 1. Display welcome message. Fetch username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("lab5Username", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        welcome =  (TextView) findViewById(R.id.welcome);
        welcome.setText("Welcome " + username + "!");

        // 2. Get SQLiteDatabase instance.
        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("notes", Context.MODE_PRIVATE,null);

        // 3. Initiate the "notes" class variable using readNotes method implemented in DBHelper class. Use the username you
        // got from SharedPreferences as a parameter to readNotes method.
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);
        notes = dbHelper.readNotes(username);

        // 4. Create an ArrayList<String> object by iterating over notes object
        ArrayList<String> displayNotes = new ArrayList<>();
        for (Note note : notes) {
            Log.i("LOOK HERE", note.getTitle());
            displayNotes.add(String.format("Title:%s\nDate:%s", note.getTitle(), note.getDate()));
        }

        // 5. Use ListView view to display notes on screen.
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, displayNotes);
        ListView listView = (ListView) findViewById(R.id.notesListView);
        listView.setAdapter(adapter);

        // 6. Add onItemClickListener for ListView item, a note in our case.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Initialise intent to take user to third activity (NoteActivity in this case).
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                // Add the position of the item that was clicked on as "noteid".
                intent.putExtra("noteid", position);
                startActivity(intent);
            }
        });
    }
}