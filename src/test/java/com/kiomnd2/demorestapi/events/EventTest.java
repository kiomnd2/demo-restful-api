package com.kiomnd2.demorestapi.events;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class EventTest {


    @Test
    public void builder()throws Exception{
        Event event = Event.builder()
                .name("TestAPI")
                .description("REST API development")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {
        Event event = new Event();
        String name = "Event";
        String event_desc = "event Desc";

        event.setDescription(event_desc);
        event.setName(name);

        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(event_desc);

    }

}