[![shields](https://img.shields.io/badge/project%20status-validated-deepgreen)](https://shields.io/)
[![shields](https://img.shields.io/badge/made%20with-java-orange)](https://shields.io/)
[![shields](https://img.shields.io/badge/powered%20by-spring-green)](https://shields.io/)
____________________

> Ce README est basé sur les conclusions évoquées dans la présentation réalisée à la fin du projet.

# Améliorez votre application avec des systèmes distribués

## Dans le cadre de la formation OpenClassrooms "Développeur d'application Java"

### Objectif du projet
À partir d'un projet existant, optimiser les performances de l'application notamment par la création de systèmes distribués, ajouter des tests et des fonctionnalités.

### Progression
Ce projet a été l'opportunité de découvrir de nouvelles technologies fondamentales, telles que Gradle, Docker et Docker compose, ou encore les frameworks de concurrence de Java. Ce dernier point a été le plus délicat durant le projet, avec l'exploration des différentes options d'optimisation.

### Réalisation
J'ai obtenu des résultats probants sur l'optimisation avec les technologies requises, tout en essayant d'aller plus loin encore. 
Notamment :
* Optimisation du code (Tracker.java par exemple)
* Utilisation de Docker compose pour gérer l'ensemble de l'application
* Implémentation de controllers sur les différents systèmes pour une communication HTTP
* Recherche de la meilleure performance et non du minimum demandé (Voir Annexes)
	* Première tentative avec les ThreadPools, qui remplissaient déjà les objectifs fixés
	* Amélioration avec les CompletableFuture dont la performance est nettement plus élevée
* Création d'un module "common" supplémentaire, pour le partage des modèles
* _build.gradle_ centralisé

### Axes d'améliorations
* Les performances HTTP pourraient être améliorées avec un meilleur paramètrage de Tomcat, ou l'utilisation d’un load balancer
* Des tests d’intégration supplémentaires pourraient être ajoutés, mais aussi et surtout intégré dans le cycle de Gradle, avec le lancement et l'arrêt automatique des containers Docker appropriés.

### Annexes

* Rouge = Pas de concurrence
* Vert = ThreadPools
* Bleu = CompletableFuture
* Violet = CompletableFuture (version HTTP)

#### Rewards performance
![rewards_performance](https://github.com/Thibeylard/TourGuideApp/blob/master/img/rewards_performance.png)

#### Track performance
![track_performance](https://github.com/Thibeylard/TourGuideApp/blob/master/img/track_performance.png)