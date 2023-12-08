package com.utc.formut.web.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "journey")
public class Journey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float score;

    private int duration;

    @NotNull
    @OneToOne
    @JoinColumn(name = "intern_id")
    private User user;

    @NotNull
    @OneToOne
    @JoinColumn(name = "form_id")
    private Form form;

    public Journey() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public User getIntern() {
        return user;
    }

    public void setIntern(User intern) {
        this.user = intern;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }
}
