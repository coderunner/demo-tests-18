# Frontend

Dans le répertoire frontend, utiliser

```
ng test
```

pour exécuter les tests.

Pour rouler le frontend

```
ng serve
```

## Démo tests Angular

1. Composant intelligent - ViewPageComponent
2. Composant de présentation - DisplayBooksComponent
3. Service - BookService
4. Couverture - ng test --no-watch --code-coverage (Ouvrir le rapport dans /coverage)
5. Debug - https://angular.dev/guide/testing/debugging
    - Activer Debug de karma
    - Ouvrir les outils de développement
    - Open File (cmd + p) et ouvrir le fichier spec (ex AddPageComponent)
    - Mettre une break point
    - Recharger la page

Bien comprendre describe, it, la définition du test, les mock, les spy, le client HTTP de test.
Option de rouler une seule suite (fdescribe) ou seulement certains tests (fit).

# Backend

Vous devez installer le firebase cli.

```
npm install -g firebase-tools
```

Puis dans le répertoire backend

```
firebase init
```

Pour installer

- firestore
- emulator (firestore)

Ensuite, copier votre clé firebase dans le fichier firebase-key.json à la racine du répertoire backend.

Ajuster l'id de votre projet firebase dans firebase.properties.
Assurez-vous que le port de l'émulateur est 8181 (firebase.json).

Pour exécuter les tests

```
firebase emulators:exec "./gradlew clean test"
```

Pour rouler le backend

```
./gradlew bootRun
```

Pour rouler les tests en debug avec Visual Studio Code, vous pouvez rouler l'émulateur avec

```
firebase emulators:start
```

** Vous devez configurer Visual Studio Code (cherchez java.test.config dans les settings) pour qu'il ajoute dans les variables d'environnement:

```
FIRESTORE_EMULATOR_HOST=localhost:8181
```

Celà devrait ressembler au json suivant dans votre settings.json.

```
{
  ...
  "java.test.config": {
    "env": {
      "FIRESTORE_EMULATOR_HOST": "localhost:8181"
    }
  }
  ...
}

```

## Démo tests Spring Boot

firebase emulators:exec "./gradlew clean test"

1. Unit tests
2. Integration tests
3. Rapport de couverture dans build/reports/jacoco
4. Debug
    - Rouler l'émulateur dans un terminal
    - Dans un autre terminal, assigner la variable d'enviromment (export FIRESTORE_EMULATOR_HOST=localhost:8181)
    - Break point + clic droit + Debug Test
    - Debugger VSCode

# Cypress

Terminal #1

```
firebase emulators:start
```

Terminal #2

```
export FIRESTORE_EMULATOR_HOST=localhost:8181
./gradlew bootRun

```

Terminal #3

```
ng e2e
```

# Visual VM and wrk

0. Mise en place

- Terminal 1:
  firebase emulators:start
- Terminal 2:
  export FIRESTORE_EMULATOR_HOST=localhost:8181
  ./gradlew bootRun

1. Démarrer Visual VM
2. Démarrer l'émulateur firestore
   firebase emulators:start
3. Démarrer le backend
   export FIRESTORE_EMULATOR_HOST=localhost:8181
   ./gradlew bootRun
4. Ajouter un livre avec POSTMAN (!important ou front)
5. Connecter Visual VM
6. Utiliser wrk

Ex: wrk -t12 -c400 -d30s "http://127.0.0.1:8080/books?limit=20"

## Goulot d'étranglement

1. Montrer les stats/threads
2. Montrer un threaddump (rechercher pour AbstractApiFuture)
3. Éliminer le goulot
4. Montrer les stats/threads

## Fuite de mémoire

1. Petit réchauffement
2. wrk + GC x 2
3. Voir la tendance de la heap
4. Heap dump + GC Root
5. Profiler Heap - org.*

# Docker

Il faut installer Docker: https://docs.docker.com/get-docker/

Construire le projet pour obtenir un jar avec:

```
./gradlew bootJar
```

Le jar sera dans backend/build/libs

S'assurer que Docker roule.

Voir le Dockerfile.

Construire l'image _inf5190/app-books_ avec le tag _latest_.

