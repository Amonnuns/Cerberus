package com.amonnuns.gate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "TB_HISTORICAL_GATE")
public class UserHistoricalModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false, length = 70)
    private String userName;
    @Column(nullable = false, length = 20)
    private String actionType;
    private LocalDateTime dateOfInsert;

    public LocalDateTime getDateOfInsert() {
        return dateOfInsert;
    }

    public void setDateOfInsert(LocalDateTime dateOfInsert) {
        this.dateOfInsert = dateOfInsert;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
