package io.blog.devlog;

import io.blog.devlog.global.utils.StartupConfigLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DevlogApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplicationBuilder()
				.sources(DevlogApplication.class)
				.listeners(new ApplicationPidFileWriter("./server.pid"))
				.build();
		application.addListeners(new StartupConfigLogger());
		application.run(args);
//		SpringApplication.run(DevlogApplication.class, args);
//		ConfigurableApplicationContext context = app.run(args);
//		Environment environment = context.getEnvironment();
	}
}
