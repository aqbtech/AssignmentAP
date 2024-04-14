package com.hcmutap.elearning.dao.firebase;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.hcmutap.elearning.constant.SystemConstant;
import com.hcmutap.elearning.utils.MapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class DefaultFirebaseDatabase<T, ID> implements IDefaultFirebaseDatabase<T, ID> {
	// logger
	private static final Logger logger = LoggerFactory.getLogger(DefaultFirebaseDatabase.class);
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
		// Set the ID to the document reference ID before saving
		ReflectionUtils.setField(documentId, t, docRef.getId());
		docRef.set(t);
		return (ID) docRef.getId();
	}

	@Override
	public T update(T t) {
		ReflectionUtils.makeAccessible(documentId);
		ID id = (ID) ReflectionUtils.getField(documentId, t);
		assert id != null : String.format("DocumentId of %s must not be null", documentClass.getSimpleName());
		DocumentReference docRef = db.collection(collectionPath).document(id.toString());
		Map<String, ?> test = MapperUtil.getInstance().toMap(t);
		ApiFuture<WriteResult> apiFuture = docRef.update(Map.copyOf(test));
		apiFuture.isDone();
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
			logger.error("Error while deleting document with id: {}", id);
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
			logger.error("Error while getting all documents");
		}
		return List.of();
	}

	@Override
	public Page<T> findAll(Pageable pageable) {
		return new PageImpl<>(findAll());
	}

	@Override
	public Page<T> search(String keyword, Pageable pageable) {
		try {
			int offset = (pageable.getPageNumber()) * pageable.getPageSize();
			Query query = db.collection(collectionPath).orderBy("id").startAt(keyword).endAt(keyword + "\uf8ff");
			if (offset > 0) {
				DocumentSnapshot last = query.limit(offset).get().get().getDocuments().get(offset - 1);
				query = query.startAfter(last);
			}
			List<QueryDocumentSnapshot> documents = query.limit(pageable.getPageSize()).get().get().getDocuments();
			List<T> content = documents.stream().map(doc -> doc.toObject(documentClass)).collect(Collectors.toList());
			return new PageImpl<>(content, pageable, this.size());
		} catch (ExecutionException | InterruptedException e) {
			e.printStackTrace();
			logger.error("Error while searching documents with keyword: {}", keyword);
		}
		return Page.empty();
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
			logger.error("Error while getting documents by key: {} and value: {}", key, value);
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
			logger.error("Error while getting documents by key and Options: {} and value: {}", key, value);
		}
		return List.of();
	}

	@Override
	public Long size() {
		try {
			Firestore db = FirestoreClient.getFirestore();
			ApiFuture<QuerySnapshot> querySnapshotApiFuture = db.collection(collectionPath).get();
			List<QueryDocumentSnapshot> documents = querySnapshotApiFuture.get().getDocuments();
			return (long) documents.size();
		} catch (ExecutionException | InterruptedException e) {
			e.printStackTrace();
			logger.error("Error while getting size of collection: {}", collectionPath);
		}
		return 0L;
	}

}
