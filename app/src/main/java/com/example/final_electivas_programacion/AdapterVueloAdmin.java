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

public class AdapterVueloAdmin extends RecyclerView.Adapter<AdapterVueloAdmin.MyViewHolder>{
    private Context context;
    private ArrayList nombres, ids, fechaOrigen, fechaDestino, precioVuelos;
    private Activity activity;
    private String modoUso, cantPasajeros, tarifa;


    AdapterVueloAdmin(Activity activity, Context context, ArrayList codigo, ArrayList ids, String modoUso, String cantPasajeros, ArrayList fechaOrigen, ArrayList fechaDestino, ArrayList precioVuelos, String tarifa)
    {
        this.activity = activity;
        this.context = context;
        this.nombres = codigo;
        this.ids = ids;
        this.modoUso = modoUso;
        this.cantPasajeros = cantPasajeros;
        this.fechaOrigen = fechaOrigen;
        this.fechaDestino = fechaDestino;
        this.precioVuelos = precioVuelos;
        this.tarifa = tarifa;

    }
    @NonNull
    @Override
    public AdapterVueloAdmin.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if(modoUso.equals("user"))
            view = inflater.inflate(R.layout.activity_fila_buscar_vuelos, parent,false);
        else
            view = inflater.inflate(R.layout.activity_fila_editar_vuelos, parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterVueloAdmin.MyViewHolder holder, int position) {
        if(modoUso.equals("user"))
        {
            String[] partesFecha = fechaOrigen.get(position).toString().split(" ");
            holder.fechaOrigen.setText(partesFecha[0]);
            holder.horaOrigen.setText(partesFecha[1]);
            partesFecha = fechaDestino.get(position).toString().split(" ");
            holder.fechaDestino.setText(partesFecha[0]);
            holder.horaDestino.setText(partesFecha[1]);
            holder.precioVuelo.setText("$ "+precioVuelos.get(position));

            holder.searchLayout.setOnClickListener(view -> {
                Intent intent = new Intent(context, PantallaReservarVuelo.class);
                intent.putExtra("reservar_vuelo", true);
                intent.putExtra("codigo_vuelo", String.valueOf(nombres.get(position)));
                intent.putExtra("cant_pasajeros", cantPasajeros);
                intent.putExtra("precio_vuelo", String.valueOf(precioVuelos.get(position)));
                intent.putExtra("tarifa", tarifa);
                activity.startActivityForResult(intent, 1);
            });
        }
        else
        {
            holder.nombres.setText(String.valueOf(nombres.get(position)));
            holder.ids.setText(String.valueOf(ids.get(position)));
            holder.mainLayout.setOnClickListener(view -> {

                Intent intent = new Intent(context, PantallaVuelos.class);
                intent.putExtra("editar", true);
                intent.putExtra("codigo_vuelo", String.valueOf(nombres.get(position)));
                activity.startActivityForResult(intent, 1);
            });
        }

    }

    @Override
    public int getItemCount() {
        return ids.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nombres, ids, fechaOrigen, fechaDestino, horaOrigen, horaDestino, precioVuelo;
        LinearLayout mainLayout, searchLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            if(modoUso.equals("user"))
            {
                fechaOrigen = itemView.findViewById(R.id.textViewFechaOrigenBV);
                fechaDestino = itemView.findViewById(R.id.textViewFechaDestinoBV);
                horaOrigen = itemView.findViewById(R.id.textViewhoraOrigenBV);
                horaDestino = itemView.findViewById(R.id.textViewHoraDestinoBV);
                precioVuelo = itemView.findViewById(R.id.textViewCodigoReservaBV);
                searchLayout = itemView.findViewById(R.id.searchLayout);
            }
            {
                ids = itemView.findViewById(R.id.id_vueloFV);
                nombres = itemView.findViewById(R.id.textViewNombreVueloMT);
                mainLayout = itemView.findViewById(R.id.mainLayout);
            }
        }
    }
}
