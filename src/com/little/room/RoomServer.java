package com.little.room;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import com.alibaba.fastjson.JSON;
import com.little.room.impl.ServerImpl;
import com.little.room.model.ChatMessageModel;
import com.little.room.model.ChatModel;
import com.little.room.view.ServerFrame;

import util.ResultMessageUtil;

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
			String string = serverFrame.getJTextField().getText().trim();
			if(string != null && string.length() > 0){
				String result = ResultMessageUtil.getChatResultString(string);
				mServer.sendClientMsg(result);
				serverFrame.getJTextField().setText("");
			}else{
				System.out.println("输入信息不能为空");
			}
			
		}
	};
	
	
}
