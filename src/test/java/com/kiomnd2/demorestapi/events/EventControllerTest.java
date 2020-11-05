package com.kiomnd2.demorestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiomnd2.demorestapi.common.RestCDocsConfiguration;
import com.kiomnd2.demorestapi.events.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(RestCDocsConfiguration.class)
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
                .maxPrice(200)
                .location("강남역")
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
                .andExpect(jsonPath("free").value(false ))
                .andExpect(jsonPath("offline").value(true ))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-events").exists())
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-events").description("link to update an existing event"),
                                linkWithRel("profile").description("profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("Description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("BeginEnrollmentDateTime of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("CloseEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("BeginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
//                        relaxedResponseFields( // 응답에 일부분만 문서화 해도 오류가 생기지 않음
                        responseFields( // 응답에 일부분만 문서화 해도 오류가 생기지 않음
                                fieldWithPath("id").description("identifier of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("Description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("BeginEnrollmentDateTime of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("CloseEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("BeginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event"),
                                fieldWithPath("free").description("It tell if this event is free"),
                                fieldWithPath("offline").description("It tell if this event is offline"),
                                fieldWithPath("eventStatus").description("Event Status"),
                                fieldWithPath("_links.self.href").description("Link to self"),
                                fieldWithPath("_links.query-events.href").description("Link to query event"),
                                fieldWithPath("_links.update-events.href").description("Link to update event"),
                                fieldWithPath("_links.profile.href").description("Link to profile")
                        )
                ));
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists());
    }







}
