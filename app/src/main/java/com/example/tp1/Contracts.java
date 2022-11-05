package com.example.tp1;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Contracts extends AppCompatActivity {
    Button add , list ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contracts);
    add = findViewById(R.id.btnaddcontrat);

    list = findViewById(R.id.btnlistcontrat);

    add.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(), AjoutContracts.class));
        }
    });

    list.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext() , ListContrat.class));
        }
    });

    }
}