package io.blog.devlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class DevlogApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplicationBuilder()
				.sources(DevlogApplication.class)
				.listeners(new ApplicationPidFileWriter("./server.pid"))
				.build();
		application.run(args);
//		SpringApplication.run(DevlogApplication.class, args);
//		ConfigurableApplicationContext context = app.run(args);
//		Environment environment = context.getEnvironment();
	}
}
