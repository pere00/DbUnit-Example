package com.qsi.example;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public class TaskDao
{
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void save(Task task)
	{
		entityManager.persist(task);
		entityManager.flush();
	}

	@SuppressWarnings("unchecked")
	public List<Task> list()
	{
		return entityManager.createQuery("select t from Task t").getResultList();
	}

	@SuppressWarnings("unchecked")
	public Task get(long id)
	{
		return entityManager.find(Task.class, id);
	}
	@Transactional
	public void remove(long id)
	{
		Task task = entityManager.find(Task.class, id);
		entityManager.remove(task);
	
	}
	
}
