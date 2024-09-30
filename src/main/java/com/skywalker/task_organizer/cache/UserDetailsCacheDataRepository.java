package com.skywalker.task_organizer.cache;

import com.skywalker.task_organizer.dto.UserDetailsCacheData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsCacheDataRepository extends CrudRepository<UserDetailsCacheData, String> {
}
