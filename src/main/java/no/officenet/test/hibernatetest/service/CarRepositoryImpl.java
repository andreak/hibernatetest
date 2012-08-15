package no.officenet.test.hibernatetest.service;

import no.officenet.test.hibernatetest.infrastructure.jpa.AbstractGenericRepository;
import no.officenet.test.hibernatetest.model.Car;
import org.springframework.stereotype.Repository;

@Repository
public class CarRepositoryImpl extends AbstractGenericRepository<Car, Long> implements CarRepository {

	public CarRepositoryImpl() {
		super(Car.class);
	}
}
