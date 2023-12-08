package com.utc.formut.web.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String wording;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "form_id")
    private Form form;

    private String status;

    @OneToOne
    @JoinColumn(name = "good_answer_id")
    private Answer good_answer;

    public Question() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWording() {
        return wording;
    }

    public void setWording(String wording) {
        this.wording = wording;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Answer getGood_answer() {
        return good_answer;
    }

    public void setGood_answer(Answer good_answer) {
        this.good_answer = good_answer;
    }
}
