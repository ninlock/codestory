package com.codestory;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class QuestionReponseEJBBean implements QuestionReponseEJB {

    @PersistenceContext(name="codestory")
    EntityManager em;

    public List<QuestionReponse> findAllQuestionReponse(){
        Query query = em.createQuery("select qr from QuestionReponse qr");
        return query.getResultList();
    }

    public QuestionReponse findQuestionReponseByQuestion(String question){
        Query query = em.createQuery("select qr from QuestionReponse qr where question='" + question + "'");
        QuestionReponse qr = null;
        try {
            qr = (QuestionReponse)query.getSingleResult();
        } catch (Exception e) {}
        return qr;
    }

    public void saveQuestionReponse(QuestionReponse questionReponse) {
        em.persist(questionReponse);
    }
    
    public void updateQuestionReponse(QuestionReponse questionReponse) {
        em.merge(questionReponse);
    }
    
    public void deleteByQuestion(String question) {
        QuestionReponse questionReponse = findQuestionReponseByQuestion(question);        
        em.remove(questionReponse);
    }
}
