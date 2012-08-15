package no.officenet.test.hibernatetest.service;


import no.officenet.test.hibernatetest.infrastructure.jpa.GenericRepository;
import no.officenet.test.hibernatetest.model.Car;

public interface CarRepository extends GenericRepository<Car, Long> {
}
