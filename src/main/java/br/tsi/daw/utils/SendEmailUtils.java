package br.tsi.daw.utils;

import java.util.Properties;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * Classe utilitária para enviar e-mails usando o Gmail SMTP.
 * 
 * <p>
 * Este método usa a biblioteca Jakarta Mail para enviar e-mails através do
 * servidor SMTP do Gmail.
 * </p>
 * 
 * <p>
 * Exemplo de uso:
 * </p>
 * 
 * <pre>
 * String to = "destinatario@dominio.com";
 * String subject = "Assunto do E-mail";
 * StringBuilder content = new StringBuilder("Conteúdo do e-mail.");
 * SendEmailUtils.sendEmail(to, subject, content);
 * </pre>
 */
public class SendEmailUtils {

	/* 
	 * Configurações do e-mail
	 * 
	 * Necessário o email que será usado para enviar a mensagem, 
	 * 		- from 
	 *      - username
	 * 
	 * O password por segurança gmail, não pode ser a senha do email.
	 * Tem que ser uma senha gerada para aplicativo, nas configuração do gmail;
	 * 
	 * - https://myaccount.google.com/
	 * - Pesquisa: Senhas de App
	 * - Dê um nome para o app e gere a senha
	 * 
	 * OBS.: Verificação de duas etapas tem que estar ativa
	 * 
	 * */
	private static final String from = "";
	private static final String username = "";
	private static final String password = "";
	private static final String host = "smtp.gmail.com";
	
	/**
	 * Envia um e-mail para o endereço especificado com o assunto e conteúdo
	 * fornecidos.
	 * 
	 * @param addressTo O endereço de e-mail do destinatário.
	 * @param subject   O assunto do e-mail.
	 * @param content   O conteúdo do e-mail.
	 */
	public static void sendEmail(String addressTo, String subject, StringBuilder content) {

		// Definindo as propriedades do servidor SMTP
		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "587"); // Porta do Gmail para envio com TLS
		properties.put("mail.smtp.auth", "true"); // Habilitar autenticação SMTP
		properties.put("mail.smtp.starttls.enable", "true"); // Habilitar STARTTLS

		Session session = Session.getInstance(properties, new jakarta.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			
			// E-mail de envio
			message.setFrom(new InternetAddress(from)); 
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(addressTo)); // Destinatário
			message.setSubject(subject);

			// Enviando o conteúdo com formatação HTML
			message.setContent(content.toString(), "text/html; charset=UTF-8");

			jakarta.mail.Transport.send(message);
			System.out.println("E-mail enviado com sucesso!");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
