package com.skywalker.task_organizer.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skywalker.task_organizer.dto.UserDetailsCacheData;
import com.skywalker.task_organizer.entity.User;
import com.skywalker.task_organizer.cache.UserDetailsCacheDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserCacheImpl implements UserCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCacheImpl.class);

    private final UserDetailsCacheDataRepository userDetailsCacheDataRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserCacheImpl(UserDetailsCacheDataRepository userDetailsCacheDataRepository,
                         ObjectMapper objectMapper){
        this.userDetailsCacheDataRepository = userDetailsCacheDataRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public UserDetails getUserFromCache(String username){

        Optional<UserDetailsCacheData> optionalCacheData = userDetailsCacheDataRepository.findById(username);

        if (optionalCacheData.isPresent()) {
            String userDetailsAsString = optionalCacheData.get().getValue();

            TypeReference<User> mapType = new TypeReference<User>() {};
            UserDetails userDetails = null;
            try {
                userDetails = objectMapper.readValue(userDetailsAsString, mapType);
            } catch (JsonProcessingException e) {
                LOGGER.error("Serialization Error {}", e);
                throw new RuntimeException(e);
            }
            return userDetails;
        }else{
            return null;
        }
    }

    @Override
    public void putUserInCache(UserDetails user) {
        String username = user.getUsername();
        String userDetailsAsString = null;
        try{
            userDetailsAsString = objectMapper.writeValueAsString(user);
        }catch(JsonProcessingException e){
            LOGGER.error("Serialization Error {}", e);
            throw new RuntimeException(e);
        }catch(Exception e){
            LOGGER.error("Unexpected Error", e);
        }
        UserDetailsCacheData cacheData = new UserDetailsCacheData(username, userDetailsAsString);
        userDetailsCacheDataRepository.save(cacheData);
    }

    @Override
    public void removeUserFromCache(String username) {
        userDetailsCacheDataRepository.deleteById(username);
    }
}
