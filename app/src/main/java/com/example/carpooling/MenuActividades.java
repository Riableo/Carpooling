package com.example.carpooling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuActividades extends AppCompatActivity {
    ImageView crear,consultar,modificar,eliminar;
    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_actividades);

        name=(TextView)findViewById(R.id.nusu);
        crear=(ImageView) findViewById(R.id.but);
        consultar=(ImageView) findViewById(R.id.but2);
        modificar=(ImageView)findViewById(R.id.edit);
        eliminar=(ImageView)findViewById(R.id.delete);


        Intent i=getIntent();
        String nam=i.getStringExtra("Nombre");
        String lastna=i.getStringExtra("Apellido");
        name.setText(nam+" "+lastna);


        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MenuActividades.this, Agregar.class);
                i.putExtra("Nombre",name.getText().toString());
                startActivity(i);
            }
        });

        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MenuActividades.this, Consultar.class);
                i.putExtra("Nombre",name.getText().toString());
                startActivity(i);
            }
        });

        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MenuActividades.this, Editar.class);
                i.putExtra("Nombre",name.getText().toString());
                startActivity(i);
            }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MenuActividades.this, Eliminar.class);
                i.putExtra("Nombre",name.getText().toString());
                startActivity(i);
            }
        });

    }

}