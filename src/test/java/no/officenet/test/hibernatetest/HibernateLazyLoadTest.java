package no.officenet.test.hibernatetest;

import no.officenet.test.hibernatetest.model.AbstractEntity;
import no.officenet.test.hibernatetest.model.Car;
import no.officenet.test.hibernatetest.model.Company;
import no.officenet.test.hibernatetest.model.Person;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class HibernateLazyLoadTest extends BaseCoreFunctionalTestCase {

	private static final String companyName = "OfficeNet AS";
	private static final String personUserName = "andreak";

	public HibernateLazyLoadTest() {
		System.setProperty( "hibernate.enable_specj_proprietary_syntax", "true" );
	}

	@Before
	public void setUpData() {
		Session s = openSession();
		s.beginTransaction();
		Company officeNet = new Company(companyName)
			.addEmployee(
				new Person(personUserName, "Andreas", "Krogh", Arrays.asList(
					new Car("Volvo")
				))
			).addEmployee(
				new Person("foo", "Foo", "Bar", Arrays.asList(
					new Car("Ferrari")
				))
			);
		s.persist( officeNet);
		s.getTransaction().commit();
		s.close();
	}

	@After
	public void cleanUpData() {
		Session s = openSession();
		s.beginTransaction();
		s.delete( s.get( Company.class, 1 ) );
		s.getTransaction().commit();
		s.close();
	}

	@Test
	public void testLazyLoad() {
		Session s = openSession();
		s.beginTransaction();
		Company officeNet = (Company) s.get(Company.class, 1L);
		s.getTransaction().commit();
		s.close();

		Assert.assertNotNull(officeNet);
		for (Person person : officeNet.getEmployees()) {
			for (Car car : person.getCars()) {
				System.out.println("Employee " + person.getFirstName() + " has car: " + car.getModel());
			}
		}

	}

	@Override
	protected void configure(Configuration cfg) {
		super.configure( cfg );
		cfg.setProperty( Environment.ENABLE_LAZY_LOAD_NO_TRANS, "true" );
		cfg.setProperty( Environment.GENERATE_STATISTICS, "true" );
	}

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] { AbstractEntity.class, Company.class, Person.class, Car.class };
	}
}
