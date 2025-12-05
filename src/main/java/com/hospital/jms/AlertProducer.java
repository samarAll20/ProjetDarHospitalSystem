package com.hospital.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

public class AlertProducer {
    public static void sendAlert(String alertMessage) {
        Connection connection = null;

        try {
            // 1. Créer la ConnectionFactory
            ConnectionFactory factory = new ActiveMQConnectionFactory(
                    "tcp://localhost:61616");

            // 2. Créer la connexion
            connection = factory.createConnection();
            connection.start();

            // 3. Créer la session
            Session session = connection.createSession(
                    false, Session.AUTO_ACKNOWLEDGE);

            // 4. Créer la destination (Topic pour les alertes)
            Destination destination = session.createTopic("hospital.alerts");

            // 5. Créer le producteur
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // 6. Créer et envoyer le message
            TextMessage message = session.createTextMessage();
            message.setText("🚨 ALERTE: " + alertMessage);
            producer.send(message);

            System.out.println("📤 Alert envoyée: " + alertMessage);

            // 7. Nettoyer
            session.close();

        } catch (Exception e) {
            System.err.println("❌ Erreur JMS Producer: " + e);
        } finally {
            if (connection != null) {
                try { connection.close(); }
                catch (JMSException e) {}
            }
        }
    }

    // Méthode de test
    public static void main(String[] args) {
        sendAlert("Patient critique arrivant dans 10 minutes");
        sendAlert("Pénurie de lits en soins intensifs");
        sendAlert("Scanner IRM disponible");
    }
}