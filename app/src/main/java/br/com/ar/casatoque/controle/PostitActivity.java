
package br.com.ar.casatoque.controle;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import br.com.ar.casatoque.R;
import br.com.ar.casatoque.comum.Dialog;

//import android.widget.CheckBox;

public class PostitActivity extends Activity {



    //declarcao de variaveis login;
    private ImageButton postit;  //declando a variavel usuario;

    //private CheckBox checkBox; //declarando a variavel check box;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }
}