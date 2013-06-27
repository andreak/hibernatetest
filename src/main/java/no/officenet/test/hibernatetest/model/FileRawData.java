package no.officenet.test.hibernatetest.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.sql.Blob;

@Entity
@Table(name = "raw_data")
@SequenceGenerator(name = "SEQ_STORE_RAWDATA", sequenceName = "raw_data_id_seq", allocationSize = 1)
public class FileRawData {

	protected FileRawData() {}
	public FileRawData(Blob data) {
		this.data = data;
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_STORE_RAWDATA")
	private Long id = null;

	public Long getId() {
		return id;
	}

	private Blob data;

	public Blob getData() {
		return data;
	}

	public void setData(Blob data) {
		this.data = data;
	}
}
