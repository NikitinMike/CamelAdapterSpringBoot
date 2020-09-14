package camel.springboot.msg.adapter;

import camel.springboot.msg.adapter.data.Coordinates;
import camel.springboot.msg.adapter.data.MsgA;
import camel.springboot.msg.adapter.data.MsgB;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;


@Component
public class AdapterRouter extends RouteBuilder {

  @Override
  public void configure() {

    restConfiguration()
        .component("jetty")
        .bindingMode(RestBindingMode.json)
//        .host("localhost")
        .port(8080)
        .apiProperty("cors", "true");

    from("direct:getmsg")
        .process(exchange -> {
          String msg = exchange.getIn().getBody(MsgA.class).getMsg();
          if (msg == null || msg.equals("")) {
            exchange.getIn().setBody(null);
          }
        });

    from("direct:getlng")
        .process(exchange -> {
          String lng = exchange.getIn().getBody(MsgA.class).getLng();
          if (!lng.matches("ru|RU")) {
            exchange.getIn().setBody(null);
          }
        });

    from("jetty:http://0.0.0.0/adapter")
        .unmarshal().json(JsonLibrary.Jackson, MsgA.class)
        .log("ADAPTER< ${body}")
        .to("direct:getlng").filter(body().isNotNull())
        .to("direct:getmsg")
        .choice()
        .when(body().isNull())
        .to("direct:error")
        .otherwise()
        .to("direct:message")
        .end()
        .marshal().json(JsonLibrary.Jackson, String.class)
        .log("<${body}>");

    from("direct:error")
        .setBody(simple("Invalid input message"))
        .log("Error: ${body}");

    from("direct:message")
        .process(exchange -> {
          Coordinates coordinates = exchange.getIn().getBody(MsgA.class).getCoordinates();
          exchange.getIn().setHeader("coordinates", String.format("lat=%s&lon=%s",
              coordinates.getLatitude(), coordinates.getLongitude()));
          exchange.getIn().setHeader("msg", exchange.getIn().getBody(MsgA.class).getMsg());
        })
//            .log("COORDINATES> ${header.coordinates}")
        .to("direct:weather")
//            .log("TEMPERATURE> ${header.currentTemp}")
        .process(exchange -> {
          Integer currentTemp = exchange.getIn().getHeader("currentTemp", Integer.class);
          String msg = exchange.getIn().getHeader("msg", String.class);
          exchange.getIn().setBody(
              new MsgB(msg, new Date().toString(), currentTemp).toString()
          );
        })
        .to("activemq:queue:MsgB")
        .log("AMPQ< ${body}");

    from("direct:weather")
//            .log("WEATHER< ${header.coordinates}")
//            .log("WEATHER< ${body}")
        .setHeader(Exchange.HTTP_METHOD, simple("GET"))
        .setHeader("X-Yandex-API-Key",
            constant("6667c51e-dc0b-4b1c-9218-4ed662bcdab5"))
        .to("https://api.weather.yandex.ru/v2/forecast?${header.coordinates}"
            + "&bridgeEndpoint=true&useSystemProperties=true"
        )
        .process(exchange -> {
          final String rows = exchange.getIn().getBody(String.class)
              .replaceAll("\"", "")
              .replaceAll(" ", "");
          Matcher m = Pattern.compile("fact:\\{temp:(\\d+)").matcher(rows);
          exchange.getIn().setHeader("currentTemp", m.find() ? m.group(1) : 0);
        });

    from("direct:serviceb")
        .log("ServiceB> ${body}");

//    from("jms:queue:MsgB")
    from("activemq:queue:MsgB")
        .to("direct:serviceb")
        .transform(simple("${body}"));

  }

}
