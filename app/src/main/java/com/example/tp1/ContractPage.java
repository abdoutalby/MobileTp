package com.example.tp1;

import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import utils.DBHelper;

public class ContractPage extends AppCompatActivity {
    TextView id ;
    EditText datedebut , datefin , reference , clientName ;
    Button update , delete;
    Intent intent ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_page);

        intent =getIntent();
        int x = intent.getIntExtra("idContract" , -1);

        datedebut = findViewById(R.id.updatedebut);
        datefin = findViewById(R.id.updatefin);
        reference = findViewById(R.id.upref);
        clientName = findViewById(R.id.upcontractClient);
        update = findViewById(R.id.btnupdate);
        delete = findViewById(R.id.btndeleteContract);
        getContract(x);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper dbHelper = new DBHelper(getApplicationContext());
              if ( dbHelper.deleteContract(x)){
                  Toast.makeText(ContractPage.this, "Deleted", Toast.LENGTH_SHORT).show();
              }else {
                  Toast.makeText(ContractPage.this, "OOPS something wrong", Toast.LENGTH_SHORT).show();
              }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                if(dbHelper.updateContract(x,
                        datedebut.getText().toString(),
                        datefin.getText().toString() ,
                        reference.getText().toString() ,
                        clientName.getText().toString()
                )){
                    Toast.makeText(ContractPage.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ContractPage.this, "OOPS  Please try again ", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void getContract(int x) {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
       Cursor c =  dbHelper.findContractById(x);
       if (c.getCount()>0){
       c.moveToFirst();
       datedebut.setText(c.getString(c.getColumnIndexOrThrow("datedebut")));
       datefin.setText(c.getString(c.getColumnIndexOrThrow("datefin")));
       reference.setText(c.getString(c.getColumnIndexOrThrow("ref")));
       Cursor c2 = dbHelper.findClientById( c.getInt(c.getColumnIndexOrThrow("id_client")));
       if (c2.getCount()>0){
           c2.moveToFirst();
           clientName.setText(c2.getString(c2.getColumnIndexOrThrow("nom")));
       }

    }}
}