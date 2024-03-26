package com.hcmutap.elearning.dao.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.hcmutap.elearning.utils.MapperUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Abstract class for DAO include crud method for model
 * @param <T> model
 */
public class AbstractDAO <T>{
	/**
	 * Add model to database and return id of model
	 * @param collection collection to added
	 * @param model model to added
	 * @return id of model
	 */
	public String create(String collection, T model){
		Firestore db = FirestoreClient.getFirestore();
		DocumentReference docRef = db.collection(collection).document();
		docRef.set(model);
		return docRef.getId();
	}
	/**
	 * Update model in database
	 * @param collection collection to update
	 * @param id id of model to update
	 * @param model model to update
	 */
	public T update(String collection, String id, T model){
		Firestore db = FirestoreClient.getFirestore();
		DocumentReference docRef = db.collection(collection).document(id);
//		docRef.set(model);
		ApiFuture<WriteResult> apiFuture = docRef.update(Map.copyOf(MapperUtil.getInstance().toMap(model)));
		return model;
	}
	/**
	 * Delete model in database
	 * @param collection collection to delete
	 * @param id id of model to delete
	 */
	public void delete(String collection, String id){
		Firestore db = FirestoreClient.getFirestore();
		DocumentReference docRef = db.collection(collection).document(id);
		docRef.delete();
	}
	/**
	 * Get model from database
	 * @param collection collection to get
	 * @param id id of model to get
	 * @param tClass class of model
	 * @return model
	 */
	public T get(String collection, String id, Class<T> tClass) throws ExecutionException, InterruptedException {
		Firestore db = FirestoreClient.getFirestore();
		DocumentReference docRef = db.collection(collection).document(id);
		return docRef.get().get().toObject(tClass);
	}
	/**
	 * Get all model from database
	 * @param collection collection to get
	 * @param tClass class of model
	 * @return list of model
	 */
	public List<T> query(String collection, Class<T> tClass) throws ExecutionException, InterruptedException {
		Firestore db = FirestoreClient.getFirestore();
		ApiFuture<QuerySnapshot> querySnapshotApiFuture = db.collection(collection).get();
		List<QueryDocumentSnapshot> list = querySnapshotApiFuture.get().getDocuments();
		return list.stream().map(queryDocumentSnapshot -> queryDocumentSnapshot.toObject(tClass)).toList();
	}
	/**
	 * Get model from database by key and value
	 * @param collection collection to get
	 * @param key key to get
	 * @param value value to get
	 * @param tClass class of model
	 * @return list of model
	 */
	public List<T> queryBy(String collection, String key, String value, Class<T> tClass) throws ExecutionException, InterruptedException {
		Firestore db = FirestoreClient.getFirestore();
		ApiFuture<QuerySnapshot> querySnapshotApiFuture = db.collection(collection)
				.whereGreaterThanOrEqualTo(key, value)
				.whereLessThan(key, value + '\uf8ff')
				.get();
		List<QueryDocumentSnapshot> list = querySnapshotApiFuture.get().getDocuments();
		return list.stream().map(queryDocumentSnapshot -> queryDocumentSnapshot.toObject(tClass)).toList();
	}
	/**
	 * Find document id by key and value
	 * @param collection collection to find
	 * @param key key to find
	 * @param value value to find
	 * @return list of id document
	 */
	protected List<String> findDocumentId(String collection, String key, String value) throws ExecutionException, InterruptedException {
		Firestore db = FirestoreClient.getFirestore();
		ApiFuture<QuerySnapshot> querySnapshotApiFuture = db.collection(collection)
				.whereGreaterThanOrEqualTo(key, value)
				.whereLessThan(key, value + '\uf8ff')
				.get();
		List<QueryDocumentSnapshot> list = querySnapshotApiFuture.get().getDocuments();
		return list.stream().map(queryDocumentSnapshot -> queryDocumentSnapshot.getReference().getId()).toList();
	}
}
