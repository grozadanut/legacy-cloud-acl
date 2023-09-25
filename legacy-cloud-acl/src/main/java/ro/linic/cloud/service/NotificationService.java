package ro.linic.cloud.service;

import ro.linic.cloud.command.CreateNotificationCommand;

public interface NotificationService {

	void execute(CreateNotificationCommand command);

}
