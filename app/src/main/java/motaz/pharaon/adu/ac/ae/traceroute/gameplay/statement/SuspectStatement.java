package motaz.pharaon.adu.ac.ae.traceroute.gameplay.statement;

public class SuspectStatement {
    /*
     * Purpose:
     * - Data model for a short suspect statement.
     *
     * Responsibilities:
     * - Store speaker, claim text, unlock condition, contradiction evidence, and resolution text later.
     * - Keep statements lightweight to avoid branching interview scope.
     *
     * Depends on:
     * - Static content from DataLoader.
     *
     * Used by:
     * - StatementSystem.
     * - HandheldFragment through HandheldController.
     */

    private final String id;
    private final String speaker;
    private final String claim;
    private final String suspiciousDetail;
    private final String contradictionText;
    private boolean visible;
    private boolean contradicted;

    public SuspectStatement(
            String id,
            String speaker,
            String claim,
            String suspiciousDetail,
            String contradictionText
    ) {
        this.id = id;
        this.speaker = speaker;
        this.claim = claim;
        this.suspiciousDetail = suspiciousDetail;
        this.contradictionText = contradictionText;
        visible = false;
        contradicted = false;
    }

    public String getId() {
        return id;
    }

    public String getSpeaker() {
        return speaker;
    }

    public String getClaim() {
        return claim;
    }

    public String getSuspiciousDetail() {
        return suspiciousDetail;
    }

    public String getContradictionText() {
        return contradictionText;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isContradicted() {
        return contradicted;
    }

    public void setContradicted(boolean contradicted) {
        this.contradicted = contradicted;
    }
}
