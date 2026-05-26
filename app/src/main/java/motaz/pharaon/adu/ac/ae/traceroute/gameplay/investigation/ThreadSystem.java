package motaz.pharaon.adu.ac.ae.traceroute.gameplay.investigation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceSystem;

public class ThreadSystem {
    /*
     * Purpose:
     * - Owns investigation thread progress for the current playthrough.
     *
     * Responsibilities:
     * - Unlock thread observations when relevant evidence is collected.
     * - Mark threads stabilized when their required clue chain is complete.
     * - Provide readable thread state to the handheld UI.
     *
     * Depends on:
     * - InvestigationThread data.
     * - EvidenceSystem events or GameController notifications.
     * - GameRepository for save/load.
     *
     * Used by:
     * - GameController.
     * - HandheldFragment through HandheldController.
     */

    private static final String DEATH_WINDOW_THREAD_ID = "T01_DEATH_WINDOW_INSTABILITY";
    private static final String POSTMORTEM_ACTIVITY_THREAD_ID = "T02_POSTMORTEM_ACTIVITY";
    private static final String APARTMENT_ACCESS_THREAD_ID = "T03_APARTMENT_ACCESS";
    private static final String DATA_SALE_MOTIVE_THREAD_ID = "T04_DATA_SALE_MOTIVE";
    private static final String SCENE_DISTURBANCE_THREAD_ID = "T05_SCENE_DISTURBANCE";
    private static final String BODY_REPORT_ID = "E01_BODY_CONDITION_REPORT";
    private static final String AC_LOG_ID = "E02_THERMOSTAT_AC_LOG";
    private static final String BROKEN_VASE_ID = "E03_BROKEN_VASE_DISTURBED_TABLE";
    private static final String COMPANY_AWARD_ID = "E04_BLOOD_MARKED_COMPANY_AWARD";
    private static final String SMART_LOCK_LOG_ID = "E05_SMART_LOCK_ENTRY_EXIT_LOG";
    private static final String SCHEDULED_MESSAGE_ID = "E06_SCHEDULED_VICTIM_MESSAGE";
    private static final String LAPTOP_METADATA_ID = "E07_LAPTOP_SESSION_METADATA";
    private static final String ROUTER_ADMIN_ACCESS_ID = "E08_ROUTER_ADMIN_ACCESS_RECORD";
    private static final String ACCESS_DATA_SALE_FILE_ID = "E12_ACCESS_DATA_SALE_FILE";
    private static final String DRAFT_DISCLOSURE_EMAIL_ID = "E13_VICTIM_DRAFT_DISCLOSURE_EMAIL";

    private final Map<String, InvestigationThread> threads;

    public ThreadSystem() {
        threads = new LinkedHashMap<>();
    }

    public void registerThread(InvestigationThread investigationThread) {
        threads.put(investigationThread.getId(), investigationThread);
    }

    public InvestigationThread getThread(String threadId) {
        return threads.get(threadId);
    }

    public void onEvidenceCollected(String evidenceId, EvidenceSystem evidenceSystem) {
        InvestigationThread deathWindowThread = threads.get(DEATH_WINDOW_THREAD_ID);
        if (deathWindowThread == null) {
            updatePostmortemActivityThread(evidenceId);
            return;
        }

        if (BODY_REPORT_ID.equals(evidenceId)) {
            deathWindowThread.addObservation(
                    "Body condition weakens the assumption that the victim was alive after 21:00."
            );
        } else if (AC_LOG_ID.equals(evidenceId)) {
            deathWindowThread.addObservation(
                    "Thermostat activity shows the apartment was cooled during the suspicious window."
            );
        }

        if (evidenceSystem.isCollected(BODY_REPORT_ID) && evidenceSystem.isCollected(AC_LOG_ID)) {
            deathWindowThread.setStabilized(true);
        }

        updatePostmortemActivityThread(evidenceId);
        updateApartmentAccessThread(evidenceId, evidenceSystem);
        updateDataSaleMotiveThread(evidenceId, evidenceSystem);
        updateSceneDisturbanceThread(evidenceId, evidenceSystem);
    }

