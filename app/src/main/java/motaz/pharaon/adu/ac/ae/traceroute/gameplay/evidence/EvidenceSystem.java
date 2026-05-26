package motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EvidenceSystem {
    /*
     * Purpose:
     * - Owns evidence collection state for the current playthrough.
     *
     * Responsibilities:
     * - Track discovered evidence ids.
     * - Notify ThreadSystem/TimelineSystem when important evidence is collected.
     * - Provide evidence lists to handheld UI.
     * - Avoid SQLite details; persistence goes through GameRepository.
     *
     * Depends on:
     * - EvidenceItem.
     * - GameRepository for save/load.
     *
     * Used by:
     * - GameController.
     * - WorldInvestigationScene.
     * - HandheldFragment through HandheldController.
     */

    private final Map<String, EvidenceItem> evidenceDefinitions;
    private final Set<String> collectedEvidenceIds;

    public EvidenceSystem() {
        evidenceDefinitions = new LinkedHashMap<>();
        collectedEvidenceIds = new LinkedHashSet<>();
    }

    public synchronized void registerEvidence(EvidenceItem evidenceItem) {
        evidenceDefinitions.put(evidenceItem.getId(), evidenceItem);
    }

    public synchronized EvidenceItem collectEvidence(String evidenceId) {
        EvidenceItem evidenceItem = evidenceDefinitions.get(evidenceId);
        if (evidenceItem == null) {
            return null;
        }

        collectedEvidenceIds.add(evidenceId);
        return evidenceItem;
    }

    public synchronized EvidenceItem getEvidence(String evidenceId) {
        return evidenceDefinitions.get(evidenceId);
    }

    public synchronized boolean isCollected(String evidenceId) {
        return collectedEvidenceIds.contains(evidenceId);
    }

    public synchronized List<EvidenceItem> getCollectedEvidence() {
        List<EvidenceItem> collectedEvidence = new ArrayList<>();

        for (String evidenceId : collectedEvidenceIds) {
            EvidenceItem evidenceItem = evidenceDefinitions.get(evidenceId);
            if (evidenceItem != null) {
                collectedEvidence.add(evidenceItem);
            }
        }

        return collectedEvidence;
    }

    public synchronized List<String> getCollectedEvidenceIds() {
        return new ArrayList<>(collectedEvidenceIds);
    }

    public synchronized List<String> getAllEvidenceIds() {
        return new ArrayList<>(evidenceDefinitions.keySet());
    }
}
