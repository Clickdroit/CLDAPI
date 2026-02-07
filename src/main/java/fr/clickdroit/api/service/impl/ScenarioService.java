package fr.clickdroit.api.service.impl;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.event.ScenarioToggleEvent;
import fr.clickdroit.api.service.GameService;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service de gestion des scénarios UHC.
 */
public class ScenarioService implements GameService {

    private API api;
    private boolean initialized = false;
    private final Set<Scenario> enabledScenarios = ConcurrentHashMap.newKeySet();
    private final Map<Scenario, Integer> scenarioValues = new ConcurrentHashMap<>();

    @Override
    public void initialize(API api) {
        this.api = api;
        loadDefaultScenarios();
        this.initialized = true;
    }

    @Override
    public void shutdown() {
        enabledScenarios.clear();
        scenarioValues.clear();
        initialized = false;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public int getPriority() {
        return 60;
    }

    private void loadDefaultScenarios() {
        // Scénarios activés par défaut
        Scenario[] defaults = {
            Scenario.TIMBER,
            Scenario.CUTCLEAN,
            Scenario.HASTEYBOYS,
            Scenario.SAFEMINER,
            Scenario.CAT_EYES,
            Scenario.BETAZOMBIE
        };

        for (Scenario scenario : defaults) {
            enableScenarioSilent(scenario);
        }
    }

    // ===== Activation/Désactivation =====

    public boolean enableScenario(Scenario scenario) {
        ScenarioToggleEvent event = new ScenarioToggleEvent(scenario, true);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return false;
        }

        return enableScenarioSilent(scenario);
    }

    public boolean disableScenario(Scenario scenario) {
        ScenarioToggleEvent event = new ScenarioToggleEvent(scenario, false);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return false;
        }

        return disableScenarioSilent(scenario);
    }

    private boolean enableScenarioSilent(Scenario scenario) {
        if (enabledScenarios.add(scenario)) {
            scenario.getScenarioManager().onEnable();
            return true;
        }
        return false;
    }

    private boolean disableScenarioSilent(Scenario scenario) {
        if (enabledScenarios.remove(scenario)) {
            scenario.getScenarioManager().onDisable();
            return true;
        }
        return false;
    }

    public boolean toggleScenario(Scenario scenario) {
        if (isEnabled(scenario)) {
            return disableScenario(scenario);
        } else {
            return enableScenario(scenario);
        }
    }

    // ===== État des scénarios =====

    public boolean isEnabled(Scenario scenario) {
        return enabledScenarios.contains(scenario);
    }

    public Set<Scenario> getEnabledScenarios() {
        return Collections.unmodifiableSet(enabledScenarios);
    }

    public int getEnabledCount() {
        return enabledScenarios.size();
    }

    // ===== Valeurs des scénarios =====

    public void setScenarioValue(Scenario scenario, int value) {
        scenarioValues.put(scenario, value);
    }

    public int getScenarioValue(Scenario scenario) {
        return scenarioValues.getOrDefault(scenario, 0);
    }

    // ===== Utilitaires =====

    public void startAllScenarios() {
        for (Scenario scenario : enabledScenarios) {
            scenario.getScenarioManager().onStart();
        }
    }

    public void disableAllScenarios() {
        for (Scenario scenario : new HashSet<>(enabledScenarios)) {
            disableScenarioSilent(scenario);
        }
    }

    public List<Scenario> getEnabledScenariosList() {
        return new ArrayList<>(enabledScenarios);
    }

    public boolean hasTeamScenarios() {
        for (Scenario scenario : enabledScenarios) {
            if (scenario.isNeedTeams()) {
                return true;
            }
        }
        return false;
    }
}

