package restWithMock.tests.wireMock;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import restWithMock.base.WireMockBaseTest;
import restWithMock.models.Post;
import restWithMock.models.User;
import restWithMock.specs.Specifications;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class

JsonPlaceHolderWireMockTest extends WireMockBaseTest {

    @Test
    @Order(1)
    public void getUsersTest() {
        wireMockServer
                .stubFor(WireMock
                        .get(urlEqualTo("/users"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("""
                                        [
                                          {
                                            "id": 1,
                                            "name": "Leanne Graham",
                                            "username": "Bret",
                                            "email": "leanne@example.com",
                                            "address": {
                                              "street": "Kulas Light",
                                              "suite": "Apt. 556",
                                              "city": "Gwenborough",
                                              "zipcode": "92998-3874",
                                              "geo": {
                                                "lat": "-37.3159",
                                                "lng": "81.1496"
                                              }
                                            },
                                            "phone": "1-770-736-8031",
                                            "website": "hildegard.org",
                                            "company": {
                                              "name": "Romaguera-Crona",
                                              "catchPhrase": "Multi-layered client-server neural-net",
                                              "bs": "harness real-time e-markets"
                                            }
                                          }
                                        ]
                                        """)));
        List<User> users =
                given()
                        .spec(Specifications.requestSpecification)
                        .when()
                        .get("/users")
                        .then()
                        .spec(Specifications.responseSpecification200Code)
                        .extract()
                        .jsonPath()
                        .getList("", User.class);
        assertEquals(users.size(),1);
        Assertions.assertNotNull(users.get(0).email,"Email пользователя не должен быть null");
    }
    @Test
    @Order(2)
    public void getPostTest() {
        wireMockServer
                .stubFor(WireMock
                        .get(urlEqualTo("/posts/1"))
                        .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                
                                {
                                	"userId": 1,
                                	"id": 1,
                                	"title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
                                	"body": "quia et suscipit suscipit recusandae consequuntur expedita et cum reprehenderit molestiae ut ut quas totam nostrum rerum est autem sunt rem eveniet architecto"
                                }
                                
                                """)));
        Post post =
                given()
                        .spec(Specifications.requestSpecification)
                        .when()
                        .get("/posts/1")
                        .then()
                        .spec(Specifications.responseSpecification200Code)
                        .extract()
                        .jsonPath()
                        .getObject("",Post.class);
        assertEquals(post.id,1);
        Assertions.assertNotNull(post);
        Assertions.assertNotNull(post.title);
    }

    @Test
    @Order(3)
    public void postPostTest(){
        wireMockServer
                .stubFor(WireMock
                        .post(urlEqualTo("/posts"))
                        .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                
                                {
                                	"userId": 1,
                                	"id": 1,
                                	"title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
                                	"body": "quia et suscipit suscipit recusandae consequuntur expedita et cum reprehenderit molestiae ut ut quas totam nostrum rerum est autem sunt rem eveniet architecto"
                                }
                                
                                """)));
        Post post = new Post();
        post.id = 1;
        post.title = "Title";
        post.body = "Body";
        Post postResponse =
                given()
                        .spec(Specifications.requestSpecification)
                        .body(post)
                        .when()
                        .post("/posts")
                        .then()
                        .spec(Specifications.responseSpecification201Code)
                        .extract()
                        .as(Post.class);
        Assertions.assertNotNull(postResponse);
        assertEquals(postResponse.id,1);
        assertEquals("sunt aut facere repellat provident occaecati excepturi optio reprehenderit",postResponse.title);
        assertEquals(postResponse.body,"quia et suscipit suscipit recusandae consequuntur expedita et cum reprehenderit molestiae ut ut quas totam nostrum rerum est autem sunt rem eveniet architecto");

    }

    @Test
    @Order(4)
    public void deletePostTest(){
        wireMockServer
                .stubFor(WireMock
                        .delete(urlEqualTo("/posts/1"))
                        .willReturn(aResponse()
                        .withStatus(200)));

                given()
                        .spec(Specifications.requestSpecification)
                        .when()
                        .delete("/posts/1")
                        .then()
                        .spec(Specifications.responseSpecification200Code);
    }
}
