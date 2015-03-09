
package br.com.ar.casatoque.adapter;

import br.com.ar.casatoque.R;
import br.com.ar.casatoque.controle.InfoPerf;
import br.com.ar.casatoque.controle.Perfis;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Classe que monta a lista de views exibida pela ExpandableListView a 
 * partir das informacoes dos perfis criados.
 */
public class PerfisAdapter extends BaseExpandableListAdapter {
	    
    private final int paddingGroup = 36;
    private final int paddingChild = 53;
    private boolean pVez;
    
    private LayoutInflater mInflater;
	private Perfis pai;
    
    public PerfisAdapter(Context context, boolean pv) {
		mInflater = LayoutInflater.from(context);
		pai = (Perfis) context;		
		pVez = pv;
	}

    public Object getChild(int groupPosition, int childPosition) {
        return pai.getInfoPerf().get(groupPosition).nomeDisp.get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * @return quantidade de subitens que serao exibidos por um determido perfil
     */
    public int getChildrenCount(int groupPosition) {
        return pai.getInfoPerf().get(groupPosition).endereco.size();
    }

    /**
	 * Metodo invocado a cada atualizacao de tela que retorna a View
	 * que sera exibida em cada posicao da ExpandableListView.
	 * @param item objeto contendo as informacoes dos dispositivos pertencentes aos perfis
	 * @param groupPosition posicao do item de perfil na lista em que o subitem sera exibido
     * @param childPosition posicao em que o subitem sera exibido no item de perfil
	 * @param isChild true se a View retornada a um item (perfil) ou subitem (informacao de dispositivo)
	 * @return View que sera exibida em uma determinada posicao da ExpandableListView
	 */
    public View getGenericView( Object item, final int groupPosition, final int childPosition, final boolean isChild ) {
    	
    	View xml = mInflater.inflate(R.layout.itens_lista_layout, null);
    	View root = xml.findViewById(R.id.root);
    	
    	TextView textoTitulo = (TextView)xml.findViewById(R.id.itemListaTitulo);
    	TextView textoSubtitulo = (TextView)xml.findViewById(R.id.itemListaSubtitulo);
    	CheckBox enable = (CheckBox)xml.findViewById(R.id.enableCheckBox); 	
    	
    	enable.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChild == true){
					pai.getInfoPerf().get(groupPosition).enableDisp.set(childPosition, isChecked);
				}else{
					pai.getEnablePerfil().set(groupPosition, isChecked);
				}
				pai.setMudou(true);
			}   		
    	});
    	
    	String titulo = "";
    	String subtitulo = "";
    	
    	if(isChild == true){
    		
    		InfoPerf infoPerf = ((InfoPerf)item);
    		
    		String ini_hora = infoPerf.ini_hora.get(childPosition);
    		String ini_min = infoPerf.ini_min.get(childPosition);
    		String fim_hora = infoPerf.fim_hora.get(childPosition);
    		String fim_min = infoPerf.fim_min.get(childPosition);
    		String ini_valor = infoPerf.valorInicial.get(childPosition);
    		String fim_valor = infoPerf.valorFinal.get(childPosition);
    		
    		titulo = infoPerf.nomeDisp.get(childPosition);
    		subtitulo = ini_hora+":"+ini_min+" - "+fim_hora+":"+fim_min+", "+ini_valor+"%"+" - "+fim_valor+"%";
    		enable.setChecked(pai.getInfoPerf().get(groupPosition).enableDisp.get(childPosition));
    		
    		root.setBackgroundColor(Color.argb(100, 40, 40, 40));
    		textoTitulo.setPadding(paddingChild, 0, 0, 0);  
        	textoSubtitulo.setPadding(paddingChild, 0, 0, 0);  
    		((ImageView)xml.findViewById(R.id.itemListaIcone)).setBackgroundResource(R.drawable.lampapagada);
    	
    	}else{
    		
    		String[] dias = pai.getDiasPerfil().get(groupPosition);
    		String strTemp = "";
    		
    		if(dias[0].equals("-1")){		
    			strTemp = dias[1] + " de " + pai.meses[Integer.parseInt(dias[2])] + " de " + dias[3];
    		}else{
    			for(String s : dias){
        			strTemp += ", "+pai.dias_da_semana[Integer.parseInt(s) - 1].substring(0,3);
        		}
    			strTemp = strTemp.substring(2);
    		}
    		
    		titulo = (String) getGroup(groupPosition);
    		subtitulo = strTemp;
    		enable.setChecked(pai.getEnablePerfil().get(groupPosition));
    		
    		root.setPadding(paddingGroup, 0, 0, 0); 
    	}
    	
    	if(pVez == true){
    		pai.setMudou(false);
    	}
    	textoTitulo.setText(titulo);
    	textoSubtitulo.setText(subtitulo);
        
        return root;
    }
    
    /**
     * @param groupPosition posicao do item de perfil na lista em que o subitem sera exibido
     * @param childPosition posicao em que o subitem sera exibido no item de perfil
     * @param isLastChild se o subitem sera exibido na ultima posicao no item de perfil
     * @param convertView View (informacao de um dispositivo) que sera exibida no item de perfil
     * @param parent ExpandableListView onde serao exibidos os itens
     * @return subitem (informacao de um dispositivo) que sera exibido por um determido item de perfil
     */
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {
    	return getGenericView(pai.getInfoPerf().get(groupPosition), groupPosition, childPosition, true);
    }

    public Object getGroup(int groupPosition) {
        return pai.getNomePerfil().get(groupPosition);
    }

    /**
     * @return quantidade de itens (informacao de perfis) que serao exibidos
     */
    public int getGroupCount() {
        return pai.getNomePerfil().size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * @param groupPosition posicao da lista em que o perfil sera exibido
     * @param isExpanded true se o item de perfil esta atualmente expandido
     * @param convertView View (item) que sera exibida na ListView
     * @param parent ExpandableListView onde serao exibidos os itens
     * @return item (informacoes de perfil) que sera exibido na ExpandableListView
     */
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {    
    	return getGenericView(null, groupPosition, 0, false);
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }
}
