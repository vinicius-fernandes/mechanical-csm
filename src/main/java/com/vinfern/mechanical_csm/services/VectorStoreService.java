package com.vinfern.mechanical_csm.services;

import com.vinfern.mechanical_csm.models.ServiceInfo;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VectorStoreService {
    @Autowired
    SimpleVectorStore vectorStore;
    @PostConstruct
    private void initiateServiceInfo(){
        var basicStoreInfo= List.of(
                new ServiceInfo("In the heart of a quaint, picturesque small town, nestled between charming cafes and antique stores, lies a\n" +
                        "bustling mechanical shop known as \"Town Mechanics.\" The shop is easily recognizable by its distinctive red and\n" +
                        "white sign adorned with an image of a gear and wrench."),
                new ServiceInfo("Upon entering the shop, customers are greeted by the pleasant smell of oil and freshly-cut metal, a testament to\n" +
                        "the skilled craftsmanship that takes place within. The walls are lined with shelves filled with an array of tools\n" +
                        "and spare parts, each carefully organized and ready for use. A large, open garage area occupies most of the space,\n" +
                        "where mechanics work tirelessly on various vehicles, from classic cars to modern SUVs."),
                new ServiceInfo("The team at Town Mechanics takes pride in their attention to detail and commitment to quality. They understand\n" +
                        "that a vehicle is not just a means of transportation but a cherished possession for many, and they treat each one\n" +
                        "with the care and respect it deserves. Customers can expect friendly service, fair prices, and expert advice on\n" +
                        "everything from routine maintenance to extensive repairs."),
                new ServiceInfo("In addition to their services, Town Mechanics also hosts workshops and classes for those interested in learning\n" +
                        "more about auto repair. Whether a seasoned mechanic or a curious beginner, there's always something new to learn\n" +
                        "at this welcoming shop. With its knowledgeable staff, state-of-the-art equipment, and warm, community-oriented\n" +
                        "atmosphere, Town Mechanics has earned its place as the go-to mechanical shop in the small town it calls home.")
        );
        this.addServiceInfo(basicStoreInfo);
    }

    public void addServiceInfo(List<ServiceInfo> serviceInfo) {
        serviceInfo.parallelStream().forEach(s->{
            Document document = new Document(s.content());
            vectorStore.add(List.of(document));
        });
    }

    public String retrieveRelevantInfo(String query) {
        List<Document> results = vectorStore.similaritySearch(query);
        return results.isEmpty() ? "No relevant info found." : results.get(0).getFormattedContent();
    }
}
