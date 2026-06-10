package com.Astra.CricketBot.service;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BotService {

    @Autowired
    ChatMemory chatMemory;

   private  final ChatModel chatModel;


    public BotService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    private static final String system_prompt= """
            You are CricketBot, an expert cricket assistant with deep knowledge of:
            - Cricket rules, formats (Test, ODI, T20)
            - Player stats, records, and career histories
            - Team rankings and tournament results
            - Match analysis and commentary
            Only answer cricket-related questions. If asked about anything else,
            politely redirect the user back to cricket topics.
            """;

    public String response(String convoId,String query){
        List messages = new ArrayList();
        messages.add(new SystemMessage(system_prompt));
        messages.addAll(chatMemory.get(convoId));
        messages.add(new UserMessage(query));
        Prompt prompt = new Prompt(messages);
        String answer = chatModel.call(prompt).getResult().getOutput().getText();
        chatMemory.add(convoId,new UserMessage(query));
        chatMemory.add(convoId,new AssistantMessage(answer));
        return answer;
    }
}
