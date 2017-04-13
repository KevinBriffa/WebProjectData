package se.kebr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@JsonIgnoreProperties(ignoreUnknown = false)
public class Issue extends AbstractEntity {

	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String description;

	@OneToOne(mappedBy = "issue")
	@JoinColumn(unique = true)
	private WorkItem item;

	protected Issue() {
	}

	public Issue(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	@JsonProperty("entityNumber")
	public String getIssuenumber() {
		return getEntityNumber();
	}

}
