package fr.clickdroit.api.common.scoreboard.blink;

/**
 * Effet de titre animé pour "SOLO LEVELING UHC"
 * Vague violet de 3 caractères se déplaçant de droite à gauche
 * Avec système de cooldown pour éviter les mises à jour trop fréquentes
 */
public class AnimatedTitleEffect {
    private int position = 0;
    private final String baseText = "SOLO LEVELING UHC";
    private final String baseColor = "§d§l"; // Rose/Magenta Bold
    private final String waveColor = "§5§l"; // Violet Bold
    private final int waveLength = 3; // Longueur de la vague

    // Système de cooldown
    private long lastUpdate = 0;
    private final long cooldownMs; // Cooldown en millisecondes
    private String cachedText; // Cache du texte pour éviter de recalculer
    private boolean textChanged = false; // Flag pour savoir si le texte a changé

    /**
     * Constructeur avec cooldown par défaut de 100ms (10 fois par seconde)
     */
    public AnimatedTitleEffect() {
        this(100); // 100ms par défaut
    }

    /**
     * Constructeur avec cooldown personnalisé
     * @param cooldownMs Cooldown en millisecondes entre les mises à jour
     */
    public AnimatedTitleEffect(long cooldownMs) {
        this.cooldownMs = cooldownMs;
        this.cachedText = buildText(); // Initialiser le cache
    }

    /**
     * Met à jour l'animation si le cooldown est écoulé
     * @return true si l'animation a été mise à jour, false sinon
     */
    public boolean next() {
        long currentTime = System.currentTimeMillis();

        // Vérifier si le cooldown est écoulé
        if (currentTime - lastUpdate < cooldownMs) {
            return false; // Pas de mise à jour
        }

        // Mettre à jour l'animation
        lastUpdate = currentTime;
        position++;

        // Quand la vague est complètement sortie à gauche, recommencer à droite
        if (position > baseText.length() + waveLength) {
            position = 0;
        }

        // Mettre à jour le cache et marquer comme changé
        String newText = buildText();
        if (!newText.equals(cachedText)) {
            cachedText = newText;
            textChanged = true;
        }

        return true; // Animation mise à jour
    }

    /**
     * Obtient le texte animé (utilise le cache)
     * @return Le texte avec l'animation actuelle
     */
    public String getText() {
        // Si aucune mise à jour n'a eu lieu, utiliser le cache
        if (cachedText == null) {
            cachedText = buildText();
        }
        return cachedText;
    }

    /**
     * Vérifie si le texte a changé depuis la dernière fois
     * Utile pour éviter des mises à jour inutiles du scoreboard
     * @return true si le texte a changé
     */
    public boolean hasChanged() {
        boolean changed = textChanged;
        textChanged = false; // Reset le flag après lecture
        return changed;
    }

    /**
     * Force une mise à jour immédiate (ignore le cooldown)
     * @return Le nouveau texte
     */
    public String forceNext() {
        position++;
        if (position > baseText.length() + waveLength) {
            position = 0;
        }
        cachedText = buildText();
        textChanged = true;
        lastUpdate = System.currentTimeMillis();
        return cachedText;
    }

    /**
     * Construit le texte avec l'animation
     */
    private String buildText() {
        StringBuilder result = new StringBuilder();

        // Construire le texte avec la vague violet
        for (int i = 0; i < baseText.length(); i++) {
            char currentChar = baseText.charAt(i);

            // Calculer si ce caractère fait partie de la vague
            int waveStart = position - waveLength;
            int waveEnd = position;

            // Si le caractère est dans la zone de la vague violet
            if (i >= waveStart && i < waveEnd) {
                result.append(waveColor).append(currentChar);
            } else {
                result.append(baseColor).append(currentChar);
            }
        }

        return result.toString();
    }

    /**
     * Reset l'animation au début
     */
    public void reset() {
        this.position = 0;
        this.cachedText = buildText();
        this.textChanged = true;
        this.lastUpdate = System.currentTimeMillis();
    }

    /**
     * Obtient la position actuelle de la vague (pour debug)
     */
    public int getPosition() {
        return position;
    }

    /**
     * Obtient le cooldown configuré
     */
    public long getCooldown() {
        return cooldownMs;
    }

    /**
     * Vérifie si l'animation est prête pour une mise à jour
     */
    public boolean canUpdate() {
        return (System.currentTimeMillis() - lastUpdate) >= cooldownMs;
    }

    /**
     * Obtient le temps restant avant la prochaine mise à jour possible
     */
    public long getTimeUntilNextUpdate() {
        long elapsed = System.currentTimeMillis() - lastUpdate;
        return Math.max(0, cooldownMs - elapsed);
    }
}