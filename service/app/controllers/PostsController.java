package controllers;


import models.Register;
import models.RegisterRepository;
import models.Posts;
import models.PostsRepository;
import models.*;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import com.fasterxml.jackson.databind.JsonNode;
import utils.EmailUtil;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;

/**
 * The controller keeps all database operations behind the repository, and uses
 * {@link HttpExecutionContext} to provide access to the
 * {@link play.mvc.Http.Context} methods like {@code request()} and {@code flash()}.
 */
public class PostsController extends Controller {

    private final PostsRepository postsRepository;
    private final HttpExecutionContext ec;
    private final FormFactory formFactory;
    private final RegisterRepository registerRepository;
    private final RegisterController registerController;
    private final InterestsRepository interestsRepository;
    private final InterestsController interestsController;
    private final TagsRepository tagsRepository;
    private final TagsController tagsController;
    EmailUtil emailUtil;

    @Inject
    public PostsController(FormFactory formFactory,
                           PostsRepository postsRepository,
                           RegisterRepository registerRepository,
                           RegisterController registerController,
                           TagsController tagsController,
                           TagsRepository tagsRepository,
                           InterestsController interestsController,
                           InterestsRepository interestsRepository,
                           HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.postsRepository = postsRepository;
        this.tagsController=tagsController;
        this.tagsRepository=tagsRepository;
        this.registerController=registerController;
        this.registerRepository=registerRepository;
        this.interestsController=interestsController;
        this.interestsRepository=interestsRepository;
        this.ec = ec;
    }

    public CompletionStage<Result> createPost() {
        JsonNode js = request().body().asJson();
        Posts posts = fromJson(js, Posts.class);
        return postsRepository.add(posts).thenApplyAsync(p -> {
            //System.out.println("id is"+p.id);
            //tagsController.sendID(p.id);
            return ok(toJson(p));
        }, ec.current());
    }


    public CompletionStage<Result> viewPosts(Long uid, String role) {
        return postsRepository.listposts(uid, role).thenApplyAsync(postsStream -> {
            //System.out.println(toJson(postsStream.collect(Collectors.toList())));
            return ok(toJson(postsStream.collect(Collectors.toList())));
        }, ec.current());
    }

    public CompletionStage<Result> newsfeed(Long uid) {
        return postsRepository.listallposts(uid).thenApplyAsync(postsStream -> {
            //System.out.println(toJson(postsStream.collect(Collectors.toList())));
            return ok(toJson(postsStream.collect(Collectors.toList())));
        }, ec.current());
    }

    public CompletionStage<Result> getTags(Long id) {
        return postsRepository.getTags(id).thenApplyAsync(tagsStream -> {
            //System.out.println(toJson(interests));
            return ok(toJson(tagsStream.collect(Collectors.toList())));
        }, ec.current());
    }

    public CompletionStage<Result> getNames(Long id) {
        return postsRepository.getNames(id).thenApplyAsync(name -> {
            //System.out.println(toJson(interests));
            return ok(toJson(name));
        }, ec.current());
    }

}