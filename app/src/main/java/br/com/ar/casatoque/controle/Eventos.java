package br.com.ar.casatoque.controle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.ar.casatoque.R;


/**
 * Created by Rodrigo on 07/02/2015.
 */

    public class Eventos extends Activity {
        Button btnentrar;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.eventos);

        }
    }