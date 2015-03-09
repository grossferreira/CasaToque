

package br.com.ar.casatoque.controle;

import br.com.ar.casatoque.R;

import br.com.ar.casatoque.adapter.PerfisAdapter;
import br.com.ar.casatoque.comum.ConfConexao;
import br.com.ar.casatoque.comum.Dialog;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.UUID;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ExpandableListActivity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Classe responsavel pela criacao e gerenciamento da lista de perfis 
 * e seus subitens (dispositivos pertencentes ao perfil). Ela tambem 
 * e responsavel pelo envio e recebimento dos perfis ao o SAR.
 */
public class Perfis extends ExpandableListActivity{

    private PerfisAdapter mAdapter;
    private boolean mudou;
    
    private String[] dispositivos;
    private String[] dispTipos;
    private ArrayAdapter<String> adapterDisp;
    private Spinner sDisp;
    private Integer[][] dispSelect;
    private int ini_hora;
    private int ini_min;
    private int fim_hora;
    private int fim_min;
    
    private String uuid;
    
    private String usuario;
    private String senha;
    private String ip;
    private String porta;
    private boolean isHttp;
    
    Dialog erro = new Dialog(); //iniciando as mensagens
    
    private ArrayList<String> nomePerfil;
    private ArrayList<String[]> dias;
    private ArrayList<InfoPerf> infoPerf;
    private ArrayList<Boolean> enablePerfil;
    
    private ArrayList<String[]> nDisp;
    private ArrayList<String[]> tipoDisp;
    private ArrayList<String> enderecoDisp;
    
    private ConfConexao conexao;
    
