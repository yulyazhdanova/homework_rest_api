package restWithoutMock.tests.hamcrest;

import restWithoutMock.models.Post;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import restWithoutMock.specs.Specifications;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JsonPlaceHolderHamcrestTest {

    // Отправляем GET-запрос /users
    // Используем общую спецификацию запроса (baseUrl + JSON)
    // Ожидаем ответ по спецификации 200 (код + время)
    // Десериализуем ответ в список объектов User

    @Test
    @Order(1)
    public void getUsersTest(){
                given()
                        .spec(Specifications.requestSpecification)
                        .when()
                        .get("/users")
                        .then()
                        .spec(Specifications.responseSpecification200Code)
                        .body("$",hasSize(10))
                        .body("[0].email",instanceOf(String.class));
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
                given()
                        .spec(Specifications.requestSpecification)
                        .when()
                        .get("/posts/1")
                        .then()
                        .spec(Specifications.responseSpecification200Code)
                        .body("id",instanceOf(Integer.class))
                        .body("id",equalTo(1))
                        .body("title",instanceOf(String.class))
                        .body("title",notNullValue())
                        .body("$",notNullValue());
    }

    @Test
    @Order(3)
    public void postPostTest(){
                Post post = new Post();
                post.userId = 1;
                post.title = "Test Title";
                post.body = "Test Body";
                        given()
                                .spec(Specifications.requestSpecification)
                                .body(post)
                                .when()
                                .post("/posts")
                                .then()
                                .spec(Specifications.responseSpecification201Code)
                                .body("$",notNullValue())
                                .body("userId",equalTo(1))
                                .body("userId",instanceOf(Integer.class))
                                .body("title",equalTo("Test Title"))
                                .body("title",instanceOf(String.class))
                                .body("body",equalTo("Test Body"))
                                .body("body",instanceOf(String.class));
    }

    // Отправляем DELETE-запрос
    // На JSONPlaceholder данные не удаляются реально,
    // но сервер обязан вернуть корректный статус

    @Test
    @Order(4)
    public void deletePostTest(){
                given()
                        .spec(Specifications.requestSpecification)
                        .when()
                        .delete("/posts/1")
                        .then()
                        .spec(Specifications.responseSpecification200Code)
                        .body("title",nullValue())
                        .body("body",nullValue())
                        .body("id",nullValue())
                        .body("userId",nullValue());
    }
}
