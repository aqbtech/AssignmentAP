package com.hcmutap.elearning.dao;


import java.util.List;
public interface AdminDAO<T> {
	String save(T model);
	T update(T model);
	void delete(String id);
	T findById(String id);
	List<T> findAll();
	List<T> findBy(String key, String value);
}
