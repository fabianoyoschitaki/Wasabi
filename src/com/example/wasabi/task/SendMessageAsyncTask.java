package com.example.wasabi.task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

public class SendMessageAsyncTask extends AsyncTask<String, Void, String>{
		
	private static final String TAG = SendMessageAsyncTask.class.getName();
	private OnTaskCompleted listener;
	
	public SendMessageAsyncTask(OnTaskCompleted listener){
		this.listener = listener;
	}
	
	 protected void onPostExecute(Boolean isMessageSent){
        if (isMessageSent){
        	listener.onTaskSendMessageCompleted();
    	}
    }
	
	@Override
	protected String doInBackground(String... params) {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			String usuario = params[0];
			String senha = params[1];
			String url = params[2];
			String mensagem = params[3];
		    
			HttpPost httppost = new HttpPost(url);
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("action", "yell"));
	        nameValuePairs.add(new BasicNameValuePair("from", usuario));
	        nameValuePairs.add(new BasicNameValuePair("password", senha));
	        nameValuePairs.add(new BasicNameValuePair("message", mensagem));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	
	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        BufferedReader br = new BufferedReader(new InputStreamReader(
	        		response.getEntity().getContent()));

	        StringBuffer b = new StringBuffer();
            String linha;
            while ((linha = br.readLine()) != null) {
            	b.append(linha);
            }
            Log.d(TAG, "resultado yell [" + b.toString() + "]");
            br.close();
	        return b.toString();
		} catch (Exception e){
			Log.d(TAG, "erro yell [" + e + "]");
			e.printStackTrace();
			return e.getMessage();
		}
	}
}
