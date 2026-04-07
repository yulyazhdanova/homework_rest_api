package restWithoutMock.tests.withoutPojo;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import restWithoutMock.specs.Specifications;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JsonPlaceHolderResponseTest {

    // Отправляем GET-запрос /users
    // Используем общую спецификацию запроса (baseUrl + JSON)
    // Ожидаем ответ по спецификации 200 (код + время)
    // Десериализуем ответ в список объектов User

    @Test
    @Order(1)
    public void getUsersTest(){
        Response response =
                given()
                        .spec(Specifications.requestSpecification)
                        .when()
                        .get("/users")
                        .then()
                        .spec(Specifications.responseSpecification200Code)
                        .extract()
                        .response();
        int userCount = response.jsonPath().getList("$").size();
        assertEquals(10,userCount,"Ожидалось 10 пользователей");
        String userEmail = response.jsonPath().getString("[0].email");
        assertNotNull(userEmail,"Email пользователя не должен быть null");
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
        Response response =
                given()
                        .spec(Specifications.requestSpecification)
                        .when()
                        .get("/posts/1")
                        .then()
                        .spec(Specifications.responseSpecification200Code)
                        .extract()
                        .response();
        int id = response.jsonPath().getInt("id");
        assertEquals(1,id,"Идентификатор поста не найден");
        assertNotNull(response, "Пост не существует!!!");
        String title = response.jsonPath().getString("title");
        assertNotNull(title, "Заголовок поста не найден");
    }

    @Test
    @Order(3)
    public void postPostTest(){
        String response1 = "{\"userId\": 1,\"id\": 1,\"title\": \"sut aut facere repellat providet occaecati excepturi optio reprehederit\",\"body\": \"quia et suscipitsuscipit recusadae cosequutur expedita et cumreprehederit molestiae ut ut quas totamostrum rerum est autem sut rem eveiet architecto\"}";
        Response response =
                given()
                        .spec(Specifications.requestSpecification)
                        .body(response1)
                        .when()
                        .post("/posts")
                        .then()
                        .spec(Specifications.responseSpecification201Code)
                        .extract()
                        .response();
        assertNotNull(response, "Пост не создан");
        int userId = response.jsonPath().getInt("userId");
        assertEquals(1,userId);
        String title = response.jsonPath().getString("title");
        assertEquals("sut aut facere repellat providet occaecati excepturi optio reprehederit",title);
        String body = response.jsonPath().getString("body");
        assertEquals("quia et suscipitsuscipit recusadae cosequutur expedita et cumreprehederit molestiae ut ut quas totamostrum rerum est autem sut rem eveiet architecto",body);
    }

    @Test
    @Order(4)
    public void deletePostTest(){
        Response response =
                given()
                        .spec(Specifications.requestSpecification)
                        .when()
                        .delete("/posts/1")
                        .then()
                        .spec(Specifications.responseSpecification200Code)
                        .extract()
                        .response();
        int status = response.getStatusCode();
        assertEquals(200,status);
        String title = response.jsonPath().getString("title");
        assertNull(title);
        String body = response.jsonPath().getString("body");
        assertNull(body);
    }
}
