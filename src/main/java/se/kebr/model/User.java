package se.kebr.model;

import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@JsonIgnoreProperties(ignoreUnknown = false)
public class User extends AbstractEntity {

	@Column(unique = false, nullable = false)
	private String firstname;
	@Column(unique = false, nullable = false)
	private String lastname;
	@Column(unique = true, nullable = false)
	private String username;
	@Enumerated(EnumType.STRING)
	private Status status;

	@ManyToOne
	private Team team;

	@OneToMany(mappedBy = "user")
	private Collection<WorkItem> workItems;

	public User() {
	}

	public User(String firstname, String lastname, String username, Status status) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.status = status;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getUsername() {
		return username;
	}

	public Status getStatus() {
		return status;
	}

	@JsonProperty("entityNumber")
	public String getUserNumber() {
		return super.getEntityNumber();
	}

	@Override
	public Long getId() {
		return super.getId();
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setWorkItems(Collection<WorkItem> workItems) {
		this.workItems = workItems;
	}

	@Override
	public String toString() {
		return String.format("firstname:%s, lastname:%s, username:%s", firstname, lastname, username);
	}

}
