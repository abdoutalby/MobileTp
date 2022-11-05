package com.example.tp1;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import utils.DBHelper;

public class Clients extends AppCompatActivity {

    EditText nom , adr , tel , fax , mail , contact , telcontact ;

    Button save , cancel ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);


        save = findViewById(R.id.saveclient);
        cancel = findViewById(R.id.clientcancel);


        nom = findViewById(R.id.clientnom);
        adr = findViewById(R.id.clientadr);
        tel = findViewById(R.id.clienttel);
        fax = findViewById(R.id.clientfax);
        mail = findViewById(R.id.clientmail);
        contact = findViewById(R.id.clientcontact);
        telcontact = findViewById(R.id.clientcontacttel);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper dbHelper = new DBHelper(getApplicationContext());
           boolean sql =       dbHelper.addClient(
                      nom.getText().toString() ,
                      adr.getText().toString() ,
                      tel.getText().toString() ,
                      fax.getText().toString() ,
                      mail.getText().toString() ,
                      contact.getText().toString() ,
                      telcontact.getText().toString()  ) ;
            if(sql){
                nom.setText("");
                adr.setText("");
                tel.setText("");
                fax.setText("");
                mail.setText("");
                contact.setText("");
                telcontact.setText("");
                Toast.makeText(Clients.this, "saved ", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(Clients.this, "some thing wrong please try again", Toast.LENGTH_SHORT).show();
            }
            }
        });
        
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext() , MainActivity.class));
                finish();
            }
        });

    }
}