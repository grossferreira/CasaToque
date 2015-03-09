
package br.com.ar.casatoque.controle;

import br.com.ar.casatoque.R;

import br.com.ar.casatoque.comum.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

public class HomeFragment extends Fragment {
    private Dialog showDialog = new Dialog();// instanciando as mensagens

	public HomeFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);



        return rootView;
    }
}
