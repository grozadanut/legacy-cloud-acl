package ro.linic.cloud.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ro.linic.cloud.command.CreateNotificationCommand;
import ro.linic.cloud.entity.AccountingDocument;
import ro.linic.cloud.entity.Company;
import ro.linic.cloud.entity.Document.TipDoc;
import ro.linic.cloud.entity.Gestiune;
import ro.linic.cloud.entity.Operatiune;
import ro.linic.cloud.entity.Operatiune.TipOp;
import ro.linic.cloud.entity.Partner;
import ro.linic.cloud.entity.User;
import ro.linic.cloud.repository.AccountingDocumentRepository;
import ro.linic.cloud.repository.CompanyRepository;
import ro.linic.cloud.repository.GestiuneRepository;
import ro.linic.cloud.repository.OperatiuneRepository;
import ro.linic.cloud.repository.PartnerRepository;
import ro.linic.cloud.repository.UserRepository;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired private UserRepository userRepo;
	@Autowired private CompanyRepository companyRepo;
	@Autowired private GestiuneRepository gestiuneRepo;
	@Autowired private OperatiuneRepository operatiuneRepo;
	@Autowired private AccountingDocumentRepository accDocRepo;
	@Autowired private PartnerRepository partnerRepo;
	
	@Value("${notification.user.id}")
	private Integer userId;
	@Value("${notification.gestiune.id}")
	private Integer gestiuneId;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void execute(final CreateNotificationCommand command) {
		createNotificationOperation(companyRepo.findById(command.getCompanyId()).get(),
				gestiuneRepo.findById(gestiuneId).get(),
				userRepo.findById(userId).get(),
				"Notificare", command.getNotification());
	}

	private Operatiune createNotificationOperation(final Company company, final Gestiune gestiune, final User user,
			final String barcode, final String name)
	{
		// create new notification operation
		Operatiune notifOp = new Operatiune();
		notifOp.setCompany(company);
		notifOp.setBarcode(barcode);
		notifOp.setCantitate(BigDecimal.ZERO);
		notifOp.setCategorie("NOTIFICARE");
		notifOp.setGestiune(gestiune);
		notifOp.setName(name);
		notifOp.setOperator(user);
		notifOp.setPretUnitarAchizitieFaraTVA(BigDecimal.ZERO);
		notifOp.setValoareAchizitieFaraTVA(BigDecimal.ZERO);
		notifOp.setValoareAchizitieTVA(BigDecimal.ZERO);
		notifOp.setPretVanzareUnitarCuTVA(BigDecimal.ZERO);
		notifOp.setValoareVanzareFaraTVA(BigDecimal.ZERO);
		notifOp.setValoareVanzareTVA(BigDecimal.ZERO);
		notifOp.setRpz(false);
		notifOp.setTipOp(TipOp.INTRARE);
		notifOp.setUom("");
		notifOp.setShouldVerify(true);
		
		notifOp = operatiuneRepo.save(notifOp);
		notifOp.setAccDoc(automaticNotificationOfTheDay(company, gestiune, user));
		notifOp.getAccDoc().getOperatiuni().add(notifOp);
		return operatiuneRepo.save(notifOp);
	}
	
	private AccountingDocument automaticNotificationOfTheDay(final Company company, final Gestiune gestiune, final User user)
	{
        AccountingDocument notif =  accDocRepo.findByCompanyIdAndNrDoc(company.getId(),
        		AccountingDocument.automaticNotificationNr(LocalDate.now()))
        		.stream().findFirst().orElse(null);
        
        if (notif == null)
        {
        	notif = new AccountingDocument();
        	notif.setCompany(company);
        	notif.setPartner(opInternaPartner(company));
        	notif.setTipDoc(TipDoc.CUMPARARE);
        	notif.setName("#notificari");
        	notif.setOperator(user);
        	notif.setGestiune(gestiune);
        	notif.setDoc(AccountingDocument.NOTIFICARE_NAME);
        	notif.setNrDoc(AccountingDocument.automaticNotificationNr(LocalDate.now()));
        	notif.setNrReceptie(AccountingDocument.automaticNotificationNr(LocalDate.now()));
        	notif.setRpz(false);
        	notif = accDocRepo.save(notif);
        }
        
		return notif;
	}
	
	private Partner opInternaPartner(final Company company)
	{
        Partner opInternaPartner = partnerRepo.findByCompanyIdAndName(company.getId(), Partner.OP_INTERNA)
        		.stream().findFirst().orElse(null);
        
        if (opInternaPartner == null)
        {
        	opInternaPartner = new Partner();
        	opInternaPartner.setCompany(company);
        	opInternaPartner.setName(Partner.OP_INTERNA);
        	opInternaPartner = partnerRepo.save(opInternaPartner);
        }
        
        return opInternaPartner;
	}
}
