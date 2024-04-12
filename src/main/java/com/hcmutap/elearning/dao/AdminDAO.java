package com.hcmutap.elearning.dao;


import com.hcmutap.elearning.dao.firebase.Options;
import com.hcmutap.elearning.exception.NotFoundException;

import java.util.List;
public interface AdminDAO<T> {
	String save(T model);
	T update(T model);
	void delete(String id);
	T findById(String id) throws NotFoundException;
	List<T> findAll();
	List<T> findBy(String key, String value);
	List<T> findBy(String key, String value, Options options);
}
