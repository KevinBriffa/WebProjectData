package se.kebr.service;

import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import se.kebr.exceptions.ServiceException;
import se.kebr.model.Issue;
import se.kebr.model.Status;
import se.kebr.model.Team;
import se.kebr.model.User;
import se.kebr.model.WorkItem;
import se.kebr.repository.IssueRepository;
import se.kebr.repository.TeamRepository;
import se.kebr.repository.UserRepository;
import se.kebr.repository.WorkItemRepository;

@Component
public class EntityService {

	private final UserRepository userRepository;
	private final TeamRepository teamRepository;
	private final WorkItemRepository workItemRepository;
	private final IssueRepository issueRepository;
	private final ServiceTransaction executor;

	@Autowired
	public EntityService(UserRepository userRepository, TeamRepository teamRepository,
			WorkItemRepository workItemRepository, IssueRepository issueRepository, ServiceTransaction executor) {
		this.userRepository = userRepository;
		this.teamRepository = teamRepository;
		this.workItemRepository = workItemRepository;
		this.issueRepository = issueRepository;
		this.executor = executor;
	}

	public User saveOrUpdateUser(User user) {
		if (user.getUsername().length() < 5) {
			throw new ServiceException("Can not add user.");
		}
		return userRepository.save(user);
	}

	public User inactivateUser(Long userId) {
		try {
			return executor.execute(() -> {
				User user = userRepository.findOne(userId);
				user.setStatus(Status.UNACTIVE);
				setItemStatus(user);
				return userRepository.save(user);
			});
		} catch (DataAccessException e) {
			throw new ServiceException("Can not inactivate user");
		}
	}

	public User addUserToTeam(Long teamId, Long userId) {
		try {
			if (!isValidNumbersOfUsers(teamId)) {
				throw new ServiceException("Can not add user to team");
			}

			return executor.execute(() -> {
				User user = userRepository.findOne(userId);
				Team team = teamRepository.findOne(teamId);
				user.setTeam(team);
				return userRepository.save(user);
			});
		} catch (DataAccessException e) {
			throw new ServiceException("Could not add user to team. ");
		}
	}

	public List<User> findUserByAll(String content) {
		return userRepository.getUserByAll(content);
	}

	public User findUserByNumber(String number) {
		return userRepository.findByEntityNumber(number);
	}

	public List<User> getAllUsersInATeam(Long id) {
		return userRepository.getAllUsersInATeam(id);
	}

	public List<User> getAllUsersInATeam(String entityNumber) {
		return userRepository.getAllUsersInATeam(entityNumber);
	}

	public Page<User> getAllUsersByPage(int page, int size) {
		return userRepository.findAll(new PageRequest(page, size));
	}

	public Team saveOrUpdateTeam(Team team) {
		return teamRepository.save(team);
	}

	public Team findTeamById(Long id) {
		return teamRepository.findOne(id);
	}

	public Collection<Team> getAllTeams() {
		return teamRepository.getAllTeams();
	}

	public Team findTeamByEntityNumber(String teamNumber) {
		return teamRepository.findByEntityNumber(teamNumber);
	}

	public WorkItem saveOrUpdateWorkItem(WorkItem workItem) {
		return workItemRepository.save(workItem);
	}

	public WorkItem changeWorkStatus(Long itemId, Status status) {
		try {
			return executor.execute(() -> {
				if (status == Status.DONE) {
					throw new ServiceException("Can't set workstatus to finished.");
				}
				WorkItem workItem = workItemRepository.findOne(itemId);
				workItem.setStatus(status);
				return workItemRepository.save(workItem);
			});
		} catch (DataAccessException e) {
			throw new ServiceException("Could not change status");
		}
	}

	public void deleteWorkItem(Long itemId) {
		workItemRepository.delete(itemId);
	}

	public WorkItem assignWorkItem(Long itemId, Long userId) {
		try {
			if (!isActiveUser(userId)) {
				throw new ServiceException("User is not active.");
			}
			if (!isValidNumbersOfItems(userId)) {
				throw new ServiceException("Maximum amount of Workitems.");
			}
			return executor.execute(() -> {
				WorkItem item = workItemRepository.findOne(itemId);
				User user = userRepository.findOne(userId);
				item.setUser(user);
				userRepository.save(user);
				return workItemRepository.save(item);
			});
		} catch (DataAccessException e) {
			throw new ServiceException("Could not assign item. ");
		}
	}

	public Collection<WorkItem> getWorkItemsByStatus(Status status) {
		if (status == Status.UNSTARTED || status == Status.STARTED || status == Status.DONE) {
			return workItemRepository.findWorkItemsByStatus(status);
		}
		throw new ServiceException("Invalid status");
	}

	public Collection<WorkItem> getWorkItemsFromTeam(Long teamId) {
		Team team = teamRepository.findOne(teamId);
		return workItemRepository.findWorkItemByUserTeam(team);
	}

	public Collection<WorkItem> getWorkItemsByUser(Long userId) {
		User user = userRepository.findOne(userId);
		return workItemRepository.findWorkItemByUser(user);
	}

	public Collection<WorkItem> getWorkItemByDescription(String description) {
		return workItemRepository.findByDescriptionContains(description);
	}

	public WorkItem getWorkItemByNumber(String entityNumber) {
		return workItemRepository.findByEntityNumber(entityNumber);
	}

	public Page<WorkItem> getAllItemsByPage(int page, int size) {
		return workItemRepository.findAll(new PageRequest(page, size));
	}

	public Collection<WorkItem> getItemsBetweenDates(String firstDate, String secondDate) {
		return workItemRepository.findByFinishedBetween(firstDate, secondDate);
	}

	public Issue saveOrUpdateIssue(Issue issue, Long itemId) {
		try {
			if (!workItemIsDone(itemId)) {
				throw new ServiceException("Workitem is not done.");
			}

			return executor.execute(() -> {
				WorkItem item = workItemRepository.findOne(itemId);
				item.setStatus(Status.UNSTARTED);
				issueRepository.save(issue);
				item.setIssue(issue);
				workItemRepository.save(item);
				return issue;
			});
		} catch (DataAccessException e) {
			throw new ServiceException("Could not save or update issue. ");
		}
	}

	public Collection<WorkItem> getIssuedWorkItems() {
		return issueRepository.getIssuedWorkItems();
	}

	public Page<Issue> getAllIssuesByPage(int page, int size) {
		return issueRepository.findAll(new PageRequest(page, size));
	}

	private void setItemStatus(User user) {
		Collection<WorkItem> items = getWorkItemsByUser(user.getId());
		for (WorkItem currentItem : items) {
			currentItem.setStatus(Status.UNSTARTED);
			workItemRepository.save(currentItem);
		}

	}

	private boolean isValidNumbersOfUsers(Long teamId) {
		return getAllUsersInATeam(teamId).size() < 10;
	}

	private boolean isActiveUser(Long userId) {
		return userRepository.findOne(userId).getStatus() == Status.ACTIVE;
	}

	private boolean isValidNumbersOfItems(Long userId) {
		return getWorkItemsByUser(userId).size() < 5;
	}

	private boolean workItemIsDone(Long itemId) {
		return workItemRepository.findOne(itemId).getStatus() == Status.DONE;
	}

}
