
package br.com.ar.casatoque.menu;

import br.com.ar.casatoque.R;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class CentralAjuda extends Activity {
@Override
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.centajuda);
	//esse codigo faz com que o usuario nao consiga girar o aplicativo.
  	this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


}