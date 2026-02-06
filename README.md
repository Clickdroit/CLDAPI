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
- **Modules externes** : Système d'intégration pour les modes de jeu personnalisés

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
│   ├── GameModule.java       # Interface pour les modules externes
│   ├── GameModuleRegistry.java # Registre des modules
│   └── GameModuleAdapter.java  # Adaptateur interne
├── utils/                # Classes utilitaires
└── worlds/               # Gestion des mondes
```

## Intégration de Modules Externes (Modes de Jeu)

L'API permet aux plugins externes d'enregistrer leurs propres modes de jeu. Voici comment créer et intégrer un module personnalisé.

### 1. Configuration du plugin externe

**plugin.yml** de votre plugin externe :
```yaml
name: MonModeDeJeu
main: com.example.MonPlugin
version: 1.0
depend: [UHCAPI]
```

### 2. Dépendance dans build.gradle

```groovy
dependencies {
    compileOnly files('libs/UHCAPI.jar')
    // ou utiliser un repository Maven si disponible
}
```

### 3. Créer votre module de jeu

```java
package com.example.module;

import fr.clickdroit.api.API;
import fr.clickdroit.api.module.GameModule;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class MonModule implements GameModule {
    private final JavaPlugin plugin;
    
    public MonModule(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getId() {
        return "MON_MODE";  // Identifiant unique
    }
    
    @Override
    public String getDisplayName() {
        return "Mon Mode de Jeu";
    }
    
    @Override
    public String getColor() {
        return "§6";  // Orange
    }
    
    @Override
    public Material getIconMaterial() {
        return Material.DIAMOND_SWORD;
    }
    
    @Override
    public boolean hasRoles() {
        return true;  // true si votre mode a des rôles
    }
    
    @Override
    public boolean hasTeams() {
        return false; // false pour solo, true pour équipes
    }
    
    @Override
    public JavaPlugin getOwnerPlugin() {
        return plugin;
    }
    
    @Override
    public String[] getDescription() {
        return new String[] {
            "Description de votre mode",
            "sur plusieurs lignes"
        };
    }
    
    @Override
    public void onLoad() {
        plugin.getLogger().info("Module chargé !");
    }
    
    @Override
    public void onEnable(API api) {
        plugin.getLogger().info("Module activé !");
        // Initialiser vos listeners, etc.
    }
    
    @Override
    public void onDisable(API api) {
        plugin.getLogger().info("Module désactivé !");
    }
    
    @Override
    public void onGameStart(API api) {
        // Appelé au démarrage de la partie
    }
    
    @Override
    public void onPlayerDeath(Player player, Player killer) {
        // Gérer la mort d'un joueur
    }
    
    @Override
    public void onPlayerDeathByDisconnect(UUID uuid) {
        // Gérer la mort par déconnexion
    }
    
    @Override
    public void onEpisodeSwitch() {
        // Appelé à chaque changement d'épisode
    }
    
    @Override
    public void onClockUpdate(int gameTime) {
        // Appelé chaque seconde
    }
    
    @Override
    public void onPlayerReconnect(Player player) {
        // Gérer la reconnexion
    }
    
    @Override
    public void onPlayerDisconnect(Player player) {
        // Gérer la déconnexion
    }
    
    @Override
    public void onPlayerChat(Player player, String message) {
        // Gérer le chat
    }
    
    @Override
    public void openConfig(Player player) {
        // Ouvrir votre GUI de configuration
        player.sendMessage("§aOuverture de la config...");
    }
}
```

### 4. Enregistrer le module dans votre plugin principal

```java
package com.example;

import fr.clickdroit.api.API;
import com.example.module.MonModule;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MonPlugin extends JavaPlugin {
    
    @Override
    public void onEnable() {
        // Attendre que CLDAPI soit chargé
        if (Bukkit.getPluginManager().getPlugin("UHCAPI") == null) {
            getLogger().severe("UHCAPI non trouvé ! Le plugin va se désactiver.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        
        // Récupérer l'API
        API api = API.getAPI();
        if (api == null) {
            getLogger().severe("Impossible de récupérer l'API UHCAPI !");
            return;
        }
        
        // Enregistrer votre module
        MonModule module = new MonModule(this);
        boolean success = api.getModuleRegistry().registerModule(module);
        
        if (success) {
            getLogger().info("Module enregistré avec succès !");
        } else {
            getLogger().warning("Échec de l'enregistrement du module.");
        }
    }
    
    @Override
    public void onDisable() {
        // Les modules sont automatiquement désenregistrés
        // mais vous pouvez le faire manuellement si nécessaire
        API api = API.getAPI();
        if (api != null) {
            api.getModuleRegistry().unregisterModules(this);
        }
    }
}
```

### 5. Utilisation dans le jeu

Une fois votre plugin installé :
1. Placez le JAR de CLDAPI dans le dossier `plugins`
2. Placez le JAR de votre plugin dans le dossier `plugins`
3. Redémarrez le serveur
4. Utilisez `/host` puis cliquez sur l'étoile du Nether "Sélection du mode de jeu"
5. Votre mode apparaîtra dans la liste !

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
