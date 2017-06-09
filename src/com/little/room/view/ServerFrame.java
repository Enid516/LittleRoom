package com.little.room.view;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ServerFrame extends JFrame {
	JButton button;
	JTextField textField;
	public ServerFrame(){  
        this.setTitle("little room");  
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        this.setBounds(800, 400, 500, 400);  
 
        //list
        String[] jString = {"aa","cc"};
        JList jList = new JList<>(jString);
        this.add(jList,BorderLayout.NORTH); 
        JPanel jPanel = new JPanel();
        //text
        textField = new JTextField();  
        textField.setColumns(30);
        jPanel.add(textField);  
        //button
        button = new JButton("send");
        
        jPanel.add(button);  
        this.add(jPanel,BorderLayout.SOUTH);
        this.setVisible(true);
    }  
	
	public void setActionListener(ActionListener actionListener){
		button.addActionListener(actionListener);
	}
	
	public JTextField getJTextField(){
		return textField;
	}
	
}
