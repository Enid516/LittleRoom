package util;

import com.little.room.model.BaseResultModel;
import com.little.room.model.ChatMessageModel;
import com.little.room.model.ChatModel;

public class ResultMessageUtil {
	
	public static String getConnectResultString(boolean success){
		BaseResultModel connectResult = new BaseResultModel();
		connectResult.setAction("connect");
		if (success) {
			connectResult.setCode(0);
			connectResult.setMessage("连接成功");
		}else{
			connectResult.setCode(1);
			connectResult.setMessage("连接失败");
		}
		String result = JsonUtil.object2Json(connectResult);
		return result;
	}

	public static String getLoginResultString(boolean success){
		BaseResultModel loginResult = new BaseResultModel();
		loginResult.setAction("login");
		if (success) {
			loginResult.setCode(0);
			loginResult.setMessage("登录成功");
		}else{
			loginResult.setCode(1);
			loginResult.setMessage("登录失败");
		}
		String result = JsonUtil.object2Json(loginResult);
		return result;
	}
	
	public static String getChatResultString(String msg){
		ChatModel chatModel = new ChatModel();
		chatModel.setCode(0);
		chatModel.setAction("chat");
		chatModel.setMessage("chat from server");
		
		ChatMessageModel msgModel = new ChatMessageModel();
		msgModel.setNickName("server");
		msgModel.setMessage(msg);
		chatModel.setData(msgModel);
		
		String result = JsonUtil.object2Json(chatModel);
		return result;
	}
	
}
