package fr.clickdroit.api.utils.role;

public interface Use {
    boolean canUse(String... paramVarArgs);

    void use(String... paramVarArgs);
}
