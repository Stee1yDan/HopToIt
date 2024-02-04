package com.example.stockinfoservice.service;

import com.example.stockinfoservice.model.Stock;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class StockService
{
    private String StockCollectionName = "stock";
    public String createStock(Stock stock) throws ExecutionException, InterruptedException
    {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collections = firestore
                .collection(StockCollectionName)
                .document(stock.getSymbol())
                .set(stock);
        return collections.get().getUpdateTime().toString();
    }

    public Stock getStock(String documentId) throws ExecutionException, InterruptedException
    {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection(StockCollectionName).document(documentId);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot documentSnapshot = future.get();

        if (documentSnapshot != null)
            return documentSnapshot.toObject(Stock.class);
        return null;
    }

    public void deleteStock(String documentId)
    {
        Firestore firestore = FirestoreClient.getFirestore();
        firestore
                .collection(StockCollectionName)
                .document(documentId)
                .delete();
    }

    public String updateStock(Stock stock) throws ExecutionException, InterruptedException
    {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> apiFuture = firestore
                .collection(StockCollectionName)
                .document(stock.getSymbol())
                .set(stock);
        return apiFuture.get().getUpdateTime().toString();
    }
}
