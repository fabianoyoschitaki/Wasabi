package com.example.wasabi.adapter;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uatsapi.R;
import com.example.wasabi.common.Message;

public class MessageAdapter extends ArrayAdapter<Message> {

	public MessageAdapter(Activity context, int resource, List<Message> objects, String me) {
		super(context, resource, objects);
		this.context = context;
		this.me = me;
		this.messages = objects;
	}

	private static final String TAG = MessageAdapter.class.getName();
	private Activity context;
	private String me;
	private List<Message> messages;
	private static final int LIMITE_MENSAGENS = 15;
	
	@Override
	public int getCount() {
		return super.getCount();
	}

	@Override
	public Message getItem(int position) {
		return super.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		return super.getItemId(position);
	}

	private class ViewHolder {
		ImageView foto;
		TextView user_name;
		TextView message;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		ViewHolder holder = null;
//		Message row_pos = super.getItem(position);
//		LayoutInflater mInflater = (LayoutInflater) super.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//		if (convertView == null) {
//			convertView = mInflater.inflate(R.layout.message_item, null);
//			holder = new ViewHolder();
//
//			holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
//			holder.foto = (ImageView) convertView.findViewById(R.id.foto);
//			holder.message = (TextView) convertView.findViewById(R.id.message);
//
//			holder.foto.setImageResource(R.drawable.ic_launcher);
//			holder.user_name.setText(row_pos.getSender());
//			holder.message.setText("[" + row_pos.getIdMessage() + "] " + row_pos.getText());
//
//			convertView.setTag(holder);
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
		
	    // reuse views
	    if (convertView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			convertView = inflater.inflate(R.layout.message_item, null);
			// configure view holder
			ViewHolder holder = new ViewHolder();
			holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
			holder.foto = (ImageView) convertView.findViewById(R.id.foto);
			holder.message = (TextView) convertView.findViewById(R.id.message);
			convertView.setTag(holder);
	    }

	    // fill data
	    ViewHolder holder = (ViewHolder) convertView.getTag();
	    
	    Message m = getItem(position);
	    if (m.getSender().equals(me)){
	    	holder.foto.setImageResource(R.drawable.wasabi_me);
	    } else {
	    	holder.foto.setImageResource(R.drawable.wasabi_other);
	    }
		
	    String apelido = null;
	    if (Integer.valueOf(m.getIdMessage()) % 4 == 0){
	    	apelido = "da re no kibe";
	    } else if (Integer.valueOf(m.getIdMessage()) % 4 == 1){
	    	apelido = "kid pirokinha";
	    } else if (Integer.valueOf(m.getIdMessage()) % 4 == 2){
	    	apelido = "boiolinha";
	    } else if (Integer.valueOf(m.getIdMessage()) % 4 == 3){
	    	apelido = "granola";
	    } 
	    
	    String temp = null;
		if (m.getAction().equals("login")){
			temp = apelido + " entrou...";
		} else if (m.getAction().equals("say")){
			temp = apelido + " diz para " + m.getReceiver() + ":";
		} else if (m.getAction().equals("yell")){
			temp = apelido + " diz para todos:";
		} else if (m.getAction().equals("comeback")){
			temp = apelido + " voltou...";
		}
		String userName = m.getSender() + " " + temp;
	    
	    holder.user_name.setText(userName);
		holder.message.setText(m.getText());
		

	    return convertView;
	}

	public List<Message> getItems() {
        return messages;
    }
	
	public void checkLimit() {
		if (getItems().size() > 30){
			Log.d(TAG, "Passou limite [" + getItems().size() + " > " + LIMITE_MENSAGENS + "]");
			for (Iterator<Message> iter = getItems().iterator(); iter.hasNext() && getItems().size() > LIMITE_MENSAGENS; ){
				Log.d(TAG, "Removendo");
				Message m = iter.next();
				this.remove(m);
			}
		}
		
	}

}