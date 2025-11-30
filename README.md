# CLDAPI - UHC Minecraft Plugin

Plugin Minecraft UHC (Ultra Hardcore) pour les serveurs Spigot 1.8.8.

## Description

CLDAPI est une API complète pour la création et la gestion de parties UHC (Ultra Hardcore) sur les serveurs Minecraft. Ce plugin offre une multitude de fonctionnalités pour personnaliser et gérer des parties compétitives.

## Fonctionnalités

- **Gestion des parties** : Système complet de démarrage, configuration et gestion des parties UHC
- **Système d'équipes** : Support des modes solo et en équipes
- **Scénarios** : Multiple scénarios configurables (CutClean, Timber, HasteyBoys, etc.)
- **Configuration flexible** : Interface GUI pour configurer les paramètres de jeu
- **Système de bordure** : Gestion automatique de la réduction de bordure
- **Combat Log** : Protection contre les déconnexions en combat
- **Prégénération de map** : Outil de prégénération pour optimiser les performances

## Prérequis

- Java 8 ou supérieur
- Serveur Spigot/Paper 1.8.8
- Gradle 8.x (pour la compilation)

## Installation

1. Clonez le repository :
   ```bash
   git clone https://github.com/Clickdroit/CLDAPI.git
   ```

2. Compilez le plugin :
   ```bash
   ./gradlew build
   ```

3. Copiez le fichier JAR généré depuis `build/libs/` vers le dossier `plugins` de votre serveur.

## Configuration

Le plugin se configure via l'interface en jeu avec la commande `/host config`.

## Commandes principales

| Commande | Description | Permission |
|----------|-------------|------------|
| `/host` | Menu de gestion de la partie | `uhc.host` |
| `/scenario` | Configuration des scénarios | `uhc.host` |
| `/whitelist` | Gestion de la whitelist | `uhc.host` |
| `/rules` | Afficher les règles | `uhc.player` |
| `/helpop` | Demander de l'aide | `uhc.player` |
| `/tc` | Coordonnées d'équipe | `uhc.player` |

## Permissions

- `uhc.host` - Accès aux commandes d'administration
- `uhc.staff` - Accès aux commandes staff
- `uhc.spectator` - Commandes spectateur
- `uhc.player` - Commandes joueur de base

## Structure du projet

```
src/main/java/fr/clickdroit/api/
├── API.java              # Classe principale du plugin
├── GamePlayer.java       # Gestion des joueurs en jeu
├── commands/             # Commandes du plugin
├── common/               # Utilitaires communs
├── config/               # Configuration et GUIs
├── game/                 # Logique de jeu
├── listener/             # Event listeners
├── module/               # Modules de jeu
├── utils/                # Classes utilitaires
└── worlds/               # Gestion des mondes
```

## Compilation

Le projet utilise Gradle pour la compilation. Les dépendances sont gérées automatiquement.

```bash
# Compilation simple
./gradlew build

# Compilation avec Shadow JAR (inclut les dépendances)
./gradlew shadowJar
```

## Licence

Ce projet est propriétaire. Tous droits réservés © Clickdroit.

## Auteur

- **Clickdroit** - Développeur principal
