package com.example.tp1;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;
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

public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.ViewHolder> {
    Context context;
    ArrayList<Contract> contracts;

    ArrayList<Client> clients = new ArrayList<>();

    IclickListner iclickListner;

    public ContractAdapter(Context context, ArrayList<Contract> contracts , ArrayList<Client> clients, IclickListner iclickListner) {
        this.context = context;
        this.contracts = contracts;
        this.iclickListner = iclickListner;
        this.clients  = clients;
    }

    @NonNull
    @NotNull
    @Override
    public ContractAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item, parent, false);
        return new ContractAdapter.ViewHolder(v , iclickListner);


    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ContractAdapter.ViewHolder holder, int position) {
         holder.id.setText("client name  : "+ clients.get(0).getNom());
        holder.ref.setText("contract reference : "+String.valueOf(contracts.get(position).getReference()));
        holder.datedebut.setText("contract debut : "+contracts.get(position).getDatedebut());
        holder.datefin.setText("contract fin : "+contracts.get(position).getDatefin());
    }

    @Override
    public int getItemCount() {
        return this.contracts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView id, ref, datedebut , datefin;
        IclickListner iclickListner;
        public ViewHolder(@NonNull @NotNull View itemView , IclickListner iclickListner) {
            super(itemView);
            id = itemView.findViewById(R.id.itemid);
            ref = itemView.findViewById(R.id.itemref);
            datedebut = itemView.findViewById(R.id.itemdatedebut);
            datefin = itemView.findViewById(R.id.itemdatefin);
            this.iclickListner = iclickListner;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
                iclickListner.onSelectItem(getAdapterPosition());
        }
    }

    public interface IclickListner{
        void onSelectItem(int position);
    }





}
