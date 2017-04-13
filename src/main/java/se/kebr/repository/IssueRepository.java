package se.kebr.repository;

import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import se.kebr.model.Issue;
import se.kebr.model.WorkItem;

public interface IssueRepository extends PagingAndSortingRepository<Issue, Long> {

	@Query("SELECT e FROM WorkItem e join fetch e.issue ed WHERE ed.id IS NOT NULL")
	Collection<WorkItem> getIssuedWorkItems();

	Page<Issue> findAll(Pageable pageable);

}
