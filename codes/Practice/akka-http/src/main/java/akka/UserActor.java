package akka;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.FI;

public class UserActor extends AbstractActor {

    private UserService userService = new UserService();

    static Props props() {
        return Props.create(UserActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(UserMessage.CreateUserMessage.class, handleCreateUser())
                .match(UserMessage.GetUserMessage.class, handleGetUser())
                .build();
    }

    private FI.UnitApply<UserMessage.CreateUserMessage> handleCreateUser() {
        return createUserMessage -> {
            userService.createUser(createUserMessage.getUser);
            sender()
                    .tell(new UserMessage.ActionPerformed(
                            String.format("User %s created.", createUserMessage.getUser)
                    ));
        }
    }
}
