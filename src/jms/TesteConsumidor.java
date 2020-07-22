package jms;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

public class TesteConsumidor {
    public static void main(String[] args) throws Exception {
        InitialContext context = new InitialContext();

        ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session sessao = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination fila = (Destination) context.lookup("financeiro");
        MessageConsumer messageConsumer = sessao.createConsumer(fila);
        messageConsumer.setMessageListener(msn -> {
            TextMessage textMessage = (TextMessage) msn;
            try {
                System.out.println("Recebendo msg: " + textMessage.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });

        new Scanner(System.in).nextLine();

        sessao.close();
        connection.close();
        context.close();
    }

    private static Connection createConnection() throws NamingException, JMSException {
        InitialContext context = new InitialContext();

        ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();
        connection.start();

        new Scanner(System.in).nextLine();

        connection.close();
        context.close();
        return connection;
    }
}
