package com.example.tp1;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import utils.DBHelper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AjoutContracts extends AppCompatActivity {

    EditText datedebut , datefin, reference , clientName;

    Button save , cancel ;

    ImageButton findClient ;

    int clientId =-1;
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
        findClient = findViewById(R.id.contractFindClient);
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
        findClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(),FindClient.class ), 111);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111){
            assert data != null;
            clientName.setText(data.getStringExtra("nameClient"));
            clientName.setEnabled(false);
            clientId = data.getIntExtra("idClient", -1);
        }
    }

    private void saveContract() {
       /* DBHelper dbHelper = new DBHelper(getApplicationContext());
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
    } */

        SaveContract saveContract = new SaveContract(getApplicationContext());
        saveContract.execute(String.valueOf(clientId) ,
                reference.getText().toString() ,
                datedebut.getText().toString() ,
                datefin.getText().toString());

    }
    private  class SaveContract extends AsyncTask<String , Void , String > {
        Context context ;

        public SaveContract(Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder result = new StringBuilder();
            String idClient = strings[0] ;
            String ref = strings[1] ;
            String dd = strings[2] ;
            String df =strings[3] ;
            try {
                URL url = new URL("http://192.168.0.139/php/addContract.php");

                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);
                OutputStream out = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("id_client", "UTF-8") + "="+ URLEncoder.encode(idClient , "UTF-8")+
                        "&&"+URLEncoder.encode("datedebut", "UTF-8") + "="+ URLEncoder.encode(dd , "UTF-8")+
                        "&&"+URLEncoder.encode("datefin", "UTF-8") + "="+ URLEncoder.encode(df , "UTF-8")+
                        "&&"+URLEncoder.encode("valsync", "UTF-8") + "="+ URLEncoder.encode("0", "UTF-8")+
                        "&&"+URLEncoder.encode("ref", "UTF-8") + "="+ URLEncoder.encode(ref , "UTF-8");

                writer.write(data);
                writer.flush();
                writer.close();
                InputStream in = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.ISO_8859_1));
                String row = "" ;
                while ((row = reader.readLine())!= null){
                    result.append(row);
                }
                reader.close();
                in.close();
                http.disconnect();
                return String.valueOf(result);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(String s){
            System.out.println(s);
            if (s.contains("OK")){
                Toast.makeText(this.context, "Added successfully", Toast.LENGTH_SHORT).show();
                reference.setText("");
                datedebut.setText("");
                datefin.setText("");
                clientName.setEnabled(true);
            }
            else {
                Toast.makeText(this.context, "Error saving contract", Toast.LENGTH_SHORT).show();
                System.out.println(s);
            }
        }
    }

}