    public final String dias_da_semana[]= {"Domingo","Segunda-Feira","Terca-Feira","Quarta-Feira",
			   							   "Quinta-Feira","Sexta-Feira","Sabado"};
    public final String meses[]= {"Janeiro","Fevereiro","Marco","Abril","Maio","Junho",
    							  "Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};
    
    public Perfis(){
    	
    	nDisp = new ArrayList<String[]>();
    	tipoDisp = new ArrayList<String[]>();
    	enderecoDisp = new ArrayList<String>();
    	dispositivos = new String[0];   	
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // As informacoes do servidor no arquivo de conexao
		if (setInfoServidor() == true) {
			conexao = new ConfConexao(this, ip, porta, isHttp);

			conexao.enviaMensagem(ConfConexao.MSG_AUTENTICACAO, usuario, senha, "");

			// Obtem lista de dispositivos ativos do SAR
			this.setDisps();

			// gerar vetor dos tipos de dispositivos existentes na rede
			dispTipos = gerarDispTipos();

			this.recuperarListas();
			verificaPerfis();
			registerForContextMenu(getExpandableListView());
			mudou = false;
		}else{
			startActivity(new Intent(this, Conexao.class));
			finish();
		}
    }
    
    /**
     * Cria novo adapter e atualiza lista de perfis.
     * @param pVez true se o metodo foi invocado do metodo onCreate()
     */
    public void updateAdapter(boolean pVez){       	
        mAdapter = new PerfisAdapter(this, pVez);
        setListAdapter(mAdapter);
        mudou = true;
    }
    
    /**
     * Verifica se lista de perfis do SAR a mesma do cliente Android, caso nao seja
     * entao o usuario e questionado se deseja enviar seus perfis ao SAR ou receber os perfis dele.
     */
    public void verificaPerfis(){
    	
    	final String perfil_uuid = conexao.enviaMensagem(ConfConexao.REQUEST_PERFIL_UUID, "0", "0", "0");
    	
    	if(perfil_uuid.equals("")){
    		
    		this.updateAdapter(true); 
    		//Toast.makeText(this, "Erro ao conectar no servidor!", Toast.LENGTH_SHORT).show();
    		erro.ConexaoServidor(Perfis.this, "Erro", "Erro ao conectar no servidor!");
    		// verifica se o perfil recebido do SAR diferente do perfil contido no cliente
    	}else if(!perfil_uuid.equals(uuid) && !perfil_uuid.equals("nulo")){
    
    		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    		dialog.setPositiveButton("Enviar", new DialogInterface.OnClickListener(){
    			public void onClick(DialogInterface dialog, int whichButton){
    				
    				Perfis.this.updateAdapter(true);
    				Perfis.this.uploadPerfis(false);  				
    				
    			}
    		});
    		dialog.setPositiveButton("Receber", new DialogInterface.OnClickListener(){
    			public void onClick(DialogInterface dialog, int whichButton){
    				
    				String perfis = conexao.enviaMensagem(ConfConexao.REQUEST_PERFIL, "0", "0", "0");
    				
					if (perfis.equals("")) {
						//Toast.makeText(Perfis.this, "Erro ao conectar no servidor!", Toast.LENGTH_SHORT).show();
						erro.ConexaoServidor(Perfis.this, "Erro", "Erro ao conectar no servidor!");
						
					}else {
						// preenche as listas com as informacoes dos perfis

						uuid = perfil_uuid;
						nomePerfil.clear();
						infoPerf.clear();
						dias.clear();
						enablePerfil.clear();
						
						if (!perfis.equals("nulo")) {

							String[] modulos = perfis.split("%");

							for (int i = 0; i < modulos.length; i++) {

								String[] partes = modulos[i].split("#", 2);
								
								if (partes.length == 2) {

									String endereco = partes[0];

									// ex: Teste;Sem nome;14:0;2:0;0;0:100;1:2:3:4:5:6:7
									partes = partes[1].split("#");

									for (int k = 0; k < partes.length; k++) {

										String[] dados = partes[k].split(";");

										String pNome = dados[0];
										String dNome = dados[1];
										String[] iniHorario = dados[2].split(":");
										String[] fimHorario = dados[3].split(":");
										String ID = dados[4];
										String[] valores = dados[5].split(":");
										String[] listaDia = dados[6].split(":");

										int index = nomePerfil.indexOf(pNome);

										// verifica se o perfil ja existe, se nao existe então cria um novo
										if (index == -1) {
											// criar perfil
											nomePerfil.add(pNome);
											infoPerf.add(new InfoPerf());
											dias.add(listaDia);
											enablePerfil.add(true);

											// seta variavel index com posicao atual da lista infoPerf
											index = infoPerf.size() - 1;
										}

										// adicionar dispositivos ao perfil
										infoPerf.get(index).nomeDisp.add(dNome);
										infoPerf.get(index).endereco.add(endereco);
										infoPerf.get(index).idDisp.add(ID);
										infoPerf.get(index).ini_hora.add(getValorFormat(iniHorario[0]));
										infoPerf.get(index).ini_min.add(getValorFormat(iniHorario[1]));
										infoPerf.get(index).fim_hora.add(getValorFormat(fimHorario[0]));
										infoPerf.get(index).fim_min.add(getValorFormat(fimHorario[1]));
										infoPerf.get(index).valorInicial.add(valores[0]);
										infoPerf.get(index).valorFinal.add(valores[1]);
										infoPerf.get(index).enableDisp.add(true);
									}
								}
							}
							
							Perfis.this.updateAdapter(true);
						}

						Perfis.this.gravarObjeto(nomePerfil, "nomes");
						Perfis.this.gravarObjeto(dias, "dias");
						Perfis.this.gravarObjeto(infoPerf, "info");
						Perfis.this.gravarObjeto(enablePerfil, "enable");
						Perfis.this.gravaUUID();					
					}
    			}
    		});
    		dialog.setTitle("Perfil");
    		dialog.setMessage("Existem perfis configurados no servidor diferentes dos seus. Voce deseja enviar seus perfis ao servidor ou receber os perfis dele?");
    		dialog.setOnCancelListener(new OnCancelListener(){
				@Override
				public void onCancel(DialogInterface arg0) {
					Perfis.this.finish();
				} 			
    		});
    		dialog.show();
   		
    	}else{
    		// atualiza tela
    		this.updateAdapter(true);   		
    	}   	
    }
    
	/**
     * Envia lista de perfis ao SAR.
     * @param newUUID true se a necessario criar um novo UUID (ID da lista de perfis)
     * o que indicaria ao SAR que houveram alteracoes na lista
     */
    public void uploadPerfis(boolean newUUID){
    	
    	if(newUUID == true){
    		uuid = ""+UUID.randomUUID();
    		this.gravaUUID();
    	}
    	    	
    	this.gravarObjeto(nomePerfil, "nomes");
    	this.gravarObjeto(dias, "dias");
    	this.gravarObjeto(infoPerf, "info");
    	this.gravarObjeto(enablePerfil, "enable");
 
    	// cria mensagem com as informacoes dos perfis para ser enviada ao SAR
    	
    	String msgPerf = uuid;
        
		for (int i = 0; i < enderecoDisp.size(); i++) {

			boolean pVez = true;

			for (int k = 0; k < infoPerf.size(); k++) {

				if (enablePerfil.get(k) == true) {

					ArrayList<String> endereco = infoPerf.get(k).endereco;

					for (int j = 0; j < endereco.size(); j++) {

						if (enderecoDisp.get(i).equals(endereco.get(j)) && (infoPerf.get(k).enableDisp.get(j) == true)) {

							if (pVez == true) {
								msgPerf += "%" + endereco.get(j);
								pVez = false;
							}

							msgPerf += "#" + nomePerfil.get(k) + ";" + infoPerf.get(k).nomeDisp.get(j) + ";" + infoPerf.get(k).ini_hora.get(j) + ":" + infoPerf.get(k).ini_min.get(j)
									+";" + infoPerf.get(k).fim_hora.get(j) + ":" + infoPerf.get(k).fim_min.get(j)
									+";" + infoPerf.get(k).idDisp.get(j) + ";" + infoPerf.get(k).valorInicial.get(j) + ":" + infoPerf.get(k).valorFinal.get(j);

							String[] str = dias.get(k);
							String tmp = "";
							for (String s : str) {
								tmp += ":" + s;
							}

							msgPerf += ";" + tmp.substring(1);
						}
					}
				}
			}
		}
        
        String response = conexao.enviaMensagem(ConfConexao.MSG_PERFIL, "0", msgPerf, "0");
        if(response.equals("")){
        	Toast.makeText(this, "Erro ao enviar perfis", Toast.LENGTH_SHORT).show();
		}else if(response.equals(ConfConexao.MSG_SUCESSO)){
			Toast.makeText(this, "Perfis enviados com sucesso!", Toast.LENGTH_SHORT).show();
		}else if(response.equals(ConfConexao.MSG_ERRO)){
			Toast.makeText(this, "Erro na autenticação", Toast.LENGTH_SHORT).show();
		}
        
        mudou = false;
    }
    
    /**
     * Recupera as informacoes dos perfis que foram gravadas em arquivos.
     */
	@SuppressWarnings("unchecked")
	public void recuperarListas(){       	        
    	
		Object obj = this.recuperarObjeto("nomes");		
		if(obj instanceof ArrayList<?>){
			nomePerfil = (ArrayList<String>) obj;
		}else{
			nomePerfil = new ArrayList<String>();        
		}
		
		obj = this.recuperarObjeto("dias");
		if(obj instanceof ArrayList<?>){
			dias = (ArrayList<String[]>) obj;
		}else{
			dias = new ArrayList<String[]>();			
		}
		
		obj = this.recuperarObjeto("info");
		if(obj instanceof ArrayList<?>){
			infoPerf = (ArrayList<InfoPerf>) obj;
		}else{
			infoPerf = new ArrayList<InfoPerf>();
		}
		
		obj = this.recuperarObjeto("enable");
		if(obj instanceof ArrayList<?>){
			enablePerfil = (ArrayList<Boolean>) obj;
		}else{
			enablePerfil = new ArrayList<Boolean>();
		}
		
		this.restauraUUID();			  	
    }
    
    @Override
    public boolean  onCreateOptionsMenu(android.view.Menu menu){
    	super.onCreateOptionsMenu(menu);
    	menu.add(0,0,0,"Criar Perfil");
    	menu.add(0,1,0,"Salvar Perfis");
		return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	super.onOptionsItemSelected(item);
    	switch(item.getItemId()){
    		case  0:   dialogEditarPerfil(0, true); 
    				   break;
    				   
    		case  1:   this.uploadPerfis(true);
    	}
    	
    	return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	
    	ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) menuInfo;
    	int type = ExpandableListView.getPackedPositionType(info.packedPosition);
    	
    	if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
    		menu.setHeaderTitle("Opcoes dos Dispositivos");
    		menu.add(0, 0, 0, "Editar Dispositivo");
    		menu.add(0, 1, 0, "Remover Dispositivo");
    	} else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
    		menu.setHeaderTitle("Opcoes dos Perfis");
    		menu.add(0, 0, 0, "Adicionar Dispositivo");
    		menu.add(0, 1, 0, "Editar Perfil");
    		menu.add(0, 2, 0, "Remover Perfil");
        }
        
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	super.onContextItemSelected(item);
        
    	ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();   
        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        
        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                 	 
            int childPos = ExpandableListView.getPackedPositionChild(info.packedPosition); 
            
            switch(item.getItemId()){ 
            
            	case 0: dialogEditarDisp(groupPos, childPos); break;
            	
            	case 1: dialogRemoveDisp(groupPos, childPos);
            }
            
        } else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    	
        	switch(item.getItemId()){
        	
        		case 0: dialogAddDisp(groupPos); break;
        		
        		case 1: dialogEditarPerfil(groupPos, false); break;
        		
        		case 2: dialogRemovePerfil(groupPos); 
        	
        	}
        }
        
        return true;
    }
    
    /**
     * Cria dialog para o usuário adicionar dispositivos num determinado perfil.
     * @param groupPosition indica a posicoes do perfil na ExpandableListView
     */
    public void dialogAddDisp(final int groupPosition){
   					
		LayoutInflater layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		final View view = layoutInflater.inflate(R.layout.menu_adicionar_disp, null);
		
		String[] tiposNomes = new String[dispTipos.length];		
		
		for(int i=0; i<dispTipos.length; i++){
			switch(Integer.parseInt(dispTipos[i])){
			
				case 100: tiposNomes[i] = "Lampada"; 
				break;
				case 200: tiposNomes[i] = "Persiana"; 
				break;
				case 300: tiposNomes[i] = "Portao";
				default: tiposNomes[i] = "Desconhecido";
			
			}
		}
		
		Spinner sTipo = (Spinner) view.findViewById(R.id.tipos_dispositivos);
		ArrayAdapter<String> adapterTipo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tiposNomes);
		adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sTipo.setAdapter(adapterTipo);
		sTipo.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String[][] matriz = getPorTipo(dispTipos[arg2]);
				dispositivos = new String[matriz.length];
				dispSelect = new Integer[matriz.length][2];
				
				for(int i=0; i<matriz.length; i++){
					dispSelect[i][0] = Integer.parseInt(matriz[i][0]);
					dispSelect[i][1] = Integer.parseInt(matriz[i][1]);
					dispositivos[i] = nDisp.get(dispSelect[i][0])[dispSelect[i][1]];
				}
				
				adapterDisp = new ArrayAdapter<String>(Perfis.this, android.R.layout.simple_spinner_item, dispositivos);
				adapterDisp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				sDisp.setAdapter(adapterDisp);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
			
		});
		
		sDisp = (Spinner) view.findViewById(R.id.escolher_dispositivos);
		adapterDisp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dispositivos);
		adapterDisp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sDisp.setAdapter(adapterDisp);
		
		ini_hora = 0;
	    ini_min = 0;
	    fim_hora = 0;
	    fim_min = 0;
				
		Button iniBtn = (Button)view.findViewById(R.id.addBtnIni);
		iniBtn.setOnClickListener(new OnClickListener(){
			
			OnTimeSetListener listener = new OnTimeSetListener(){
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					ini_hora = hourOfDay;
					ini_min = minute;
				}			
			};
			
			@Override
			public void onClick(View arg0) {
				TimePickerDialog tpDialog = new TimePickerDialog(Perfis.this, listener, ini_hora, ini_min, true);
				tpDialog.show();
			}		
		});
		
		Button fimBtn = (Button)view.findViewById(R.id.addBtnFim);	
		fimBtn.setOnClickListener(new OnClickListener(){
			
			OnTimeSetListener listener = new OnTimeSetListener(){
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					fim_hora = hourOfDay;
					fim_min = minute;
				}			
			};
			
			@Override
			public void onClick(View arg0) {
				TimePickerDialog tpDialog = new TimePickerDialog(Perfis.this, listener, fim_hora, fim_min, true);
				tpDialog.show();
			}		
		});
				
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setPositiveButton("Adicionar", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
				// adicionar dispositivo ao perfil
				
				int selPos = sDisp.getSelectedItemPosition();
				
				if (selPos != -1) {
					int mod = dispSelect[selPos][0];
					int disp = dispSelect[selPos][1];

					int ini_valor = ((SeekBar) view.findViewById(R.id.editValorIni)).getProgress();
					int fim_valor = ((SeekBar) view.findViewById(R.id.editValorFinal)).getProgress();

					infoPerf.get(groupPosition).nomeDisp.add(nDisp.get(mod)[disp]);
					infoPerf.get(groupPosition).endereco.add(enderecoDisp.get(mod));
					infoPerf.get(groupPosition).idDisp.add("" + disp);
					infoPerf.get(groupPosition).ini_hora.add(getValorFormat(ini_hora));
					infoPerf.get(groupPosition).ini_min.add(getValorFormat(ini_min));
					infoPerf.get(groupPosition).fim_hora.add(getValorFormat(fim_hora));
					infoPerf.get(groupPosition).fim_min.add(getValorFormat(fim_min));
					infoPerf.get(groupPosition).valorInicial.add("" + ini_valor);
					infoPerf.get(groupPosition).valorFinal.add("" + fim_valor);
					infoPerf.get(groupPosition).enableDisp.add(true);

					Perfis.this.updateAdapter(false);
				}
			}
		});
		dialog.setPositiveButton("Cancelar", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){}
		});
		dialog.setTitle("Adicionar Dispositivo");
		dialog.setView(view);
		dialog.show();
	}
    
    /**
     * Cria dialog para que o usuario possa alterar um determinado dispositivo de um perfil.
     * @param groupPosition indica a posicao do perfil na ExpandableListView
     * @param childPosition indica a posicao do dispositivo na lista de dispositivos do perfil
     */
    public void dialogEditarDisp(final int groupPosition, final int childPosition){
			
		LayoutInflater layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		final View view = layoutInflater.inflate(R.layout.menu_editar_disp, null);
	
		ini_hora = Integer.parseInt(infoPerf.get(groupPosition).ini_hora.get(childPosition));
	    ini_min = Integer.parseInt(infoPerf.get(groupPosition).ini_min.get(childPosition));
	    fim_hora = Integer.parseInt(infoPerf.get(groupPosition).fim_hora.get(childPosition));
	    fim_min = Integer.parseInt(infoPerf.get(groupPosition).fim_min.get(childPosition));
	    
	    int valor = Integer.parseInt(infoPerf.get(groupPosition).valorInicial.get(childPosition));
	    ((SeekBar)view.findViewById(R.id.editValorIni)).setProgress(valor);
	    
	    valor = Integer.parseInt(infoPerf.get(groupPosition).valorFinal.get(childPosition));
		((SeekBar)view.findViewById(R.id.editValorFinal)).setProgress(valor);
				
		Button iniBtn = (Button)view.findViewById(R.id.editBtnIni);
		iniBtn.setOnClickListener(new OnClickListener(){
			
			OnTimeSetListener listener = new OnTimeSetListener(){
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					ini_hora = hourOfDay;
					ini_min = minute;
				}			
			};
			
			@Override
			public void onClick(View arg0) {
				TimePickerDialog tpDialog = new TimePickerDialog(Perfis.this, listener, ini_hora, ini_min, true);
				tpDialog.show();
			}		
		});
		
		Button fimBtn = (Button)view.findViewById(R.id.editBtnFim);	
		fimBtn.setOnClickListener(new OnClickListener(){
			
			OnTimeSetListener listener = new OnTimeSetListener(){
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					fim_hora = hourOfDay;
					fim_min = minute;
				}			
			};
			
			@Override
			public void onClick(View arg0) {
				TimePickerDialog tpDialog = new TimePickerDialog(Perfis.this, listener, fim_hora, fim_min, true);
				tpDialog.show();
			}		
		});
				
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setPositiveButton("Salvar", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
				// Editar dispositivo	
				int ini_valor = ((SeekBar)view.findViewById(R.id.editValorIni)).getProgress();
				int fim_valor = ((SeekBar)view.findViewById(R.id.editValorFinal)).getProgress();

				infoPerf.get(groupPosition).ini_hora.set(childPosition, getValorFormat(ini_hora));
				infoPerf.get(groupPosition).ini_min.set(childPosition, getValorFormat(ini_min));
				infoPerf.get(groupPosition).fim_hora.set(childPosition, getValorFormat(fim_hora));
				infoPerf.get(groupPosition).fim_min.set(childPosition, getValorFormat(fim_min));
				infoPerf.get(groupPosition).valorInicial.set(childPosition, ""+ini_valor);
				infoPerf.get(groupPosition).valorFinal.set(childPosition, ""+fim_valor);
				
				Perfis.this.updateAdapter(false);
				
			}
		});
		dialog.setPositiveButton("Cancelar", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){}
		});
		dialog.setTitle("Editar Dispositivo");
		dialog.setView(view);
		dialog.show();
	}
    
    /**
     * Remove dispositivo de uma determinado perfil.
     * @param groupPos indica a posicao do perfil na ExpandableListView
     * @param childPos indica a posicao do dispositivo na lista de dispositivos do perfil
     */
    public void dialogRemoveDisp(final int groupPos, final int childPos){
    	AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){		
				
				infoPerf.get(groupPos).nomeDisp.remove(childPos);
				infoPerf.get(groupPos).endereco.remove(childPos);
				infoPerf.get(groupPos).idDisp.remove(childPos);
				infoPerf.get(groupPos).ini_hora.remove(childPos);
				infoPerf.get(groupPos).ini_min.remove(childPos);
				infoPerf.get(groupPos).fim_hora.remove(childPos);
				infoPerf.get(groupPos).fim_min.remove(childPos);
				infoPerf.get(groupPos).valorInicial.remove(childPos);
				infoPerf.get(groupPos).valorFinal.remove(childPos);
				infoPerf.get(groupPos).enableDisp.remove(childPos);		
				updateAdapter(false);
			}
		});
		dialog.setPositiveButton("Nao", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){}
		});
		dialog.setTitle("Remover Dispositivo");
		dialog.setMessage("Voce tem certeza que deseja remover o dispositivo \""+infoPerf.get(groupPos).nomeDisp.get(childPos)+"\" ?");
		dialog.show();
    }
    
    /**
     * Cria ou edita a lista de perfis.
     * @param position indica a posicao do perfil que se deseja editar na ExpandableListView
     * @param isNew true se para criar um novo perfil
     */
    public void dialogEditarPerfil(final int position, final boolean isNew){
    	
    	LayoutInflater layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.menu_editar_perfil, null);
		Button btnSel = (Button)view.findViewById(R.id.diaSelec);
        final EditText editarNome = (EditText)view.findViewById(R.id.ePerfilNomeEdit);
        final RadioGroup rGrupo = (RadioGroup)view.findViewById(R.id.RadioGroup);
    	
        String btnText = "";
        String titulo = "";
        String perfilNome = "";
        final boolean dias_escolhidos[] = new boolean[7];
        final String[] data = new String[4];
        final Calendar calendario = Calendar.getInstance();
        
    	if( isNew == true ){
    		
    		titulo = "Criar Perfil";
    		btnText = "Criar";
    	}else{
    		
    		String[] tempDias = dias.get(position);
    		
    		if(tempDias[0].equals("-1")){
    			
    			calendario.set(Calendar.DAY_OF_MONTH, Integer.parseInt(tempDias[1]));
    			calendario.set(Calendar.MONTH, Integer.parseInt(tempDias[2]));
    			calendario.set(Calendar.YEAR, Integer.parseInt(tempDias[3]));
    			// Marcar checkbox Data Especifica (Dias da Semana a checkbox default)
    			rGrupo.check(R.id.rbDataEspec);
    		}else{
    			// Seta como "true" as posicoes do vetor correspondentes aos dias
    			for(int i=0; i < tempDias.length; i++){
    				dias_escolhidos[Integer.parseInt(tempDias[i]) - 1] = true;
    			}
    		}
    		
    		perfilNome = nomePerfil.get(position);
    		titulo = "Editar Perfil";
    		btnText = "Salvar";
    	}
      
        editarNome.setText(perfilNome);             
        btnSel.setOnClickListener(new OnClickListener(){
       
        	@Override
			public void onClick(View v) {
        		
        		if(rGrupo.getCheckedRadioButtonId() == R.id.rbDiaDaSemana){	
        			AlertDialog.Builder builder = new AlertDialog.Builder(Perfis.this);
        			builder.setMultiChoiceItems(dias_da_semana, dias_escolhidos, new DialogInterface.OnMultiChoiceClickListener() {
        				public void onClick(DialogInterface dialog,int whichButton, boolean isChecked) {
        					// quando algum CheckBox for marcado o seu estado sera salvo num vetor de booleanos
        					dias_escolhidos[whichButton] = isChecked;
        				}                
        			});
        			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        				public void onClick(DialogInterface dialog, int whichButton) {}});
        			builder.create();
        			builder.show();
        		}else{
        			DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener(){
						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							data[0] = "-1";
							data[1] = ""+dayOfMonth;
							data[2] = ""+monthOfYear;
							data[3] = ""+year;
						}			
        			};
        			DatePickerDialog dataDialog = new DatePickerDialog(Perfis.this, listener, calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH));	
        			dataDialog.show();	
        		}
			}
        });
        
        // Dialog Principal
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setPositiveButton(btnText, new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
				
				String nome = editarNome.getText().toString();
				String[] dia = null;
			
				if(rGrupo.getCheckedRadioButtonId() == R.id.rbDiaDaSemana){	
					
					String vetor = "";
					for(int i=0; i < dias_escolhidos.length; i++){
						if(dias_escolhidos[i] == true){
							vetor += ","+(i+1);
						}
					}
					if(!vetor.equals("")){
						dia = vetor.substring(1).split(",");
					}
				}else if(data[0] != null){
						dia = data;
				}
				
				if (!nome.equals("") && (dia != null)) {
					String pn = editarNome.getText().toString();
					int indexPN = nomePerfil.indexOf(pn);

					if (isNew == true) {
						// verifica se nao existe um perfil com o nome que se atribuir
						if (indexPN == -1) {
							// criar perfil
							nomePerfil.add(pn);
							infoPerf.add(new InfoPerf());
							dias.add(dia);
							enablePerfil.add(true);
						} else {
							criarDialog(pn);
						}
						
					} else {
						// verifica se nao existe um perfil com o nome que se atribui ou se o perfil a ele proprio
						if ((indexPN == -1) || (indexPN == position)) {
							// Editar pefil
							nomePerfil.set(position, pn);
							dias.set(position, dia);
						} else {
							criarDialog(pn);
						}
						
					}
					updateAdapter(false);
				}
			
			}
		});
		dialog.setPositiveButton("Cancelar", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){}
		});
		dialog.setTitle(titulo);
		dialog.setView(view);
		dialog.show();
	}
       
    /**
     * Remove perfil da ExpandableListView.
     * @param position posicao do perfil na ExpandableListView
     */
    public void dialogRemovePerfil(final int position){
    	AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){		
				
				nomePerfil.remove(position);
				infoPerf.remove(position);
				dias.remove(position);
				enablePerfil.remove(position);
				updateAdapter(false);
			}
		});
		dialog.setPositiveButton("Nao", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){}
		});
		dialog.setTitle("Remover Perfil");
		dialog.setMessage("Voce tem certeza que deseja remover o pefil \""+nomePerfil.get(position)+"\" ?");
		dialog.show();
    }
    
    /**
     * @return vetor com os tipos de dispositivos conhecidos pelo SAR
     */
    public String[] gerarDispTipos(){
    	
    	ArrayList<String> tempTipo = new ArrayList<String>();
    	
    	for(String[] tiposVetor : tipoDisp){
    		for(String t : tiposVetor){
    			if(!tempTipo.contains(t)){
    				tempTipo.add(t);
    			}
    		}
    	}
    	// Converte ArrayList de tipos em vetor
    	String[] tp = new String[tempTipo.size()]; 	
    	for(int i=0; i<tempTipo.size(); i++){
    		tp[i] = tempTipo.get(i);
    	}

    	return tp;
    }
    
    /**
     * Retorna matriz com as posicoes dos dispositivos de um determinado tipo
     * na lista geral de tipos como tambem o ID do mesmo.
     * @param tipo tipo de dispositivo no qual se deve fazer a busca
     * @return matriz com as posicoes e IDs dos dispositivos de um certo tipo
     */
    public String[][] getPorTipo(String tipo){
    	
    	ArrayList<String[]> tempTipo = new ArrayList<String[]>();
    	
    	for(int i=0; i < tipoDisp.size(); i++){ 		
    		String t[] = tipoDisp.get(i);
    		for(int k=0; k < t.length; k++){
    			if(t[k].equals(tipo)){
    				tempTipo.add(new String[]{ ""+i , ""+k });
    			}
    		}
    	}
    	// Converte ArrayList de tipos em vetor
    	String[][] tp = new String[tempTipo.size()][tempTipo.size()]; 	
    	for(int j=0; j<tempTipo.size(); j++){
    		tp[j] = tempTipo.get(j);
    	}

    	return tp;
    }
    
    /**
     * Cria lista com todos os dispositivos conhecidos pelo SAR a partir de uma resposta a uma requisicoes ao SAR.
     */
    public void setDisps(){
    	
    	String base = conexao.enviaMensagem(ConfConexao.REQUEST_DISP, "0", "0", "0");
    	
    	nDisp.clear();
    	tipoDisp.clear();
    	enderecoDisp.clear();
    	
		if (!base.equals("null") && !base.equals(ConfConexao.MSG_ERRO) && !base.equals("")) {

			String[] partes = base.split("#");

			for (String temp : partes) {

				String[] inf = temp.split("%", 2);
				
				if (inf.length == 2) {

					String endereco = inf[0];

					inf = inf[1].split("%");

					String[] nomeTemp = new String[inf.length];
					String[] tipoTemp = new String[inf.length];

					for (int i = 0; i < inf.length; i++) {
						String[] vetor = inf[i].split(":");

						nomeTemp[i] = vetor[0];
						tipoTemp[i] = vetor[1];
					}

					nDisp.add(nomeTemp);
					tipoDisp.add(tipoTemp);
					enderecoDisp.add(endereco);
				}
			}
		}
    }
    
    /**
     * Converte o valor dos horarios para a forma que ele sera exibido na lista. Ex: 0 -> 09
     * @param valor valor que sera formatado
     * @return valor formatado
     */
    public String getValorFormat(int valor){
    	String retorno = "";
    	if(valor < 10){
    		retorno = "0";
    	}
    	retorno += valor;
    	
    	return retorno;
    }
    
    /**
     * Converte o valor dos horarios para a forma que ele sera exibido na lista. Ex: 0 -> 09
     * @param valor valor que sera formatado
     * @return valor formatado
     */
    public String getValorFormat(String valor){
    	String retorno = "";
    	if(Integer.parseInt(valor) < 10){
    		retorno = "0";
    	}
    	retorno += valor;
    	
    	return retorno;
    }
    
    /**
     * Grava obejtos em arquivos.
     * @param obj objeto que sera gravado
     * @param fileName nome do arquivo que o objeto sera gravado
     */
    public void gravarObjeto(Object obj, String fileName){
        try {

            FileOutputStream fout = openFileOutput(fileName + ".obj", MODE_PRIVATE);
            ObjectOutputStream oout = new ObjectOutputStream(fout);

            oout.writeObject(obj);

            oout.flush();
            oout.close();
            fout.close();

        } catch (Exception ex) {
            System.err.println("erro: " + ex.toString());
        }
    }
    
    /**
     * Restaura objeto de um arquivo.
     * @param fileName nome do arquivo que o objeto sera restaurado
     * @return objeto restaurado
     */
    public Object recuperarObjeto(String fileName){

    	Object obj = null;
		try {
			FileInputStream fin = openFileInput(fileName + ".obj");
			ObjectInputStream oin = new ObjectInputStream(fin);

			// Lendo os objetos de um arquivo e fazendo a conversao de tipos
			obj = oin.readObject();

			oin.close();
			fin.close();

		} catch (Exception ex) {
			System.err.println("erro: " + ex.toString());
		}      

        return obj;
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
     * Grava valor do UUID em um arquivo.
     */
    public void gravaUUID(){
    	try {
			FileOutputStream fOut = openFileOutput("uuid.obj", MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			osw.write(uuid);	
			osw.close();
			fOut.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}		
    }
    
    /**
     * Restaura valor do UUID de um arquivo.
     */
    public void restauraUUID(){
    	try {
			FileInputStream fIn = openFileInput("uuid.obj");
			Scanner file_scan = new Scanner(fIn);
			if (file_scan.hasNext()) {
				uuid = file_scan.nextLine();
			}
			file_scan.close();
			fIn.close();

		} catch (IOException ioe) {
			ioe.printStackTrace();		
			uuid = ""+UUID.randomUUID();
    		this.gravaUUID();
		}	
    }
    
    /**
     * Cria dialog para exibir mensagem de erro por causa de perfis repetidos.
     * @param pNome nome do perfil que ja existe
     */
    public void criarDialog(String pNome){		
    	//AlertDialog dialog = new AlertDialog.Builder(this).create();
    	AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
				//Perfis.this.finish();
			}
		});
		dialog.setTitle("Erro");
		dialog.setMessage("Lista de perfis ja contem um perfil chamado \""+pNome+"\"");
		dialog.show();
    }
    
    /**
     * @return lista com os nomes dos perfis
     */
    public ArrayList<String> getNomePerfil(){
    	return nomePerfil;
    }
    
    /**
     * @return lista com os dias de execucao dos perfis
     */
    public ArrayList<String[]> getDiasPerfil(){
    	return dias;
    }
    
    /**
     * @return lista com as informacoes dos dispositivos contidas nos perfis
     */
    public ArrayList<InfoPerf> getInfoPerf(){
    	return infoPerf;
    } 
    
    /**
     * @return lista com estado das CheckBoxs dos perfis
     */
    public ArrayList<Boolean> getEnablePerfil(){
    	return enablePerfil;
    }  
    
    /**
     * Metodo invocado quando ocorrer ou quando se quizer desfazer alguma alteracao na lista de perfis.
     * @param m true se houve alguma alteracao
     */
    public void setMudou(boolean m){
    	mudou = m;
    }
    
    /**
     * Metodo invocado quando o botao 'voltar' for pressionado ou quando alguma
     * alteracao na lista de perfis tiver ocorrido. Questiona o usuario se ele
     * deseja enviar as suas informacoes de perfil ao SAR.
     */
    public boolean onKeyDown (int keyCode, KeyEvent event){
    	if((keyCode == KeyEvent.KEYCODE_BACK) && (mudou == true)){
    		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    		dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener(){
    			public void onClick(DialogInterface dialog, int whichButton){		
    				uploadPerfis(true);	
    				finish();
    			}
    		});
    		dialog.setPositiveButton("Nao", new DialogInterface.OnClickListener(){
    			public void onClick(DialogInterface dialog, int whichButton){
    				finish();
    			}
    		});
    		dialog.setTitle("Salvar Lista");
    		dialog.setMessage("Voce Deseja salvar as alteracoes?");
    		dialog.show();
    		return false;
    	}else{   	
    		return super.onKeyDown(keyCode, event);
    	}
    }
}
