package com.example.wasabi.task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

import com.example.wasabi.activity.OnTaskCompleted;
import com.example.wasabi.common.Message;

// terceiro parâmetro especifica o que você quer de retorno
public class ListMessageAsyncTask extends AsyncTask<String, Void, List<Message>>{
	
	private static final String TAG = ListMessageAsyncTask.class.getName();
	private OnTaskCompleted listener;
	
	public ListMessageAsyncTask(OnTaskCompleted listener){
		this.listener = listener;
	}
	
	@Override
	protected List<Message> doInBackground(String... params) {
		List<Message> retorno = new ArrayList<Message>();
		try {
			HttpClient httpclient = new DefaultHttpClient();
			String usuario = params[0];
			String url = params[1];
			String lastMessageId = params[2];
		    
			HttpPost httppost = new HttpPost(url);
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("action", "list"));
	        nameValuePairs.add(new BasicNameValuePair("from", usuario));
	        nameValuePairs.add(new BasicNameValuePair("lastmessageid", lastMessageId));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	
	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        BufferedReader br = new BufferedReader(new InputStreamReader(
	        		response.getEntity().getContent()));
            
            String linha;
            
            while ((linha = br.readLine()) != null) {
            	Log.d(TAG, linha);
            	if (!linha.trim().equals("")){
            		retorno.add(new Message(linha));
            	}
            }
            br.close();
	        return retorno;
		} catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected void onPostExecute(List<Message> result) {
		if (!result.isEmpty()){
			listener.onTaskListMessageCompleted(result);
		}
	}
}
