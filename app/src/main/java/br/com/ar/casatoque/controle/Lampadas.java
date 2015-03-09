
package br.com.ar.casatoque.controle;

import br.com.ar.casatoque.R;

import br.com.ar.casatoque.adapter.LampadaAdapter;
import br.com.ar.casatoque.comum.ConfConexao;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


@SuppressLint("ClickableViewAccessibility")
public class Lampadas extends Activity implements OnSeekBarChangeListener, OnClickListener, OnTouchListener {

	
	private ArrayList<String> enderecos;
	private ArrayList<String> nomes;
	private ArrayList<String> estados;
	private ArrayList<String> disps;
	
	private ListView lista;
	private LampadaAdapter adapter;
	private ConfConexao conexao;
	private ArrayList<Integer> limiares;
	private boolean mudou;
	private int seekPosition;
	private String seekResposnse;
	
	protected String usuario;
	protected String senha;
	protected String ip;
	protected String porta;
	protected boolean isHttp;
	
	public Lampadas(){
		nomes = new ArrayList<String>();
		estados = new ArrayList<String>();
		enderecos = new ArrayList<String>();
		disps = new ArrayList<String>();
		limiares = new ArrayList<Integer>();	
		mudou = false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lampadas);
		
		// se conseguir recuperar as informaces de conexao com o 
		// SAR entao envia pedido de autenticacao, se nao conseguir entao inicia a
		// tela de conexao
		 
