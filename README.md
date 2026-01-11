#  Projet MSA - Application de Gestion d'Emprunts de Bibliothèque

**Auteur :** EDDIB Salma  
**Filière :** DSE  
**Date de remise :** 11 Janvier 2026  




## 🎯 Vue d'ensemble

### Description du projet

Cette application est un système de gestion d'emprunts de bibliothèque basé sur une **architecture microservices moderne**. Elle permet de gérer les utilisateurs, les livres et les emprunts de manière distribuée, avec communication synchrone (REST) et asynchrone (Kafka).

### Objectifs pédagogiques

- Comprendre et implémenter une architecture microservices
- Maîtriser Spring Cloud (Eureka, Gateway)
- Gérer la communication inter-services (REST, Kafka)
- Orchestrer des conteneurs avec Docker Compose
- Implémenter des bases de données distribuées

### Fonctionnalités principales

- **Gestion des utilisateurs** : Création, consultation, modification et suppression  
- **Gestion des livres** : Catalogue complet avec informations détaillées  
- **Gestion des emprunts** : Emprunter et retourner des livres  
- **Notifications en temps réel** : Alertes via Kafka lors des emprunts  
- **API Gateway** : Point d'entrée unique avec routage intelligent  
- **Service Discovery** : Enregistrement automatique des services

---

## 🏗️ Architecture détaillée

### Schéma de l'architecture

```
                                 ┌─────────────────┐
                                 │  Eureka Server  │
                                 │   Port: 8761    │
                                 └────────┬────────┘
                                          │
                        ┌─────────────────┴─────────────────┐
                        │                                   │
              ┌─────────▼─────────┐              ┌──────────▼─────────┐
              │   API Gateway     │              │  All Microservices │
              │   Port: 9999      │◄─────────────┤  Register Here     │
              └─────────┬─────────┘              └────────────────────┘
                        │
        ┌───────────────┼───────────────┐
        │               │               │
┌───────▼──────┐ ┌──────▼──────┐ ┌─────▼────────┐
│ User Service │ │ Book Service│ │Emprunt Service│
│  Port: 8082  │ │ Port: 8081  │ │  Port: 8085   │
└──────┬───────┘ └──────┬──────┘ └──────┬────────┘
       │                │                │
       │                │                └──────┐
┌──────▼──────┐ ┌───────▼──────┐               │
│  MySQL User │ │  MySQL Book  │         ┌─────▼──────┐
│ Port: 3310  │ │ Port: 3311   │         │   Kafka    │
└─────────────┘ └──────────────┘         │ Port: 9092 │
                                          └─────┬──────┘
       ┌──────────────────────────────┐        │
       │      MySQL Emprunt           │        │
       │      Port: 3312              │        │
       └──────────────────────────────┘        │
                                                │
                                     ┌──────────▼────────────┐
                                     │ Notification Service  │
                                     │     Port: 8084        │
                                     └───────────────────────┘
```

### Description des composants

#### 1. **Eureka Server** (Port 8761)
- **Rôle** : Service de découverte (Service Registry)
- **Fonction** : Tous les microservices s'enregistrent auprès d'Eureka au démarrage
- **Avantages** : Permet la découverte dynamique des services sans configuration manuelle

#### 2. **API Gateway** (Port 9999)
- **Rôle** : Point d'entrée unique de l'application
- **Fonction** : Routage des requêtes vers les microservices appropriés
- **Routes configurées** :
  - `/users/**` → User Service
  - `/books/**` → Book Service
  - `/emprunts/**` → Emprunt Service
- **Avantages** : Sécurité centralisée, load balancing, gestion des CORS

#### 3. **User Service** (Port 8082)
- **Base de données** : MySQL (db_user) sur le port 3310
- **Responsabilités** :
  - Gestion du CRUD des utilisateurs
  - Validation des données utilisateur
  - Exposition d'API REST pour les utilisateurs
