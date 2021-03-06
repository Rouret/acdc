# ACDC

## Fonctionnalités

* Connaitre les paramètres a envoyé pour un script connu

**GET /scripts/quicksort**
```json
    {
        "name": "quicksort",
        "params": {
            "generatorName": "ListNumericalGenerator",
            "arguments": [
                {
                    "name": "lengthNumberGenerator",
                    "type": "Integer"
                },
                {
                    "name": "lengthListGenerator",
                    "type": "Integer"
                }
            ]
        }
    }
```

* Connaitre tous les scripts disponibles
**GET /scripts**
```json
    {
        "name": "quicksort",
        "params": {
            "generatorName": "ListNumericalGenerator",
            "arguments": [
                {
                    "name": "lengthNumberGenerator",
                    "type": "Integer"
                },
                {
                    "name": "lengthListGenerator",
                    "type": "Integer"
                }
            ]
        }
    }
    ...
    {
        "name": "Fibonacci",
        "params": {
            "generatorName": "NumericalGenerator",
            "arguments": [
                {
                    "name": "lengthNumberGenerator",
                    "type": "Integer"
                }
            ]
        }
    }
```

* Connaitre le prix en gas en fonction des filtres donné

**GET scripts/Bubblesort/result?lengthNumberGenerator=1&lengthListGenerator=2&selected=lengthListGenerator&inc=1&numberOfLoop=5**
```json
    {
        "1": 22148,
        "2": 22558,
        "3": 24574,
        "4": 26699
    }
```


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

**routes** -> **controllers** <-> **services**

### generated

Classes générés par web3j

### generators

Possédent les classes qui gerents les generateus qui sont dans le dossier generators

### scripts

Possédent les classes qui gerents l'acces aux abi
               
# Doc

Les requetes postman sont dans le fichiers :**projet.postman_collection.json**

Le projet comporte 3 services:
* **GET /scripts**
* **GET /scripts/{scriptName}**
* **GET /scripts/{scriptName}/result**

Il n'y que le 3 services qui a besoin d'autre parametre.

PS: Tous les paramatres avec "lengthNumber" repressent le nombre de nombre, exemple:
*lengthNumber=2 => min=-99 et max=99*

# **GET /scripts/{scriptName}/result**

Parametres obligatoire:

nom | type |description 
--- | --- | --- 
inc | int | Nombre d'incrémentation
numberOfLoop | int  | Nombre de tests
selected | string  | Le parametre a faire varier


Pour appeler le 3eme service il faut s'appuyé de ce qui est donné dans **/scripts** ou **/scripts/{scriptName}**.

Exemple:

**GET /scripts/quicksort**
```json
    {
        "name": "quicksort",
        "params": {
            "generatorName": "ListNumericalGenerator",
            "arguments": [
                {
                    "name": "lengthNumberGenerator",
                    "type": "Integer"
                },
                {
                    "name": "lengthListGenerator",
                    "type": "Integer"
                }
            ]
        }
    }
```

Ici il est mentionné qu'il doit y avoir **lengthNumberGenerator** et **lengthListGenerator** qui doit etre donné.Imaginons que nous voulions faire varier la taille de la liste de 1 sur 10 tours on aura donc:

param | value 
--- | --- 
inc | 1  
numberOfLoop | 10  
selected | lengthListGenerator  
lengthNumberGenerator | 2
lengthListGenerator | 1
<br>

soit :
**GET /scripts/QuickSort/result?lengthNumberGenerator=2&lengthListGenerator=1&selected=lengthListGenerator&inc=1&numberOfLoop=10**