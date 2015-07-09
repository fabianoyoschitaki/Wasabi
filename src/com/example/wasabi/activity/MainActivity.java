package com.example.wasabi.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uatsapi.R;
import com.example.wasabi.task.LoginAsyncTask;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements View.OnClickListener {

	private final String TAG = "MainActivity";
	
	private Button entrar;
	private EditText usuario;
	private EditText senha;
//	private Button mudarServidor;
//	private TextView servidorAtual;
	
	private int contTryConnect;
	
	private String context;
	private String serverUrl;
//	private String serverPort;
	
	final static String APP_PREFS = "app_prefs";
	final static String USERNAME_KEY = "username";
	final static String SERVERURL_KEY = "server";
	final static String SERVERPORT_KEY = "port";

	private SharedPreferences prefs; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/** sumindo com a barra de menu superior **/
		getActionBar().hide();
		setContentView(R.layout.main);
		loadActivityConfiguration();
	}
	
	private void loadActivityConfiguration() {
		
		loadViews();
		loadValues();
		
	}
	
	private void loadValues() {
		prefs = getSharedPreferences(APP_PREFS, MODE_PRIVATE);
		serverUrl = "www.homeexperiments.com.br";
//		serverUrl = "192.168.0.3:8080";
		contTryConnect = 0;
		context = "/xato-server/xato.do";
		
		String username = prefs.getString(USERNAME_KEY, null);

		if (username != null){
			usuario.setText(username);
		}
		
	}

	private void loadViews() {
		entrar = (Button) findViewById(R.id.login_entrar_button);
		entrar.setOnClickListener(this);
		usuario = (EditText) findViewById(R.id.login_usuario_edittext);
		senha = (EditText) findViewById(R.id.login_senha_edittext);
//		servidorAtual = (TextView) findViewById(R.id.server_textView);
//		mudarServidor = (Button) findViewById(R.id.mudar_servidor_button);
/**		mudarServidor.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				try {
					// get prompts.xml view
					// http://stackoverflow.com/questions/5796611/dialog-throwing-unable-to-add-window-token-null-is-not-for-an-application-wi
					// PASSAR THIS em vez de getApplicationContext()
//					LayoutInflater li = LayoutInflater.from(getApplicationContext());
					LayoutInflater li = LayoutInflater.from(view.getContext());
					View promptsView = li.inflate(R.layout.prompt_novo_servidor, null);
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
	 
					// set prompts.xml to alertdialog builder
					alertDialogBuilder.setView(promptsView);
	 
					final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
	 
					// set dialog message
					alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton("OK",
						  new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog,int id) {
						    	if (!userInput.getText().toString().contains(":")){
						    		Toast.makeText(getApplicationContext(), "Servidor deve conter porta!", Toast.LENGTH_SHORT).show();
						    		return;
						    	}
						    	
						    	serverUrl = userInput.getText().toString().split(":")[0];
						    	serverPort = userInput.getText().toString().split(":")[1];
						    	                                          
						    	servidorAtual.setText(serverUrl + ":" + serverPort);
						    }
						  })
						.setNegativeButton("Cancelar",
						  new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog,int id) {
						    	dialog.cancel();
						    }
						 })
						 .setNeutralButton("Padrão", 
							new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog,int id) {
						    	serverUrl = defaultServerUrl;
						    	serverPort = defaultServerPort;
						    	servidorAtual.setText(serverUrl + ":" + serverPort);
						    	Toast.makeText(getApplicationContext(), "Voltando ao valor padrão", Toast.LENGTH_SHORT).show();
						    }
						 });
	 
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
	 
					// show it
					alertDialog.show();
				} catch (Exception e){
					Log.d(TAG, "Erro:" + e);
					Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
				}
	 
			}
		});*/
	}

	//	http://blog.vandersonguidi.com.br/programacao-2/android/resolvendo-o-erro-networkonmainthreadexception-no-desenvolvimento-android
	/**
	 * Método que faz a tentativa de login
	 * 
	 * @param usuarioString
	 * @param senhaString
	 */
	private void tentaLogar(String usuarioString, String senhaString) {
		if (usuarioString.trim().equals("") || senhaString.trim().equals("")){
			if (contTryConnect == 0){
				Toast.makeText(getApplicationContext(), "Não, não tem bug se deixar vazio, digita logo essa porra!", Toast.LENGTH_LONG).show();
			} else if (contTryConnect == 1){
				Toast.makeText(getApplicationContext(), "Puta merda, de novo!", Toast.LENGTH_LONG).show();
			} else if (contTryConnect == 2){
				Toast.makeText(getApplicationContext(), "VSF, vou fechar, fui!", Toast.LENGTH_LONG).show();
				finish();
			}
			contTryConnect++;
			return;
		} 
		
		try {
	    	String resultado = new LoginAsyncTask().execute(usuarioString, senhaString, getUrlString(usuarioString, senhaString)).get();
	    	if (resultado.equals("true")){
	    		Intent intent = new Intent(MainActivity.this, ChatActivity.class);
	    		
	    		/** passa parâmetros para activity **/
	    		Bundle params = new Bundle();
	    		params.putString("context", context);
	    		params.putString("server", serverUrl);
	    		params.putString("password", senhaString);
	    		params.putString("user", usuarioString);
	    		intent.putExtras(params);
	    		
	    		Editor editor = prefs.edit();
				editor.putString(MainActivity.USERNAME_KEY, usuarioString);
				editor.putString(MainActivity.SERVERURL_KEY, serverUrl);
				editor.commit();
				
				startActivity(intent);
	    	} else if (resultado.equals("false")){
	    		Toast.makeText(getBaseContext(), "Usuário e Senha inválidos!", Toast.LENGTH_SHORT).show();
	    	} else {
	    		Toast.makeText(getBaseContext(), "Resultado desconhecido:" + resultado, Toast.LENGTH_SHORT).show();
	    	}
	    } catch (Exception e){
	    	Toast.makeText(getBaseContext(), "exception:" + e.toString(), Toast.LENGTH_SHORT).show();
	    }
		
	}
	
	private String getUrlString(String usuarioString, String senhaString) {
		String urlString = "http://" + serverUrl + context;
		Log.d(TAG, "getUrlString: " + urlString);
		return urlString;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		String usuarioString = usuario.getText().toString();
		String senhaString = senha.getText().toString();
		tentaLogar(usuarioString, senhaString);				
	}
}
