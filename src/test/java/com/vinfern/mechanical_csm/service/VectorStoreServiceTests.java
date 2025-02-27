package com.vinfern.mechanical_csm.service;

import com.vinfern.mechanical_csm.models.ServiceInfo;
import com.vinfern.mechanical_csm.services.VectorStoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SimpleVectorStore;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VectorStoreServiceTests {
    @InjectMocks
    VectorStoreService vectorStoreService;
    @Mock
    SimpleVectorStore vectorStore;

    @Test
    void testAddServiceInfo() {
        List<ServiceInfo> serviceInfos = List.of(
                new ServiceInfo("Service 1 description"),
                new ServiceInfo("Service 2 description")
        );


        vectorStoreService.addServiceInfo(serviceInfos);

        verify(vectorStore, times(2)).add(anyList());

    }

    @Test
    void testRetrieveRelevantInfo() {
        String query = "classic cars";
        Document mockDocument = new Document("Information about classic cars.");
        when(vectorStore.similaritySearch(query)).thenReturn(List.of(mockDocument));

        String result = vectorStoreService.retrieveRelevantInfo(query);

        assertEquals(mockDocument.getFormattedContent(), result, "The retrieved content should match the mock document content.");
    }

    @Test
    void testRetrieveRelevantInfoNoResults() {
        String query = "non-existent topic";
        when(vectorStore.similaritySearch(query)).thenReturn(List.of());

        String result = vectorStoreService.retrieveRelevantInfo(query);

        assertEquals("No relevant info found.", result, "The result should indicate no relevant information was found.");
    }
}
