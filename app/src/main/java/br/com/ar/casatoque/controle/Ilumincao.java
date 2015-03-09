
package br.com.ar.casatoque.controle;

import br.com.ar.casatoque.R;

import br.com.ar.casatoque.comum.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
import android.widget.Button;


public class Ilumincao extends Fragment implements OnClickListener {
	 Dialog showDialog = new Dialog();//instanciando mensagens
	 //private Animation imcAnim;
	
	 //imcAnim = AnimationUtils.loadAnimation(Iluminacao.this, R.anim.fade);
	
	 public Ilumincao(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
        View rootView = inflater.inflate(R.layout.iluminacao, container, false);
       
        //autenticar();
  		
       
  		Button lampadaBtn = (Button) rootView.findViewById(R.id.botaoLampada);

  		lampadaBtn.setOnClickListener(this);

        //Dialog simples
        Button DialSimp = (Button) rootView.findViewById(R.id.btDialSimp);
        
        DialSimp.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	
            	showDialog.ShowDialogContrluz(getActivity(), "Controle de Luminosidade");
            }
        });
        
       
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
		
		case R.id.botaoLampada:
			i = new Intent(getActivity(), Lampadas.class);
			break;
/**		
		case R.id.botaoMidia:
			Dialog midia = new Dialog();
        	midia.ShowDialogContrluz(getActivity(), "Midia");
			break;

		case R.id.botaoPerfis:
			i = new Intent(getActivity(), Perfis.class);
			break;
**/
/**
		case R.id.botaoConfig:
			i = new Intent(getActivity(), Configuracoes.class);
			requestCode = 1;
**/
		}	
		
		if(i != null){
			startActivityForResult(i, requestCode);
		}		
	}
}//fim
	

