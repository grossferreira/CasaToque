
package br.com.ar.casatoque.menu;

import br.com.ar.casatoque.R;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

public class Tutorial extends Activity {
@Override
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.tutorial);
	//esse codigo faz com que o usuario nao consiga girar o aplicativo.
  	this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

   /** @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode== KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){
           // Toast.makeText(this, "Teste Voltar", Toast.LENGTH_SHORT).show();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right); //transicao entre as telas
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);

    }*/
}