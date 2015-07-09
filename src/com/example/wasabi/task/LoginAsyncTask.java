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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;
import android.util.Log;

public class LoginAsyncTask extends AsyncTask<String, Void, String> {

	private static final String TAG = "LoginAsyncTask";
	private static final int timeoutConnection = 5000;

	@Override
	protected String doInBackground(String... params) {
		try {
			String usuario = params[0];
			String senha = params[1];
			String url = params[2];
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("action", "login"));
			nameValuePairs.add(new BasicNameValuePair("from", usuario));
			nameValuePairs.add(new BasicNameValuePair("password", senha));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			/** setando limite de tempo p/ chamada **/
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			httppost.setParams(httpParameters);
			
			// Execute HTTP Post Request
			Log.d(TAG, "Tentando logar com [usuario:" + usuario + "] [senha:" + senha + "] [url:"+ url + "]");
			HttpResponse response = httpclient.execute(httppost);
			if (response != null) {
				Log.d(TAG, "Retornou");
				BufferedReader br = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				StringBuffer sb = new StringBuffer("");
				String linha;
				while ((linha = br.readLine()) != null) {
					sb.append(linha);
				}
				br.close();
				linha = sb.toString();
				return linha;
			} else {
				return "response é nulo";
			}
		} catch (Exception e) {
			Log.d(TAG, "Erro ao fazer login: " + e);
			return e.getMessage();
		}
	}

	/**
	 * @Override protected Boolean doInBackground(String... params) { String URL
	 *           = "http://vandersonguidi.com.br/networkonmainthread.txt";
	 *           String linha = ""; Boolean Erro = true;
	 * 
	 *           if (params.length > 0) // faço qualquer coisa com os parâmetros
	 * 
	 *           try { HttpClient client = new DefaultHttpClient(); HttpGet
	 *           requisicao = new HttpGet();
	 *           requisicao.setHeader("Content-Type",
	 *           "text/plain; charset=utf-8"); requisicao.setURI(new URI(URL));
	 *           HttpResponse resposta = client.execute(requisicao);
	 *           BufferedReader br = new BufferedReader(new InputStreamReader(
	 *           resposta.getEntity().getContent())); StringBuffer sb = new
	 *           StringBuffer(""); while ((linha = br.readLine()) != null) {
	 *           sb.append(linha); } br.close(); linha = sb.toString(); Erro =
	 *           false; } catch (Exception e) { Erro = true; } return Erro; }
	 **/
}
