package ttree.it.ttreeGradle.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ttree.it.ttreeGradle.dto.MailDto;

@Service
@AllArgsConstructor
public class MailService { //신청서 제출용 이메일
    private JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "ttree1906@gmail.com";

    public void mailSend(MailDto mailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setFrom(MailService.FROM_ADDRESS);
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());

        mailSender.send(message);
    }

    public void mailTokenSend(String email, String token) { //인증 번호용 이메일
        SimpleMailMessage authCodeMail = new SimpleMailMessage();
        authCodeMail.setTo(email);
        authCodeMail.setSubject("[졸업프로젝트 관리 시스템] 인증 코드입니다.");
        authCodeMail.setFrom(FROM_ADDRESS);
        authCodeMail.setText(
                "숙명여자대학교 IT공학전공 졸업프로젝트 관리 시스템입니다.\n하단의 인증 코드를 회원가입 창에 입력하세요.\n" + token);

        mailSender.send(authCodeMail);
    }

    public void mailPasswordSend(String email, String password) {
        SimpleMailMessage passCodeMail = new SimpleMailMessage();
        passCodeMail.setTo(email);
        passCodeMail.setSubject("[졸업프로젝트 관리 시스템] 임시 비밀번호입니다.");
        passCodeMail.setFrom(FROM_ADDRESS);
        passCodeMail.setText(
                "숙명여자대학교 IT공학전공 졸업프로젝트 관리 시스템입니다.\n하단의 임시 비밀번호를 로그인 시에 입력하세요.\n" + password
                        + "\n로그인 후에 마이페이지에서 비밀번호를 변경하세요.");

        mailSender.send(passCodeMail);
    }
}
