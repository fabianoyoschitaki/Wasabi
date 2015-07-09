package com.example.wasabi.activity;

import java.util.List;

import com.example.wasabi.common.Message;

public interface OnTaskCompleted {
	void onTaskHasNewMessageCompleted();
	void onTaskSendMessageCompleted();
	void onTaskListMessageCompleted(List<Message> m);
}
