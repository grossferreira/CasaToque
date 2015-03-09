

package br.com.ar.casatoque.controle;

import br.com.ar.casatoque.R;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;


public class Configuracoes extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuracoes);
        //esse codigo faz com que o usuario nao consiga girar o aplicativo.
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


    }
}

