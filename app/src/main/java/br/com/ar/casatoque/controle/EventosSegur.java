package br.com.ar.casatoque.controle;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.ar.casatoque.R;
import br.com.ar.casatoque.adapter.EventosSegurListAdapter;


/**
 * Created by Rodrigo on 07/02/2015.
 */

    public class EventosSegur extends Activity {
        private ListView listViewEventosSegur;
        private Context ctx;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.eventos_segur);
            //esse codigo faz com que o usuario nao consiga girar o aplicativo.
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //chamada do metodo que preenche a lista de eventos de seguranca
            ListViewEnventosSegur();
        }

    public void ListViewEnventosSegur(){//String titulo, String descr, String nomImagem, String data
        ctx=this;
        List<br.com.ar.casatoque.modelo.EventosSegur> legendList= new ArrayList<br.com.ar.casatoque.modelo.EventosSegur>();
        legendList.add(new br.com.ar.casatoque.modelo.EventosSegur("Tentativa de Invasão","Tentativa de violação das portas dos fundos.","iceventossegur","18/03/15 20:17:00"));
        legendList.add(new br.com.ar.casatoque.modelo.EventosSegur("Invasão","Foi violada a porta principal.","iceventossegur","18/03/15 20:17:00"));
        legendList.add(new br.com.ar.casatoque.modelo.EventosSegur("Tentativa de Invasão","Violação a porta principal.","iceventossegur","18/03/15 20:17:00"));
        legendList.add(new br.com.ar.casatoque.modelo.EventosSegur("Tentativa de Invasão","Violação a portão principal.","iceventossegur","18/03/15 20:17:00"));
        legendList.add(new br.com.ar.casatoque.modelo.EventosSegur("Tentativa de Invasão","Violação a porta garagem.","iceventossegur","18/03/15 20:17:00"));
        legendList.add(new br.com.ar.casatoque.modelo.EventosSegur("Tentativa de Invasão","Violação a porta principal.","iceventossegur","18/03/15 20:17:00"));
        legendList.add(new br.com.ar.casatoque.modelo.EventosSegur("Tentativa de Invasão","Violação a porta principal.","iceventossegur","18/03/15 20:17:00"));
        legendList.add(new br.com.ar.casatoque.modelo.EventosSegur("Tentativa de Invasão","Violação a porta principal.","iceventossegur","18/03/15 20:17:00"));
        legendList.add(new br.com.ar.casatoque.modelo.EventosSegur("Tentativa de Invasão","Violação a porta principal.","iceventossegur","18/03/15 20:17:00"));
        legendList.add(new br.com.ar.casatoque.modelo.EventosSegur("Tentativa de Invasão","Violação a porta principal.","iceventossegur","18/03/15 20:17:00"));


        listViewEventosSegur = ( ListView ) findViewById( R.id.FootballLegend_list);
        listViewEventosSegur.setAdapter( new EventosSegurListAdapter(ctx, R.layout.legend_row_item_eventos, legendList ) );

        // Click event for single list row
        listViewEventosSegur.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                br.com.ar.casatoque.modelo.EventosSegur evento = (br.com.ar.casatoque.modelo.EventosSegur) parent.getItemAtPosition(position);
                Toast.makeText(EventosSegur.this, evento.getName().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
