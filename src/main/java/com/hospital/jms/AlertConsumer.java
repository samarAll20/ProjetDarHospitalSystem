package com.hospital.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

public class AlertConsumer implements MessageListener {

    public void startListening() {
        try {
            // 1. Créer la ConnectionFactory
            ConnectionFactory factory = new ActiveMQConnectionFactory(
                    "tcp://localhost:61616");

            // 2. Créer la connexion
            Connection connection = factory.createConnection();
            connection.start();

            // 3. Créer la session
            Session session = connection.createSession(
                    false, Session.AUTO_ACKNOWLEDGE);

            // 4. Créer la destination (même Topic)
            Destination destination = session.createTopic("hospital.alerts");

            // 5. Créer le consommateur
            MessageConsumer consumer = session.createConsumer(destination);

            // 6. Définir le listener (cette classe)
            consumer.setMessageListener(this);

            System.out.println("👂 En écoute des alertes hospitalières...");
            System.out.println("Appuyez sur Entrée pour arrêter");

            // Garder le programme actif
            System.in.read();

            // 7. Nettoyer
            session.close();
            connection.close();

        } catch (Exception e) {
            System.err.println("❌ Erreur JMS Consumer: " + e);
        }
    }

    // Cette méthode est appelée quand un message arrive
    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                System.out.println("\n📨 NOUVELLE ALERTE REÇUE:");
                System.out.println("   " + textMessage.getText());
                System.out.println("   Heure: " + new java.util.Date());
                System.out.println("---");
            }
        } catch (JMSException e) {
            System.err.println("❌ Erreur lecture message: " + e);
        }
    }

    // Méthode principale
    public static void main(String[] args) {
        new AlertConsumer().startListening();
    }
}