package ch.aaap.education.springdatarest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ch.aaap.education.springdatarest.domain.Organization;
import ch.aaap.education.springdatarest.domain.Repository;
import ch.aaap.education.springdatarest.domain.User;
import ch.aaap.education.springdatarest.repository.OrganizationRepository;
import ch.aaap.education.springdatarest.repository.RepositoryRepository;
import ch.aaap.education.springdatarest.repository.UserRepository;

@SpringBootApplication
public class SpringDataRestApplication {
	private static final Logger log = LoggerFactory.getLogger(SpringDataRestApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringDataRestApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner demo(OrganizationRepository organizationRepository, UserRepository userRepository, RepositoryRepository repoRepository) {
		return (args) -> {
			Organization org1 = new Organization("3ap");
			Organization org2 = new Organization("IBM");
			Organization org3 = new Organization("UBS");
			
			organizationRepository.save(org1);
			organizationRepository.save(org2);
			organizationRepository.save(org3);
			
			userRepository.save(new User("Daniel Harzheim","harzheim@3ap.ch","test",org1));
			userRepository.save(new User("Cyricl Gabathuler","gabathuler@3ap.ch","test2",org1));
			userRepository.save(new User("IBM Mitarbeiter","ma@ibm.ch","test3",org2));
			
			repoRepository.save(new Repository("Spring Data","spring-data-rest",false,org1));
			repoRepository.save(new Repository("Hello World","hello-world",false,org1));
			repoRepository.save(new Repository("Top Secret Project","top-secret",true,org1));
			repoRepository.save(new Repository("Public","public",false,org2));
			repoRepository.save(new Repository("Private","private",true,org2));
		};
	}
}