		/**
		if (setInfoServidor() == true) {
usuario
			conexao = new Conexao(this, ip, porta, isHttp);
			String response = conexao.enviaMensagem(Conexao.MSG_AUTENTICACAO, , senha, "");

			if (response.equals(Conexao.MSG_ERRO)) {
				criarDialog(true);
			} else if (response.equals("")) {
				criarDialog(false);
			} else {
				lista = (ListView) findViewById(R.id.lampLista);
				this.updateAdapter();
			}
		}else{
		
			startActivity(new Intent(this, RegisterActivity.class));
			finish();
		}*/
	}
	
	@Override
    public boolean onCreateOptionsMenu(android.view.Menu menu){
    	super.onCreateOptionsMenu(menu);
    	menu.add(0,0,0,"Atualizar Lista");
    	menu.add(0,1,0,"Salvar Nomes");
		return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	super.onOptionsItemSelected(item);
    	switch(item.getItemId()){
    		case  0: this.updateAdapter();
    			     break;
    		
    		case  1: this.salvarNomes(); 
    	}
    	
    	return true;
    }

    /**
     * Metodo executado quando o usuario arrasta alguma SeekBar.
     * Envia o estado da SeekBar arrastada (0-100) para o SAR como 
     * valor de estado da lampada.
     */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		// verifica se a mudanca de progresso foi causada pelo usuario
		if (fromUser == true) {

			seekPosition = lista.getPositionForView(seekBar);
			
			// utilizado para diminuir a quantidade de mensagens enviadas em sequencia
			int limite = limiares.get(seekPosition);
			CheckBox box = (CheckBox)adapter.getBoxes().get(seekPosition);
			box.setChecked(true);
			
			seekResposnse = conexao.enviaMensagem(ConfConexao.MSG_CONTROLE, enderecos.get(seekPosition), ""+(progress*255/100), disps.get(seekPosition));

			if ((progress == 0) && (limite != 0)) {

				box.setBackgroundResource(R.drawable.lampapagada);
				box.setChecked(false);
				limiares.set(seekPosition, 0);
			} else if ((progress > 0) && (progress < 25) && (limite != 1)) {

				box.setBackgroundResource(R.drawable.lampacesa25);
				limiares.set(seekPosition, 1);
			} else if ((progress >= 25) && (progress < 50) && (limite != 2)) {

				box.setBackgroundResource(R.drawable.lampacesa50);
				limiares.set(seekPosition, 2);
			} else if ((progress >= 50) && (progress < 75) && (limite != 3)) {

				box.setBackgroundResource(R.drawable.lampacesa75);
				limiares.set(seekPosition, 3);
			} else if ((progress >= 75) && (progress < 100) && (limite != 4)) {

				box.setBackgroundResource(R.drawable.lampacesa100);
				limiares.set(seekPosition, 4);
			} else if ((progress == 100) && (limite != 5)) {

				box.setBackgroundResource(R.drawable.lampacesa100);
				limiares.set(seekPosition, 5);
			}
			
			estados.set(seekPosition, ""+(progress*255/100));
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
	}

	/**
	 * Metodo invocado quando o usario para de tocar em alguma SeekBar.
	 * Analisa a resposta do SAR e configura a cor do texto com o nome da 
	 * lampada dependendo da resposta recebida.
	 */
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if (seekResposnse.equals(ConfConexao.MSG_ERRO)) {
			((TextView)adapter.getLabels().get(seekPosition)).setTextColor(Color.RED);
			adapter.getCores().set(seekPosition, Color.RED);
		}else{
			((TextView)adapter.getLabels().get(seekPosition)).setTextColor(Color.WHITE);
			adapter.getCores().set(seekPosition, Color.WHITE);
		}
	}

	/**
	 * Metodo invocado quando algum CheckBox (imagem de lampada) for tocado.
	 * Alterna estado da lampada entre 0 e 100 porcento e o valor para o SAR.
	 */
	@Override
	public void onClick(View arg0) {
		
		CheckBox box = (CheckBox)arg0;
		int i = lista.getPositionForView(box);
		String response = "";
		int valor;
		int limite;
		int prog;

		if (box.isChecked()) {
			valor = 255;
			limite = 5;
			prog = 100;
			box.setBackgroundResource(R.drawable.lampacesa100);
		} else {
			valor = 0;
			limite = 0;
			prog = 0;
			box.setBackgroundResource(R.drawable.lampapagada);
		}	
		
		response = conexao.enviaMensagem(ConfConexao.MSG_CONTROLE, enderecos.get(i), ""+valor, disps.get(i));
		
		if (response.equals("") || response.equals(ConfConexao.MSG_ERRO)) {
			((TextView)adapter.getLabels().get(i)).setTextColor(Color.RED);
			adapter.getCores().set(i, Color.RED);
		}else{
			((TextView)adapter.getLabels().get(i)).setTextColor(Color.WHITE);
			adapter.getCores().set(i, Color.WHITE);
		}
		((SeekBar)adapter.getBarras().get(i)).setProgress(prog);
		estados.set(i, ""+valor);
		limiares.set(i, limite);
	}
	
	/**
	 * Requisita ao SAR a lista de lampadas ativas na rede, com essas informacoes atualiza a ListView.
	 */
	public void updateAdapter(){
		
		String base = conexao.enviaMensagem(ConfConexao.REQUEST_TIPO, "0", "100", "0");
		if(base.equals("")){
			criarDialog(false);
			return;
		}
		
		enderecos.clear();
		nomes.clear();
		estados.clear();
		disps.clear();
		
		limiares.clear();
		
		if (!base.equals("null")) {

			String[] partes = base.split("#");

			for (String temp : partes) {

				String[] inf = temp.split("%", 2);

				if (inf.length == 2) {
					String endereco = inf[0];

					inf = inf[1].split("%");

					for (String s : inf) {

						String[] vetor = s.split(":");

						enderecos.add(endereco);
						nomes.add(vetor[0]);
						disps.add(vetor[1]);
						estados.add(vetor[2]);

						limiares.add(-1);
					}
				}
			}
		}
		
		adapter = new LampadaAdapter(this);			
		lista.setAdapter(adapter);
			
	}
	
	/**
	 * Envia lista de nomes das lampadas para o SAR.
	 */
	public void salvarNomes(){
		
		String msg = "";
		String antigoAddr = "";
		
		if (enderecos.size() > 0) {
			if (enderecos.size() == nomes.size()) {
				for (int i = 0; i < enderecos.size(); i++) {
					if (enderecos.get(i).equals(antigoAddr)) {
						msg += "#" + nomes.get(i);
					} else {
						msg += "%" + enderecos.get(i) + "#" + nomes.get(i);
					}
					antigoAddr = enderecos.get(i);
				}
			}
			mudou = false;
		}
		
		if (!msg.equals("")) {
			String base = conexao.enviaMensagem(ConfConexao.MSG_SET_NOME, "0", msg.substring(1), "0");
			if (base.equals("")) {
				criarDialog(false);
			}
			if (base.equals(ConfConexao.MSG_ERRO)) {
				Toast.makeText(this, "Permissao negada!", Toast.LENGTH_LONG).show();
			}
			if (base.equals(ConfConexao.MSG_SUCESSO)) {
				Toast.makeText(this, "Nomes atualizados!", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	/**
	 * Configura a imagem de lampada de uma determinada CheckBox de acordo o valor do parametro progresso. 
	 * @param box CheckBox onde sera alterada a imagem
	 * @param progresso valor da luminosidade da lampada obtido da SeekBar
	 */
	public void setImagem(CheckBox box, int progresso){
		
		box.setChecked(true);
		
		if(progresso == 0){
			box.setBackgroundResource(R.drawable.lampapagada);
			box.setChecked(false);
		}else if((progresso > 0) && (progresso < 25)){
			box.setBackgroundResource(R.drawable.lampacesa25);
		}else if((progresso >= 25) && (progresso < 50)){
			box.setBackgroundResource(R.drawable.lampacesa50);
		}else if((progresso >= 50) && (progresso < 75)){
			box.setBackgroundResource(R.drawable.lampacesa75);
		}else{
			box.setBackgroundResource(R.drawable.lampacesa100);
		}
		
	}
	
	/**
	 * Recupera as informacoes necessarias para a conexao com o SAR.
	 * @return true se as informacoes foram obtidas com sucesso
	 */
	public boolean setInfoServidor(){
    	try {
			FileInputStream fIn = openFileInput("conf.dat");
			Scanner file_scan = new Scanner(fIn);

			if (file_scan.hasNext()) {
				usuario = file_scan.nextLine();
			}
			if (file_scan.hasNext()) {
				senha = file_scan.nextLine();
			}
			if (file_scan.hasNext()) {
				ip = file_scan.nextLine();
			}
			if (file_scan.hasNext()) {
				porta = file_scan.nextLine();
			}
			if (file_scan.hasNext()) {
				if (file_scan.nextLine().equals("true")) {
					isHttp = true;
				} else {
					isHttp = false;
				}
			}
			
			file_scan.close();
			fIn.close();

		} catch (IOException ioe) {		
			ioe.printStackTrace();		
			return false;
		}	
		
		return true;
    }
	
	/**
	 * Cria dialog para exibir mensagens de erro.
	 * @param isAutErro tipo de mensagem de erro exibida.
	 */
	public void criarDialog(boolean isAutErro){
		
		String mensagem = "";
		
		if(isAutErro == true){
			mensagem = "Erro na autenticacao";
		}else{
			mensagem = "Erro na conexao com o servidor!";
		}
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		//AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
				Lampadas.this.finish();
			}
		});
		dialog.setTitle("Erro");
		dialog.setMessage(mensagem);
		dialog.show();
    }
	
	/**
	 * @return lista com os nomes das lampadas
	 */
	public ArrayList<String> getNomes(){
		return nomes;
	}
	
	/**
	 * @return lista com os estados das lampadas
	 */
	public ArrayList<String> getEstados(){
		return estados;
	}

	/**
	 * Metodo invocado quando o usuario tocar no nome de alguma lampada.
	 * Cria dialog para que o usuario possa alterar o nome da lampada tocada.
	 */
	@Override
	public boolean onTouch(final View textView, MotionEvent arg1) {
		
		final int position = lista.getPositionForView(textView);
							
		LayoutInflater layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.edit_nome, null);
			
		final EditText etNome = (EditText)view.findViewById(R.id.etEditNome);
			
		AlertDialog.Builder dialogEditNome = new AlertDialog.Builder(this);
		dialogEditNome.setPositiveButton("Alterar", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
				((TextView)textView).setText(etNome.getText().toString());
				nomes.set(position, etNome.getText().toString());
				mudou = true;
			}
		});
		dialogEditNome.setPositiveButton("Cancelar", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){}
		});
		dialogEditNome.setTitle("Editar Nome");
		dialogEditNome.setView(view);
		
		etNome.setText(nomes.get(position));		
		dialogEditNome.show();
		
		return false;
	}

	/**
	 * Cria dialog com a opcao enviar a lista com os nomes das
	 * lampadas para o SAR caso o botao 'voltar' tenha sido pressionado 
	 * e alguma alteracao tenha sido feita.
	 */
	public boolean onKeyDown (int keyCode, KeyEvent event){
    	if((keyCode == KeyEvent.KEYCODE_BACK) && (mudou == true)){
    		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    		dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener(){
    			public void onClick(DialogInterface dialog, int whichButton){		 					
    				salvarNomes();
    				finish();
    			}
    		});
    		dialog.setPositiveButton("Nao", new DialogInterface.OnClickListener(){
    			public void onClick(DialogInterface dialog, int whichButton){
    				finish();
    			}
    		});
    		dialog.setTitle("Salvar Nomes");
    		dialog.setMessage("Voce deseja salvar as alteracoes?");
    		dialog.show();
    		return false;
    	}else{   	
    		return super.onKeyDown(keyCode, event);
    	}
    }

}