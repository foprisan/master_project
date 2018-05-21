package com.github.dawidstankiewicz.forum.controller;






import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.github.dawidstankiewicz.forum.controller.model.ChatMessage;
import com.pandorabots.api.MagicParameters;
import com.pandorabots.api.PandorabotsAPI;

/**
 * Created by rajeevkumarsingh on 24/07/17.
 */
@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
    	
        return chatMessage;
    }
    
    @MessageMapping("/chat.chatbotSendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessageChatBot(@Payload ChatMessage chatMessage) {
    	
    	MagicParameters mp = new MagicParameters();
    	PandorabotsAPI pbAPI = new  PandorabotsAPI(mp.getHostName(), mp.getAppId(), mp.getUserKey(),
    			mp.isDebug());
    	String response="";
    	try {
			response=pbAPI.talk("test", "filip1", chatMessage.getContent());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	ChatMessage cm=new ChatMessage();
    	cm.setType(ChatMessage.MessageType.CHAT);
    	cm.setSender("chatbot");
    	cm.setContent(response);
        return cm;
    }
    
    
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

}
