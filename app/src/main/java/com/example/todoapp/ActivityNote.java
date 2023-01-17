package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.widget.ImageView;
import android.widget.TextView;

public class ActivityNote extends AppCompatActivity {

    private Context context;
    private ImageView backBtn;
    private TextView save;
    private EditText note;
    private Boolean saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        getSupportActionBar().hide();

        context = this;
        note = findViewById(R.id.editText);
        backBtn = findViewById(R.id.backBtn);
        save = findViewById(R.id.save);

        note.setText(readFromFile(context));
        setSaved(true);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });

        note.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setSaved(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setSaved(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setSaved(false);
                try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("note.txt", Context.MODE_PRIVATE));
                    try {
                        outputStreamWriter.write(String.valueOf(editable));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    outputStreamWriter.close();
                    setSaved(true);
                }
                catch (IOException e) {
                    Log.e("Exception", "Save note failed: " + e.toString());
                }

            }
        });

    }

    private String readFromFile(Context context) {

        String toReturn = "";

        try {
            InputStream inputStream = context.openFileInput("note.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                toReturn = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return toReturn;
    }

    public Boolean getSaved() {
        return saved;
    }

    public void setSaved(Boolean saved) {
        this.saved = saved;
        if (saved == true) {
            save.setText("Saved");
            save.setTextColor(Color.rgb(99,149,8));
        } else {
            save.setText("Saving...");
            save.setTextColor(Color.DKGRAY);
        }
    }
}