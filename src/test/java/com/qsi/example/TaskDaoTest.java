package com.qsi.example;

import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class TaskDaoTest extends AbstractUnitTest
{
	@Autowired
	TaskDao taskDao;

	@Test
	public void testSave() throws Exception
	{
		// DbTestData.xml has 2 rows that get inserted before each test
		assertEquals(2, dataset.getTable("task").getRowCount());
		assertEquals(2, taskDao.list().size());
		Task task = new Task().setDescription("Write a bunch of stuff").setDueDate("12/01/2011");
		taskDao.save(task);
		assertEquals(3, taskDao.list().size());
		// Hibernate session has not flushed to dbunit db
		assertEquals(2, dataset.getTable("task").getRowCount());
    }

	@Test
	public void testList() throws Exception
	{
		assertEquals(2, taskDao.list().size());
	}

	@Test
	public void testGet()
	{
		assertEquals("test first something", taskDao.get(123).getDescription());
	}
}
