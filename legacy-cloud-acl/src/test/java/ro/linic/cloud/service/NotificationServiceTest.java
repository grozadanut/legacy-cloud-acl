package ro.linic.cloud.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

	@Mock private UserRepository userRepo;
	@Mock private CompanyRepository companyRepo;
	@Mock private GestiuneRepository gestiuneRepo;
	@Mock private AccountingDocumentRepository accDocRepo;
	@Mock private PartnerRepository partnerRepo;
	
	@InjectMocks private NotificationServiceImpl notificationService;
	@Mock private OperatiuneRepository operatiuneRepo;

	@Test
	public void whenCreateNotification_thenNotifOperationShouldBeCreated() {
		// given
		final Company company = new Company();
		company.setId(1);
		when(companyRepo.findById(1)).thenReturn(Optional.of(company));
		final Gestiune gestiune = new Gestiune();
		gestiune.setId(1);
		when(gestiuneRepo.findById(null)).thenReturn(Optional.of(gestiune));
		final User user = new User();
		user.setId(1);
		when(userRepo.findById(null)).thenReturn(Optional.of(user));
		when(operatiuneRepo.save(any())).then(AdditionalAnswers.returnsFirstArg());
		when(accDocRepo.save(any())).then(AdditionalAnswers.returnsFirstArg());
		when(partnerRepo.save(any())).then(AdditionalAnswers.returnsFirstArg());

		// when
		final CreateNotificationCommand command = new CreateNotificationCommand(1, "Notification text");
		notificationService.execute(command);

		// then
		final ArgumentCaptor<Operatiune> notifCaptor = ArgumentCaptor.forClass(Operatiune.class);
		verify(operatiuneRepo, times(2)).save(notifCaptor.capture());

		final Operatiune capturedOp = notifCaptor.getValue();
		assertThat(capturedOp.getCompany()).isEqualTo(company);
		assertThat(capturedOp.getBarcode()).isEqualTo("Notificare");
		assertThat(capturedOp.getCantitate()).isEqualByComparingTo(BigDecimal.ZERO);
		assertThat(capturedOp.getCategorie()).isEqualTo("NOTIFICARE");
		assertThat(capturedOp.getGestiune()).isEqualTo(gestiune);
		assertThat(capturedOp.getName()).isEqualTo("Notification text");
		assertThat(capturedOp.getOperator()).isEqualTo(user);
		assertThat(capturedOp.getPretUnitarAchizitieFaraTVA()).isEqualByComparingTo(BigDecimal.ZERO);
		assertThat(capturedOp.getValoareAchizitieFaraTVA()).isEqualByComparingTo(BigDecimal.ZERO);
		assertThat(capturedOp.getValoareAchizitieTVA()).isEqualByComparingTo(BigDecimal.ZERO);
		assertThat(capturedOp.getPretVanzareUnitarCuTVA()).isEqualByComparingTo(BigDecimal.ZERO);
		assertThat(capturedOp.getValoareVanzareFaraTVA()).isEqualByComparingTo(BigDecimal.ZERO);
		assertThat(capturedOp.getValoareVanzareTVA()).isEqualByComparingTo(BigDecimal.ZERO);
		assertThat(capturedOp.isRpz()).isFalse();
		assertThat(capturedOp.getTipOp()).isEqualTo(TipOp.INTRARE);
		assertThat(capturedOp.getUom()).isEmpty();
		assertThat(capturedOp.isShouldVerify()).isTrue();
		
    	assertThat(capturedOp.getAccDoc().getCompany()).isEqualTo(company);
    	assertThat(capturedOp.getAccDoc().getTipDoc()).isEqualTo(TipDoc.CUMPARARE);
    	assertThat(capturedOp.getAccDoc().getName()).isEqualTo("#notificari");
    	assertThat(capturedOp.getAccDoc().getOperator()).isEqualTo(user);
    	assertThat(capturedOp.getAccDoc().getGestiune()).isEqualTo(gestiune);
    	assertThat(capturedOp.getAccDoc().getDoc()).isEqualTo(AccountingDocument.NOTIFICARE_NAME);
    	assertThat(capturedOp.getAccDoc().getNrDoc()).isEqualTo(AccountingDocument.automaticNotificationNr(LocalDate.now()));
    	assertThat(capturedOp.getAccDoc().getNrReceptie()).isEqualTo(AccountingDocument.automaticNotificationNr(LocalDate.now()));
    	assertThat(capturedOp.getAccDoc().isRpz()).isFalse();
    	
    	assertThat(capturedOp.getAccDoc().getPartner().getCompany()).isEqualTo(company);
    	assertThat(capturedOp.getAccDoc().getPartner().getName()).isEqualTo(Partner.OP_INTERNA);
	}
}
