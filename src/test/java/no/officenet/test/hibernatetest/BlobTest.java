package no.officenet.test.hibernatetest;

import no.officenet.test.hibernatetest.model.Car;
import no.officenet.test.hibernatetest.model.Company;
import no.officenet.test.hibernatetest.model.FileRawData;
import no.officenet.test.hibernatetest.service.CarRepository;
import no.officenet.test.hibernatetest.service.CompanyRepository;
import no.officenet.test.hibernatetest.service.EntityRepository;
import no.officenet.test.hibernatetest.service.PersonRepository;
import org.apache.commons.io.FileUtils;
import org.hibernate.LobHelper;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnitUtil;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;

@Test
@ContextConfiguration(locations = {
	"classpath*:spring/*-context.xml"
})
public class BlobTest extends AbstractTestNGSpringContextTests {

	@Resource
	PersonRepository personRepository;
	@Resource
	CarRepository carRepository;
	@Resource
	CompanyRepository companyRepository;
	@Resource
	EntityRepository entityRepository;
	@Resource
	DataSource dataSource;
	@PersistenceContext
	protected EntityManager entityManager;

	@Resource
	protected PlatformTransactionManager transactionManager;
	@Resource
	protected EntityManagerFactory entityManagerFactory;

	protected PersistenceUnitUtil persistenceUnitUtil;

	protected TransactionTemplate transactionTemplate;

	@BeforeClass
	private void beforeClass() throws Exception {
		transactionTemplate = new TransactionTemplate(transactionManager, new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED));
		persistenceUnitUtil = entityManagerFactory.getPersistenceUnitUtil();
	}

	public void testCreateBlobUsingLobHelperFails() throws Exception {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				try {
					File fph = new File("/home/andreak/a.txt");
					Session session = entityManager.unwrap(Session.class);
					LobHelper lobHelper = session.getLobHelper();
					Blob b = lobHelper.createBlob(new FileInputStream(fph), fph.length());
					Car car = new Car("Vamonda!");
					car.setData(new FileRawData(b));
					carRepository.save(car);
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
		});
	}

	public void testCreateBlobSuccessButCommitFails() throws Exception {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				companyRepository.save(new Company("Stuffaroo_" + System.currentTimeMillis()));
				Blob b = new JdbcTemplate(dataSource).execute(new ConnectionCallback<Blob>() {
					@Override
					public Blob doInConnection(Connection con) throws SQLException, DataAccessException {
						Blob b = con.createBlob();
						OutputStream outputStream = b.setBinaryStream(1);
						try {
							FileUtils.copyFile(new File("/home/andreak/a.txt"), outputStream);
//							FileUtils.copyFile(new File("/home/andreak/Downloads/quantal-desktop-amd64.iso"), outputStream);
							outputStream.flush();
							outputStream.close();
						} catch (IOException e) {
							e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
						}
						return b;
					}
				});
				Car car = new Car("Vamonda!");
				car.setData(new FileRawData(b));
				carRepository.save(car);
			}
		});
	}

	/**
	 * Only difference between testCreateBlobFails is that this method doesn't save using companyRepository first
	 * @throws Exception
	 */
	public void testCreateBlobFails() throws Exception {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				Blob b = new JdbcTemplate(dataSource).execute(new ConnectionCallback<Blob>() {
					@Override
					public Blob doInConnection(Connection con) throws SQLException, DataAccessException {
						Blob b = con.createBlob();
						OutputStream outputStream = b.setBinaryStream(1);
						try {
							FileUtils.copyFile(new File("/home/andreak/a.txt"), outputStream);
//							FileUtils.copyFile(new File("/home/andreak/Downloads/quantal-desktop-amd64.iso"), outputStream);
							outputStream.flush();
							outputStream.close();
						} catch (IOException e) {
							e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
						}
						return b;
					}
				});
				Car car = new Car("Vamonda!");
				car.setData(new FileRawData(b));
				carRepository.save(car);
			}
		});
	}

	public void testReadBlobHangsForever() throws Exception {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				try {
					final Car car = carRepository.retrieve(177l);
					FileUtils.copyInputStreamToFile(car.getData().getData().getBinaryStream(), new File("/home/andreak/hei-" + System.currentTimeMillis() + ".dmp"));
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
		});
	}


}
