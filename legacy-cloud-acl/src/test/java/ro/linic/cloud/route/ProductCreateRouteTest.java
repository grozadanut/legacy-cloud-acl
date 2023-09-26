package ro.linic.cloud.route;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.http.HttpMethods;
import org.apache.camel.component.mapstruct.MapstructComponent;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spi.Registry;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import com.google.gson.Gson;

import ro.linic.cloud.command.CreateProductCommand;
import ro.linic.cloud.entity.Company;
import ro.linic.cloud.entity.Product;

@ExtendWith(MockitoExtension.class)
public class ProductCreateRouteTest extends CamelTestSupport {

    @EndpointInject("mock:listenForComplete")
    private MockEndpoint mock;
    
    @InjectMocks private ProductCreateRoute route;
    @Mock private PlatformTransactionManager manager;
    @Mock private TransactionStatus transactionStatus;
    
    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
    	final Registry registry = context.getRegistry();
        registry.bind("Transacted", PlatformTransactionManager.class, manager);
        
        final MapstructComponent mc = context.getComponent("mapstruct", MapstructComponent.class);
        mc.setMapperPackageName("ro.linic.cloud.mapper");
        return route;
    }
    
    @Override
    public boolean isUseAdviceWith() {
        return true;
    }
    
    @Override
    protected void doPostSetup() throws Exception {
    	when(manager.getTransaction(any())).thenReturn(transactionStatus);
    	
    	AdviceWith.adviceWith(context, ProductCreateRoute.ID,
    			a -> 
    	{
    		a.replaceFromWith("direct:start");
    		a.weaveByToUri("http*").replace().to("mock:listenForComplete");
    	});
    	context.start();
    }

    @Test
    void whenSendBody_thenPostReceivedSuccessfully() throws InterruptedException {
    	// given
    	final Company company = new Company();
    	company.setId(1);
    	final Product product = new Product();
    	product.setCompany(company);
    	product.setId(100);
    	product.setBarcode("59");
    	product.setName("ciment holcim");
    	product.setUom("buc");
    	product.setPricePerUom(new BigDecimal("29.5"));
    	
    	// expected
    	final Gson gson = new Gson();
    	mock.expectedHeaderReceived(Exchange.HTTP_METHOD, HttpMethods.POST);
    	mock.expectedBodiesReceived(gson.toJson(new CreateProductCommand(1, 100, "59", "ciment holcim", "buc", new BigDecimal("29.5"))));
        
    	// when
    	template.sendBody("direct:start", gson.toJson(product));
    	
        // then
        mock.assertIsSatisfied();
    }
}
