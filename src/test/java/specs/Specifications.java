package specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static org.hamcrest.Matchers.lessThan;

public class Specifications {

    public static RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBaseUri("https://jsonplaceholder.typicode.com")
            .setContentType(ContentType.JSON)
            .build();

    public static ResponseSpecification responseSpecification200Code = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectResponseTime(lessThan(5000L))
            .build();

    public static ResponseSpecification responseSpecification201Code = new ResponseSpecBuilder()
            .expectStatusCode(201)
            .expectResponseTime(lessThan(5000L))
            .build();
}
