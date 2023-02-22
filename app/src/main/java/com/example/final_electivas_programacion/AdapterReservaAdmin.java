package com.example.final_electivas_programacion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class AdapterReservaAdmin extends RecyclerView.Adapter<AdapterReservaAdmin.MyViewHolder>{
    private Context context;
    private ArrayList nombres, ids, fechaOrigen, fechaDestino, precioVuelos;
    private Activity activity;
    private String modoUso, cantPasajeros;


    AdapterReservaAdmin(Activity activity, Context context, ArrayList ids, ArrayList codigo)
    {
        this.activity = activity;
        this.context = context;
        this.nombres = codigo;
        this.ids = ids;

    }
    @NonNull
    @Override
    public AdapterReservaAdmin.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        view = inflater.inflate(R.layout.activity_fila_buscar_reserva, parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterReservaAdmin.MyViewHolder holder, int position) {
        holder.nombres.setText(String.valueOf(nombres.get(position)));
        holder.mainLayout.setOnClickListener(view -> {

            Intent intent = new Intent(context, PantallaReservarVuelo.class);
            intent.putExtra("editar", true);
            intent.putExtra("codigo_reserva", String.valueOf(nombres.get(position)));
            activity.startActivityForResult(intent, 1);
        });
    }

    @Override
    public int getItemCount() {
        return nombres.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nombres, ids, fechaOrigen, fechaDestino, horaOrigen, horaDestino, precioVuelo;
        LinearLayout mainLayout, searchLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            {
                ids = itemView.findViewById(R.id.textViewCodigoReservaBV);
                nombres = itemView.findViewById(R.id.textViewCodigoReservaBV);
                mainLayout = itemView.findViewById(R.id.searchRevLayout);
            }
        }
    }
}
