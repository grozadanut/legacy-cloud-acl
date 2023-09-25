package ro.linic.cloud.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpMethods;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import ro.linic.cloud.command.ChangeNameCommand;
import ro.linic.cloud.entity.Product;

@Component
public class NameChangeRoute extends RouteBuilder {
	
	@Override
	public void configure() throws Exception {
        from("jms:queue:sync?cacheLevelName=CACHE_CONSUMER&selector="+JMSMessageType.JMS_MESSAGE_TYPE_KEY+"='"+JMSMessageType.NAME_CHANGE+"'")
        .routeId("nameChangeRoute")
        .transacted()
        .unmarshal().json(JsonLibrary.Gson, Product.class)
        .convertBodyTo(ChangeNameCommand.class)
        .marshal().json(JsonLibrary.Gson)
        .throttle(1).timePeriodMillis(1000)
        .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.POST))
        .to("http:{{cloud.product.synchronizer.url}}/update/name");
	}
}
