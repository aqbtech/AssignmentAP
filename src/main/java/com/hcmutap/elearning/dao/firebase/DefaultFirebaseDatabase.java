package com.hcmutap.elearning.dao.firebase;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.hcmutap.elearning.constant.SystemConstant;
import com.hcmutap.elearning.utils.MapperUtil;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("unchecked")
public class DefaultFirebaseDatabase<T, ID> implements IDefaultFirebaseDatabase<T, ID> {
	private final Firestore db = FirestoreClient.getFirestore();
	private final Class<T> documentClass;
	private final Field documentId;
	private final String collectionPath;
	public DefaultFirebaseDatabase() {
		documentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		FirebaseCollection annotation = AnnotationUtils.findAnnotation(documentClass, FirebaseCollection.class);
		assert annotation != null : String.format("Class %s must have FirebaseCollection annotation", documentClass.getSimpleName());
		documentId = Arrays.stream(documentClass.getDeclaredFields()).filter(
				field -> field.isAnnotationPresent(DocumentId.class)).findFirst().orElseThrow(() ->
				new FirebaseDAOException(String.format("Class %s must have DocumentId annotation", documentClass.getSimpleName())));
		collectionPath = annotation.value();
		assert collectionPath.charAt(0) != '/' : String.format("Collection path of %s must not start with '/'", documentClass.getSimpleName());
	}
	@Override
	public ID save(T t) {
		ReflectionUtils.makeAccessible(documentId);
		ID id = (ID) ReflectionUtils.getField(documentId, t);
		assert id != null : String.format("DocumentId of %s must not be null", documentClass.getSimpleName());
		DocumentReference docRef = db.collection(collectionPath).document();
		docRef.set(t);
		return (ID) docRef.getId();
	}

	@Override
	public T update(T t) {
		ReflectionUtils.makeAccessible(documentId);
		ID id = (ID) ReflectionUtils.getField(documentId, t);
		assert id != null : String.format("DocumentId of %s must not be null", documentClass.getSimpleName());
		DocumentReference docRef = db.collection(collectionPath).document(id.toString());
		ApiFuture<WriteResult> apiFuture = docRef.update(Map.copyOf(MapperUtil.getInstance().toMap(t)));
		// TODO: need to return response model
		return t;
	}
	private List<String> findDocumentId(ID id) throws ExecutionException, InterruptedException {
		ApiFuture<QuerySnapshot> querySnapshotApiFuture = db.collection(collectionPath)
				.whereEqualTo(documentId.getName(), id)
				.get();
		List<QueryDocumentSnapshot> list = querySnapshotApiFuture.get().getDocuments();
		return list.stream().map(DocumentSnapshot::getId).toList();
	}
	@Override
	public void delete(ID id) {
		List<String> ids = null;
		try {
			ids = findDocumentId(id);
			assert ids != null : String.format("%s Not found!", documentClass.getSimpleName());
			db.collection(collectionPath).document(ids.getFirst()).delete();
		} catch (ExecutionException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<T> findAll() {
		try {
			Firestore db = FirestoreClient.getFirestore();
			ApiFuture<QuerySnapshot> querySnapshotApiFuture = db.collection(collectionPath).get();
			List<QueryDocumentSnapshot> list = querySnapshotApiFuture.get().getDocuments();
			return list.stream().map(queryDocumentSnapshot -> queryDocumentSnapshot.toObject(documentClass)).toList();
		} catch (ExecutionException | InterruptedException e) {
			e.printStackTrace();
		}
		return List.of();
	}
	@Override
	public List<T> findBy(String key, String value) {
		try {
			Firestore db = FirestoreClient.getFirestore();
			ApiFuture<QuerySnapshot> querySnapshotApiFuture = db.collection(collectionPath)
					.whereGreaterThanOrEqualTo(key, value)
					.whereLessThan(key, value + '\uf8ff')
					.get();
			List<QueryDocumentSnapshot> list = querySnapshotApiFuture.get().getDocuments();
			return list.stream().map(queryDocumentSnapshot -> queryDocumentSnapshot.toObject(documentClass)).toList();
		} catch (ExecutionException | InterruptedException e) {
			e.printStackTrace();
		}
		return List.of();
	}

	@Override
	public List<T> findBy(String key, String value, Options options) {
		try {
			CollectionReference db = FirestoreClient.getFirestore().collection(collectionPath);
			ApiFuture<QuerySnapshot> querySnapshotApiFuture;
			if (options.getComparison().equalsIgnoreCase(SystemConstant.EQUAL)) {
				querySnapshotApiFuture = db.whereEqualTo(key, value).get();
				List<QueryDocumentSnapshot> list = querySnapshotApiFuture.get().getDocuments();
				return list.stream().map(queryDocumentSnapshot -> queryDocumentSnapshot.toObject(documentClass)).toList();
			} else return findBy(key, value);
		} catch (ExecutionException | InterruptedException e) {
			e.printStackTrace();
		}
		return List.of();
	}

}
