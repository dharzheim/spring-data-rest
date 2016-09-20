package ch.aaap.education.springdatarest;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.spi.EvaluationContextExtension;
import org.springframework.data.repository.query.spi.EvaluationContextExtensionSupport;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


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
	
	@Configuration
	class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

		@Autowired
	 	UserRepository userRepository;
	
		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService());
		}
		
		@Configuration
		@EnableJpaRepositories
		class SecurityConfiguration {

		    @Bean
		    EvaluationContextExtension securityExtension() {
		        return new SecurityEvaluationContextExtension();
		    }
		    
		    class SecurityEvaluationContextExtension extends EvaluationContextExtensionSupport {

				@Override
				public String getExtensionId() {
					return "security";
				}
		
				@Override
				public SecurityExpressionRoot getRootObject() {
					Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
					return new SecurityExpressionRoot(authentication) {};
				}
			}
		}
		
		
	
		@Bean
		UserDetailsService userDetailsService() {
			return new UserDetailsService() {
	
				@Override
				public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
					User user = userRepository.findByEmail(username);
					if(user != null) {
						return new UserDetailWithOrganizationImpl(user.getEmail(), user.getPassword(),
								AuthorityUtils.createAuthorityList("USER"),user.getOrganization());
					} else {
						throw new UsernameNotFoundException("could not find the user '" + username + "'");
					}
				}
	      
			};
		}
		
		public class UserDetailWithOrganizationImpl extends org.springframework.security.core.userdetails.User {

		    private static final long serialVersionUID = 3149421282945554897L;
		    private Organization organization;

		    public UserDetailWithOrganizationImpl(String username, String password,
		            Collection<? extends GrantedAuthority> authorities, Organization organization) {
		        super(username, password, authorities);
		        this.organization = organization;

		    }

		    public UserDetailWithOrganizationImpl(String username, String password, boolean enabled,
		            boolean accountNonExpired, boolean credentialsNonExpired,
		            boolean accountNonLocked,
		            Collection<? extends GrantedAuthority> authorities, Organization organization) {
		        super(username, password, enabled, accountNonExpired,
		                credentialsNonExpired, accountNonLocked, authorities);
		        this.organization = organization;
		    }
		    public Organization getOrganization() {
				return organization;
			}
			public void setOrganization(Organization organization) {
				this.organization = organization;
			}
		}
		
		
		@Configuration
		@EnableWebSecurity
		@EnableGlobalMethodSecurity(prePostEnabled=true)
		class WebSecurityConfig extends WebSecurityConfigurerAdapter {
		 
			@Override
			protected void configure(HttpSecurity http) throws Exception {
				//http.authorizeRequests().anyRequest().permitAll();
				
				http.authorizeRequests().anyRequest().fullyAuthenticated().and().
			    httpBasic().and().
			    csrf().disable();
			}
		}
	}
}
