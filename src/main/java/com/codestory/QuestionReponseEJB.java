package com.codestory;

import java.util.List;

import javax.ejb.Local;

@Local
public interface QuestionReponseEJB {
    
   List<QuestionReponse> findAllQuestionReponse();
   QuestionReponse findQuestionReponseByQuestion(String question);
   void saveQuestionReponse(QuestionReponse questionReponse);
   void updateQuestionReponse(QuestionReponse questionReponse);
   void deleteByQuestion(String question);
}
