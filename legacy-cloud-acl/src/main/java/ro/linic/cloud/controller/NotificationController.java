package ro.linic.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ro.linic.cloud.command.CreateNotificationCommand;
import ro.linic.cloud.service.NotificationService;

@RestController
@RequestMapping("/notification")
public class NotificationController {

	@Autowired private NotificationService notificationService;
	
	@PostMapping
    public void create(@RequestBody final CreateNotificationCommand command) {
		notificationService.execute(command);
    }
}