- **Endpoints principaux** :
  - `GET /api/users` : Liste tous les utilisateurs
  - `POST /api/users` : Créer un utilisateur
  - `GET /api/users/{id}` : Obtenir un utilisateur
  - `PUT /api/users/{id}` : Modifier un utilisateur
  - `DELETE /api/users/{id}` : Supprimer un utilisateur

#### 4. **Book Service** (Port 8081)
- **Base de données** : MySQL (db_book) sur le port 3311
- **Responsabilités** :
  - Gestion du catalogue de livres
  - Disponibilité des livres
  - Statistiques sur les livres
- **Endpoints principaux** :
  - `GET /api/books` : Liste tous les livres
  - `POST /api/books` : Ajouter un livre
  - `GET /api/books/{id}` : Détails d'un livre
  - `PUT /api/books/{id}` : Modifier un livre
  - `DELETE /api/books/{id}` : Supprimer un livre

#### 5. **Emprunt Service** (Port 8085)
- **Base de données** : MySQL (db_emprunter) sur le port 3312
- **Communication** : Kafka pour publier des événements
- **Responsabilités** :
  - Gestion des emprunts et retours
  - Communication avec User Service (vérification utilisateur)
  - Communication avec Book Service (vérification disponibilité)
  - Publication d'événements Kafka lors de nouveaux emprunts
- **Endpoints principaux** :
  - `GET /emprunts` : Liste des emprunts
  - `POST /emprunts` : Créer un emprunt
  - `PUT /emprunts/{id}/return` : Retourner un livre

#### 6. **Notification Service** (Port 8084)
- **Rôle** : Consommateur Kafka
- **Fonction** : Écoute les événements d'emprunt et envoie des notifications
- **Topic Kafka écouté** : `emprunt-created`
- **Actions** : Logging des notifications (extensible vers email/SMS)

#### 7. **Infrastructure de messaging**
- **Kafka** (Port 9092) : Message broker pour communication asynchrone
- **Zookeeper** (Port 2181) : Coordination de Kafka

#### 8. **Bases de données MySQL**
- **mysql-user** (Port 3310) : Base de données des utilisateurs
- **mysql-book** (Port 3311) : Base de données des livres
- **mysql-emprunt** (Port 3312) : Base de données des emprunts

### Patterns utilisés

 **API Gateway Pattern** : Point d'entrée unique  
 **Service Discovery Pattern** : Avec Eureka  
 **Database per Service Pattern** : Une base par microservice  
 **Event-Driven Architecture** : Avec Kafka  
 **Containerization** : Avec Docker  
 **Circuit Breaker** : Résilience des appels inter-services

---

## 🛠️ Technologies utilisées

### Backend Framework
- **Spring Boot 3.2.0** : Framework principal
- **Spring Cloud 2023.0.0** : Outils microservices
- **Spring Data JPA** : Persistence des données
- **Spring Kafka** : Integration Kafka
- **Spring Cloud Netflix Eureka** : Service discovery
- **Spring Cloud Gateway** : API Gateway

### Base de données
- **MySQL 8** : Base de données relationnelle
- **3 instances distinctes** pour respecter le pattern "Database per Service"

### Messaging
- **Apache Kafka 7.4.0** : Message broker
- **Zookeeper 7.4.0** : Coordination Kafka

### Conteneurisation
- **Docker** : Containerisation des services
- **Docker Compose** : Orchestration multi-conteneurs

### Build & Packaging
- **Maven 3.x** : Gestion des dépendances et build
- **Java 17** : Version LTS

---

##  Prérequis

### Logiciels requis

| Logiciel | Version minimale | Vérification |
|----------|------------------|--------------|
| Java JDK | 17+ | `java -version` |
| Maven | 3.6+ | `mvn -version` |
| Docker Desktop | 20.10+ | `docker --version` |
| Docker Compose | 2.0+ | `docker compose version` |
| Git | 2.30+ | `git --version` |





##  Installation et démarrage

### Étape 1 : Cloner le projet

```bash
# Cloner depuis GitHub
git clone https://github.com/seddib03/microservices-emprunt-app.git

# Naviguer dans le répertoire
cd microservices-emprunt-app
```

