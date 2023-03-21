package com.example.carpooling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class modif extends AppCompatActivity {
    EditText or, dest, cup;
    TextView name;
    Button bactual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modif);

        name=(TextView)findViewById(R.id.nusu);
        or = (EditText) findViewById(R.id.orige);
        dest = (EditText) findViewById(R.id.Destino);
        cup = (EditText) findViewById(R.id.Cupo);

        Intent i = getIntent();
        String names=i.getStringExtra("Nombre");
        final String cel =i.getStringExtra("Celular");
        String nam = i.getStringExtra("Origen");
        String desta = i.getStringExtra("Destino");
        String coup = i.getStringExtra("Cupo");

        name.setText(names);

        or.setText(nam);
        dest.setText(desta);
        cup.setText(coup);

        bactual = (Button) findViewById(R.id.button);
        bactual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new modif.CargarDatos().execute("http://10.0.2.2/carpool/modificar.php?Celular=" + cel + "&Origen=" + or.getText().toString() + "&Destino=" + dest.getText().toString() + "&Cupo=" + cup.getText().toString());
            }
        });
    }

    private class CargarDatos extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        // Ejecuta el resultado de la clase AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "Datos actualizados", Toast.LENGTH_LONG).show();
        }
    }

    private String downloadUrl(String myurl) throws IOException {
        Log.i("URL", "" + myurl);
        myurl = myurl.replace(" ", "%20");
        InputStream is = null;
        int len = 500;
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("respuesta", "The response is: " + response);
            is = conn.getInputStream();
            String contentAsString = readIt(is, len);
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String readIt(InputStream stream, int len) throws IOException,
            UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

}
