package com.example.stockinfoservice.service;

import com.example.stockinfoservice.model.StockFormattedInfo;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FirebaseService
{
    public String createDocument(String documentId, Object document, String collectionName)
    {
        try {
            Firestore firestore = FirestoreClient.getFirestore();
            ApiFuture<WriteResult> collections = firestore
                    .collection(collectionName)
                    .document(documentId)
                    .set(document);
            return collections.get().getUpdateTime().toString();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Object getDocument(String collectionName, String documentId, Class template)
    {
        try{
            Firestore firestore = FirestoreClient.getFirestore();
            DocumentReference documentReference = firestore.collection(collectionName).document(documentId);
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot documentSnapshot = future.get();
            if (documentSnapshot != null)
                return documentSnapshot.toObject(template);
            return null;
        }
        catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
    }

    public void deleteDocument(String collectionName, String documentId)
    {
        Firestore firestore = FirestoreClient.getFirestore();
        firestore
                .collection(collectionName)
                .document(documentId)
                .delete();
    }

}
