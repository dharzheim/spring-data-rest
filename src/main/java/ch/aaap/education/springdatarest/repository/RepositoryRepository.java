package ch.aaap.education.springdatarest.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import ch.aaap.education.springdatarest.domain.Repository;

public interface RepositoryRepository extends PagingAndSortingRepository<Repository, Long> {
	public Repository findBySlug(String slug);
}
