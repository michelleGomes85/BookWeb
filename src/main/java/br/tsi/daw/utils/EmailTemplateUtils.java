package br.tsi.daw.utils;

public class EmailTemplateUtils {

	public static StringBuilder getConfirmationEmail(String username, String userType, String confirmLink) {
		
	    StringBuilder emailContent = new StringBuilder();

	    emailContent.append("<!DOCTYPE html>");
	    emailContent.append("<html lang='pt'>");
	    emailContent.append("<head>");
	    emailContent.append("<meta charset='UTF-8'>");
	    emailContent.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
	    emailContent.append("<title>Confirmação de Cadastro</title>");
	    emailContent.append("</head>");
	    emailContent.append("<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; padding: 30px;'>");
	    emailContent.append("<div style='background: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); max-width: 500px; margin: auto;'>");

	    // Mensagem personalizada com base no tipo de usuário
	    emailContent.append("<h1 style='color: #333;'>Olá, ").append(username).append("!</h1>");
	    
	    if ("FUNCIONÁRIO".equalsIgnoreCase(userType))
	        emailContent.append("<p style='font-size: 16px; color: #555;'>Você foi cadastrado como <strong>funcionário</strong> na plataforma <strong>Library Virtual</strong>.</p>");
	    else
	        emailContent.append("<p style='font-size: 16px; color: #555;'>Você foi cadastrado como <strong>cliente</strong> na <strong>Library Virtual</strong>.</p>");

	    emailContent.append("<p style='font-size: 16px; color: #555;'>Para ativar sua conta, clique no botão abaixo:</p>");
	    
	    // Cor do botão em inline CSS
	    emailContent.append("<a href='").append(confirmLink).append("' style='display: inline-block; padding: 10px 20px; background: #007bff; color: #fff; text-decoration: none; font-weight: bold; border-radius: 5px;'>Confirmar Cadastro</a>");

	    emailContent.append("<p style='font-size: 16px; color: #555;'>Se você não solicitou este cadastro, ignore este e-mail.</p>");
	    emailContent.append("</div>");
	    emailContent.append("</body>");
	    emailContent.append("</html>");

	    return emailContent;
	}
}
