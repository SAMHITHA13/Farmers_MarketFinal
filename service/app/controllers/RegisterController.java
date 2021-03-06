package controllers;


import models.Register;
import models.RegisterRepository;
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
public class RegisterController extends Controller {

    private final RegisterRepository registerRepository;
    private final HttpExecutionContext ec;
    private final FormFactory formFactory;
    EmailUtil emailUtil;

    @Inject
    public RegisterController(FormFactory formFactory,
                              RegisterRepository registerRepository,
                              HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.registerRepository = registerRepository;
        this.ec = ec;
    }

    public CompletionStage<Result> getTags(Long id) {
        return registerRepository.getTags(id).thenApplyAsync(interests -> {
            //System.out.println(toJson(interests));
            return ok(toJson(interests));
        }, ec.current());
    }

    public CompletionStage<Result> getUserId(String email) {
        return registerRepository.getUserId(email).thenApplyAsync(userId -> {
            //System.out.println(toJson(interests));
            return ok(toJson(userId));
        }, ec.current());
    }

    public CompletionStage<Result> addRegister() {
        JsonNode js = request().body().asJson();
        Register register = fromJson(js, Register.class);
        AdminController adminController = new AdminController(ec, emailUtil);
        return registerRepository.add(register).thenApplyAsync(p -> {
            adminController.sendAuthEmail(p.email, p.id);
            return ok("Created");
        }, ec.current());
    }


    public Result sendResetLink() {
        String email = request().body().asJson().get("email").asText();
        String str = registerRepository.sendResetLink(email);
        String exists = "";
        Long id = 0L;
        if(str.equals("Absent"))
            exists = "No";
        else{
            exists = "Yes";
            id = Long.valueOf(str.substring(7));
            AdminController adminController = new AdminController(ec,emailUtil);
            System.out.println(email+":"+id+".");
            adminController.sendResetLink(email,id);
        }
        String msg = "{\"exists\" : \""+exists+"\"}";
        return ok(Json.parse(msg));
    }

    public Result login(){
        JsonNode j = request().body().asJson();
        String email = j.get("email").asText();
        String password = j.get("password").asText();
        Register ps = registerRepository.login(email,password);
        if(ps.email== null || ps.password==null){
            return null;
        }
        else{
            String msg = "{\"role\" : \""+ps.role+"\"," +
                    "\"name\":\""+ps.name+"\"," +
                    "\"status\":\""+ps.status+"\"," +
                    "\"id\":\""+ps.id+"\"}";
            return ok(Json.parse(msg));
        }
    }

    public CompletionStage<Result> getFarmer(Long fid) {
        return registerRepository.getFarmer(fid).thenApplyAsync(farmer -> {
            return ok(toJson(farmer));
        }, ec.current());
    }

    public CompletionStage<Result> viewProfile(Long id) {
        return registerRepository.viewProfile(id).thenApplyAsync(profile -> {
            return ok(toJson(profile));
        }, ec.current());
    }

    public CompletionStage<Result> updateRegister(Long id){
        JsonNode js = request().body().asJson();
        String name = js.get("name").asText();
        String email = js.get("email").asText();
        String mobile = js.get("mobile").asText();
        return registerRepository.update(id, name, email, mobile).thenApplyAsync(p->{
            return ok("Update successful");
        },ec.current());
    }

    public CompletionStage<Result> resetPassword(){
        JsonNode js = request().body().asJson();
        Long id = js.get("id").asLong();
        String password = js.get("password").asText();
        return registerRepository.resetPassword(id, password).thenApplyAsync(p->{
            return ok("Update successful");
        },ec.current());
    }

    public CompletionStage<Result> verifyRegister(Long id){
        return registerRepository.verify(id).thenApplyAsync(p->{
            System.out.println(p);
            String text = "";
            if(p.equals("unauthenticated")) text="Verification successful";
            else text="Already verified";
            String str = "{ \"verify\" : \""+text+"\"}";
            System.out.println(str);
            return ok(Json.parse(str));
        },ec.current());
    }

}