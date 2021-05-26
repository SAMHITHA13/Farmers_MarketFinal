package models;

import com.google.inject.ImplementedBy;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * This interface provides a non-blocking API for possibly blocking operations.
 */
@ImplementedBy(JPAInterestsRepository.class)
public interface InterestsRepository {

    CompletionStage<Interests> add(Interests interests);
    CompletionStage<String> deleteInterests(Long uid);

}