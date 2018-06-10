package com.dizeratie.forum.controller;






import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mustache.web.MustacheView;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.dizeratie.forum.controller.model.ChatMessage;
import com.dizeratie.forum.entity.Section;
import com.dizeratie.forum.entity.Topic;
import com.dizeratie.forum.service.RoleService;
import com.dizeratie.forum.service.SectionService;
import com.dizeratie.forum.service.TopicService;
import com.dizeratie.forum.service.UserService;
import com.dizeratie.forum.tools.HashMapSort;
import com.dizeratie.forum.tools.Recommender;
import com.pandorabots.api.MagicParameters;
import com.pandorabots.api.PandorabotsAPI;


@Controller
public class ChatController {

	 @Autowired
	    private SectionService sectionService;
	    
	    @Autowired
	    private TopicService topicService;
	    
	    @Autowired
	    private UserService userService;
	    
	    @Autowired
	    private RoleService roleService;
	
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
			response=pbAPI.talk("master", chatMessage.getSender(), chatMessage.getContent());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response="Sorry , i am unable to process your request at the moment. Please come back and try again later";
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

    @MessageMapping("/chat.specificProjectApplication")
    @SendTo("/topic/public")
    public ChatMessage specificProjectApplication (@Payload ChatMessage chatMessage,SimpMessageHeaderAccessor headerAccessor){
    	ChatMessage cm=new ChatMessage();
    	String response="";
    	
    	MagicParameters mp = new MagicParameters();
    	PandorabotsAPI pbAPI = new  PandorabotsAPI(mp.getHostName(), mp.getAppId(), mp.getUserKey(),
    			mp.isDebug());
    	
    	try {
			response=pbAPI.talk("master", chatMessage.getSender(), "I want to apply for project 1");
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
    	
    	
    	Set<Topic> topics=topicService.findBySection(sectionService.findByName("Projects").getId());
    	HashMap<Integer,List<String>> projects= new HashMap<Integer,List<String>>();
    	for (Topic topic: topics){
    		ArrayList<String> topicContent= new ArrayList<String>(Arrays.asList(topic.getContent().split(" |\\.|,")));
    		projects.put(topic.getId(), topicContent);
    	}
    	ArrayList<String> userPref= new ArrayList<String>(Arrays.asList(userService.findByUsername(chatMessage.getSender()).getInfo().getAboutMe().split(" |\\.|,")));
    	Recommender recommender= new Recommender(0, projects,userPref);
    	HashMapSort hsort = new HashMapSort(recommender.getRecommendations());
    	HashMap<Integer, HashMap<String, Double>> termsTFIDF=recommender.getTFIDFforProjectTerms();
    	 System.out.println("termtfidf Map: " + Arrays.toString(termsTFIDF.entrySet().toArray()));
    	LinkedHashMap <Integer,Double> result=hsort.sortHashMap();
    	
    	List<Topic>recommendedProjects= new ArrayList<Topic>();
    	for (Integer projectId : result.keySet()){
    		if (result.get(projectId)>0.1 && !projectId.equals(99999999)){
    			Topic topic = topicService.findOne(projectId);
    			recommendedProjects.add(topic);
    	    	System.out.println(topic.getTitle());
    		}
    	}
    	Integer projectId=Integer.parseInt(chatMessage.getContent());
    	
    	HashMap <String,Double> termTfIDFforProject=termsTFIDF.get(projectId);
    	HashMapSort sorter = new HashMapSort(termTfIDFforProject);
    	LinkedHashMap<String,Double> sortedResult = sorter.sortHashMap();
    	
    	System.out.println("Sorted map for project : " + projectId+"Terms :"+sortedResult.entrySet().toArray());
    	List<String> projectSkill = new ArrayList<String>();
    	List<String> acceptedSkills= Arrays.asList("java c sql accounting".split(" "));
    
    	for (String term : sortedResult.keySet()){
    		boolean containsSearchStr = acceptedSkills.stream().filter(s -> s.equalsIgnoreCase(term)).findFirst().isPresent();
    		
    		if(containsSearchStr){
    			projectSkill.add(term);
    		}
    		
    	}
    	
    	String content=" : "+response+projectSkill.get(0) +", " + projectSkill.get(1)+". Would you like to start?";
    
    	
    	cm.setType(ChatMessage.MessageType.CHAT);
    	cm.setSender("chatbot");
    	cm.setContent(content);
        return cm;
    }
}
