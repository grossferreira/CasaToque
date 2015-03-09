
package br.com.ar.casatoque.controle;

import br.com.ar.casatoque.R;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ListView;
import br.com.ar.casatoque.adapter.NavDrawerListAdapter;


import br.com.ar.casatoque.comum.*;

import br.com.ar.casatoque.menu.CentralAjuda;
import br.com.ar.casatoque.menu.PoliticasPrivacidade;
import br.com.ar.casatoque.menu.Tutorial;
import br.com.ar.casatoque.modelo.NavDrawerItem;
import android.content.Context;
import android.net.ConnectivityManager;

public class Principal extends FragmentActivity {
	private ConfConexao conexao;
	private String usuario;
	private String senha;
	private String ip;
	private String porta;
	private boolean isHttp;
	private AlertDialog dialogVar;

	// /////////////////////////////////////
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;
	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	private Dialog showDialog = new Dialog();// instanciando as mensagens

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//esse codigo faz com que o usuario nao consiga girar o aplicativo.
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		verificaConexao();
		// autenticar();
		
		//////////////////////////
		//Notificacao
	     // Texto que aparecera na barra de status (chamada para a notificacao)
	      String tickerText = "Voce recebeu uma mensagem.";
	 
	        // Detalhes da notificacao
	      CharSequence titulo = "Cena 1";
	       CharSequence mensagem = "Todas as Luzes dos Quartos Foram apagadas";
	 
	        // Exibe a notificacao
	        
	        criarNotificacao(this, tickerText, titulo, mensagem, Multimidia.class);
	

	
		// ////////////////////////////

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		// Iluminacao
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// Irrigacao
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// Climatizacao
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "2"));
		// Multimidia
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		// Seguranca
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "23+"));
		// navDrawerItems.add(new NavDrawerItem(navMenuTitles[6],
		// navMenuIcons.getResourceId(6, -1), true, "23+"));

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
	}// onCreat

