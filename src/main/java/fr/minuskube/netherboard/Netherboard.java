package fr.minuskube.netherboard;

import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public class Netherboard {
    private static Netherboard instance;

    private final Map<Player, BPlayerBoard> boards = new HashMap<>();

    public BPlayerBoard createBoard(Player player, String name) {
        return createBoard(player, null, name);
    }

    public BPlayerBoard createBoard(Player player, Scoreboard scoreboard, String name) {
        deleteBoard(player);
        BPlayerBoard board = new BPlayerBoard(player, scoreboard, name);
        this.boards.put(player, board);
        return board;
    }

    public void deleteBoard(Player player) {
        if (this.boards.containsKey(player))
            ((BPlayerBoard)this.boards.get(player)).delete();
    }

    public void removeBoard(Player player) {
        this.boards.remove(player);
    }

    public boolean hasBoard(Player player) {
        return this.boards.containsKey(player);
    }

    public BPlayerBoard getBoard(Player player) {
        return this.boards.get(player);
    }

    public Map<Player, BPlayerBoard> getBoards() {
        return new HashMap<>(this.boards);
    }

    public static Netherboard instance() {
        if (instance == null)
            instance = new Netherboard();
        return instance;
    }
}