```
docker build -t inf5190/app-books:latest . 
```

Exécuter le conteneur

```
docker run -dp 8080:8080 inf5190/app-books:latest
docker ps
docker stop ID
docker start ID
docker container ls -a
docker exec -it <name or id> /bin/sh
docker logs -f <name or id>
```

# CI

Voir exemple sur Gitlab.

Fichier .gitlab-ci.yml

Variables d'environnement: dans Settings - CI/CD - Variables

Token pour l'émulateur: https://firebase.google.com/docs/emulator-suite/install_and_configure#integrate_with_your_ci_system

# Déploiement (TBD)

## Spring Boot

S'assurer que le Artifact Registry est actif et qu'on est authorisé avec gcloud.
Utiliser l'accès avec la clé.

Avec le jib plugin:

```
./gradle jib
```

Voir le Artifact Registry.

Voir Cloud Run (variable d'environnement ALLOWED_ORIGINS pour CORS)

Voir les Journaux

## Angular

Construire la version production (avec environment.ts)

```
ng build
```

```
firebase deploy --only hosting
```

# Exemple de test avec Locust (+bug?)

Installer Locust (https://locust.io/).

S'assurer d'utiliser la tâche GET /counter

- Mettre le compteur à 0 (DELETE /counter avec Postman)
- Exécuter la commande `locust` dans le répertoire `locust`
- Sur l'interface web utiliser les paramèetres suivant: users -> 1000; ramp -> 100; durée: 60s
- Comparer le rapport avec un GET sur /counter
- humm ?

# Surveillance

## Spring Boot Actuator 

Il est possible d'ajouter des endpoints de surveillance sur une serveur Spring Boot.

Dans `build.gradle.kts`, section dependencies, ajouter:

```
implementation("org.springframework.boot:spring-boot-starter-actuator")
```

Puis exposer les endpoints de surveillance dans le fichier `application.properties` avec 

```   
management.endpoints.web.exposure.include=prometheus,health,metrics
management.endpoint.prometheus.enabled=true
management.endpoint.health.show-details=always
```

Voir http://127.0.0.1:8080/actuator

## Métriques avec micrometer

https://micrometer.io/

Ajouter dans `build.gradle.kts`, section dependencies:
```
implementation("io.micrometer:micrometer-registry-prometheus")
```

Voir le code dans le BooksController
Voir les métriques dans http://127.0.0.1:8080/actuator/metrics
Voir l'export Prometheus dans http://127.0.0.1:8080/actuator/prometheus

Faire quelques requêtes avec Postman ou UI.

Voir les métriques/compteurs:

http_server_requests_seconds_count
org_inf5190_library_books_BooksController_get_seconds

Voir la gauge
jvm_memory_committed_bytes

## Prometheus

- Démarrer l'instance avec le fichier de config dans le dossier /prometheus

Exemple
```
./prometheus --config.file ../demo-tests-18/prometheus/prometheus.yml
```

- Voir http://localhost:9090/
- Faire quelques requêtes, puis voir les résultats.
- Exemple de requête prometheus

`http_server_requests_seconds_count{status="200", method="GET",uri=~"/books"}` (dernière valeur)
vs
`http_server_requests_seconds_count{status="200", method="GET",uri=~"/books"}[1m]` (sur la dernière minute, donc 4 valeurs)
vs
vue en mode graph

Puis calcule de taux de requête par seconde:
`rate(http_server_requests_seconds_count{status="200", method="GET",uri=~"/books"}[1m])`

Puis calcule de temps de réponse moyen
`rate(http_server_requests_seconds_sum{status="200", method="GET",uri=~"/books"}[1m]) / rate(http_server_requests_seconds_count{status="200", method="GET",uri=~"/books"}[1m])`

Calcule de percentile 
`histogram_quantile(0.99, sum(rate(org_inf5190_library_books_BooksController_get_seconds_bucket[1m])) by (le))`

Pourquoi le calcule suivant est mauvais:
`avg(org_inf5190_library_books_BooksController_post_seconds{quantile="0.95"})`

## Grafana

- exécuter : brew services start grafana 
- login avec le compte admin