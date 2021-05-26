package models;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;
import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * This interface provides a non-blocking API for possibly blocking operations.
 */
@ImplementedBy(JPAPostsRepository.class)
public interface PostsRepository {


    //CompletionStage<Posts> add(Posts posts);
    CompletionStage<JsonNode> add(Posts posts);

    CompletionStage<Stream<Posts>> listposts(Long uid, String role);

    CompletionStage<Stream<Posts>> listallposts(Long uid);

    CompletionStage<Stream<Tags>> getTags(Long id);

    CompletionStage<JsonNode> getNames(Long id);

}