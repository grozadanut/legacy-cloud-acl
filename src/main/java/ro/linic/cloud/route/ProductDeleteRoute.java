package ro.linic.cloud.route;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.SpringTransactionErrorHandlerBuilder;
import org.apache.camel.component.http.HttpMethods;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import ro.linic.cloud.command.DeleteProductCommand;
import ro.linic.cloud.entity.Product;

@Component
public class ProductDeleteRoute extends RouteBuilder {
	
	public static final String ID = "productDeleteRoute";
	
	@Override
	public void configure() throws Exception {
		errorHandler(new SpringTransactionErrorHandlerBuilder()
    			.retryAttemptedLogLevel(LoggingLevel.ERROR)
    			.maximumRedeliveries(-1)
    			.redeliveryDelay(60000));
    	
        from("jms:queue:sync?cacheLevelName=CACHE_CONSUMER&selector="+JMSMessageType.JMS_MESSAGE_TYPE_KEY+"='"+JMSMessageType.PRODUCT_DELETE+"'")
        .routeId(ID)
        .transacted()
        .unmarshal().json(JsonLibrary.Gson, Product.class)
        .convertBodyTo(DeleteProductCommand.class)
        .marshal().json(JsonLibrary.Gson)
        .throttle(1).timePeriodMillis(500)
        .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.POST))
        .to("http:{{cloud.product.synchronizer.url}}/update/deleteProduct");
	}
}
