package motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence;

public class EvidenceItem {
    /*
     * Purpose:
     * - Runtime representation of an evidence item the player can collect or inspect.
     *
     * Responsibilities:
     * - Identify evidence by stable id such as E01_BODY_CONDITION.
     * - Store player-facing summary, category, and linked thread ids.
     * - Avoid owning UI rendering or database writes directly.
     *
     * Depends on:
     * - Static content loaded by DataLoader or hardcoded MVP content.
     *
     * Used by:
     * - EvidenceSystem.
     * - Handheld evidence screen.
     * - GameRepository for persistence.
     */

    private final String id;
    private final String title;
    private final String category;
    private final String source;
    private final String playerSummary;
    private final String technicalNote;

    public EvidenceItem(
            String id,
            String title,
            String category,
            String source,
            String playerSummary,
            String technicalNote
    ) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.source = source;
        this.playerSummary = playerSummary;
        this.technicalNote = technicalNote;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getSource() {
        return source;
    }

    public String getPlayerSummary() {
        return playerSummary;
    }

    public String getTechnicalNote() {
        return technicalNote;
    }
}
