
package br.com.ar.casatoque.controle;

import br.com.ar.casatoque.R;

import br.com.ar.casatoque.comum.Dialog;
import android.util.Log;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
//import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
	
	Dialog msg = new Dialog(); //instanciando a classe dialog para gerar a mensagem no login
	
	//declarcao de variaveis login;
	private EditText usuario;  //declando a variavel usuario;
	private EditText senha;    //declarando a variavel senha;
	//private CheckBox checkBox; //declarando a variavel check box;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //esse codigo faz com que o usuario nao consiga girar o aplicativo.
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
       
        //botao que verificar se o login e velido e chama a pagina principal
        final Button btnLogin = (Button) findViewById(R.id.btnLogin);   
        	
            btnLogin.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {

                    Log.v("Login","Recuperando usuario e senha da tela");
                	usuario = (EditText) findViewById(R.id.etLogin); //pegando o valor do campo na tela e enviando para a variavel;
                    senha = (EditText) findViewById(R.id.etSenha);   
                  //  checkBox = (CheckBox) findViewById(R.id.lembLogin);
                	if(usuario.getText().toString().equals("") || senha.getText().toString().equals("")){
                		// btnLogin.setEnabled(false);
                		msg.aviso(LoginActivity.this, "Aviso", "Todos os campos devem ser preenchidos!");
                	}else
                		 if(usuario.getText().toString().equals("admin") && senha.getText().toString().equals("admin")){
                    		  Log.v("Login","Inicinado a tela principal do sistema apos o login");
                    		  Intent i = new Intent(getApplicationContext(), Principal.class);
                    		  startActivity(i);
                    		 
                		 }
                		 else{
                			 msg.aviso(LoginActivity.this, "Aviso", "Login ou senha incorreto(s)!");
                		 }
                	 
                }
            });
            
    
        TextView registerScreen = (TextView) findViewById(R.id.link_to_register);
        
        //Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// Switching to Register screen
				Intent i = new Intent(getApplicationContext(), Conexao.class);
				startActivity(i);
			}
		});
    }
}