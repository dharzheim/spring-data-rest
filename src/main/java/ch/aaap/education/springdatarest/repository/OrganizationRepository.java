package ch.aaap.education.springdatarest.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ch.aaap.education.springdatarest.domain.Organization;

@RepositoryRestResource
public interface OrganizationRepository extends CrudRepository<Organization, Long> {
	public List<Organization> findByName(String name);
}
