package pl.setblack.pongi.users;

import ratpack.http.TypedData;
import org.junit.jupiter.api.Test;
import pl.setblack.pongi.Server;
import pl.setblack.pongi.users.repo.SessionsRepo;
import pl.setblack.pongi.users.repo.UsersRepositoryInMemory;
import ratpack.test.embed.EmbeddedApp;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by jarek on 2/8/17.
 */
class UsersServiceTest {
    private final Clock clock = Clock.fixed(Instant.parse("2007-12-03T10:15:30.00Z"), ZoneId.of("GMT"));


    @Test
    public void shouldRegisterUser() throws Exception {
       prepareServer().test(
                testHttpClient -> {
                    final Object response = testHttpClient.requestSpec(rs ->
                            rs.headers( mh -> mh.add("Content-type", "application/json"))
                            .body( body -> body.text("{\"password\": \"upa\"}")))
                            .post("/api/users/aa")
                            .getBody().getText();

                    assertEquals("{\"problem\":null,\"ok\":true}", response);
                }
        );
    }

    private EmbeddedApp prepareServer() {
        final UsersService usersService = initService();
        return EmbeddedApp.fromServer(
                Server.createServer(usersService.usersApi())
        );
    }


    private UsersService initService (){

        return new UsersService(new UsersRepositoryInMemory(),
                new SessionsRepo(clock));

    }


}