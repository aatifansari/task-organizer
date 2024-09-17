package com.skywalker.task_organizer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@AllArgsConstructor
@Getter
@Accessors(chain = true)
@RedisHash("UserDetailsCacheData")
public class UserDetailsCacheData {
    @Id
    String key;

    @Indexed
    String value;
}
