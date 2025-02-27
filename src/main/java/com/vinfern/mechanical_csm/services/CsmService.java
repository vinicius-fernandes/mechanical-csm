package com.vinfern.mechanical_csm.services;

import com.vinfern.mechanical_csm.models.Query;
import com.vinfern.mechanical_csm.models.QueryAnswer;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CsmService {
    @Autowired
    VectorStoreService vectorStoreService;
    @Autowired
    ChatModel chatModel;


    public QueryAnswer query(Query userQuery) {
        String context = vectorStoreService.retrieveRelevantInfo(userQuery.query());
        if (context.equals("No relevant info found.")) {
            context = "No relevant context available.";
        }

        String promptMessage = String.format("""
                You are a customer service assistant for a mechanical shop.
                Your responses must be based **only** on the provided context.
                If the context does not contain enough information to answer, politely state that you don't have the required details and ask for clarification if needed. Remember that you are a customer service assistant for a mechanical shop and should restrict your answer to this.
                Keep your response **concise and to the point**, with a maximum of three lines.

                **Context:** %s

                **Question:** %s
                                """, context, userQuery.query());

        Prompt prompt = new Prompt(new UserMessage(promptMessage));

        return new QueryAnswer(chatModel.call(prompt).getResult().getOutput().getText());
    }
}
