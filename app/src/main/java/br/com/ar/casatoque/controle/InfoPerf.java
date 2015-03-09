
package br.com.ar.casatoque.controle;

import java.io.Serializable;
import java.util.ArrayList;


public class InfoPerf implements Serializable{
	
	public InfoPerf(){
		ini_hora = new ArrayList<String>();
		ini_min = new ArrayList<String>();
		fim_hora = new ArrayList<String>();
		fim_min = new ArrayList<String>();
		valorInicial = new ArrayList<String>();
		valorFinal = new ArrayList<String>();
		endereco = new ArrayList<String>();
		nomeDisp = new ArrayList<String>();
		idDisp = new ArrayList<String>();
		enableDisp = new ArrayList<Boolean>();
	}

    public ArrayList<String> ini_hora;
    public ArrayList<String> ini_min;
    public ArrayList<String> fim_hora;
    public ArrayList<String> fim_min;
    public ArrayList<String> valorInicial;
    public ArrayList<String> valorFinal;
    public ArrayList<String> endereco;
    public ArrayList<String> nomeDisp;
    public ArrayList<String> idDisp;
    public ArrayList<Boolean> enableDisp;
}
