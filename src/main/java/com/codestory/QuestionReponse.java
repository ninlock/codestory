package com.codestory;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class QuestionReponse {
    
    @Id @GeneratedValue
    private Long id;
    private String question;
    private String reponse;
        
    
    public Long getId() {
        return id;
    }    
    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }    
    public void setQuestion(String question) {
        this.question = question;
    }
    
    public String getReponse() {
        return reponse;
    }    
    public void setReponse(String reponse) {
        this.reponse = reponse;
    }
    
    
}
