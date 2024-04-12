package com.hcmutap.elearning.service;


import com.hcmutap.elearning.exception.NotFoundException;

import java.util.List;

/**
 * This is a generic interface for admin service includes all common methods for admin service
 * @param <T>
 */
public interface IGenericAdminService<T> {
	/**
	 * Find all objects
	 * @return list of objects
	 */
	List<T> findAll();
	List<T> findBy(String key, String value)  throws NotFoundException;
	/**
	 * Find object by id
	 * @param id id of object
	 * @return object
	 */
	T findById(String id) throws NotFoundException;

	/**
	 * Save object
	 * @param object object model to save
	 * @return id of object
	 */
	String save(T object);

	/**
	 * Update object
	 * @param object object model to update
	 */
	void update(T object) throws NotFoundException;

	/**
	 * Delete object by id
	 * @param ids list of id need to delete
	 */
	void delete(List<String> ids) throws NotFoundException;

}
