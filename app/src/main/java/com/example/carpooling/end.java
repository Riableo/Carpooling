package com.example.carpooling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class end extends AppCompatActivity {
    EditText or, dest;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        name=(TextView)findViewById(R.id.nusu);
        or = (EditText) findViewById(R.id.orige);
        dest = (EditText) findViewById(R.id.Destino);


        Intent i = getIntent();
        String names=i.getStringExtra("Nombre");
        String nam = i.getStringExtra("Origen");
        String desta = i.getStringExtra("Destino");

        name.setText(names);

        or.setText(nam);
        dest.setText(desta);

        or.setEnabled(false);
        dest.setEnabled(false);

    }
}