package com.kiomnd2.demorestapi.events;


import com.kiomnd2.demorestapi.accounts.Account;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
@Entity
public class Event {

    @Id @GeneratedValue
    private Integer id;

    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모험
    private int basePrice;
    private int maxPrice;
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;
    @ManyToOne
    private Account manager;

    public void update() {
        if (this.basePrice == 0 && this.maxPrice ==0) {
            this.free = true;
        } else {
            this.free = false;
        }

        //Update Offline
        if (this.location == null ||this.location.isBlank()) {
            this.offline = false;
        } else {
            this.offline = true;
        }
    }
}
