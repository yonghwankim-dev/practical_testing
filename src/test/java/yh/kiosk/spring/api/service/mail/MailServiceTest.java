package yh.kiosk.spring.api.service.mail;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import yh.kiosk.spring.client.mail.MailSendClient;
import yh.kiosk.spring.domain.history.mail.MailSendHistory;
import yh.kiosk.spring.domain.history.mail.MailSendHistoryRepository;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

	@Mock // 모든 메소드가 가짜 객체 호출
	// @Spy // 일부 메소드는 진짜 객체의 메소드 호출, 모킹한 메소드만 가짜 객체 호출
	private MailSendClient mailSendClient;

	@Mock
	private MailSendHistoryRepository mailSendHistoryRepository;

	@InjectMocks
	private MailService mailService;

	@DisplayName("메일을 전송한다")
	@Test
	public void sendMail() {
		// given
		String fromEmail = "test@cafekiosk.com";
		String toEmail = "test@test.com";
		String subject = String.format("[매출통계] %s", LocalDate.of(2023, 3, 5));
		String content = String.format("총 매출 합계는 %d원 입니다.", 12000);

		// @Mock 목객체 사용
		// when(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
		// 	.thenReturn(true);

		// @Spy 목객체 사용
		// doReturn(true)
		// 	.when(mailSendClient)
		// 	.sendEmail(anyString(), anyString(), anyString(), anyString());

		// BDDMokito 사용 (given 사용)
		// Mockito.when 과 기능은 동일하다. 이름만 가독성있게 변경됨.
		BDDMockito.given(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
			.willReturn(true);
		// when
		boolean result = mailService.sendMail(fromEmail, toEmail, subject, content);

		// then
		assertThat(result).isTrue();
		verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
	}

}
