package fr.clickdroit.api.utils;

public enum CommonString {
    CLICK_HERE_TO_APPLY("§8 -> §fCliquez pour §aappliquer§f."),
    CLICK_HERE_TO_ACTIVATE("§8 -> §fCliquez pour §aactiver§f."),
    CLICK_HERE_TO_DESACTIVATE("§8 -> §fCliquez pour §cdésactiver§f."),
    CLICK_HERE_TO_MODIFY("§8 -> §fCliquez pour §6modifier§f."),
    CLICK_HERE_TO_ACCESS("§8 -> §fCliquez pour y §caccéder§f."),
    BAR("§f§m                                                                           §r"),
    NO_PERMISSION("§cI'm sorry but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");

    private String message;

    CommonString(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
