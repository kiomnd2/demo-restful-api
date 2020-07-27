package com.kiomnd2.demorestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiomnd2.demorestapi.events.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("RESTAPI Deveopmemnt")
                .beginEnrollmentDateTime(LocalDateTime.of(2020,7,27,11,19))
                .closeEnrollmentDateTime(LocalDateTime.of(2020,7,28,11,19))
                .beginEventDateTime(LocalDateTime.of(2020, 7, 29, 14,21 ))
                .endEventDateTime(LocalDateTime.of(2020, 7, 30 ,14, 21))
                .basePrice(100)
                .location("강남역")
                .maxPrice(200)
                .limitOfEnrollment(100)
                .build();


        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsBytes(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.LOCATION))
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true )));
    }


    @Test
    @TestDescription("입력받을 수 없는 값을 사용한 경우이 에러가 발생하는 테스트")
    public void createEvent_badRequest() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("RESTAPI Deveopmemnt")
                .beginEnrollmentDateTime(LocalDateTime.of(2020,7,27,11,19))
                .closeEnrollmentDateTime(LocalDateTime.of(2020,7,28,11,19))
                .beginEventDateTime(LocalDateTime.of(2020, 7, 29, 14,21 ))
                .endEventDateTime(LocalDateTime.of(2020, 7, 30 ,14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .eventStatus(EventStatus.DRAFT)
                .build();


        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsBytes(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }



    @Test
    @TestDescription("입력값이 비어있는 경우 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsBytes(eventDto)))
                .andExpect(status().isBadRequest());

    }



    @Test
    @TestDescription("잘못된 값이 들어갔을 때 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("RESTAPI Deveopmemnt")
                .beginEnrollmentDateTime(LocalDateTime.of(2020,7,27,11,19))
                .closeEnrollmentDateTime(LocalDateTime.of(2020,7,28,11,19))
                .beginEventDateTime(LocalDateTime.of(2020, 7, 29, 14,21 ))
                .endEventDateTime(LocalDateTime.of(2020, 7, 30 ,14, 21))
                .basePrice(101110)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();


        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsBytes(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }



}