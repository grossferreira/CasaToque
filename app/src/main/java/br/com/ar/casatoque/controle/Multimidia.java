package br.com.ar.casatoque.controle;

import br.com.ar.casatoque.R;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Multimidia extends Fragment {

    public Multimidia() {
    }


    Intent i = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.multimidia, container, false);

        Button btradio = (Button) rootView.findViewById(R.id.btMultRadio);
        btradio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

               /** i = new Intent(getActivity(), Tutorial.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right); //transicao entre as telas
            */
            }
        });

        Button musica = (Button) rootView.findViewById(R.id.btMultMusica);
        musica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

               //  i = new Intent(getActivity(), MusicaPlayer.class);
                // startActivity(i);
                 //overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right); //transicao entre as telas

            }
        });
        return rootView;
    }
}