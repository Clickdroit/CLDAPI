package fr.clickdroit.api.common.rules;

import fr.clickdroit.api.API;

public interface Rule {
    default void onLoad(API main) {}
}
