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

    public class Eventos extends Activity {
        private ListView listViewEventosSegur;
        private Context ctx;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.eventos);
            //esse codigo faz com que o usuario nao consiga girar o aplicativo.
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }
}
