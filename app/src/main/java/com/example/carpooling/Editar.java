package com.example.carpooling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class Editar extends AppCompatActivity {
    EditText cel;
    Button blista;
    ListView lista;
    String var1,var2,var3,var4;
    String docu, miurl="";
    String[] elementos;
    JSONArray ja = null;
    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar);
        name=(TextView)findViewById(R.id.nusu);
        cel=(EditText)findViewById(R.id.telf);
        blista=(Button)findViewById(R.id.button);
        lista=(ListView) findViewById(R.id.LISTA);

        Intent i=getIntent();
        String nam=i.getStringExtra("Nombre");
        name.setText(nam);

        blista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init2();
            }
        });



        //init2();
    }
    public void init2(){
        miurl="http://10.0.2.2/carpool/list_telf.php?Celular="+cel.getText().toString();
        new Editar.ConsultarDatos().execute(miurl);
    }
    public void mostrar() {
        ArrayAdapter<String> adapter1 = new ArrayAdapter(Editar.this,
                android.R.layout.simple_list_item_1, elementos);
        lista.setAdapter(adapter1);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {//Al hacer click en cualquiera de los elementos de la lista
                Intent ir=new Intent(Editar.this, modif.class);
                ir.putExtra("Nombre", name.getText());
                ir.putExtra("Celular",var1);
                ir.putExtra("Origen",var2);
                ir.putExtra("Destino",var3);
                ir.putExtra("Cupo",var4);
                startActivity(ir);
            }
        });
    }
    private class ConsultarDatos extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                //String cadena=urls[0];
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                ja = new JSONArray(result);
                elementos=new String[ja.length()];

                //id.setText(ja.getString(0));// Aqui imprime todo el arreglo asociativo
                if(ja.length()!=0) {
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject obj = ja.getJSONObject(i);
                        var1 = obj.getString("Celular");
                        var2 = obj.getString("Origen");
                        var3 = obj.getString("Destino");
                        var4 = obj.getString("Cupo");

                        elementos[i] = var2+"    "+ var3+"    "+var4;

                    }
                    mostrar();
                }
                else{
                    Toast.makeText(getApplicationContext(), "No se ha creado el registro",
                            Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private String downloadUrl(String myurl) throws IOException {
        Log.i("URL", "" + myurl);
        myurl = myurl.replace(" ", "%20");
        InputStream is = null;
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
            String contentAsString = readIt(is);
            return contentAsString;
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String readIt(InputStream stream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder buffer = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            buffer.append(inputLine);
        }
        return buffer.toString();
    }
}