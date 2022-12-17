package com.example.tp1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.Optional;

public class Clients extends AppCompatActivity {
    ArrayList<Client> clients = new ArrayList<>();
    int currentIndex =-1;
    EditText nom , adr , tel , fax , mail , contact , telcontact  , findClientEt;
    Button save , cancel   , getAllClients;
    ImageButton find , nextClient , prevClient , firstClient , lastClient , deleteClient , updateClient ;
    LinearLayout updateAndDelete ;
    DBHelper dbHelper ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);
        //dbHelper = new DBHelper(getApplicationContext());

        save = findViewById(R.id.saveclient);
        cancel = findViewById(R.id.clientcancel);
        find = findViewById(R.id.btnFindClient);
        updateAndDelete = findViewById(R.id.lin);
        updateAndDelete.setVisibility(View.GONE);
        nom = findViewById(R.id.clientnom);
        adr = findViewById(R.id.clientadr);
        tel = findViewById(R.id.clienttel);
        fax = findViewById(R.id.clientfax);
        mail = findViewById(R.id.clientmail);
        contact = findViewById(R.id.clientcontact);
        telcontact = findViewById(R.id.clientcontacttel);
        findClientEt = findViewById(R.id.EtFindClient);
        getAllClients= findViewById(R.id.getAllClients);
        firstClient = findViewById(R.id.firstClient);
        nextClient = findViewById(R.id.nextClient);
        prevClient = findViewById(R.id.prevClient);
        lastClient = findViewById(R.id.lastClient);
        deleteClient = findViewById(R.id.deleteClient);
        updateClient = findViewById(R.id.updateClient);
        updateClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateClient update = new UpdateClient(getApplicationContext());
                update.execute(
                        nom.getText().toString() ,
                        adr.getText().toString() ,
                        tel.getText().toString() ,
                        fax.getText().toString() ,
                        mail.getText().toString() ,
                        contact.getText().toString() ,
                        telcontact.getText().toString() ,
                        "0",
                        String.valueOf(clients.get(currentIndex).getId())
                );
            }
        });
        deleteClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               DeleteClient();

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
        getAllClients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllClients();
             }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddClient backTask = new AddClient(getApplicationContext());
                backTask.execute(
                        nom.getText().toString() ,
                        adr.getText().toString() ,
                        tel.getText().toString() ,
                        fax.getText().toString() ,
                        mail.getText().toString() ,
                        contact.getText().toString() ,
                        telcontact.getText().toString() ,
                       "0"
                        );

          /* boolean sql =       dbHelper.addClient(
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
            }*/
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext() , MainActivity.class));
                finish();
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
    private class UpdateClient extends AsyncTask<String  , Void  , String>{
       Context context ;

       public UpdateClient(Context context){
           this.context = context;
       }
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder result = new StringBuilder();

            String nom = strings[0] ;
            String adr = strings[1] ;
            String tel = strings[2] ;
            String fax =strings[3] ;
            String mail = strings[4] ;
            String contact = strings[5];
            String telcontact= strings[6]   ;
            String valsync = strings[7];
            String id = strings[8];
            try {
                URL url = new URL("http://192.168.0.139/php/updateClient.php");

                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);
                OutputStream out = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("id", "UTF-8") + "="+ URLEncoder.encode(id , "UTF-8")+
                        "&&"+URLEncoder.encode("nom", "UTF-8") + "="+ URLEncoder.encode(nom , "UTF-8")+
                        "&&"+URLEncoder.encode("adr", "UTF-8") + "="+ URLEncoder.encode(adr , "UTF-8")+
                        "&&"+URLEncoder.encode("tel", "UTF-8") + "="+ URLEncoder.encode(tel , "UTF-8")+
                        "&&"+URLEncoder.encode("fax", "UTF-8") + "="+ URLEncoder.encode(fax , "UTF-8")+
                        "&&"+URLEncoder.encode("mail", "UTF-8") + "="+ URLEncoder.encode(mail , "UTF-8")+
                        "&&"+URLEncoder.encode("contact", "UTF-8") + "="+ URLEncoder.encode(contact , "UTF-8")+
                        "&&"+URLEncoder.encode("contacttel", "UTF-8") + "="+ URLEncoder.encode(telcontact , "UTF-8")+
                        "&&"+URLEncoder.encode("valsync", "UTF-8") + "="+ URLEncoder.encode(valsync , "UTF-8");


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
        protected void onPostExecute(String s) {
            if (s.contains("OK")){
                Toast.makeText(context, "Updated successfully ", Toast.LENGTH_SHORT).show();
                getAllClients();
            }else {
                Toast.makeText(context, "OOPS something wrong", Toast.LENGTH_SHORT).show();
                System.out.println(s);
            }
       }
    }
    private void DeleteClient(){
        DeleteClient delete = new DeleteClient(getApplicationContext());
        delete.execute(String.valueOf( clients.get(currentIndex).getId()));
        getAllClients();
    }
    private class DeleteClient extends  AsyncTask<String , Void , String >{

        Context context;

        public DeleteClient(Context context){
            this.context  = context;
        }
        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL("http://192.168.0.139/php/deleteClient.php");

                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);
                OutputStream out = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("id", "UTF-8") + "="+ URLEncoder.encode(id , "UTF-8");
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
        protected void onPostExecute(String s) {
            System.out.println(s);
            if (s.contains("successfully")){
                Toast.makeText(context, "deleted successfully", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "OOPS error ", Toast.LENGTH_SHORT).show();
                }
         }
    }
    private  class AddClient extends AsyncTask<String , Void , String > {
        Context context ;

        public AddClient(Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder result = new StringBuilder();
            String nom = strings[0] ;
            String adr = strings[1] ;
            String tel = strings[2] ;
            String fax =strings[3] ;
            String mail = strings[4] ;
            String contact = strings[5];
            String telcontact= strings[6]   ;
            String valsync = strings[7];
            try {
                URL url = new URL("http://192.168.0.139/php/addClient.php");

                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);
                OutputStream out = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("nom", "UTF-8") + "="+ URLEncoder.encode(nom , "UTF-8")+
              "&&"+URLEncoder.encode("adr", "UTF-8") + "="+ URLEncoder.encode(adr , "UTF-8")+
              "&&"+URLEncoder.encode("tel", "UTF-8") + "="+ URLEncoder.encode(tel , "UTF-8")+
              "&&"+URLEncoder.encode("fax", "UTF-8") + "="+ URLEncoder.encode(fax , "UTF-8")+
              "&&"+URLEncoder.encode("mail", "UTF-8") + "="+ URLEncoder.encode(mail , "UTF-8")+
              "&&"+URLEncoder.encode("contact", "UTF-8") + "="+ URLEncoder.encode(contact , "UTF-8")+
              "&&"+URLEncoder.encode("contacttel", "UTF-8") + "="+ URLEncoder.encode(telcontact , "UTF-8")+
              "&&"+URLEncoder.encode("valsync", "UTF-8") + "="+ URLEncoder.encode(valsync , "UTF-8");


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
            if (s.contains("OK")){
                Toast.makeText(this.context, "Added successfully", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this.context, "Error adding client", Toast.LENGTH_SHORT).show();
                System.out.println(s);
            }
        }
    }
    private void getAllClients(){
        clients  = new ArrayList<>();
        GetAllClients getAllClients = new GetAllClients(getApplicationContext());
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
                }else {
                    currentIndex = -1;
                    Toast.makeText(context, "no client found ", Toast.LENGTH_SHORT).show();

                }
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
                showCurrent();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
             }
        }
    private  class GetAllClients extends AsyncTask<String , Void , String > {
        Context context ;
        public GetAllClients(Context context){
            this.context = context;
        }



        @Override
        protected String doInBackground(String... strings) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL("http://192.168.0.139/php/getAllClients.php");
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);
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
            clients = new ArrayList<>();
            try {
                JSONArray res = new JSONArray(s);
                if (res.length()>0){
                    currentIndex = 0;
                    updateAndDelete.setVisibility(View.VISIBLE);
                }else {
                    currentIndex = -1;
                    Toast.makeText(context, "no client found ", Toast.LENGTH_SHORT).show();
                }
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
                showCurrent();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}