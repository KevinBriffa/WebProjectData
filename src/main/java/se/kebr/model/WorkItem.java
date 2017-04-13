package se.kebr.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@JsonIgnoreProperties(ignoreUnknown = false)
public class WorkItem extends AbstractEntity {

	@Column(nullable = false)
	private String name;
	private String description;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;

	@ManyToOne(cascade = CascadeType.DETACH)
	private User user;

	@OneToOne(cascade = CascadeType.REMOVE)
	private Issue issue;

	protected WorkItem() {
	}

	public WorkItem(String name, String description, Status status) {
		this.name = name;
		this.description = description;
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Status getStatus() {
		return status;
	}

	@JsonProperty("entityNumber")
	public String getItemnumber() {
		return getEntityNumber();
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setIssue(Issue issue) {
		this.issue = issue;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

}
