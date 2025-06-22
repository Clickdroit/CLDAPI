package fr.clickdroit.api.common.scoreboard;


import fr.minuskube.netherboard.bukkit.BPlayerBoard;

import java.util.UUID;

public interface ScoreboardContents {
    void reloadData(UUID paramUUID);

    void setLines(BPlayerBoard paramBPlayerBoard, UUID paramUUID, String paramString);
}

