package no.officenet.test.hibernatetest;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import no.officenet.test.hibernatetest.model.AbstractEntity;
import no.officenet.test.hibernatetest.model.Car;
import no.officenet.test.hibernatetest.model.Company;
import no.officenet.test.hibernatetest.model.Person;
import no.officenet.test.hibernatetest.service.CarRepository;
import no.officenet.test.hibernatetest.service.CompanyRepository;
import no.officenet.test.hibernatetest.service.EntityRepository;
import no.officenet.test.hibernatetest.service.PersonRepository;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnitUtil;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;

@Test
@ContextConfiguration(locations = {
	"classpath*:spring/*-context.xml"
})
public class LazyLoadTest extends AbstractTestNGSpringContextTests {

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

	private static final String companyName = "OfficeNet AS";
	private static final String personUserName = "andreak";

	@BeforeClass
	private void beforeClass() throws Exception {
		transactionTemplate = new TransactionTemplate(transactionManager, new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED));
		persistenceUnitUtil = entityManagerFactory.getPersistenceUnitUtil();
	}

	@BeforeMethod
	private void beforeEachMethod() throws Exception {
		generateTestData();
	}

	@AfterMethod
	private void afterEachMethod() throws Exception {
		deleteTestData();
	}

	@SuppressWarnings({"unchecked"})
	protected <T> T getTargetObject(Object proxy, Class<T> targetClass) throws Exception {
		if (AopUtils.isJdkDynamicProxy(proxy)) {
			return (T) ((Advised)proxy).getTargetSource().getTarget();
		} else {
			return (T) proxy; // expected to be cglib proxy then, which is simply a specialized class
		}
	}

	public void testLazyLoadLeaksConnections() throws Exception {
		ComboPooledDataSource c3poDataSource = (ComboPooledDataSource) dataSource;
		System.out.println("Looping to trigger connection-leak");
		for (int i = 0; i < 10; i++) {
			System.out.println("Iteration " + (i + 1));
			Company officeNet = transactionTemplate.execute(new TransactionCallback<Company>() {
				@Override
				public Company doInTransaction(TransactionStatus status) {
					return companyRepository.findByCompanyName(companyName);
				}
			});
			for (Person person : officeNet.getEmployees()) {
				for (Car car : person.getCars()) {
					System.out.println("Used connections in pool: " + c3poDataSource.getNumBusyConnections());
					System.out.println("Employee " + person.getFirstName() + " has car: " + car.getModel());
				}
			}
		}
		// never gets here cause the loop triggers the connection-leak
	}

	public void retreiveEntity() throws Exception {
		AbstractEntity abstractEntity = entityRepository.retrieve(1L);
		System.out.println(abstractEntity);
	}

	public void testLazyLoadManyToOne() throws Exception {
		final Person person = personRepository.findByUserName(personUserName);
		Company company = person.getCompany();
		System.out.println("Got " + person.getFirstName() + "'s company: " + company.getName());
	}

	public void testLazyLoadManyToOneWithNaturalKeyFails() throws Exception {
		Long carId = transactionTemplate.execute(new TransactionCallback<Long>() {
			@Override
			public Long doInTransaction(TransactionStatus status) {
				Person p = personRepository.findByUserName(personUserName);
				Car car = p.getCars().get(0);
				return car.getId();
			}
		});
		System.out.println("Retrieving car with id = " + carId);
		Car car = carRepository.retrieve(carId);
		Assert.assertTrue(persistenceUnitUtil.isLoaded(car.getOwner()), "car.getOwner is lazy but should be initialized because it's not mapped by PK");
	}

	public void testPolymorphicAssoc() throws Exception {
		Company on = companyRepository.findByCompanyName(companyName);
		System.out.println("Got company");
		System.out.println(on.getRole());
		Assert.assertTrue(on.getRole() instanceof Person);
		Person role = (Person) on.getRole();
		System.out.println("role: " + role.getFirstName() + " " + role.getLastName());
	}

	private void deleteTestData() {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				Company companyToDelete = companyRepository.findByCompanyName(companyName);
				if (companyToDelete != null) {
					companyRepository.remove(companyToDelete);
				}
			}
		});
	}

	private void generateTestData() {
		final Company officeNet = new Company(companyName)
			.addEmployee(
				new Person(personUserName, "Andreas", "Krogh", Arrays.asList(
					new Car("Volvo")
				))
			).addEmployee(
				new Person("foo", "Foo", "Bar", Arrays.asList(
					new Car("Ferrari")
				))
			);
		final Person superRole = new Person("superRole", "Super", "Role", Collections.<Car>emptyList());
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				Company savedON = companyRepository.save(officeNet);
				savedON.setRole(personRepository.save(superRole));
				companyRepository.save(savedON);
			}
		});
	}

}
