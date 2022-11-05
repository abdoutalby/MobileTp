package com.example.tp1;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import utils.DBHelper;

public class AjoutContracts extends AppCompatActivity {

    EditText datedebut , datefin, reference , clientName;

    Button save , cancel ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contracts);

        datedebut = findViewById(R.id.datedebut);
        datefin = findViewById(R.id.datefin);
        reference = findViewById(R.id.ref);
        clientName = findViewById(R.id.contractClient);
        save = findViewById(R.id.contratbtnsave);
        cancel = findViewById(R.id.contratbtncancel);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveContract();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Contracts.class));
                finish();
            }
        });
    }

    private void saveContract() {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        int clientId = dbHelper.findClientByName(clientName.getText().toString());
      if (clientId  != -1){
          boolean res =   dbHelper.addContact(clientId,
                  datedebut.getText().toString()
                  , datefin.getText().toString()
                  ,reference.getText().toString()
          );
          if (res){
              datefin.setText("");
              datedebut.setText("");
              reference.setText("");
              clientName.setText("");
              Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
          }
          else{
          Toast.makeText(this, "oops something wrong ", Toast.LENGTH_SHORT).show();
             }
    }else {
        startActivityForResult(new Intent(getApplicationContext(), FindClient.class), 111);
      }
    }

}