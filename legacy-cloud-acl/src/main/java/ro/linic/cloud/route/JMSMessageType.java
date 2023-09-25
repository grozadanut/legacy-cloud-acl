package ro.linic.cloud.route;

public enum JMSMessageType {
	PRODUCT_CREATE, PRODUCT_DELETE, PRICE_CHANGE, STOCK_CHANGE, NAME_CHANGE;
	
	public static final String JMS_MESSAGE_TYPE_KEY = "jms_message_type";
}
