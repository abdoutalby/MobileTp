package com.example.tp1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import utils.DBHelper;

import java.util.ArrayList;
import java.util.Date;

public class ListContrat extends AppCompatActivity implements ContractAdapter.IclickListner {

 ContractAdapter adapter ;

 RecyclerView recyclerView;

 TextView noContractYet ;

 EditText contractFindByName ;
 ImageView btnSearchContractByname;

 Button btnAddContract;
 ArrayList<Contract> list  = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contrat);
        getContracts();
        contractFindByName = findViewById(R.id.contractFindByName);

        recyclerView = findViewById(R.id.contactlistr);
        noContractYet = findViewById(R.id.noContractYet);
        btnSearchContractByname = findViewById(R.id.btnSearchContractByname);
        btnAddContract = findViewById(R.id.btnAddContract);
        btnAddContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext() , AjoutContracts.class));
            }
        });

        btnSearchContractByname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper dbHelper = new DBHelper(getApplicationContext());
               Cursor c =  dbHelper.findContractByName(contractFindByName.getText().toString());
                if (c.getCount()>0){
                    c.moveToFirst();
                    Contract item = new Contract(
                            c.getInt(c.getColumnIndexOrThrow("id")),
                            c.getInt(c.getColumnIndexOrThrow("id_client")),
                            c.getString(c.getColumnIndexOrThrow("datedebut")),
                            c.getString(c.getColumnIndexOrThrow("datefin")),
                            c.getDouble(c.getColumnIndexOrThrow("ref")),
                            c.getInt(c.getColumnIndexOrThrow("valsync"))
                    );
                    list.clear();
                    list.add(item);
                }

            }
        });
        if(list.size()==0){
            noContractYet.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
        adapter = new ContractAdapter(getApplicationContext(), list , this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

    }

    private void getContracts() {
        DBHelper db = new DBHelper(getApplicationContext());
        Cursor c = db.findAllContracts();
        if(c.getCount()>0){

            while (c.moveToNext()){
                Contract item = new Contract(
                        c.getInt(c.getColumnIndexOrThrow("id")),
                        c.getInt(c.getColumnIndexOrThrow("id_client")),
                        c.getString(c.getColumnIndexOrThrow("datedebut")),
                        c.getString(c.getColumnIndexOrThrow("datefin")),
                        c.getDouble(c.getColumnIndexOrThrow("ref")),
                        c.getInt(c.getColumnIndexOrThrow("valsync"))
                );
                list.add(item);
            }
        }
        c.close();
    }

    @Override
    public void onSelectItem(int position) {
        Intent i = new Intent(getApplicationContext(), ContractPage.class);
        i.putExtra("idContract", list.get(position).getId());
        startActivity(i);
    }
}


