
package br.com.ar.casatoque.controle;

import br.com.ar.casatoque.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Irrigacao extends Fragment {
	
	public Irrigacao(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.irrigacao, container, false);
         
        return rootView;
    }
}
