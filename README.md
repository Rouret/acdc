# ACDC

## Fonctionnalités

<!-- Les différentes fonctionnalités développées  -->

## Lancer l'environnement de développement

Sur linux:
```bash
 ./start
```

Sur windows:
```bash
 ./gradlew --console=plain clean build run 
```


## Environnement de développement

* Gradle 5.2.1
* openjdk version "11.0.9.1"
 
## Structure du projet

Les **scripts .sol** sont dans les ressources

* src.main.java.fr.rouret
  * api
    * controllers
    * routes
    * services
  * generated
  * generators
    * generators 
  * scripts

### api

Contient les différents élements de l'api controllers,services et routes

Le déroulement logique est:

routes -> controllers <-> services

### generated
### generators

Possédent les classes qui gerents les generateus qui sont dans le dossier generators

### scripts

Possédent les classes qui gerents l'acces aux abi
               