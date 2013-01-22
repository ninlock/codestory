package com.codestory;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class CodeStoryServiceTest {

    @Deployment
    public static Archive<?> createTestArchive() {
       return ShrinkWrap.create(WebArchive.class, "test.war")
             .addClasses(QuestionReponse.class, QuestionReponseEJB.class, QuestionReponseEJBBean.class, CodeStoryService.class)
             .addAsResource("META-INF/persistence.xml", "META-INF/codestory-test-ds.xml")
             .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    QuestionReponseEJB questionReponseEJB;
    
    @PersistenceContext
    EntityManager em;


    @Test
    public void testRegister() throws Exception {
       QuestionReponse qr = new QuestionReponse();
       qr.setQuestion("test question");
       qr.setReponse("test reponse");
       questionReponseEJB.saveQuestionReponse(qr);
       assertNotNull(qr.getId());
    }
    
}
