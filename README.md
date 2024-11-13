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

1. Montrer les stats
2. Montrer un threaddump (rechercher pour AbstractApiFuture)
3. Éliminer le goulot
4. Montrer les stats

## Fuite de mémoire

1. Petit réchauffement
2. wrk + GC x 2
3. Voir la tendance de la heap
4. Heap dump

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

Voir Cloud Run (variable d'environnement)

Voir les Journaux

## Angular

Construire la version production (avec environment.prod.ts)

```
ng build
```

```
firebase deploy --only hosting
```
