package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.Persistence;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.stream.Stream;
import java.lang.Exception;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.*;
import java.util.ArrayList;
import java.io.IOException;
import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Provide JPA operations running inside of a thread pool sized to the connection pool
 */
public class JPAPostsRepository implements PostsRepository {

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAPostsRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<JsonNode> add(Posts posts) {
        System.out.println("Inside add function");
        return supplyAsync(() -> wrap(em -> insert(em, posts)), executionContext);
    }

    /*private Posts insert(EntityManager em, Posts posts) {
        em.persist(posts);
        return posts;
    }*/
    private JsonNode insert(EntityManager em, Posts posts) {
        em.persist(posts);
        ObjectNode json = null;
        System.out.println(posts.id);
        Long postId = posts.id;
        //Long userId = (Long)em.createQuery("select r.id from Register r where r.email=:email").setParameter("email", email).getSingleResult();
        try {
            json = (ObjectNode) new ObjectMapper().readTree("{ \"postId\" : \"" + postId + "\"}");
            System.out.println(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public CompletionStage<Stream<Posts>> listposts(Long uid, String role) {
        return supplyAsync(() -> wrap(em -> listposts(em, uid, role)), executionContext);
    }
    private Stream<Posts> listposts(EntityManager em,Long uid, String role) {
        List<Posts> posts = em.createQuery("select p from Posts p where p.uid=:uid order by p.id desc", Posts.class).setParameter("uid", uid).getResultList();
        //System.out.println(posts.stream());
        return posts.stream();
    }



    @Override
    public CompletionStage<Stream<Posts>> listallposts(Long uid) {
        return supplyAsync(() -> wrap(em -> listallposts(em, uid)), executionContext);
    }
    private Stream<Posts> listallposts(EntityManager em,Long uid)
    {
        System.out.println(uid);
        System.out.println("posts");
        List<Long> id=em.createQuery("select distinct t.postId from Tags t,Interests i where i.uid=:uid and i.interests like t.tags and not t.uid=:uid")
                .setParameter("uid",uid)
                .getResultList();
        for(int i=0;i<id.size();i++)
        {
            System.out.println(id.get(i));
        }
        Collections.sort(id,Collections.reverseOrder());
        List<Posts> posts=new ArrayList<Posts>();
        for(int i=0;i<id.size();i++) {
            Long id1=id.get(i);
            Posts p1 = em.createQuery("select p from Posts p where p.id =:id1 order by p.id desc", Posts.class).setParameter("id1",id1).getSingleResult();
            posts.add(p1);
        }
        return posts.stream();
    }
    /*private Stream<Posts> listfewposts(EntityManager em, Long uid, String tags) {
        List<Posts> posts = new ArrayList<Posts>();
        List<String> tagsList = new ArrayList<String>();
        String[] tagsArray = tags.split(",");
        for(String str:tagsArray) {
            tagsList.add(str);
        }
        //System.out.println(tagsList);
        List<Posts> postsFromPosts = em.createQuery("select p from Posts p", Posts.class).getResultList();
        System.out.println(postsFromPosts);
        return postsFromPosts.stream();
    }*/

    @Override
    public CompletionStage<Stream<Tags>> getTags(Long id) {
        return supplyAsync(() -> wrap(em -> getTags(em, id)), executionContext);
    }

    private Stream<Tags> getTags(EntityManager em, Long id) {
        List<Tags> tags = em.createQuery("select r from Tags r where r.postId=:id", Tags.class).setParameter("id", id).getResultList();
        return tags.stream();
    }

    @Override
    public CompletionStage<JsonNode> getNames(Long id) {
        return supplyAsync(() -> wrap(em -> getNames(em, id)), executionContext);
    }

    private JsonNode getNames(EntityManager em, Long id) {
        //String tags = (String) em.createQuery("select r.name from Register r where r.id=:id", Register.class).setParameter("id", id).getSingleResult();
        ObjectNode json = null;
        String name = (String)em.createQuery("select r.name from Register r where r.id=:id").setParameter("id", id).getSingleResult();
        try {
            json = (ObjectNode) new ObjectMapper().readTree("{ \"name\" : \"" + name + "\"}");
            System.out.println(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }


    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }
}
