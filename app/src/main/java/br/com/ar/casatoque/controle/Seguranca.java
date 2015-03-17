
package br.com.ar.casatoque.controle;

import br.com.ar.casatoque.R;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnClickListener;


public class Seguranca extends Fragment implements OnClickListener {
	
	public Seguranca(){}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.seguranca, container, false);

        //chamada da tela de alame da residencia
        Button btnAlarme = (Button) rootView.findViewById(R.id.btAlarme);
        btnAlarme.setOnClickListener(this);

        //chamada da tela de alame da residencia
        Button btnEventos = (Button) rootView.findViewById(R.id.btEventos);
        btnEventos.setOnClickListener(this);

        return rootView;
    }

    /**
     * Inicia determinada Activity dependendo do botao clicado
     */
    @Override
    public void onClick(View v) {
        Intent i = null;
        int requestCode = 0;

        switch (v.getId()) {

            case R.id.btAlarme:
                i = new Intent(getActivity(), Alarme.class);
            break;

            case R.id.btEventos:
                i = new Intent(getActivity(), Eventos.class);
            break;

        }

        if(i != null){
            startActivityForResult(i, requestCode);
        }
    }
}//fim
