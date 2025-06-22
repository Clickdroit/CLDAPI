package fr.clickdroit.api.game.teleportation.player;


import fr.clickdroit.api.game.teleportation.plate.Plate;

public interface PlayerPlate {
    void assignPlate(Plate paramPlate);

    boolean removePlate();

    String getName();
}

