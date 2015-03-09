package br.com.ar.casatoque.controle;

import br.com.ar.casatoque.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/**
 * Created by Rodrigo on 07/02/2015.
 */

    public class EntrarActivity extends Activity {
        Button btnentrar;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.entrar);

            btnentrar = (Button) findViewById(R.id.btEntrar);


            btnentrar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), LoginActivity.class);

                    startActivity(intent);

                    finish();
                }
            });

        }
    }