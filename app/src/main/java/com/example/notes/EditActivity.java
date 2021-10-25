package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    int noteid = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // 1. Get EditText view.
        EditText newNote = (EditText) findViewById(R.id.editNote);
        String content = newNote.getText().toString();

        // 2. Get Intent.
        Intent intent = getIntent();

        // 3. Get the value of integer "noteid" from intent.
        // 4. Initialise class variable "noteid" with the value from intent.
        noteid = intent.getIntExtra("noteid", -1);

        if (noteid != -1) {
            // Display content of note by retrieving "notes" ArrayList in SecondActivity.
            Note note = NotesActivity.notes.get(noteid);
            String noteContent = note.getContent();
            // Use editText.setText() to display the contents of this note on screen.
            newNote.setText(noteContent);
        }
    }

    public void saveMethod(View view) {
        Log.i("LOOK HERE", "BONK");
        // 1. Get editText view and the content that user entered.
        EditText newNote = (EditText) findViewById(R.id.editNote);
        String content = newNote.getText().toString();
        Log.i("CONTENT", content);

        // 2. Initialise SQLiteDatabase instance.
        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("notes", Context.MODE_PRIVATE,null);

        // 3. Initialise DBHelper class.
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);

        // 4. Set username in the following variable by fetching it from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("lab5Username", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        // 5. Save information to database.
        String title;
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String date = dateFormat.format(new Date());

        if (noteid == -1) { // Add note
            title = "NOTE_" + (NotesActivity.notes.size() + 1);
            Log.i("TITLE", title);
            dbHelper.saveNotes(username, title, content, date);
        } else { // Update note
            title = "NOTE_" + (noteid + 1);
            dbHelper.updateNote(title, date, content, username);
        }

        Log.i("INTENT", "Notes?");
        // 6. Go to second activity using intents
        Intent intent = new Intent(this, NotesActivity.class);
        startActivity(intent);
    }


}