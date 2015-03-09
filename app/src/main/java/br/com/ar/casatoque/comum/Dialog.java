
package br.com.ar.casatoque.comum;

import br.com.ar.casatoque.R;
import android.widget.SeekBar;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


public class Dialog {
	//metodo que gera o TOAST
	public void showToast(Context contexto, String msg, int tempo){  
        Toast.makeText(contexto, msg, tempo).show();  
    }
	
	
	//metodo que gera o DIALOG
		public void aviso(Context contexto, String titulo, String msg){
	     
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(contexto); 
			alertDialog.setTitle(titulo); // Setting Dialog Title
			alertDialog.setMessage(msg);// Setting Dialog Message
			alertDialog.setIcon(R.drawable.ic_people);// Setting Icon to Dialog
			
			
			// ImageView image = (ImageView) alertDialog.findViewById();
			// image.setImageResource(R.drawable.ic_launcher);
		
			
			alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Write your code here to execute after dialog closed
						//Toast.makeText(getActivity(), "voce clecou em OK", Toast.LENGTH_SHORT).show();
					}
			});

	    alertDialog.show();
		
		}       
		
	
	//metodo que gera o DIALOG
	public void Autenticacao(Context contexto, String titulo, String msg){
     
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(contexto); 
		alertDialog.setTitle(titulo); // Setting Dialog Title
		alertDialog.setMessage(msg);// Setting Dialog Message
		alertDialog.setIcon(R.drawable.ic_people);// Setting Icon to Dialog
		// ImageView image = (ImageView) alertDialog.findViewById();
		// image.setImageResource(R.drawable.ic_launcher);
  
		alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog closed
					//Toast.makeText(getActivity(), "voce clecou em OK", Toast.LENGTH_SHORT).show();
				}
		});

    alertDialog.show();
	
	}       
	
    //metodo que gera o ERRO conexao
		public void ConexaoServidor(Context contexto, String titulo, String msg){
	     
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(contexto); 
			alertDialog.setTitle(titulo); // Setting Dialog Title
			alertDialog.setMessage(msg);// Setting Dialog Message
			alertDialog.setIcon(R.drawable.erro);// Setting Icon to Dialog
	  
			alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
					}
			});

	    alertDialog.show();
		
		}       
	
		//metodo erro conexao com a internet
		public void ConexaoInternet(Context contexto, String titulo, String msg){
	     
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(contexto); 
			alertDialog.setTitle(titulo); // Setting Dialog Title
			alertDialog.setMessage(msg);// Setting Dialog Message
			alertDialog.setIcon(R.drawable.ic_people);// Setting Icon to Dialog
		
			alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					
					}
			});

	    alertDialog.show();
		
		}       
		
	
	//Dialog composto de botoes
	public void showDialogBtEscolhas(Context contexto, String titulo, String msg){
		
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(contexto); 
        alertDialog.setTitle(titulo);// Setting Dialog Title
        alertDialog.setMessage(msg); // Setting Dialog Message
        alertDialog.setIcon(R.drawable.ic_photos);// Setting Icon to Dialog
 
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	//Toast.makeText(getActivity(), "Voce clicou em SIM", Toast.LENGTH_SHORT).show();
            }
        });
 
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NAO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	// Toast.makeText(getActivity(), " voce clicou em NAO", Toast.LENGTH_SHORT).show();
            dialog.cancel();
            }
        });
 
        // Showing Alert Message
        alertDialog.show();
	}
	
	
	public void ShowDialogContrluz(Context contexto, String titulo){

		final AlertDialog.Builder popDialog = new AlertDialog.Builder(contexto);
		final SeekBar seek = new SeekBar(contexto);
		//final TextView progresso = (TextView) findViewById(R.id.progr);
		
		seek.setMax(100);
		popDialog.setIcon(android.R.drawable.btn_star_big_on);
		popDialog.setTitle(titulo);
		popDialog.setView(seek);
		
		seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// Do something here with new value
				//txtView.setText("Value of : " + progress);
				//popDialog.setMessage("Value of : " + progress);
				//progresso.setText("Processing "+progress+"% ");
			}

			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

		});

		// Button OK

		popDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

	popDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}
	});
		
		popDialog.create();
		popDialog.show();

	}
}