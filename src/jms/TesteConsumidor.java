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

        produtor(sessao, fila);

        consumidor(sessao, fila);

        new Scanner(System.in).nextLine();

        sessao.close();
        connection.close();
        context.close();
    }

    private static void consumidor(Session sessao, Destination fila) throws JMSException {
        MessageConsumer messageConsumer = sessao.createConsumer(fila);
        messageConsumer.setMessageListener(msn -> {
            TextMessage textMessage = (TextMessage) msn;
            try {
                System.out.println("Recebendo msg: " + textMessage.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
    }

    private static void produtor(Session sessao, Destination fila) throws JMSException {
        MessageProducer messageProducer = sessao.createProducer(fila);
        messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        TextMessage msnProducer = sessao.createTextMessage("eu enviei de novo");
        messageProducer.send(msnProducer);
        System.out.println("msn enviada");
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
