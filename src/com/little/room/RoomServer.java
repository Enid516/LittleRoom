package com.little.room;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import com.alibaba.fastjson.JSON;
import com.little.room.impl.ServerImpl;
import com.little.room.model.ChatModel;
import com.little.room.view.ServerFrame;

public class RoomServer {
	private ServerImpl mServer;
	private ServerFrame serverFrame;
	public static void main(String[] args) {
		new RoomServer().start();
	}
	
	private void start() {
		mServer = new ServerImpl();
		mServer.start();
		serverFrame = new ServerFrame();
		serverFrame.setActionListener(actionListener);
	}
	
	private ActionListener actionListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String string = serverFrame.getJTextField().getText();
			mServer.sendClientMsg(toJsonString(string));
		}
	};
	
	private String toJsonString(String msg){
		ChatModel chatModel = new ChatModel();
		chatModel.setAction("chat");
		chatModel.setNickName("server");
		chatModel.setMessage(msg);
		String s = JSON.toJSONString(chatModel);
		return s;
	}
}
