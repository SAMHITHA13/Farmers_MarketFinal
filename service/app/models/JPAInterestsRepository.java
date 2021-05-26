package models;

import play.db.jpa.JPAApi;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.Persistence;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.List;
import java.io.IOException;
import java.util.stream.Stream;
import java.lang.Exception;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import javax.transaction.Transactional;




import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Provide JPA operations running inside of a thread pool sized to the connection pool
 */


public class JPAInterestsRepository implements InterestsRepository {

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @PersistenceContext
    private EntityManager entityManager;


    @Inject
    public JPAInterestsRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }
    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }
    @Override
    public CompletionStage<Interests> add(Interests interests) {
        System.out.println("Inside add function");
        return supplyAsync(() -> wrap(em -> insert(em, interests)), executionContext);
    }
    private Interests insert(EntityManager em, Interests interests) {
        em.persist(interests);
        return interests;
    }

    @Override
    public CompletionStage<String> deleteInterests(Long uid) {
        System.out.println("In override method");
        return supplyAsync(() -> wrap(em -> deleteInterests(em, uid)), executionContext);
    }

    private String deleteInterests(EntityManager em, Long uid) {
        int i = em.createQuery("delete from Interests i where i.uid=:uid").setParameter("uid",uid).executeUpdate();
        if(i!=0) {
            return "Successful";
        } else {
            return "Not Successful";
        }
    }


    /*@Transactional
    public int add(Long uid, String interest) {
        System.out.println(uid);
        System.out.println(interest);
        int i = entityManager.createQuery("INSERT INTO Interests (id, interests, uid) VALUES (?,?,?)")
                .setParameter(1, 1)
                .setParameter(2, interest)
                .setParameter(3,uid)
                .executeUpdate();
        return i;
    }*/




}