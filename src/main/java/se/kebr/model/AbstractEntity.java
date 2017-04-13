package se.kebr.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import com.fasterxml.jackson.annotation.JsonProperty;

import se.kebr.generator.NumberGenerator;

@MappedSuperclass
public abstract class AbstractEntity {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, unique = true)
	private String entityNumber = generateNumber();

	@JsonProperty
	protected String getEntityNumber() {
		return entityNumber;
	}

	@JsonProperty
	public Long getId() {
		return id;
	}

	private String generateNumber() {
		return new NumberGenerator().generate();
	}

}
