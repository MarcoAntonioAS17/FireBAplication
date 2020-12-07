package com.example.firebapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ToolbarWidgetWrapper;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

public class MainClass extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        recyclerView = findViewById(R.id.rvList);

        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));

        Vector<Individuo> vIndividuo = new Vector<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference people = database.getReference("Usuarios");

        people.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item: snapshot.getChildren()){
                    Individuo plp = item.getValue(Individuo.class);
                    vIndividuo.add(plp);
                    Log.d("TYAM",plp.Name);
                }
                recyclerView.setAdapter(new MyAdapter (vIndividuo));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TYAM",error.getMessage());

            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opciones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.accion_registrar:
                Intent intent = new Intent(MainClass.this,FormClass.class);
                startActivity(intent);
                return true;
        }


        return false;
    }
}

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    Vector<Individuo> data;

    public MyAdapter (Vector<Individuo> data){
        this.data = data;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate(R.layout.listitem,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Individuo individuo = data.get(position);

        holder.tvName.setText(individuo.Name);
        holder.tvLastName.setText(individuo.Last_Name);
        holder.tvAge.setText(individuo.Age);
        holder.tvAddress.setText(individuo.Address);
        holder.tvPhone.setText(individuo.Phone);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvLastName, tvAge, tvAddress, tvPhone;
        ImageView ivPerfil;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.item_Name);
            tvLastName = itemView.findViewById(R.id.item_LastName);
            tvAge = itemView.findViewById(R.id.item_Age);
            tvAddress = itemView.findViewById(R.id.item_Address);
            tvPhone = itemView.findViewById(R.id.item_Phone);
            ivPerfil = itemView.findViewById(R.id.item_perfil);
        }
    }
}