package motaz.pharaon.adu.ac.ae.traceroute.data.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LaptopPanel {
    /*
     * Purpose:
     * - One authored panel (app) on the victim laptop, opened by tapping its button.
     *
     * Responsibilities:
     * - Hold the panel id, button label, linked evidence, and readable content lines.
     * - Stay authored data only; the laptop has no command line and no shell.
     */

    private final String panelId;
    private final String label;
    private final String evidenceId;
    private final List<String> contentLines;

    public LaptopPanel(
            String panelId,
            String label,
            String evidenceId,
            List<String> contentLines
    ) {
        this.panelId = normalize(panelId);
        this.label = label;
        this.evidenceId = evidenceId;
        this.contentLines = immutableCopy(contentLines);
    }

    public boolean matches(String candidate) {
        return panelId.equals(normalize(candidate));
    }

    public String getPanelId() {
        return panelId;
    }

    public String getLabel() {
        return label;
    }

    public String getEvidenceId() {
        return evidenceId;
    }

    public List<String> getContentLines() {
        return contentLines;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.US);
    }

    private static List<String> immutableCopy(List<String> values) {
        if (values == null || values.isEmpty()) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableList(new ArrayList<>(values));
    }
}
