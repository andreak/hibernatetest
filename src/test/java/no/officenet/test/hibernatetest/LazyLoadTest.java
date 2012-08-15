package no.officenet.test.hibernatetest;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import no.officenet.test.hibernatetest.model.Car;
import no.officenet.test.hibernatetest.model.Company;
import no.officenet.test.hibernatetest.model.Person;
import no.officenet.test.hibernatetest.service.CarRepository;
import no.officenet.test.hibernatetest.service.CompanyRepository;
import no.officenet.test.hibernatetest.service.PersonRepository;
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
import javax.sql.DataSource;
import java.util.Arrays;

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
	DataSource dataSource;

	@Resource
	protected PlatformTransactionManager transactionManager;

	protected TransactionTemplate transactionTemplate;

	private static final String companyName = "OfficeNet AS";

	@BeforeClass
	private void beforeClass() throws Exception {
		transactionTemplate = new TransactionTemplate(transactionManager, new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED));
	}

	public void testLazyLoadLeaksConnections() throws Exception {
		System.out.println("First deleting old data");
		deleteTestData();
		System.out.println("Generating test-data");
		generateTestData();
		ComboPooledDataSource c3poDataSource = (ComboPooledDataSource) dataSource;
		System.out.println("Looping to trigger connection-leak");
		for (int i = 0; i < 10; i++) {
			System.out.println("Iteration " + (i + 1));
			Company officeNet = companyRepository.findByCompanyName(companyName);
			for (Person person : officeNet.getEmployees()) {
				for (Car car : person.getCars()) {
					System.out.println("Used connections in pool: " + c3poDataSource.getNumBusyConnections());
					System.out.println("Employee " + person.getFirstName() + " has car: " + car.getModel());
				}
			}
		}
		// never gets here cause the loop triggers the connection-leak
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
				new Person("andreak", "Andreas", "Krogh", Arrays.asList(
					new Car("Volvo")
				))
			).addEmployee(
				new Person("foo", "Foo", "Bar", Arrays.asList(
					new Car("Ferrari")
				))
			);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				companyRepository.save(officeNet);
			}
		});
	}

}
