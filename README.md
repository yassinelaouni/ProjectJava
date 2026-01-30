# Système de Gestion de Sécurité et d'Accréditation - CAN 2025

## Compilation et exécution du projet

1. **Ouvrez un terminal** et placez-vous dans le dossier `ProjectJava`.

2. **Compilez le projet** avec la commande suivante :

```
javac -d out -sourcepath src src/can2025/Main.java
```

3. **Exécutez le projet** avec la commande suivante :

```
java -cp out can2025.Main
```

## Structure du projet
- Le code source se trouve dans le dossier `src/can2025/`.
- Les classes principales sont :
  - `Main.java` : point d'entrée du programme
  - `services/SecurityService.java` : logique métier
  - `ui/ConsoleUI.java` : interface utilisateur en console
