package controllers;

import models.*;
import models.Tags;
import models.TagsRepository;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import com.fasterxml.jackson.databind.JsonNode;
import utils.EmailUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
public class TagsController extends Controller {

    private final TagsRepository tagsRepository;
    private final HttpExecutionContext ec;
    private final FormFactory formFactory;
    EmailUtil emailUtil;
    Long postId;
    @Inject
    public TagsController(FormFactory formFactory,
                               TagsRepository tagsRepository,
                               HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.tagsRepository = tagsRepository;
        this.ec = ec;
    }
    public Result sendID(Long i)
    {
        System.out.println(i);
        postId=i;
        System.out.println(postId);
        return ok("success");
    }
    public Result addTags() {
        JsonNode js = request().body().asJson();
        for (JsonNode tag : js.withArray("tags")) {
            Tags tags = new Tags();
            tags.setUid(js.get("uid").asLong());
            tags.setTags(tag.asText());
            tags.setPostId(js.get("postId").asLong());
            System.out.println(tags.getTags());
            tagsRepository.add(tags);
        }
        return ok("created");
    }
}