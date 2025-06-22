package fr.clickdroit.api.utils;

public enum CommonString {
    CLICK_HERE_TO_APPLY(" pour "),
    CLICK_HERE_TO_ACTIVATE(" pour "),
    CLICK_HERE_TO_DESACTIVATE(" pour "),
    CLICK_HERE_TO_MODIFY(" pour "),
    CLICK_HERE_TO_ACCESS(" pour y "),
    BAR("                                                                          "),
    NO_PERMISSION("sorry but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");

    private String message;

    CommonString(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
