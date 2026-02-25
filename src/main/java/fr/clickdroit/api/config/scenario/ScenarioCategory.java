package fr.clickdroit.api.config.scenario;

public enum ScenarioCategory {
    ALL("Tous"),
    PVP("PvP & Combat"),
    WORLD("Monde & Ressources"),
    SURVIVAL("Survie"),
    TEAM("Équipes & Alliés"),
    MISC("Divers");

    private final String displayName;

    ScenarioCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ScenarioCategory getNext(ScenarioCategory current) {
        ScenarioCategory[] vals = values();
        return vals[(current.ordinal() + 1) % vals.length];
    }

    public static ScenarioCategory getPrevious(ScenarioCategory current) {
        ScenarioCategory[] vals = values();
        int index = current.ordinal() - 1;
        if (index < 0) {
            index = vals.length - 1;
        }
        return vals[index];
    }
}
