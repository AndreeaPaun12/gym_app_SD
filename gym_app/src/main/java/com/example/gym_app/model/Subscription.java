package com.example.gym_app.model;

import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "subscriptions")
public class Subscription extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private User client;

    @Column(nullable = false)
    private LocalDate startDate;

    private boolean isAvailable;

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