### Étape 2 : Build des services Spring Boot

#### Sur Windows (PowerShell)

```powershell
# Build de tous les services (à la racine du projet)
.\mvnw.cmd clean package -DskipTests

# Ou build service par service
cd user
..\mvnw.cmd clean package -DskipTests
cd ..

cd book
..\mvnw.cmd clean package -DskipTests
cd ..

cd emprunter
..\mvnw.cmd clean package -DskipTests
cd ..

cd notification
..\mvnw.cmd clean package -DskipTests
cd ..

cd eurika
..\mvnw.cmd clean package -DskipTests
cd ..

cd gateway
..\mvnw.cmd clean package -DskipTests
cd ..
```



### Étape 3 : Build des images Docker

```bash
# Construire toutes les images Docker
docker compose build


```

### Étape 4 : Démarrer l'application

```bash
# Démarrer tous les services en arrière-plan
docker compose up -d

# Ou avec logs visibles (sans -d)
docker compose up
```

### Étape 5 : Vérifier le démarrage

```bash
# Vérifier l'état des conteneurs
docker compose ps

# Suivre les logs de tous les services
docker compose logs -f

# Logs d'un service spécifique
docker compose logs -f user-service
```

#### Temps de démarrage attendu

- **Zookeeper & Kafka** : ~10 secondes
- **MySQL** : ~40-60 secondes (healthcheck)
- **Eureka Server** : ~20 secondes
- **Gateway** : ~30 secondes
- **Services métier** : ~40-60 secondes (après MySQL)
- **Notification Service** : ~30 secondes

⏱️ **Total** : Environ 2-3 minutes pour un démarrage complet

---

##  Tests et utilisation

### Accès aux interfaces web

| Service | URL | Description |
|---------|-----|-------------|
| Eureka Dashboard | http://localhost:8761 | Visualiser tous les services enregistrés |
| API Gateway | http://localhost:9999 | Point d'entrée de l'API |

### Tests des endpoints (via Gateway)

#### 1. Gestion des utilisateurs

```bash
# Créer un utilisateur
curl -X POST http://localhost:9999/users \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "EDDIB",
    "prenom": "Salma",
    "email": "salma.eddib@example.com"
  }'

# Lister tous les utilisateurs
curl http://localhost:9999/users

# Obtenir un utilisateur spécifique
curl http://localhost:9999/users/1

# Modifier un utilisateur
curl -X PUT http://localhost:9999/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "EDDIB",
    "prenom": "Salma",
    "email": "salma.new@example.com"
  }'

# Supprimer un utilisateur
curl -X DELETE http://localhost:9999/users/1
```

#### 2. Gestion des livres

```bash
# Créer un livre
curl -X POST http://localhost:9999/books \
  -H "Content-Type: application/json" \
  -d '{
    "titre": "Architecture Microservices",
    "auteur": "Sam Newman",
    "isbn": "978-1491950357",
    "disponible": true
  }'

# Lister tous les livres
curl http://localhost:9999/books

# Obtenir un livre spécifique
curl http://localhost:9999/books/1

# Modifier un livre
curl -X PUT http://localhost:9999/books/1 \
  -H "Content-Type: application/json" \
  -d '{
    "titre": "Architecture Microservices - 2nd Edition",
    "auteur": "Sam Newman",
    "isbn": "978-1491950357",
    "disponible": true
  }'
```

#### 3. Gestion des emprunts

```bash
# Créer un emprunt (déclenche une notification Kafka)
curl -X POST http://localhost:9999/emprunts \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "bookId": 1
  }'

# Lister tous les emprunts
curl http://localhost:9999/emprunts

# Retourner un livre
curl -X PUT http://localhost:9999/emprunts/1/return
```

### Tester avec Postman

Vous pouvez importer cette collection dans Postman :

```json
{
  "info": {
    "name": "Microservices Emprunt API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Create User",
      "request": {
        "method": "POST",
        "url": "http://localhost:9999/users",
        "body": {
          "mode": "raw",
          "raw": "{\"nom\":\"Test\",\"email\":\"test@example.com\"}"
        }
      }
    }
  ]
}
```

