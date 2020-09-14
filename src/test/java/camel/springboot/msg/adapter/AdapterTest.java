package camel.springboot.msg.adapter;

import camel.springboot.msg.adapter.data.Coordinates;
import camel.springboot.msg.adapter.data.MsgA;
import camel.springboot.msg.adapter.data.MsgB;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.camel.test.spring.junit5.CamelTestContextBootstrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@BootstrapWith(CamelTestContextBootstrapper.class)
//@ContextConfiguration
public final class AdapterTest {

  RestTemplate restTemplate = new RestTemplate();

  @Test
  public void testingCamel() throws URISyntaxException {
//    template.sendBody("direct:start",
//        new MsgA(null, "en", new Coordinates(12.34, 56.78))
//    );
//    template.sendBody("direct:start",
//        new MsgA(null, "ru", new Coordinates(12.34, 56.78))
//    );
//    template.sendBody("direct:start",
//        new MsgA("", "RU", new Coordinates(12.34, 56.78))
//    );

    URI uri = new URI("http://localhost/adapter");
    ResponseEntity<String> response = restTemplate.exchange(uri,HttpMethod.POST,
        new HttpEntity<>(new MsgA("current weather", "ru",
            new Coordinates(54.35, 52.52))),
        String.class);

    System.out.println(response.getBody());

//    for (int i = 1; i <= 9; i++) {
//      template.sendBody("direct:start",
//          new MsgA("current weather " + i, "ru",
//              new Coordinates(round(random() * 90), round(random() * 90)))
//      );
//    }
  }

}