    public List<InvestigationThread> getInvestigationThreads() {
        List<InvestigationThread> visibleThreads = new ArrayList<>();

        for (InvestigationThread investigationThread : threads.values()) {
            if (!investigationThread.getObservations().isEmpty() || investigationThread.isStabilized()) {
                visibleThreads.add(investigationThread);
            }
        }

        return visibleThreads;
    }

    private void updatePostmortemActivityThread(String evidenceId) {
        InvestigationThread postmortemActivityThread = threads.get(POSTMORTEM_ACTIVITY_THREAD_ID);
        if (postmortemActivityThread == null) {
            return;
        }

        if (SCHEDULED_MESSAGE_ID.equals(evidenceId)) {
            postmortemActivityThread.addObservation(
                    "The 21:07 message was scheduled, so it cannot prove the victim was alive when it sent."
            );
        } else if (LAPTOP_METADATA_ID.equals(evidenceId)) {
            postmortemActivityThread.addObservation(
                    "Laptop activity after 21:00 follows an automated pattern rather than normal manual use."
            );
        }

        if (!postmortemActivityThread.getObservations().isEmpty()
                && postmortemActivityThread.getObservations().size() >= 2) {
            postmortemActivityThread.setStabilized(true);
        }
    }

    private void updateApartmentAccessThread(String evidenceId, EvidenceSystem evidenceSystem) {
        InvestigationThread apartmentAccessThread = threads.get(APARTMENT_ACCESS_THREAD_ID);
        if (apartmentAccessThread == null) {
            return;
        }

        if (SMART_LOCK_LOG_ID.equals(evidenceId)) {
            apartmentAccessThread.addObservation(
                    "Smart-lock records place the partner inside from 19:05 to 19:42."
            );
        } else if (ROUTER_ADMIN_ACCESS_ID.equals(evidenceId)) {
            apartmentAccessThread.addObservation(
                    "Router records show privileged technical access during the staging period."
            );
        }

        if (evidenceSystem.isCollected(SMART_LOCK_LOG_ID)
                && evidenceSystem.isCollected(ROUTER_ADMIN_ACCESS_ID)) {
            apartmentAccessThread.setStabilized(true);
        }
    }

    private void updateDataSaleMotiveThread(String evidenceId, EvidenceSystem evidenceSystem) {
        InvestigationThread dataSaleMotiveThread = threads.get(DATA_SALE_MOTIVE_THREAD_ID);
        if (dataSaleMotiveThread == null) {
            return;
        }

        if (ACCESS_DATA_SALE_FILE_ID.equals(evidenceId)) {
            dataSaleMotiveThread.addObservation(
                    "Access-data sale records establish the illegal operation behind the partnership."
            );
        } else if (DRAFT_DISCLOSURE_EMAIL_ID.equals(evidenceId)) {
            dataSaleMotiveThread.addObservation(
                    "The victim drafted a disclosure that would expose the partner and the operation."
            );
        }

        if (evidenceSystem.isCollected(ACCESS_DATA_SALE_FILE_ID)
                && evidenceSystem.isCollected(DRAFT_DISCLOSURE_EMAIL_ID)) {
            dataSaleMotiveThread.setStabilized(true);
        }
    }

    private void updateSceneDisturbanceThread(String evidenceId, EvidenceSystem evidenceSystem) {
        InvestigationThread sceneDisturbanceThread = threads.get(SCENE_DISTURBANCE_THREAD_ID);
        if (sceneDisturbanceThread == null) {
            return;
        }

        if (BODY_REPORT_ID.equals(evidenceId)) {
            sceneDisturbanceThread.addObservation(
                    "The body scene anchors the violence in the living room."
            );
        } else if (BROKEN_VASE_ID.equals(evidenceId)) {
            sceneDisturbanceThread.addObservation(
                    "The broken vase and disturbed table point to a short localized struggle."
            );
        } else if (COMPANY_AWARD_ID.equals(evidenceId)) {
            sceneDisturbanceThread.addObservation(
                    "The blood-marked company award is likely the murder weapon."
            );
        }

        if (evidenceSystem.isCollected(BROKEN_VASE_ID)
                && evidenceSystem.isCollected(COMPANY_AWARD_ID)) {
            sceneDisturbanceThread.setStabilized(true);
        }
    }
}
