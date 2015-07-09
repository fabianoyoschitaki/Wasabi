package com.example.wasabi.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.uatsapi.R;
import com.example.wasabi.adapter.MessageAdapter;
import com.example.wasabi.common.Message;
import com.example.wasabi.task.CheckNewMessageAsyncTask;
import com.example.wasabi.task.ListMessageAsyncTask;
import com.example.wasabi.task.SendMessageAsyncTask;

public class ChatActivity extends Activity implements OnTaskCompleted {

	private String context;
	private String server;
	public String user;
	public String password;
	private List<Message> messages = new ArrayList<Message>();
	
	private ListView messageListView;
	private EditText messageText;
	private Button sendButton;
	
	private MessageAdapter messageAdapter; 
	
	private final String TAG = ChatActivity.class.getName();
	
	private final static long CHECKING_INCOMING_MESSAGES_INTERVAL = 1000 * 5; //5 segundos
	private Handler newMessageHandler;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.chat);
		populaCampos();
		iniciaViews();
		listMessagesFromServer();
	}
	
	private void iniciaViews() {
		this.messageListView = (ListView) findViewById(R.id.message_list);
		this.messageAdapter = new MessageAdapter(this, R.id.message_list, messages, user);
		this.messageListView.setAdapter(messageAdapter);
		
		this.messageText = (EditText) findViewById(R.id.edit_text_message);
		this.sendButton = (Button) findViewById(R.id.button_send_message);
		this.sendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String texto = messageText.getText().toString();
				if (!texto.trim().equals("")){
					sendMessageToServer(texto);
				} else {
					Toast.makeText(getApplicationContext(), "Digita alguma coisa antes carai", Toast.LENGTH_SHORT).show();
				}
			}
	
		});
		
	}
	
	private void populaCampos() {
		Log.d(TAG, "Início populaCampos");
		
		Intent intent = getIntent();
		Bundle params = intent.getExtras();  
		if(params!=null){
			
			/** para atualizar a cada 5 segundos **/
			this.newMessageHandler = new Handler();
			newMessageHandler.post(new Runnable() {
				@Override 
			     public void run() {
			         hasNewMessages();
			         newMessageHandler.postDelayed(this, CHECKING_INCOMING_MESSAGES_INTERVAL);
			     }
			});
			
			this.context = params.getString("context");
			this.server = params.getString("server");
			this.user = params.getString("user");
			this.password = params.getString("password");
			Toast.makeText(this.getApplicationContext(), "http://" + server + context + " com usuário " + user, Toast.LENGTH_LONG).show();
		}
		Log.d(TAG, "Fim populaCampos");
	}

	/**
	 * Método que carrega as mensagens do servidor com o comando list 
	 * e depois popula as mensagens na ListView 
	 */
	private void listMessagesFromServer() {
		Log.d(TAG, "Início listMessagesFromServer [" + messages.size() + "]");
		try {
			new ListMessageAsyncTask(this).execute(user, getUrl(), getLastMessage()).get();
		} catch (Exception e){
			Toast.makeText(getBaseContext(), "Erro:" + e, Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		Log.d(TAG, "Fim listMessagesFromServer");
	}
	
	private void sendMessageToServer(String texto){
		try {
			new SendMessageAsyncTask(this).execute(user, password, getUrl(), texto).get();
			this.messageText.setText("");
		} catch (Exception e){
			Toast.makeText(getBaseContext(), "Erro Runtime:" + e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	private String getUrl() {
		return "http://" + server + context;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}
	
	private void hasNewMessages() {
//		Log.d(TAG, "Início hasNewMessages");
//		try {
//			if (new CheckNewMessageAsyncTask().execute(getLastMessage(), getUrl()).get()){
//				listMessagesFromServer();
//			} 
//		} catch (Exception e){
//			Toast.makeText(getBaseContext(), "Erro Runtime:" + e.getMessage(), Toast.LENGTH_LONG).show();
//		}
//		Log.d(TAG, "Fim hasNewMessages");
		try {
			new CheckNewMessageAsyncTask(this).execute(getLastMessage(), getUrl());
		} catch (Exception e){
			Toast.makeText(getBaseContext(), "Erro Runtime:" + e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	private String getLastMessage() {
		if (!messages.isEmpty()){
			return messages.get(messages.size()-1).getIdMessage();
		} else {
			return "0";
		}
	}
	
	@Override
	public void onTaskListMessageCompleted(List<Message> messagesTemp){
		messages.addAll(messagesTemp);
		messageAdapter.checkLimit();
		for (Message message : messagesTemp) {
			Log.d(TAG, "[" + message.getIdMessage() + "]");
		}
		messageAdapter.notifyDataSetChanged();
	}

	@Override
	public void onTaskHasNewMessageCompleted() {
		listMessagesFromServer();
	}

	@Override
	public void onTaskSendMessageCompleted() {
		listMessagesFromServer();
	}
}
