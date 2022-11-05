package utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.example.tp1.Contract;

public class DBHelper extends SQLiteOpenHelper {


    public DBHelper( Context context) {
        super(context, "TPmobile.db",null ,1);

    }

    @Override
    public void onCreate(SQLiteDatabase DB) {

        DB.execSQL("create table if not exists clients (id integer primary key autoincrement ," +
                "nom varchar ," +
                "adr varchar ," +
                "tel varchar ," +
                "fax varchar ," +
                "mail varchar ," +
                "contact varchar ," +
                "contacttel varchar ," +
                "valsync int );");


        DB.execSQL( "create table if not exists contracts (" +
                "id integer primary key autoincrement ," +
                "id_client integer ," +
                "datedebut varchar ," +
                "datefin varchar  ," +
                "ref decimal ," +
                "valsync int  ," +
                "FOREIGN KEY(id_client) REFERENCES clients(id));");



    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists clients");
        DB.execSQL("drop Table if exists contracts");
    }

    public boolean  addContact(int idClient,String dateDeb , String dateFin , String ref ){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id",  (Byte) null);
        contentValues.put("id_client" ,idClient);
        contentValues.put("datedebut", dateDeb);
        contentValues.put( "datefin" , dateFin);
        contentValues.put("ref", ref);
        contentValues.put("valsync",0);

        long res = DB.insert("contracts", null, contentValues);

        if (res == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean addClient(String nom,String adr , String tel , String fax , String mail , String contact ,
                             String contacttel
    ){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id",  (Byte) null);
        contentValues.put("nom", nom);
        contentValues.put("adr" , adr);
        contentValues.put("tel", tel );
        contentValues.put("fax", fax);
        contentValues.put("mail" , mail);
        contentValues.put( "contact" , contact);
        contentValues.put( "contacttel" ,  contacttel);
        contentValues.put("valsync", 0 );


        long res = DB.insert("clients", null, contentValues);

        if (res == -1) {
            return false;
        } else {
            return true;
        }

    }

    public Cursor findContractByName(String  name){
        SQLiteDatabase DB = this.getWritableDatabase();

        Cursor c = DB.rawQuery("select * from contracts ct , clients c where c.id = ct.id_client and c.nom like ? ", new String[]{"%"+name});
        return c;
    }

    public Cursor findAllContracts(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor c = DB.rawQuery("select * from contracts ;", null);
        return  c;
    }


    public int findClientByName(String name) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor c = DB.rawQuery("select * from clients where nom = ? ;",new String[]{name});
        if (c.getCount()>0){
            c.moveToFirst();
            return  c.getInt(c.getColumnIndexOrThrow("id"));
        }
        return -1;
    }

    public boolean deleteContract(int  id) {
        SQLiteDatabase DB = this.getWritableDatabase();
        return   DB.delete("contracts","id = ?" , new String[]{id+""})>0;
    }

    public boolean updateContract(int toString, String toString1, String toString2, String toString3, String toString4) {
    return  false;
    }

    public Cursor findContractById(int x) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor c = DB.rawQuery("select * from contracts  where id  = ?;", new String[]{String.valueOf(x)});
        return  c;
    }

    public  Cursor findClientById(int id){
        SQLiteDatabase DB = this.getWritableDatabase();
        return    DB.rawQuery("select * from clients where id = ? ;",new String[]{String.valueOf(id)});

    }
}