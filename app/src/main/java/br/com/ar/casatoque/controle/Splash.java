
package br.com.ar.casatoque.controle;

import br.com.ar.casatoque.R;
import android.util.Log;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;

public class Splash extends Activity {

	private Thread mSplashThread;
	private boolean mblnClicou = false;

	/** Evento chamado quando a activity e executada pela primeira vez */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		//esse codigo faz com que o usuario nao consiga girar o aplicativo.
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		// thread para mostrar uma tela de Splash
		mSplashThread = new Thread() {
			@Override
			public void run() {
				try {
					synchronized (this) {
						// Espera por 5 segundos ou sai quando o usuario tocar na tela wait(5000);
						wait(5000);
						mblnClicou = true;
					}
				} catch (InterruptedException ex) {
                    Log.e("Erro","Erro ao ao carregar o metodo splash");
				}

				if (mblnClicou) {
                    Log.v("Logger","clicou na tela de splash");
					// fechar a tela de Splash testeee
					finish();

					// Carrega a Activity Principal
					Intent i = new Intent(getApplicationContext(), EntrarActivity.class);
					startActivity(i);
				}
			}
		};

		mSplashThread.start();
	}

	@Override
	public void onPause() {
		super.onPause();
		// garante que quando o usuario clicar no botao "Voltar" o sistema deve finalizar a thread
		mSplashThread.interrupt();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// o metodo abaixo esta relacionado a thread de splash
			synchronized (mSplashThread) {
				mblnClicou = true;
				// o metodo abaixo finaliza o comando wait mesmo que ele nao tenha terminado sua espera
                Log.v("Logger", "sair da tela splah");
				mSplashThread.notifyAll();
			}
		}
		return true;
	}

}