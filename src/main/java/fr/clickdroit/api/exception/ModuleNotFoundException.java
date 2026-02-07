package fr.clickdroit.api.exception;

/**
 * Exception levée lorsqu'un module n'est pas trouvé.
 */
public class ModuleNotFoundException extends GameException {

    private final String moduleId;

    public ModuleNotFoundException(String moduleId) {
        super("Module non trouvé: " + moduleId);
        this.moduleId = moduleId;
    }

    public String getModuleId() {
        return moduleId;
    }
}

