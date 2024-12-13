package ro.linic.cloud.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ro.linic.cloud.pojo.Invoice;
import ro.linic.cloud.service.BillingService;
import ro.linic.cloud.service.ProsoftService;

@RestController
@RequestMapping("/invoice")
public class BillingController {
	@Autowired private BillingService billingService;
	@Autowired private ProsoftService prosoftService;
	
	@GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable(name = "id") final Long invoiceId) {
		return new ResponseEntity<>(billingService.getInvoice(invoiceId), HttpStatus.OK);
    }
	
	@PostMapping("/prosoft")
    public ResponseEntity<byte[]> exportInvoices_Prosoft(@RequestParam final LocalDate from,
    		@RequestParam final LocalDate to) {
		return new ResponseEntity<>(prosoftService.invoices(from, to), HttpStatus.OK);
    }
}
