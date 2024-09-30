package com.skywalker.task_organizer;

import com.skywalker.task_organizer.cache.UserDetailsCacheDataRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories(basePackages = {"com.skywalker.task_organizer.cache"})
@EnableJpaRepositories(basePackages = {"com.skywalker.task_organizer.repository"})
public class TaskOrganizerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskOrganizerApplication.class, args);
	}

}
