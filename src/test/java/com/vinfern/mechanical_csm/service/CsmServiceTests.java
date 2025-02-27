package com.vinfern.mechanical_csm.service;

import com.vinfern.mechanical_csm.models.Query;
import com.vinfern.mechanical_csm.models.QueryAnswer;
import com.vinfern.mechanical_csm.services.CsmService;
import com.vinfern.mechanical_csm.services.VectorStoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class CsmServiceTests {
    @InjectMocks
    CsmService csmService;

    @Mock
    VectorStoreService vectorStoreService;

    @Mock
    ChatModel chatModel;


    @Test
    void testQuery() {
        String userQuery = "What services do you offer?";
        String retrievedContext = "We offer oil changes and brake repairs.";
        String expectedResponse = "We offer oil changes and brake repairs.";
        Generation dummyResult = new Generation(new AssistantMessage(expectedResponse));

        ChatResponse dummyResponse = new ChatResponse(List.of(dummyResult));
        when(vectorStoreService.retrieveRelevantInfo(userQuery)).thenReturn(retrievedContext);


        when(chatModel.call(any(Prompt.class))).thenReturn(dummyResponse);


        QueryAnswer result = csmService.query(new Query(userQuery));

        assertNotNull(result);
        assertEquals(expectedResponse, result.answer());
        verify(vectorStoreService).retrieveRelevantInfo(userQuery);
        verify(chatModel).call(any(Prompt.class));
    }






}
