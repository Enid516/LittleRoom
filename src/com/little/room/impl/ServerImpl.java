package com.little.room.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.little.room.JsonUtil;			
import com.little.room.constant.Constant;
import com.little.room.i.Server;

public class ServerImpl implements Server {
	private static final String TAG = "ServerImpl";
	private boolean isRuning = false;
	private List<Socket> mSocketList = new ArrayList<>();
	private ServerSocket mServerSocket;
	private ExecutorService executorService;
	private Logger logger = Logger.getLogger(ServerImpl.class);

	@Override
	public void start() {
		new AcceptClientThread().start();
	}

	@Override
	public void stop() {
		closeServerSocket();
	}
	
	/**
     * 接收客户端的线程
     */

    private class AcceptClientThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                mServerSocket = new ServerSocket(Constant.PORT);
                isRuning = true;
                System.out.println("server is runing。。。");
                Socket clientSocket;
                while (isRuning) {//等待客户端连接
                    clientSocket = mServerSocket.accept();
                    if (!isConnect(clientSocket)) {
                        mSocketList.add(clientSocket);
                        String msg = clientSocket.getInetAddress().toString()+ "连接到服务器，当前连接数是：" + mSocketList.size();
                        System.out.println(msg);
                        String result = "{\"code\":0,\"message\":\"connect success\"}";
                        sendClientMsg(clientSocket,result);
                    }
                    getExecutorService().execute(new ServerWork(clientSocket));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 接收客户端发送的消息
     */
    private class ServerWork implements Runnable {
        private Socket mSocket;
        private BufferedReader reader;
        private String msg;

        ServerWork(Socket socket) {
            this.mSocket = socket;
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {//接收client消息
            try {
                msg = reader.readLine();
                if (msg != null) {
                    System.out.println(msg);
                    operatorMessage(mSocket, msg);
                    reader.close();
                    mSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void operatorMessage(Socket socket,String message){
    	logger.debug(message);
    	JSONObject jsonObject = new JSONObject(message);
    	String action = jsonObject.getString("action");
    	String result = null;
    	if(action.equals("login")){
    		result = "{\"code\":0,\"message\":\"login success\"}";
    	}else if(action.equals("sendMessage")){
    		result = "{\"code\":0,\"message\":\"send message success\"}";
    	}
    	if(result != null && result.length() > 0){
    		sendClientMsg(socket, result);
    	}
    }
    
    private void sendClientMsg(String msg) {
        if (mSocketList == null)
            return;
        for (int i = 0; i < mSocketList.size(); i++) {
            sendClientMsg(mSocketList.get(i), msg);
        }
    }

    private void sendClientMsg(Socket socket, String msg) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            writer.println(msg);
            logger.debug("sendClientMsg: " + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void closeServerSocket() {
        if (mServerSocket != null) {
            mSocketList.clear();
        }
        try {
            if (mServerSocket != null){
                mServerSocket.close();
                isRuning = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    

    
    private ExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = Executors.newCachedThreadPool();
        }
        return executorService;
    }

    
    private boolean isConnect(Socket socket) {
        if (mSocketList.isEmpty()) {
            return false;
        }
        for (int i = 0; i < mSocketList.size(); i++) {
            if (mSocketList.get(i).getInetAddress().getHostAddress().equals(socket.getInetAddress().getHostAddress())) {
                return true;
            }
        }
        return false;
    }



}
