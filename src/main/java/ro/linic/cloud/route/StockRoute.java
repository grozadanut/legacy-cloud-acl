package ro.linic.cloud.route;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.AggregationStrategies;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.SpringTransactionErrorHandlerBuilder;
import org.apache.camel.component.http.HttpMethods;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import ro.linic.cloud.command.ChangeStockCommand;
import ro.linic.cloud.entity.Product;

@Component
public class StockRoute extends RouteBuilder {
	
	public static final String ID = "stockRoute";
	
	@Override
    public void configure() throws Exception {
    	errorHandler(new SpringTransactionErrorHandlerBuilder()
    			.retryAttemptedLogLevel(LoggingLevel.ERROR)
    			.maximumRedeliveries(-1)
    			.redeliveryDelay(60000));
    	
        from("jms:queue:sync?cacheLevelName=CACHE_CONSUMER&selector="+JMSMessageType.JMS_MESSAGE_TYPE_KEY+"='"+JMSMessageType.STOCK_CHANGE+"'")
        .routeId(ID)
        .transacted()
        .unmarshal().json(JsonLibrary.Gson, Product.class)
        .convertBodyTo(ChangeStockCommand.class)
        .aggregate(simple("${body.companyId}_${body.productId}"), AggregationStrategies.useLatest()).completionTimeout(60000*5)
        .marshal().json(JsonLibrary.Gson)
        .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.POST))
        .throttle(1).timePeriodMillis(10000)
        .to("http:{{cloud.product.synchronizer.url}}/update/stock");
        
    }
}