---

##  Monitoring et vérification

### Vérifier les services enregistrés dans Eureka

1. Ouvrir http://localhost:8761
2. Vérifier que ces services sont **UP** :
   - GATEWAY-SERVICE
   - USER-SERVICE
   - BOOK-SERVICE
   - EMPRUNT-SERVICE
   - NOTIFICATION-SERVICE

### Vérifier les notifications Kafka

```bash
# Suivre les logs du service de notification
docker compose logs -f notification-service

# Vous devriez voir des messages comme :
# "📧 Notification: Nouvel emprunt créé - User: 1, Book: 1"
```

### Vérifier les bases de données MySQL

```bash
# Se connecter à la base de données user
docker exec -it mysql-user mysql -uroot -proot db_user

# Requêtes SQL de vérification
SELECT * FROM users;
exit;

# Se connecter à la base de données book
docker exec -it mysql-book mysql -uroot -proot db_book
SELECT * FROM books;
exit;

# Se connecter à la base de données emprunt
docker exec -it mysql-emprunt mysql -uroot -proot db_emprunter
SELECT * FROM emprunts;
exit;
```

### Vérifier Kafka

```bash
# Lister les topics Kafka
docker exec -it kafka kafka-topics --list --bootstrap-server localhost:9092

# Consommer les messages du topic emprunt-created
docker exec -it kafka kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic emprunt-created \
  --from-beginning
```

### Santé des conteneurs

```bash
# Vérifier l'état de santé
docker compose ps

# Vérifier l'utilisation des ressources
docker stats

# Voir les logs d'erreur
docker compose logs | grep -i error
```

---

##  Troubleshooting

### Problème : MySQL reste "unhealthy"

**Symptôme** : Les services ne démarrent pas, MySQL montre "unhealthy"

**Solution** :
```bash
# Arrêter et supprimer tous les volumes
docker compose down -v

# Redémarrer
docker compose up -d
```

### Problème : Port déjà utilisé

**Symptôme** : Erreur "port is already allocated"

**Solution** :
```bash
# Voir les ports utilisés
netstat -ano | findstr :8761  # Windows
lsof -i :8761                 # Linux/macOS

# Arrêter le processus qui utilise le port ou changer le port dans docker-compose.yaml
```

### Problème : Service ne s'enregistre pas dans Eureka

**Solution** :
1. Vérifier que `eureka.client.service-url.defaultZone` est bien configuré
2. Vérifier les logs : `docker compose logs service-name`
3. Vérifier que `@EnableDiscoveryClient` est présent dans la classe principale




##  Structure du projet

```
microservices-emprunt-app/
│
├── user/                          # Service de gestion des utilisateurs
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/user/
│   │   │   │       ├── controller/
│   │   │   │       ├── model/
│   │   │   │       ├── repository/
│   │   │   │       ├── service/
│   │   │   │       └── UserServiceApplication.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   ├── Dockerfile
│   └── pom.xml
│
├── book/                          # Service de gestion des livres
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── emprunter/                     # Service de gestion des emprunts
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── notification/                  # Service de notifications
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── eurika/                        # Serveur Eureka
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── gateway/                       # API Gateway
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── docker-compose.yaml           # Orchestration Docker
├── README.md                     # Documentation
├── .gitignore
└── pom.xml                       # Parent POM (si multi-module)
```

---

##  Arrêt et nettoyage

### Arrêt simple

```bash
# Arrêter tous les conteneurs
docker compose down
```

### Arrêt avec suppression des volumes

```bash
# Arrêter et supprimer les volumes (données perdues)
docker compose down -v
```

### Nettoyage complet

```bash
# Supprimer les images Docker
docker compose down --rmi all

# Nettoyer le système Docker
docker system prune -a

# Nettoyer les volumes
docker volume prune
```



##  Auteur

**EDDIB Salma**  
Filière : Data and Software Engineering (DSE)  
Année : 2025-2026


