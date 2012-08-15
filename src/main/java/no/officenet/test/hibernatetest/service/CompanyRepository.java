package no.officenet.test.hibernatetest.service;


import no.officenet.test.hibernatetest.infrastructure.jpa.GenericRepository;
import no.officenet.test.hibernatetest.model.Company;

public interface CompanyRepository extends GenericRepository<Company, Long> {

	Company findByCompanyName(String companyName);
}
