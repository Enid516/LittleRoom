package com.little.room;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.PageAttributes;
import java.awt.TextField;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.little.room.impl.ServerImpl;

public class RoomServer {

	public static void main(String[] args) {
		ServerImpl server = new ServerImpl();
		server.start();
		createFrame();
	}
	
	private static void createFrame(){
		JFrame jFrame = new JFrame();
		jFrame.setTitle("little room");
		jFrame.setSize(500, 400);
		
		String[] jString = {"aa","bb","cc","aa","bb","cc","aa","bb","cc","aa","bb","cc","aa","bb","cc","aa","bb","cc","aa","bb","cc","aa","bb","cc","aa","bb","cc"};
		JList jList = new JList<>(jString);
		jFrame.add(jList,BorderLayout.PAGE_START);
		
		
		JTextField text = new JTextField(40);
		jFrame.add(text,BorderLayout.CENTER);
		
		JButton button = new JButton("send");
		jFrame.add(button,BorderLayout.PAGE_END);
		
//		Container container = jFrame.getContentPane();
//		JPanel jPanel = new JPanel();
//		container.add(jPanel);
		jFrame.setVisible(true);
		
	}

}
