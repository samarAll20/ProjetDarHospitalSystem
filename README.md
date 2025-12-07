# Système Hospitalier Distribué

Un système de gestion d'urgences hospitalières utilisant des technologies de middleware distribuées : RMI, JMS, CORBA, avec interface JavaFX et base de données H2.

## Fonctionnalités
- Enregistrement des patients via RMI (communication synchrone)
- Alertes en temps réel via JMS/ActiveMQ (communication asynchrone)
- Service d'interopérabilité CORBA en Python
- Interface graphique moderne avec JavaFX
- Persistance des données avec H2 Database
- Gestion des urgences avec niveaux de gravité

##  Architecture
Interface JavaFX
↓
Serveur RMI (Java) ↔ Base de données H2
↓
Producteur JMS → ActiveMQ Broker → Consommateur JMS
↓
Service CORBA (Python)

## Technologies Utilisées
- Java 8+ (RMI, JMS, JavaFX)
- Python 3.x (CORBA via omniORB)
- Apache ActiveMQ 5.16.5 (Broker JMS)
- H2 Database (Base de données embarquée)
- JavaFX (Interface utilisateur)
- Maven (Gestion des dépendances)

## Installation et Exécution

### Prérequis
- JDK 8 ou supérieur
- Python 3.x avec omniORB (`pip install omniORB`)
- Apache ActiveMQ

### 1. Démarrer ActiveMQ
```bash
cd C:\apache-activemq-5.16.5\bin\win64\
activemq.bat
