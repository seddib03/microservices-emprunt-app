\# Projet MSA - Application de Gestion d'Emprunts



\*\*Nom :\*\* EDDIB  

\*\*Prénom :\*\* Salma

\*\*Filiere :\*\* DSE



\## Description

Application de gestion d'emprunts basée sur une architecture microservices avec MySQL et Kafka.



\## Architecture



\- \*\*Eureka Server\*\* (Port 8761) : Découverte des services

\- \*\*Gateway\*\* (Port 9999) : Point d'entrée unique

\- \*\*User Service\*\* : Gestion des utilisateurs + MySQL (db\_user)

\- \*\*Book Service\*\* : Gestion des livres + MySQL (db\_book)

\- \*\*Emprunt Service\*\* : Gestion des emprunts + MySQL (db\_emprunter)

\- \*\*Notification Service\*\* : Consommateur Kafka pour les notifications

\- \*\*Kafka + Zookeeper\*\* : Communication asynchrone

\- \*\*MySQL\*\* : 3 bases de données (une par service métier)



\## Prérequis



\- Java 17

\- Maven

\- Docker Desktop

\- Git



\## Installation et Démarrage



\### 1. Cloner le projet

```bash

git clone https://github.com/VOTRE\_USERNAME/VOTRE\_REPO.git

cd VOTRE\_REPO

```



\### 2. Build des services

```bash

\# Windows PowerShell

.\\mvnw.cmd clean package -DskipTests

```



\### 3. Build des images Docker

```bash

docker compose build

```



\### 4. Lancer l'application

```bash

docker compose up -d

```



\### 5. Vérifier que tout fonctionne

\- Eureka Dashboard: http://localhost:8761

\- Gateway: http://localhost:9999



\## Tests de l'API



\### Créer un utilisateur

```bash

curl -X POST http://localhost:9999/users -H "Content-Type: application/json" -d "{\\"nom\\":\\"Test\\",\\"email\\":\\"test@example.com\\"}"

```



\### Créer un livre

```bash

curl -X POST http://localhost:9999/books -H "Content-Type: application/json" -d "{\\"titre\\":\\"Test Book\\",\\"auteur\\":\\"Author\\"}"

```



\### Créer un emprunt

```bash

curl -X POST http://localhost:9999/emprunts -H "Content-Type: application/json" -d "{\\"userId\\":1,\\"bookId\\":1}"

```



\### Vérifier les notifications

```bash

docker compose logs -f notification-service

```



\## Arrêt de l'application

```bash

docker compose down

```



\## Technologies utilisées



\- Spring Boot

\- Spring Cloud (Eureka, Gateway)

\- Apache Kafka

\- MySQL

\- Docker \& Docker Compose

\- Maven



\## Date de rendu

13 janvier 2026

