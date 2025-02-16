package br.tsi.daw.utils;

import java.util.List;

import br.tsi.daw.model.ItemOrder;

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
	
    public static StringBuilder getOrderFinalizationEmail(String customerName, Long orderId, String orderDate, double totalPrice, List<ItemOrder> orderItems) {
        
        StringBuilder emailContent = new StringBuilder();

        emailContent.append("<!DOCTYPE html>");
        emailContent.append("<html lang='pt'>");
        emailContent.append("<head>");
        emailContent.append("<meta charset='UTF-8'>");
        emailContent.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        emailContent.append("<title>Seu Pedido foi Finalizado</title>");
        emailContent.append("</head>");
        emailContent.append("<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; padding: 30px;'>");
        emailContent.append("<div style='background: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); max-width: 600px; margin: auto;'>");

        emailContent.append("<h1 style='color: #333;'>Pedido Finalizado com Sucesso!</h1>");
        emailContent.append("<p style='font-size: 16px; color: #555;'>Olá, <strong>").append(customerName).append("</strong>!</p>");
        emailContent.append("<p style='font-size: 16px; color: #555;'>Seu pedido foi finalizado e já está sendo processado.</p>");

        // Dados do pedido
        emailContent.append("<p style='font-size: 16px;'><strong>Número do Pedido:</strong> ").append(orderId).append("</p>");
        emailContent.append("<p style='font-size: 16px;'><strong>Data do Pedido:</strong> ").append(orderDate).append("</p>");
        emailContent.append("<p style='font-size: 16px;'><strong>Valor Total:</strong> R$ ").append(String.format("%.2f", totalPrice)).append("</p>");

        // Lista de livros comprados
        emailContent.append("<h2 style='color: #007bff;'>Itens do Pedido:</h2>");
        emailContent.append("<table style='width: 100%; border-collapse: collapse;'>");
        emailContent.append("<tr style='background-color: #007bff; color: #ffffff;'>");
        emailContent.append("<th style='padding: 10px; border: 1px solid #ddd;'>Livro</th>");
        emailContent.append("<th style='padding: 10px; border: 1px solid #ddd;'>Quantidade</th>");
        emailContent.append("</tr>");

        for (ItemOrder item : orderItems) {
            emailContent.append("<tr style='background-color: #f9f9f9;'>");
            emailContent.append("<td style='padding: 10px; border: 1px solid #ddd;'>").append(item.getBook().getTitle()).append("</td>");
            emailContent.append("<td style='padding: 10px; border: 1px solid #ddd;'>").append(item.getAmount()).append("</td>");
            emailContent.append("</tr>");
        }

        emailContent.append("</table>");
        emailContent.append("<p style='font-size: 16px; color: #555;'>Agradecemos pela sua compra! 📚</p>");
        emailContent.append("</div>");
        emailContent.append("</body>");
        emailContent.append("</html>");

        return emailContent;
    }
}
