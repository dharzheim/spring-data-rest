package ch.aaap.education.springdatarest.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ch.aaap.education.springdatarest.domain.User;

@RepositoryRestResource
public interface UserRepository extends CrudRepository<User, Long> {
	public List<User> findByName(String name);
	public User findByEmail(String email);
	public List<User> findByOrganizationId(Long organizationId);
}
