package se.kebr.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import se.kebr.model.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

	User findByEntityNumber(String entityNumber);

	@Query("SELECT u from User u join fetch u.team ed WHERE ed.id = ?1")
	List<User> getAllUsersInATeam(Long team_id);

	@Query("SELECT u FROM User u join fetch u.team t WHERE t.entityNumber = ?1")
	List<User> getAllUsersInATeam(String number);

	Page<User> findAll(Pageable pageable);

	@Query("SELECT u FROM User u where u.firstname = ?1 OR u.lastname = ?1 OR u.username = ?1")
	List<User> getUserByAll(String content);

}
