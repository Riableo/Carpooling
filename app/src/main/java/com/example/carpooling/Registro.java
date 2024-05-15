package com.example.carpooling;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Registro extends AppCompatActivity {
    EditText ide,career,nam,cel,apel,disca,pass;
    Button bguardar, bVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        ide=findViewById(R.id.cod);
        career=findViewById(R.id.carrer);
        nam=findViewById(R.id.name);
        cel= findViewById(R.id.phone);
        apel= findViewById(R.id.lastname);
        disca= findViewById(R.id.discap);
        pass= findViewById(R.id.psswd);
        bguardar=findViewById(R.id.regist);
        bVolver=findViewById(R.id.back);


        bguardar.setOnClickListener(v -> new
                CargarDatos().execute("http://10.0.2.2/carpool/registrar.php?Codigo="+ide.getText().toString()+"&Celular="+cel.getText().toString()+"&Nombre="+nam.getText().toString()+"&Apellido="+apel.getText().toString()+"&Cargo="+career.getText().toString()+"&Password="+pass.getText().toString()+"&Discapacidad="+disca.getText().toString()));

        bVolver.setOnClickListener(v -> {
            Intent i=new Intent(Registro.this, MainActivity.class);

            startActivity(i);
        });
    }
    private class ConsultarDatos extends AsyncTask<String, Void, String> {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        //************************************************************
        //METODO QUE PERMITE EXTRAER LOS DATOS DE LA BASE DE DATOS
        //************************************************************
        @Override
        protected void onPostExecute(String result) {
            JSONArray ja;
            try {
                ja = new JSONArray(result);
                ide.setText(ja.getString(0));
                career.setText(ja.getString(1));
                nam.setText(ja.getString(2));
                cel.setText(ja.getString(3));
                apel.setText(ja.getString(4));
                disca.setText(ja.getString(5));
                pass.setText(ja.getString(6));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    //********************************************************************************
    //Clase que invoca el procedimiento para Inserción de datos en la Base de Datos
    //********************************************************************************
    private class CargarDatos extends AsyncTask<String, Void, String> {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
            Toast.makeText(getApplicationContext(), "Los datos se guardaron éxitosamente", Toast.LENGTH_LONG).show();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String downloadUrl(String myurl) throws IOException {
        Log.i("URL",""+myurl);
        myurl = myurl.replace(" ","%20");
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
            return readIt(is, len);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String readIt(InputStream stream, int len) throws IOException {
        Reader reader;
        reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}