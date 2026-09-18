package fr.clickdroit.api.game.cycle;

import fr.clickdroit.api.API;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CycleManager {
    private final World world;

    private final API api;

    private BukkitRunnable bukkitRunnable;

    private boolean start = false;
    private boolean isNight = false;

    public CycleManager(API api, World world) {
        this.api = api;
        this.world = world;
        this.world.setTime(0L);
        this.world.setGameRuleValue("doDaylightCycle", "false");
    }

    public void startDayCycle(long dayCycleDurationSeconds) {
        if (this.start)
            return;
        this.start = true;
        this.world.setGameRuleValue("doDaylightCycle", "false");
        this.world.setTime(0L);
        this.isNight = false;
        int a = 24000;
        long b = a / dayCycleDurationSeconds;
        final long c = b / 4L;
        this.bukkitRunnable = new BukkitRunnable() {
            public void run() {
                CycleManager.this.world.setTime(CycleManager.this.world.getTime() + c);
                long timeOfDay = CycleManager.this.world.getTime() % 24000L;
                boolean night = (timeOfDay >= 12000L && timeOfDay < 23800L);
                if (night && !CycleManager.this.isNight) {
                    CycleManager.this.isNight = true;
                    CycleManager.this.api.getModules().onNight(false);
                } else if (!night && CycleManager.this.isNight) {
                    CycleManager.this.isNight = false;
                    CycleManager.this.api.getModules().onDay(false);
                }
            }
        };
        this.bukkitRunnable.runTaskTimer((Plugin)this.api, 0L, 5L);
    }

    public void stop() {
        if (!this.start);
        this.start = false;
        this.world.setGameRuleValue("doDaylightCycle", "true");
        this.bukkitRunnable.cancel();
    }
}

