package controllers;


import models.Interests;
import models.InterestsRepository;
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
public class InterestsController extends Controller {

    private final InterestsRepository interestsRepository;
    private final HttpExecutionContext ec;
    private final FormFactory formFactory;
    EmailUtil emailUtil;

    @Inject
    public InterestsController(FormFactory formFactory,
                               InterestsRepository interestsRepository,
                               HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.interestsRepository = interestsRepository;
        this.ec = ec;
    }


    public Result addInterests() {
        JsonNode js = request().body().asJson();
        /*Interests interests=fromJson(js,Interests.class);
        System.out.println(interests.getInterests());*/
        for(JsonNode interest: js.withArray("interests"))
        {
            Interests interests=new Interests();
            interests.setUid(js.get("uid").asLong());
            interests.setInterests(interest.asText());
            interestsRepository.add(interests);
        }
        return ok("created");
       /* Long uid = js.get("uid").asLong();
        System.out.println("userid is -> " + uid);
        for (JsonNode interest : js.withArray("interests")) {
            System.out.println("interest -> " + interest.asText());
        }
        for (JsonNode interest : js.withArray("interests")) {
            //System.out.println("interest -> " + interest.asText());
            //int i = interestsRepository.add(uid, interest.asText());
            interestsRepository.add(interests);
            //System.out.println(i);
        }
        return ok("Created");
        /*Interests interests = fromJson(js, Interests.class);
        return interestsRepository.add(interests).thenApplyAsync(p -> {
            return ok("Created");
        }, ec.current());*/
    }
   /* public CompletionStage<Result> addRegister() {
        JsonNode js = request().body().asJson();
        Register register = fromJson(js, Register.class);
        AdminController adminController = new AdminController(ec, emailUtil);
        return registerRepository.add(register).thenApplyAsync(p -> {
            adminController.sendAuthEmail(p.email, p.id);
            return ok("Created");
        }, ec.current());
    }*/
   public Result updateInterests(){
       JsonNode js = request().body().asJson();
       interestsRepository.deleteInterests(js.get("uid").asLong()).thenApplyAsync(p -> {
           if(p.equals("Successful")) {
               for (JsonNode interest : js.withArray("interests")) {
                   Interests interests = new Interests();
                   interests.setUid(js.get("uid").asLong());
                   interests.setInterests(interest.asText());
                   interestsRepository.add(interests);
               }
           }
           return ok("Successful");
       }, ec.current());
       return ok("Successful");
   }

}
