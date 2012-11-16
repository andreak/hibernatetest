package no.officenet.test.hibernatetest.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "email_address")
public class EmailAddress extends AbstractEntity {

	@Column(name = "adress")
	@Basic(optional = false)
	String address = null;
}
