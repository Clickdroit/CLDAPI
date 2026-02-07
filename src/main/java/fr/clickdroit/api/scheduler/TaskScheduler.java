package fr.clickdroit.api.scheduler;

import fr.clickdroit.api.API;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Gestionnaire centralisé des tâches planifiées.
 * Permet de créer, suivre et annuler facilement des tâches.
 */
public class TaskScheduler {

    private final API api;
    private final Map<String, BukkitTask> namedTasks = new ConcurrentHashMap<>();

    public TaskScheduler(API api) {
        this.api = api;
    }

    // ===== Tâches synchrones =====

    /**
     * Exécute une tâche immédiatement sur le thread principal.
     */
    public BukkitTask run(Runnable task) {
        return Bukkit.getScheduler().runTask(api, task);
    }

    /**
     * Exécute une tâche après un délai.
     *
     * @param task la tâche
     * @param delayTicks le délai en ticks (20 ticks = 1 seconde)
     */
    public BukkitTask runLater(Runnable task, long delayTicks) {
        return Bukkit.getScheduler().runTaskLater(api, task, delayTicks);
    }

    /**
     * Exécute une tâche après un délai en secondes.
     */
    public BukkitTask runLaterSeconds(Runnable task, int delaySeconds) {
        return runLater(task, delaySeconds * 20L);
    }

    /**
     * Exécute une tâche répétée.
     *
     * @param task la tâche
     * @param delayTicks le délai initial
     * @param periodTicks la période entre chaque exécution
     */
    public BukkitTask runTimer(Runnable task, long delayTicks, long periodTicks) {
        return Bukkit.getScheduler().runTaskTimer(api, task, delayTicks, periodTicks);
    }

    /**
     * Exécute une tâche répétée avec un nom pour pouvoir l'annuler.
     */
    public BukkitTask runNamedTimer(String name, Runnable task, long delayTicks, long periodTicks) {
        cancelNamed(name);
        BukkitTask bukkitTask = runTimer(task, delayTicks, periodTicks);
        namedTasks.put(name, bukkitTask);
        return bukkitTask;
    }

    // ===== Tâches asynchrones =====

    /**
     * Exécute une tâche de manière asynchrone.
     */
    public BukkitTask runAsync(Runnable task) {
        return Bukkit.getScheduler().runTaskAsynchronously(api, task);
    }

    /**
     * Exécute une tâche asynchrone après un délai.
     */
    public BukkitTask runAsyncLater(Runnable task, long delayTicks) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(api, task, delayTicks);
    }

    /**
     * Exécute une tâche asynchrone répétée.
     */
    public BukkitTask runAsyncTimer(Runnable task, long delayTicks, long periodTicks) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(api, task, delayTicks, periodTicks);
    }

    // ===== Countdown =====

    /**
     * Crée un compte à rebours.
     *
     * @param name le nom du countdown
     * @param seconds la durée en secondes
     * @param onTick appelé à chaque seconde (reçoit le temps restant)
     * @param onComplete appelé à la fin
     */
    public void countdown(String name, int seconds, Consumer<Integer> onTick, Runnable onComplete) {
        cancelNamed(name);

        BukkitTask task = new BukkitRunnable() {
            int remaining = seconds;

            @Override
            public void run() {
                if (remaining <= 0) {
                    cancel();
                    namedTasks.remove(name);
                    if (onComplete != null) {
                        onComplete.run();
                    }
                } else {
                    if (onTick != null) {
                        onTick.accept(remaining);
                    }
                    remaining--;
                }
            }
        }.runTaskTimer(api, 0L, 20L);

        namedTasks.put(name, task);
    }

    // ===== Gestion des tâches nommées =====

    /**
     * Annule une tâche nommée.
     */
    public boolean cancelNamed(String name) {
        BukkitTask task = namedTasks.remove(name);
        if (task != null) {
            task.cancel();
            return true;
        }
        return false;
    }

    /**
     * Vérifie si une tâche nommée est en cours.
     */
    public boolean isRunning(String name) {
        BukkitTask task = namedTasks.get(name);
        return task != null && Bukkit.getScheduler().isCurrentlyRunning(task.getTaskId());
    }

    /**
     * Annule toutes les tâches nommées.
     */
    public void cancelAll() {
        for (BukkitTask task : namedTasks.values()) {
            task.cancel();
        }
        namedTasks.clear();
    }

    /**
     * Retourne le nombre de tâches nommées actives.
     */
    public int getActiveTaskCount() {
        return namedTasks.size();
    }
}

