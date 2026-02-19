package net.alloymc.mod.griefprevention.claim;

/**
 * Per-claim boolean flags that control what untrusted (public) players can do.
 * When a flag is enabled, the corresponding interaction is allowed for ALL players,
 * even those without explicit trust. Trusted players' permissions are unaffected.
 */
public enum ClaimFlag {

    BUTTONS("buttons", "Allow everyone to use buttons and levers", false),
    DOORS("doors", "Allow everyone to use doors, trapdoors, and fence gates", false),
    CONTAINERS("containers", "Allow everyone to open chests, barrels, furnaces, etc.", false),
    INTERACT("interact", "Allow everyone to use beds, cake, and lecterns", false),
    EXPLOSIONS("explosions", "Allow TNT and creeper explosions to damage blocks", false);

    private final String key;
    private final String description;
    private final boolean defaultValue;

    ClaimFlag(String key, String description, boolean defaultValue) {
        this.key = key;
        this.description = description;
        this.defaultValue = defaultValue;
    }

    public String key() { return key; }
    public String description() { return description; }
    public boolean defaultValue() { return defaultValue; }

    /**
     * Looks up a ClaimFlag by its key string (case-insensitive).
     * Returns null if no matching flag is found.
     */
    public static ClaimFlag fromKey(String key) {
        for (ClaimFlag flag : values()) {
            if (flag.key.equalsIgnoreCase(key)) return flag;
        }
        return null;
    }
}
