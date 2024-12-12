package ro.linic.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ro.linic.cloud.pojo.Invoice;
import ro.linic.cloud.service.BillingService;

@RestController
@RequestMapping("/invoice")
public class BillingController {

	@Autowired private BillingService billingService;
	
	@GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable(name = "id") final Long invoiceId) {
		return new ResponseEntity<>(billingService.getInvoice(invoiceId), HttpStatus.OK);
    }
}
