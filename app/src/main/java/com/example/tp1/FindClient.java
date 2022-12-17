package com.example.tp1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.models.Client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FindClient extends AppCompatActivity {
    ArrayList<Client> clients = new ArrayList<>();
    int currentIndex =-1;
    EditText nom , adr , tel , fax , mail , contact , telcontact  , findClientEt;
    ImageButton find , nextClient , prevClient , firstClient , lastClient , ok , cancel ;

    LinearLayout navigationBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_client);


        find = findViewById(R.id.findClientbtnFindClient);
        nom = findViewById(R.id.findclientnom);
        adr = findViewById(R.id.findclientadr);
        tel = findViewById(R.id.findclienttel);
        fax = findViewById(R.id.findclientfax);
        mail = findViewById(R.id.findclientmail);
        contact = findViewById(R.id.findclientcontact);
        telcontact = findViewById(R.id.findclientcontacttel);
        findClientEt = findViewById(R.id.findClientFindClient);
        firstClient = findViewById(R.id.findfirstClient);
        nextClient = findViewById(R.id.findnextClient);
        prevClient = findViewById(R.id.findprevClient);
        lastClient = findViewById(R.id.findlastClient);
        navigationBar = findViewById(R.id.findNavigation);
        navigationBar.setVisibility(View.GONE);
        ok = findViewById(R.id.findOK);
        cancel = findViewById(R.id.findCa);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentIndex!= -1){
                    Intent res = new Intent();
                    res.putExtra("idClient", clients.get(currentIndex).getId());
                    res.putExtra("nameClient", clients.get(currentIndex).getNom());
                    setResult(111 , res);
                    finish();
                }else {
                    Toast.makeText(FindClient.this, "no client found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nextClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });
        prevClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prev();
            }
        });
        lastClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                last();
            }
        });
        firstClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                first();
            }
        });

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetClientByName getClientByName = new GetClientByName(getApplicationContext());
                getClientByName.execute(findClientEt.getText().toString());

             /* Cursor client =  dbHelper.getClientByName(findClientEt.getText().toString());
              if(client !=null){
                  client.moveToFirst();
                    currentId = client.getInt(client.getColumnIndexOrThrow("id"));
                    nom.setText(client.getString(client.getColumnIndexOrThrow("nom")));
                    adr.setText(client.getString(client.getColumnIndexOrThrow("adr")));
                    tel.setText(client.getString(client.getColumnIndexOrThrow("tel")));
                    fax.setText(client.getString(client.getColumnIndexOrThrow("fax")));
                    mail.setText(client.getString(client.getColumnIndexOrThrow("mail")));
                    contact.setText(client.getString(client.getColumnIndexOrThrow("contact")));
                    telcontact.setText(client.getString(client.getColumnIndexOrThrow("contacttel")));
                    updateAndDelete.setVisibility(View.VISIBLE);
              }else {
                Toast.makeText(Clients.this, "no client found", Toast.LENGTH_SHORT).show();
              }*/
            }
        });
    }

    private void getAllClients(){
        clients  = new ArrayList<>();
        GetClientByName getAllClients = new  GetClientByName(getApplicationContext());
        getAllClients.execute();

    }
    private void showCurrent(){
        if (currentIndex != -1) {
            nom.setText(clients.get(currentIndex).getNom());
            adr.setText(clients.get(currentIndex).getAdr());
            tel.setText(clients.get(currentIndex).getTel());
            contact.setText(clients.get(currentIndex).getContact());
            telcontact.setText(clients.get(currentIndex).getContacttel());
            fax.setText(clients.get(currentIndex).getFax());
            mail.setText(clients.get(currentIndex).getMail());
        }
    }
    private void next(){
        if (this.currentIndex<clients.size()-1){
            currentIndex++;
            showCurrent();
        }else{
            Toast.makeText(getApplicationContext(), "no more clients", Toast.LENGTH_SHORT).show();
        }
    }
    private void prev(){
        if (this.currentIndex>0){
            currentIndex--;
            showCurrent();
        }else {
            Toast.makeText(getApplicationContext(), "no more clients", Toast.LENGTH_SHORT).show();
        }
    }
    private void first() {
        if ( clients.size()>0){
            currentIndex=0;
            showCurrent();
        }else {

            Toast.makeText(getApplicationContext(), "no more clients", Toast.LENGTH_SHORT).show();

        }

    }
    private void last(){
        if ( clients.size()>0){
            currentIndex=clients.size()-1;
            showCurrent();
        }
    }
    private  class GetClientByName extends AsyncTask<String , Void , String > {
        Context context ;
        public GetClientByName(Context context){
            this.context = context;
        }



        @Override
        protected String doInBackground(String... strings) {

            StringBuilder result = new StringBuilder();
            String nom = strings[0] ;

            try {
                URL url = new URL("http://192.168.0.139/php/getClient.php");

                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);
                OutputStream out = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("nom", "UTF-8") + "="+ URLEncoder.encode(nom , "UTF-8");
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
                if (res.length()>0){
                    currentIndex = 0;

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
                }else {
                    currentIndex = -1;
                    Toast.makeText(context, "no client found ", Toast.LENGTH_SHORT).show();

                }
                System.out.println(clients.toArray().toString());
                showCurrent();

                if (clients.size()>1){
                    navigationBar.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
    }