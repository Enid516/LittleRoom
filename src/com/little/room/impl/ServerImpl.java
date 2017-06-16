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

import com.little.room.constant.Constant;
import com.little.room.i.Server;

import util.ResultMessageUtil;

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
	 * 接受客服端连接的线程
	 */

	private class AcceptClientThread extends Thread {
		@Override
		public void run() {
			super.run();
			try {
				mServerSocket = new ServerSocket(Constant.PORT);
				isRuning = true;
				System.out.println("server is runing");
				Socket clientSocket;
				while (isRuning) {
					clientSocket = mServerSocket.accept();
					if (!isConnect(clientSocket)) {
						mSocketList.add(clientSocket);
						String msg = clientSocket.getInetAddress().toString() + "当前连接数是： " + mSocketList.size();
						System.out.println(msg);
						String result = ResultMessageUtil.getConnectResultString(true);
						sendClientMsg(clientSocket, result);
					}
					getExecutorService().execute(new ServerWork(clientSocket));
				}
				mServerSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 处理客服端消息
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
		public void run() {
			try {
				while((msg = reader.readLine())!= null){
					operatorMessage(mSocket, msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				 try {
					reader.close();
					mSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void operatorMessage(Socket socket, String message) {
		System.out.println("receive message: " + message);
		JSONObject jsonObject = new JSONObject(message);
		String action = jsonObject.getString("action");
		String result = null;
		if (action.equals("login")) {
			result = ResultMessageUtil.getLoginResultString(true);
		}
		if (result != null && result.length() > 0) {
			sendClientMsg(socket, result);
		}
	}

	public void sendClientMsg(String msg) {
		if (mSocketList == null)
			return;
		for (int i = 0; i < mSocketList.size(); i++) {
			sendClientMsg(mSocketList.get(i), msg);
		}
	}

	private void sendClientMsg(Socket socket, String msg) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			writer.println(msg);
			//这里socket不用关闭，因为要实现服务端能主动给客户端发送消息
			//这里的writer不用关闭，关闭socket相关的输入/输出流对象，将关闭socket
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}

	private void closeServerSocket() {
		if (mServerSocket != null) {
			mSocketList.clear();
		}
		try {
			if (mServerSocket != null) {
				mServerSocket.close();
				isRuning = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private ExecutorService getExecutorService() {
		if (executorService == null)
			executorService = Executors.newCachedThreadPool();

		return executorService;
	}

	private boolean isConnect(Socket socket) {
		if (mSocketList.isEmpty())
			return false;
		for (int i = 0; i < mSocketList.size(); i++) {
			if (mSocketList.get(i).getInetAddress().equals(socket.getInetAddress()))
				return true;
		}
		return false;
	}

}
