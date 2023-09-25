package ro.linic.cloud.route;

public enum JMSMessageType {
	PRICE_CHANGE, STOCK_CHANGE, PRODUCT_DELETE;
	
	public static final String JMS_MESSAGE_TYPE_KEY = "jms_message_type";
}
