package se.kebr.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import se.kebr.model.Team;

public interface TeamRepository extends PagingAndSortingRepository<Team, Long> {

	@Query("SELECT e FROM Team e")
	Collection<Team> getAllTeams();

	Team findByEntityNumber(String number);

}
