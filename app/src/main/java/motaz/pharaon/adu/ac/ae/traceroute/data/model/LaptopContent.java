package motaz.pharaon.adu.ac.ae.traceroute.data.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LaptopContent {
    /*
     * Purpose:
     * - Authored content for the victim laptop: its title, starting screen lines, and panels.
     *
     * Responsibilities:
     * - Provide the panels the player can open from the laptop's buttons.
     * - Stay authored data only; there are no typed commands to interpret.
     */

    private final String title;
    private final List<String> introLines;
    private final List<LaptopPanel> panels;

    public LaptopContent(String title, List<String> introLines, List<LaptopPanel> panels) {
        this.title = title;
        this.introLines = immutableCopy(introLines);
        this.panels = immutableCopy(panels);
    }

    public static LaptopContent empty() {
        return new LaptopContent(
                "VICTIM LAPTOP",
                Collections.singletonList("No laptop content is available."),
                Collections.emptyList()
        );
    }

    public LaptopPanel findPanel(String panelId) {
        for (LaptopPanel panel : panels) {
            if (panel.matches(panelId)) {
                return panel;
            }
        }

        return null;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getIntroLines() {
        return introLines;
    }

    public List<LaptopPanel> getPanels() {
        return panels;
    }

    private static <T> List<T> immutableCopy(List<T> values) {
        if (values == null || values.isEmpty()) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableList(new ArrayList<>(values));
    }
}
