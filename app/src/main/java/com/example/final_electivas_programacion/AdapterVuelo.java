package com.example.final_electivas_programacion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.final_electivas_programacion.R;

import java.util.ArrayList;

public class AdapterVuelo extends RecyclerView.Adapter<AdapterVuelo.MyViewHolder>{
    private Context context;
    private ArrayList nombres, ids;


    AdapterVuelo(Context context, ArrayList codigo, ArrayList ids)
    {
        this.context = context;
        this.nombres = codigo;
        this.ids = ids;

    }
    @NonNull
    @Override
    public AdapterVuelo.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_fila_vuelo, parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterVuelo.MyViewHolder holder, int position) {
        holder.nombres.setText(String.valueOf(nombres.get(position)));
        holder.ids.setText(String.valueOf(ids.get(position)));
    }

    @Override
    public int getItemCount() {
        return ids.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nombres, ids;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ids = itemView.findViewById(R.id.id_vueloFV);
            nombres = itemView.findViewById(R.id.textViewNombreVueloMT);
        }
    }
}
