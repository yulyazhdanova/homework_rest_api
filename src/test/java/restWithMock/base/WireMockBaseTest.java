package restWithMock.base;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class WireMockBaseTest {

    protected static WireMockServer wireMockServer;

    @BeforeAll
    public static void setUpWireMock(){
      wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8089));
      wireMockServer.start();
      System.setProperty("api.base.url","http://localhost:8089");
    }

    @AfterAll
    public static void tearDownWireMock(){
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }
}
