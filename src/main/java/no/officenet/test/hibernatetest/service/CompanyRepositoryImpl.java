package no.officenet.test.hibernatetest.service;

import no.officenet.test.hibernatetest.infrastructure.jpa.AbstractGenericRepository;
import no.officenet.test.hibernatetest.model.Company;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class CompanyRepositoryImpl extends AbstractGenericRepository<Company, Long> implements CompanyRepository {

	public CompanyRepositoryImpl() {
		super(Company.class);
	}

	@Override
	public Company findByCompanyName(String companyName) {
		try {
			return entityManager.createQuery("SELECT c FROM Company c WHERE c.name = :companyName", entityClass).
				setParameter("companyName", companyName).
				getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}
}
