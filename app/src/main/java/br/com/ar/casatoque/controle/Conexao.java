

package br.com.ar.casatoque.controle;

import br.com.ar.casatoque.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import br.com.ar.casatoque.comum.ConfConexao;
import br.com.ar.casatoque.comum.Dialog;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;


public class Conexao extends Activity implements OnClickListener, OnKeyListener{
	
	EditText usuario;
	EditText senha;
	EditText endereco;
	EditText porta;
	RadioButton httpButton;
	RadioButton httpsButton;
	
	private ConfConexao conexao;

	private Dialog showDialog = new Dialog();// instanciando as mensagens
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.conexao);
        //esse codigo faz com que o usuario nao consiga girar o aplicativo.
      	this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        TextView loginScreen = (TextView) findViewById(R.id.confConexao);
        
        usuario = (EditText)findViewById(R.id.etUsuario);
        senha = (EditText)findViewById(R.id.etSenha);
        endereco = (EditText)findViewById(R.id.etEndereco);
        porta = (EditText)findViewById(R.id.etPorta);
        httpButton = (RadioButton)findViewById(R.id.httpPorta);
        httpsButton = (RadioButton)findViewById(R.id.httpsPorta);
               
        porta.setOnKeyListener(this);
        httpButton.setOnClickListener(this);
        httpsButton.setOnClickListener(this);
        
        Button salvar = (Button)findViewById(R.id.btnRegister);     
        salvar.setOnClickListener(this);
        
        recuperarDados();
        
        
        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				// Switching to Login Screen/closing register screen
				finish();
			}
		});
    }
	
	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		
		case R.id.btnRegister: salvarDados();						  
		break;
			
		case R.id.httpPorta: porta.setText("8080"); 
		break;
			
		case R.id.httpsPorta: porta.setText("8181");
			
		}		
		
	}
	
	/**
	 * Grava as informacoes inseridas pelo usuario.
	 */
	private void salvarDados() {
		String u = usuario.getText().toString();
		String s = senha.getText().toString();
		String e = endereco.getText().toString();
		String p = porta.getText().toString();
		
		if (!u.equals("") && !s.equals("") && !e.equals("") && !p.equals("")) {
			conexao = new ConfConexao(this, e, p, httpButton.isChecked());

			// Salva as informacoes no arquivo de configuracoes
			try {
				FileOutputStream fOut = openFileOutput("conf.dat", MODE_PRIVATE);
				OutputStreamWriter osw = new OutputStreamWriter(fOut);

				osw.write(u + "\n" + s + "\n" + e + "\n" + p + "\n" + httpButton.isChecked());

				osw.close();
				fOut.close();
				
				String response = conexao.enviaMensagem(ConfConexao.MSG_AUTENTICACAO, u, s, "0");
				
				if(response.equals(ConfConexao.MSG_ERRO)){
					showDialog.Autenticacao(this, "Erro", "Erro na autenticacao");
				}else if(response.equals("")){
					showDialog.ConexaoServidor(this, "Erro", "Erro na conexao com o servidor!");
				}else{
					this.setResult(Activity.RESULT_OK);
					this.finish();
				}

			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}else{
			showDialog.aviso(getApplicationContext(), "Aviso", "E obrigatario o preenchimento de todos os campos!");
			
		}
	}
	
	/**
	 * Restaura informacoes da tela gravadas em arquivo.
	 */
	private void recuperarDados() {
		// Abre arquivo de configuracoes para configurar atributos
		try {
			FileInputStream fIn = openFileInput("conf.dat");
			Scanner file_scan = new Scanner(fIn);

			if (file_scan.hasNext()) {
				usuario.setText(file_scan.nextLine());
			}
			if (file_scan.hasNext()) {
				senha.setText(file_scan.nextLine());
			}
			if (file_scan.hasNext()) {
				endereco.setText(file_scan.nextLine());
			}
			if (file_scan.hasNext()) {
				porta.setText(file_scan.nextLine());
			}
			if (file_scan.hasNext()) {
				if (file_scan.nextLine().equals("true")) {
					httpButton.setChecked(true);
				} else {
					httpsButton.setChecked(true);
				}
			}
			
			file_scan.close();
			fIn.close();

		} catch (IOException ioe) {
			ioe.printStackTrace();
			porta.setText("8080");
			httpButton.setChecked(true);
		}		
	}
	
	@Override
	public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
		
		if(porta.getText().toString().equals("8080")){
			httpButton.setChecked(true);
		}else if(porta.getText().toString().equals("8181")){
			httpsButton.setChecked(true);
		}
		
		return false;
	}
	
	public boolean onKeyDown (int keyCode, KeyEvent event){
    	if(keyCode == KeyEvent.KEYCODE_BACK){
    		
    		this.setResult(Activity.RESULT_CANCELED);
    		finish();  	
    	}  	
    		
    	return super.onKeyDown(keyCode, event);  	
    }
}