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

public class CheckNewMessageAsyncTask extends AsyncTask<String, Void, Boolean>{
	
	private OnTaskCompleted listener;
	private static final String TAG = CheckNewMessageAsyncTask.class.getName();
	
	public CheckNewMessageAsyncTask(OnTaskCompleted listener){
		this.listener = listener;
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		 Log.d(TAG, "Início CheckNewMessageAsyncTask com lastmessageid [" + params[0] + "]");
		Boolean retorno = Boolean.FALSE;
		String lastMessageId = null;
		String url = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			lastMessageId = params[0];
			url = params[1];
			HttpPost httppost = new HttpPost(url);
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("action", "check"));
	        nameValuePairs.add(new BasicNameValuePair("lastmessageid", lastMessageId));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	
	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        BufferedReader br = new BufferedReader(new InputStreamReader(
	        		response.getEntity().getContent()));
            
	        StringBuffer sb = new StringBuffer("");
            String linha;
            while ((linha = br.readLine()) != null) {
                sb.append(linha);
            }
            br.close();
            linha = sb.toString();
	        retorno = linha.equals("true");
	        Log.d(TAG, "Verificando se tem mensagem nova com id [" + lastMessageId + "]: " + retorno);
	        return retorno;
		} catch (Exception e){
			e.printStackTrace();
			Log.d(TAG, "Erro ao verificar se tem mensagem nova com id [" + lastMessageId + "]: " + retorno);
			return retorno;
		}
	}
	
    protected void onPostExecute(Boolean hasNewMessage){
        if (hasNewMessage){
        	listener.onTaskHasNewMessageCompleted();
    	}
    }
}
