package restWithoutMock.tests.withPojo;

import restWithoutMock.models.Post;
import restWithoutMock.models.User;
import org.junit.jupiter.api.*;
import restWithoutMock.specs.Specifications;

import java.util.List;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JsonPlaceHolderTest {

    // Отправляем GET-запрос /users
    // Используем общую спецификацию запроса (baseUrl + JSON)
    // Ожидаем ответ по спецификации 200 (код + время)
    // Десериализуем ответ в список объектов User

    @Test
    @Order(1)
    public void getUsersTest(){
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
        assertEquals(users.size(),10);
        assertNotNull(users.get(0).email,"Email пользователя не должен быть null");
    }

    // Отправляем GET-запрос на получение конкретного поста
    // /posts/1 — фиксированный ресурс
    // Ответ сразу маппим в POJO Post
    // ПРОВЕРКА 2:
    // Проверяем, что у поста есть заголовок
    // Это проверка обязательного поля по контракту API

    @Test
    @Order(2)
    public void getPostTest(){
        Post post1 =
                given()
                        .spec(Specifications.requestSpecification)
                        .when()
                        .get("/posts/1")
                        .then()
                        .spec(Specifications.responseSpecification200Code)
                        .extract()
                        .jsonPath()
                        .getObject("", Post.class);
        assertEquals(post1.id,1);
        assertNotNull(post1);
        assertNotNull(post1.title);
    }

    @Test
    @Order(3)
    public void postPostTest(){
                Post post = new Post();
                post.userId = 1;
                post.title = "Test Title";
                post.body = "Test Body";
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
                assertNotNull(postResponse);
                assertEquals(postResponse.userId,1);
                assertEquals(postResponse.title,"Test Title");
                assertEquals(postResponse.body,"Test Body");
    }

    // Отправляем DELETE-запрос
    // На JSONPlaceholder данные не удаляются реально,
    // но сервер обязан вернуть корректный статус

    @Test
    @Order(4)
    public void deletePostTest(){
        Post post1 =
                given()
                        .spec(Specifications.requestSpecification)
                        .when()
                        .delete("/posts/1")
                        .then()
                        .spec(Specifications.responseSpecification200Code)
                        .extract()
                        .jsonPath()
                        .getObject("", Post.class);
        assertNull(post1.title);
        assertNull(post1.body);
        assertEquals(post1.id,0);
        assertEquals(post1.userId,0);
    }
}
