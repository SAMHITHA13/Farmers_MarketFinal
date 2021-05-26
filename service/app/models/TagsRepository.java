package models;
import com.google.inject.ImplementedBy;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * This interface provides a non-blocking API for possibly blocking operations.
 */
@ImplementedBy(JPATagsRepository.class)
public interface TagsRepository {

    CompletionStage<Tags> add(Tags tags);


}