/////////////////////////////////// notifiacao
	//protected void criarNotificacao(Context context, CharSequence msnBarStatus, CharSequence titulo, CharSequence mensagem, Class activity) {
	protected void criarNotificacao(Context context, CharSequence msnBarStatus, CharSequence titulo, CharSequence mensagem, Class<Multimidia> activity) {
        // Recupera o servico do NotificationManager
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification n = new Notification(R.drawable.ic_communities, msnBarStatus, System.currentTimeMillis());
        
        //Notification n = new Notification(R.drawable.ic_communities, mensagemBarraStatus, System.currentTimeMillis());
        // PendingIntent para executar a Activity se o usuario selecionar a notificacao
        PendingIntent p = PendingIntent.getActivity(this, 0, new Intent(this, Climatizacao.class), 0);
 
        // Flag utilizada para remover a notificacao da barra de status
        // quando o usuario clicar nela
        n.flags |= Notification.FLAG_AUTO_CANCEL;
 
        // Informacoes
        n.setLatestEventInfo(this, titulo, mensagem, p);
  
        // Espera 100ms e vibra por 250ms, espera por mais 100ms e vibra por 500ms
        n.vibrate = new long[] { 100, 250, 100, 500 };
 
        // id da notificacao
        nm.notify(R.string.app_name, n);
    }
	
	
	///////////////////////////////////
	
	
	
	/**
	 * Envia mensagem de autenticacao ao SAR caso ja tenham sido feitas as
	 * conexao, se nao, inicia a Activity de conexao
	 */

	public void autenticar() {
		// configura atributos para conexao
		if (setInfoServidor() == true) {

			conexao = new ConfConexao(this, ip, porta, isHttp);
			String response = conexao.enviaMensagem(ConfConexao.MSG_AUTENTICACAO,usuario, senha, "");

			if (response.equals(ConfConexao.MSG_ERRO)) {
				showDialog.Autenticacao(Principal.this, "Erro", "Erro na autenticacao");

			}
			if (response.equals(ConfConexao.MSG_SUCESSO)) {
				Toast.makeText(this, "Conexao efetuada com sucesso!",Toast.LENGTH_SHORT).show();
			} else if (response.equals("")) {
				showDialog.ConexaoServidor(Principal.this, "Erro", "Erro na conexao com o servidor!");
			}
		} else {
			startActivity(new Intent(this, Conexao.class));
		}
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// display view para selecionar o item do nav drawer
			displayView(position);
		}
	}

	/**
	 * Carrega os itens do menu atraves do menu.xml
	 * */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	/**
	 * carrega a pagina do menu de acordo com a opcao clicada.
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		Intent i = null;
		// Handle action bar actions click
		switch (item.getItemId()) {
		
		    case R.id.configuracao:
			    i = new Intent(getApplicationContext(), Configuracoes.class);
			    startActivity(i);
			    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right); //transicao entre as telas
			break;

		    case R.id.politPrivacidade:
			    i = new Intent(getApplicationContext(), PoliticasPrivacidade.class);
			    startActivity(i);
			    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right); //transicao entre as telas
			break;

		    case R.id.centralAjuda:
			    i = new Intent(getApplicationContext(), CentralAjuda.class);
			    startActivity(i);
			    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right); //transicao entre as telas
			break;

		    case R.id.tutorial:
		    	i = new Intent(getApplicationContext(), Tutorial.class);
		    	startActivity(i);
		    	overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right); //transicao entre as telas
			break;
			
		    case R.id.sobre:
			    //i = new Intent(this, Sobre.class);
			    //startActivity(i);
			    showDialog.aviso(getApplicationContext(), "SOBRE", "Autor: Rodrigo Gross");
			break;

            case R.id.sair:
                //showDialog.showDialogBtEscolhas(getApplicationContext(),"SAIR","Deseja Realente SAIR?");
                Toast.makeText(this, "Teste SAIR",Toast.LENGTH_SHORT).show();


        }

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Seleciona qual o fragmento sera iniciado (instanciado)
	 * */
	private void displayView(int position) {
		// atualiza o conteudo principal, substituindo o fragmento
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			fragment = new Ilumincao();
			break;
		case 2:
			fragment = new Irrigacao();
			break;
		case 3:
			fragment = new Climatizacao();
			break;
		case 4:
			fragment = new Multimidia();
			break;
		case 5:
			fragment = new Seguranca();
			break;

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// erro na criacao do fragmento
			Log.e("MainActivity", "Erro na cricao do fragmento");
		}
	}

	/**
	 * Exibe o titulo do fragmento de acordo com o solecionado.
	 * */
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	// //////////////////////////////////////////////////////////////////
	/**
	 * Cria dialog para configurar a varredura, tambem envia mensagens de ativacao ou desativacao da varredura ao SAR
	 */
	public void varDialog() {

		if (dialogVar == null) {

			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			View view = layoutInflater.inflate(R.layout.edit_var, null);

			final EditText time = (EditText) view.findViewById(R.id.etProg);

			AlertDialog.Builder dialogVar = new AlertDialog.Builder(this);
			dialogVar.setPositiveButton("Ativar", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
								if (!time.getText().toString().equals("")) {
								String response = conexao.enviaMensagem(ConfConexao.SET_VARREDURA, "0", "1:"+ time.getText().toString(),"0");

								if (response.equals(ConfConexao.MSG_ERRO)) {
									showDialog.aviso(Principal.this, "Aviso", "Permissao negada!");
									//Toast.makeText(Principal.this,"Permissao negada!",Toast.LENGTH_SHORT).show();
								}
								if (response.equals(ConfConexao.MSG_SUCESSO)) {
									showDialog.aviso(Principal.this, "Aviso", "Varredura ativada com sucesso!");
									//Toast.makeText(Principal.this,"Varredura ativada com sucesso!",Toast.LENGTH_SHORT).show();
								} else if (response.equals("")) {
									showDialog.ConexaoServidor(Principal.this, "Erro", "Erro na conexao com servidor");
									//Toast.makeText(Principal.this,"Erro na conexao com o servidor!", Toast.LENGTH_SHORT).show();
								}
							}
						}
					});
			dialogVar.setPositiveButton("Desativar", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							String response = conexao.enviaMensagem(ConfConexao.SET_VARREDURA, "0", "0:0", "0");

							if (response.equals(ConfConexao.MSG_ERRO)) {
								Toast.makeText(Principal.this,"Permissao negada!", Toast.LENGTH_SHORT).show();
							}
							if (response.equals(ConfConexao.MSG_SUCESSO)) {
								Toast.makeText(Principal.this,"Varredura desativada com sucesso!",Toast.LENGTH_SHORT).show();
							} else if (response.equals("")) {
								Toast.makeText(Principal.this,"Erro na conexao com o servidor!",Toast.LENGTH_SHORT).show();
							}
						}
					});
			TextView label = (TextView) view.findViewById(R.id.tvVarLabel);
			label.setText(Html.fromHtml("Intervalo de tempo entre varreduras<br>(em minutos)"));

			dialogVar.setTitle("Editar Varredura");
			dialogVar.setMessage("Voce deseja ativar ou desativar a Varredura Periodica?");
			dialogVar.setView(view);

			time.setText("15");
		}
		dialogVar.show();
	}

	/**
	 * Configura os atributos utilizados para a conexao com o SAR a partir de
	 * informacoes lidas no arquivo de conexao
	 * 
	 * @return
	 */
	public boolean setInfoServidor() {
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
	 * Chama do metodo que verifica se a conexao e 3g ou wifi ou se tem conexao e exibe a mesangem.
	 * 
	 */
	public void verificaConexao() {
		if (conectado(getApplicationContext()) == false) {
			showDialog.ConexaoInternet(this,"Erro","Dispositivo desconectado da internet");

		}
	}

	/**
	 * Verifica se a conexao com a internet este ativa, 3g ou wifi.
	 */
	public static boolean conectado(Context context) {

		try {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

			if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()) {// 3G
				return true;
			} else if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {// WIFI
				return true;
			} else {
				return false;
			}
				} catch (Exception e) {
			return false;
		}
		
	}

	/**
	 * E executado quando a aplicacao retorna da Activity de conexao,
	 * entao e feita uma nova autenticacao no SAR com os dados atualizados
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if ((requestCode == 1) && (resultCode != Activity.RESULT_CANCELED)) {
			// autenticar();
		}
	}
}
