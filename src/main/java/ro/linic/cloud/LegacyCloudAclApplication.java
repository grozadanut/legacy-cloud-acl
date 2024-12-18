package ro.linic.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import ro.linic.cloud.entity.Company;
import ro.linic.cloud.entity.Product;

@SpringBootApplication
public class LegacyCloudAclApplication implements RepositoryRestConfigurer{

	public static void main(final String[] args) {
		SpringApplication.run(LegacyCloudAclApplication.class, args);
	}

	@Override
    public void configureRepositoryRestConfiguration(final RepositoryRestConfiguration config, final CorsRegistry cors) {
		config.exposeIdsFor(Company.class);
        config.exposeIdsFor(Product.class);
    }
	
	@Bean
	public RestTemplate restTemplate(final RestTemplateBuilder builder) {
	    return builder.build();
	}
}
