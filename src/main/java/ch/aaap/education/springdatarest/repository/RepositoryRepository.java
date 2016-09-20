package ch.aaap.education.springdatarest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import ch.aaap.education.springdatarest.domain.Repository;

public interface RepositoryRepository extends PagingAndSortingRepository<Repository, Long> {
	@Override
	@PostFilter("!filterObject.isPrivate or filterObject.organization.id == principal.organization.id")
	Iterable<Repository> findAll();
	
	@Override
	@PostFilter("!filterObject.isPrivate or filterObject.organization.id == principal.organization.id")
	Iterable<Repository> findAll(Sort sort);
	
	@Override
	@Query("select r from Repository r where r.isPrivate=0 OR r.organization.id = ?#{ principal.organization.id }")
	Page<Repository> findAll(Pageable pageable);
	
	@Override
	@PostAuthorize("!returnObject.isPrivate or returnObject.organization.id == principal.organization.id")
	Repository findOne(Long id);
	
	@Override
	@PreAuthorize("#repository.organization.id == principal.organization.id")
	void delete(Repository repository);
	
	public Repository findBySlug(String slug);
}
