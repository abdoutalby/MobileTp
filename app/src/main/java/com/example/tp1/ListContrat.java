package com.example.tp1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.DBHelper;
import utils.models.Client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

public class ListContrat extends AppCompatActivity implements ContractAdapter.IclickListner {

 ContractAdapter adapter ;

 RecyclerView recyclerView;

 TextView noContractYet ;

 EditText contractFindByName ;
 ImageView btnSearchContractByname;
    int currentIndex = -1;
 Button btnAddContract;
 ArrayList<Contract> list  = new ArrayList<>();
 ArrayList<Client> clients  = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contrat);

        contractFindByName = findViewById(R.id.contractFindByName);
        recyclerView = findViewById(R.id.contactlistr);
        noContractYet = findViewById(R.id.noContractYet);
        btnSearchContractByname = findViewById(R.id.btnSearchContractByname);
        btnAddContract = findViewById(R.id.btnAddContract);
        getContracts();

        btnAddContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext() , AjoutContracts.class));
            }
        });

        btnSearchContractByname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetByClientName get = new GetByClientName(getApplicationContext());
                get.execute(contractFindByName.getText().toString());

            }
        });

    }

    private void getContracts() {
       /* DBHelper db = new DBHelper(getApplicationContext());
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
        c.close();*/
        GetByClientName getAll = new GetByClientName(getApplicationContext());
        getAll.execute();



    }

    @Override
    public void onSelectItem(int position) {
        Intent i = new Intent(getApplicationContext(), ContractPage.class);
        i.putExtra("idContract", list.get(position).getId());
        startActivity(i);
    }


    private  class GetByClientName extends AsyncTask<String , Void , String > {
        Context context ;
        public GetByClientName(Context context){
            this.context = context;
        }



        @Override
        protected String doInBackground(String... strings) {

            StringBuilder result = new StringBuilder();


            try {
                URL url = new URL("http://192.168.0.139/php/getAllContract.php");

                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);
                OutputStream out = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("nom", "UTF-8") + "="+ URLEncoder.encode("nom" , "UTF-8");
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
            list = new ArrayList<>();
            try {
                JSONArray res = new JSONArray(s);
                if (res.length()>0){
                    currentIndex = 0;
                }else {
                    currentIndex = -1;
                    Toast.makeText(context, "no contract found ", Toast.LENGTH_SHORT).show();

                }


                for (int i = 0 ;i< res.length(); i++){
                    JSONObject object = res.getJSONObject(i);
                    GetClientByName getClientByName = new GetClientByName(context.getApplicationContext());
                    getClientByName.execute((String) object.get("id_client"));
                    System.out.println("item "+i +" "+ object.toString());
                    list.add(new Contract(
                            Integer.parseInt((String) object.get("id")) ,
                            Integer.parseInt((String) object.get("id_client")),
                             object.get("datedebut").toString(),
                            object.get("datefin").toString(),
                            Integer.parseInt((String)object.get("ref")),
                            Integer.parseInt((String) object.get("valsync"))
                    ));
                }
                System.out.println(list.toArray().toString());

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private  class GetClientByName extends AsyncTask<String , Void , String >
    {
        Context context ;
        public GetClientByName(Context context){
            this.context = context;
        }



        @Override
        protected String doInBackground(String... strings) {

            StringBuilder result = new StringBuilder();
            String nom = strings[0] ;

            try {
                URL url = new URL("http://192.168.0.139/php/findClientByName.php");

                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);
                OutputStream out = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("id", "UTF-8") + "="+ URLEncoder.encode(nom , "UTF-8");
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
            clients = new ArrayList<>();
            try {
                JSONArray res = new JSONArray(s);
                for (int i = 0 ;i< res.length(); i++){
                    JSONObject object = res.getJSONObject(i);
                    System.out.println("item "+i +" "+ object.toString());
                    clients.add(new Client(
                            Integer.parseInt((String) object.get("id")) ,
                            (String) object.get("nom"),
                            (String) object.get("adr"),
                            (String) object.get("tel"),
                            (String)  object.get("fax"),
                            (String)  object.get("mail"),
                            (String)  object.get("contact"),
                            (String) object.get("contacttel"),
                            Integer.parseInt((String) object.get("valsync"))
                    ));
                }
                System.out.println(clients.toArray().toString());
                recyclerView.setVisibility(View.VISIBLE);
                adapter = new ContractAdapter(getApplicationContext(), list  , clients,ListContrat.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


