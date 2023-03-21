package com.example.carpooling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

public class MainActivity extends AppCompatActivity {
    EditText cusu,clav;
    RadioGroup grupo;
    Button log;
    CheckedTextView regis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cusu = (EditText) findViewById(R.id.User);
        clav=(EditText)findViewById(R.id.psswd);
        grupo = (RadioGroup) findViewById(R.id.options);

        log=(Button) findViewById(R.id.Log);
        regis=(CheckedTextView) findViewById(R.id.Registro);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cusu.getText().toString().isEmpty() || clav.getText().toString().isEmpty() ){
                    Toast.makeText(getApplicationContext(), "Usuario o Contraseña incorrecta",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    new MainActivity.ConsultarDatos().execute("http://10.0.2.2/carpool/validar.php?Codigo="+cusu.getText().toString()+"&Password="+clav.getText().toString());

                }
            }
        });

        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this, Registro.class);

                startActivity(i);
            }
        });

    }
    private class ConsultarDatos extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Estrae la información de la BD
            JSONArray ja = null;
            try {
                ja = new JSONArray(result);
                //Setea en variables
                cusu.setText(ja.getString(0));
                clav.setText(ja.getString(5));
                //Validación de contraseña
                if(cusu.getText().toString().equals(ja.getString(0)) &&
                        clav.getText().toString().equals(ja.getString(5)) ){
                    //Validación de Usuario, contraseña y perfil del Usuario lo que recibe de la IGU contra la BD
                    if(grupo.getCheckedRadioButtonId() == R.id.driver){
                        //Pasa los datos Consultados de la BD a la otra Activity
                        Intent i=new Intent(MainActivity.this, MenuActividades.class);
                        //info que se pasa
                        //i.putExtra("Codigo",ja.getString(0));
                        i.putExtra("Nombre",ja.getString(2));
                        i.putExtra("Apellido",ja.getString(3));
                        startActivity(i);
                    }else if(grupo.getCheckedRadioButtonId() == R.id.pasajer){
                        Intent i=new Intent(MainActivity.this, pasajero.class);
                        //info que se pasa
                        //i.putExtra("Codigo",ja.getString(0));
                        i.putExtra("Nombre",ja.getString(2));
                        i.putExtra("Apellido",ja.getString(3));
                        startActivity(i);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private String downloadUrl(String myurl) throws IOException {
        Log.i("URL",""+myurl);
        myurl = myurl.replace(" ","%20");
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;
        try {
            java.net.URL url = new java.net.URL(myurl);
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
            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
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