package com.skywalker.task_organizer.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skywalker.task_organizer.dto.TaskDto;
import com.skywalker.task_organizer.entity.User;
import com.skywalker.task_organizer.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class TaskIntegrationTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public void init(){
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
        setupUser();
    }

    private void setupUser(){
        User user = new User();
        user.setUserId("1234");
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setEmail("test@gmail.com");
        user.setPassword("1234567");
        user.setIsdCode("+91");
        user.setPhoneNumber("1234567890");
        user.setDeleted(false);
        user.setCreatedAt(Instant.now());
        user.setCreatedBy("1234");
        user.setUpdatedAt(Instant.now());
        user.setUpdatedBy("1234");
        userRepository.save(user);
    }

    @Test
    @WithUserDetails("test@gmail.com")
    public void createTaskTestWithValidUserId() throws Exception {
        SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date =  inFormat.parse("2024-03-22");
        TaskDto task = new TaskDto();
        task.setTitle("Simple Task");
        task.setDescription("Argent Work");
        task.setDate(date);
        task.setAssignee("1234");
        ObjectMapper mapper = new ObjectMapper();
        String taskDtoJson = mapper.writeValueAsString(task);

        mockMvc.perform(post("/tasks")
                        .content(taskDtoJson)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Simple Task"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Argent Work"))
                .andDo(print());
    }
}
