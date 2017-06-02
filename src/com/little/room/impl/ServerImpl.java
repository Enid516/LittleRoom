package com.little.room.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.little.room.constant.Constant;
import com.little.room.i.Server;

public class ServerImpl implements Server {
	private static final String TAG = "ServerImpl";
	private boolean isRuning = false;
	private List<Socket> mSocketList = new ArrayList<>();
	private ServerSocket mServerSocket;
	private ExecutorService executorService;

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
                    reader.close();
                    mSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
