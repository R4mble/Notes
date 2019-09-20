package akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.HttpApp;
import akka.http.javadsl.server.Route;
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import scala.concurrent.duration.Duration;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import akka.UserMessage.GetUserMessage;


public class UserServer extends HttpApp {

    private final ActorRef userActor;
    Timeout timeout = new Timeout(Duration.create(5, TimeUnit.SECONDS));

    public UserServer(ActorRef userActor) {
        this.userActor = userActor;
    }

    @Override
    protected Route routes() {
        return path("users", this::postUser);
    }

    private Route getUser(Long id) {
        return get(() -> {
            CompletionStage<Optional<User>> user = PatternsCS.ask(userActor, new GetUserMessage(id), timeout)
                    .thenApply(obj -> (Optional<User>) obj);

            return onSuccess(() -> user, performed -> {
                if (performed.isPresent()) {
                    return complete((StatusCodes.OK), performed.get(), Jackson.marshaller());
                } else {
                    return complete(StatusCodes.NOT_FOUND);
                }
            });
        });
    }

    private Route postUser() {
        return route(post(() -> {
            return entity(Jackson.unmarshaller(User.class), user -> {
                CompletionStage<UserMessage.ActionPerformed> userCreated =
                        PatternsCS.ask(userActor, new UserMessage.CreateUserMessage(user), timeout)
                                .thenApply(obj -> (UserMessage.ActionPerformed) obj);

                return onSuccess(() -> userCreated, actionPerformed -> {
                    return complete(StatusCodes.CREATED, actionPerformed, Jackson.marshaller());
                });
            });
        }));
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ActorSystem system = ActorSystem.create("userServer");
        ActorRef userActor = system.actorOf(UserActor.props(), "userActor");
        UserServer server = new UserServer(userActor);
        server.startServer("localhost", 8080, system);
    }
}
