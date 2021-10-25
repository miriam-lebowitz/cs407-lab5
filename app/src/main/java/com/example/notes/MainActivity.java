package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static String usernameKey;

    public void clickFunction(View view) {
        // 1. Get username and password via EditText view
        EditText myUsername = (EditText) findViewById(R.id.username);
        String username = myUsername.getText().toString();
        // 2. Add username to SharedPreferences object
        SharedPreferences sharedPreferences = getSharedPreferences("lab5Username", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("username", username).apply();
        // 3. Start second activity
        goToNotesActivity(username);
    }

    public void goToNotesActivity(String s) {
        Intent intent = new Intent(this, NotesActivity.class);
        intent.putExtra("login", s);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String usernameKey = "username";

        SharedPreferences sharedPreferences = getSharedPreferences("lab5Username", Context.MODE_PRIVATE);

        if (!sharedPreferences.getString(usernameKey, "").equals("")) {
            // "username key exists in SharedPreferences object which means that a user was logged in before the app close
            // Get the name of that user from SharedPreferences using sharedPreferences.getString(usernameKy, "").
            String username = sharedPreferences.getString(usernameKey, "");
            // Use Intent to start the second activity welcoming the user
            goToNotesActivity(username);
        } else {
            // SharedPreferences object has no username key set.
            // Start screen 1, that is the main activity
            setContentView(R.layout.activity_main);
        }
    }
}