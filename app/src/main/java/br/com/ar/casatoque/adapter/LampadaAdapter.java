
package br.com.ar.casatoque.adapter;

import br.com.ar.casatoque.R;
import br.com.ar.casatoque.controle.Lampadas;
import java.util.ArrayList;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Classe que utiliza as listas com os nomes dos dispositivos e 
 * seus estados para montar a lista exibida pela ListView.
 */
public class LampadaAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater;
	private Lampadas pai;
	
	private ArrayList<CheckBox> cboxes;
	private ArrayList<SeekBar> barras;
	private ArrayList<TextView> labels;
	private ArrayList<Integer> cores;

	public LampadaAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		pai = (Lampadas) context;	
		
		cboxes = new ArrayList<CheckBox>();
		barras = new ArrayList<SeekBar>();
		labels = new ArrayList<TextView>();
		cores = new ArrayList<Integer>();
	}

	/**
	 * Retorna a quantidade de posicoes exibidas pela ListView.
	 * @return quantidade de posicoes
	 */
	public int getCount() {
		return pai.getNomes().size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * Metodo invocado a cada atualizacoes de tela que retorna a View
	 * que sera exibida em cada posicoes da ListView.
	 * @param position posicao na qual sera exibida a View retornada
	 * @param convertView View que sera exibida na posicao determinada pelo parametro position
	 * @param parent ListView na qual sera exibida a lista de Views
	 * @return View que sera exibida em uma determinada posicao
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		// se convertView esta vazia entao cria os objetos a partir do XML inflado
		if (convertView == null) {
			
			convertView = mInflater.inflate(R.layout.lamp_lista, null);
			
			holder = new ViewHolder();
			holder.nomeLabel = (TextView)convertView.findViewById(R.id.nomeDisp);
			holder.interruptor = (CheckBox)convertView.findViewById(R.id.interruptor);
			holder.dimmer = (SeekBar)convertView.findViewById(R.id.dimmer);
						
			holder.interruptor.setOnClickListener(pai);
			holder.dimmer.setOnSeekBarChangeListener(pai);
			holder.nomeLabel.setOnTouchListener(pai);

			convertView.setTag(holder);
		} else {
			// se convertView nao esta vazia entao converte ela para ViewHolder
			holder = (ViewHolder) convertView.getTag();
		}
		
		// se a posicao de convertView ja existir nas listas de Views entao substitui a view atual pela convertView
		if(position < cboxes.size()){
			barras.set(position, holder.dimmer);
			cboxes.set(position, holder.interruptor);
			labels.set(position, holder.nomeLabel);	
			holder.nomeLabel.setTextColor(cores.get(position));
		}else{
			// se a posicao nao existir entao adiciona uma nova posicao nas listas de Views
			barras.add(holder.dimmer);
			cboxes.add(holder.interruptor);
			labels.add(holder.nomeLabel);
			cores.add(Color.WHITE);
		}
		
		// configura as Views com imagens, textos e etc.
			
		holder.nomeLabel.setText(pai.getNomes().get(position));			
		
		// converte valor de 0-255 em percentual (0-100)
		int estado = Integer.parseInt(pai.getEstados().get(position));
		estado = (estado*100)/255;
		
		pai.setImagem(holder.interruptor, estado);	
		holder.dimmer.setProgress(estado);
	
		return convertView;
	}
	
	/**
	 * @return lista de CheckBox que ja foram exibidas na tela
	 */
	public ArrayList<CheckBox> getBoxes(){
		return cboxes;
	}
	
	/**
	 * @return lista de SeekBar que ja foram exibidas na tela
	 */
	public ArrayList<SeekBar> getBarras(){
		return barras;
	}
	
	/**
	 * @return lista de TextView que ja foram exibidas na tela
	 */
	public ArrayList<TextView> getLabels(){
		return labels;
	}
	
	/**
	 * @return lista de cores atuais das TextViews que ja foram exibidas na tela
	 */
	public ArrayList<Integer> getCores(){
		return cores;
	}
	
	/**
	 * Classe que armazena Views inseridas em cada posicao da ListView
	 */
	public class ViewHolder {
		TextView nomeLabel;
		CheckBox interruptor;
		SeekBar dimmer;
	}
}