package br.com.ar.casatoque.controle;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.ar.casatoque.R;
import br.com.ar.casatoque.adapter.EventosSegurListAdapter;
import br.com.ar.casatoque.modelo.EventosSegur;


/**
 * Created by Rodrigo on 07/02/2015.
 */

    public class Eventos extends Activity {
        private ListView listViewFootballLegend;
        private Context ctx;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.eventos);

            //chamada do metodo que preenche a lista de eventos de seguranca
            ListViewEnventosSegur();
        }

    public void ListViewEnventosSegur(){//String titulo, String descr, String nomImagem, String data
        ctx=this;
        List<EventosSegur> legendList= new ArrayList<EventosSegur>();
        legendList.add(new EventosSegur("Tentativa de Invasão","Tentativa de violação das portas dos fundos.","iceventossegur","brazil"));
        legendList.add(new EventosSegur("Invasão","Foi violada a porta principal.","iceventossegur","brazil"));
        legendList.add(new EventosSegur("Johan Cruyff","April 25, 1947 (age 65)","iceventossegur","brazil"));
        legendList.add(new EventosSegur("Franz Beckenbauer","September 11, 1945 (age 67)","iceventossegur","brazil"));
        legendList.add(new EventosSegur("Michel Platini","June 21, 1955 (age 57)","iceventossegur","brazil"));
        legendList.add(new EventosSegur("Ronaldo De Lima","September 22, 1976 (age 36)","iceventossegur","brazil"));

        listViewFootballLegend = ( ListView ) findViewById( R.id.FootballLegend_list);
        listViewFootballLegend.setAdapter( new EventosSegurListAdapter(ctx, R.layout.legend_row_item_eventos, legendList ) );

        // Click event for single list row
        listViewFootballLegend.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventosSegur o = (EventosSegur) parent.getItemAtPosition(position);
                Toast.makeText(Eventos.this, o.getName().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
