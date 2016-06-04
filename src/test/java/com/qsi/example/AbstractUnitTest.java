package com.qsi.example;


import java.io.File;
import java.sql.Connection;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.qsi.example.config.AbstractUnitConfig;



@ContextConfiguration(classes = AbstractUnitConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ 
	 TransactionDbUnitTestExecutionListener.class, DependencyInjectionTestExecutionListener.class, })
@Transactional
@DatabaseTearDown(value = { "/DbTestData.xml"}, type = com.github.springtestdbunit.annotation.DatabaseOperation.TRUNCATE_TABLE) 
public abstract class AbstractUnitTest
{
	protected IDatabaseConnection dbconn;
	protected IDataSet dataset;
	@Autowired
	@Qualifier("dataSource")
	SimpleDriverDataSource dataSource;

	@Before
	public void loadDbUnitData() throws Exception
	{
		Connection conn = dataSource.getConnection();
		dbconn = new DatabaseConnection(conn);
		dataset = dbconn.createDataSet();
		Resource resource = new ClassPathResource("DbTestData.xml");
		if (resource.exists()) {
			File dataSetXmlFile = resource.getFile();
			FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder().setColumnSensing(true);
			dataset = builder.build(dataSetXmlFile);
			DatabaseOperation.CLEAN_INSERT.execute(dbconn, dataset);
		}
	}
}
