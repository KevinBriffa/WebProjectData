package se.kebr.model;

import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@JsonIgnoreProperties(ignoreUnknown = false)
public class Team extends AbstractEntity {

	@Column(nullable = false, unique = true)
	private String name;

	@OneToMany(mappedBy = "team")
	private Collection<User> users;

	protected Team() {
	}

	public Team(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	@JsonProperty
	public Long getId() {
		return super.getId();
	}

	@JsonProperty("entityNumber")
	public String getTeamNumber() {
		return getEntityNumber();
	}

	public void setName(String name) {
		this.name = name;
	}

}
