package se.kebr.repository;

import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import se.kebr.model.Status;
import se.kebr.model.Team;
import se.kebr.model.User;
import se.kebr.model.WorkItem;

public interface WorkItemRepository extends PagingAndSortingRepository<WorkItem, Long> {

	Collection<WorkItem> findWorkItemsByStatus(Status status);

	Collection<WorkItem> findWorkItemByUserTeam(Team team);

	Collection<WorkItem> findWorkItemByUser(User user);

	Collection<WorkItem> findByDescriptionContains(String description);

	WorkItem findByEntityNumber(String entityNumber);

	Page<WorkItem> findAll(Pageable pageable);

	@Query(value = "SELECT * FROM WorkItem WHERE finished >= ?1 AND finished <= ?2", nativeQuery = true)
	Collection<WorkItem> findByFinishedBetween(String firstDate, String secondDate);
}
