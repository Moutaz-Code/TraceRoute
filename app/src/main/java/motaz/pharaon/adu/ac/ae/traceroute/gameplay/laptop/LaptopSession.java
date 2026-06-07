package motaz.pharaon.adu.ac.ae.traceroute.gameplay.laptop;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import motaz.pharaon.adu.ac.ae.traceroute.data.model.LaptopContent;
import motaz.pharaon.adu.ac.ae.traceroute.data.model.LaptopPanel;

public class LaptopSession {
    /*
     * Purpose:
     * - Runtime state for the Win98-style victim laptop popup.
     *
     * Responsibilities:
     * - Expose the authored panels as desktop icons and track which have been opened.
     * - Open an authored panel through LaptopPanelProcessor (which may unlock evidence)
     *   and return its readable text for the floating window.
     * - Keep that state out of the LaptopFragment UI widgets.
     *
     * Depends on:
     * - LaptopPanelProcessor for opening a panel's content.
     * - LaptopContent for the title and panels.
     *
     * Used by:
     * - LaptopFragment / LaptopViewBinder.
     */

    private final Set<String> readPanelIds;
    private final LaptopContent laptopContent;

    public LaptopSession(LaptopContent laptopContent) {
        this.laptopContent = laptopContent == null
                ? LaptopContent.empty()
                : laptopContent;
        readPanelIds = new LinkedHashSet<>();
    }

    // Opens the panel (collecting any linked evidence) and returns the text to
    // show in the window. Empty string if the panel does not exist.
    public String openPanel(String panelId, LaptopPanelProcessor panelProcessor) {
        LaptopPanel laptopPanel = laptopContent.findPanel(panelId);
        if (laptopPanel == null) {
            return "";
        }

        // Run the processor for its side effect (collect the linked evidence),
        // but show only the authored, human-readable content lines — no evidence
        // ID or "Status:" metadata.
        panelProcessor.openPanel(panelId);
        readPanelIds.add(laptopPanel.getPanelId());
        return joinLines(laptopPanel.getContentLines());
    }

    private static String joinLines(List<String> lines) {
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            if (builder.length() > 0) {
                builder.append('\n');
            }
            builder.append(line);
        }
        return builder.toString();
    }

    public List<LaptopPanel> getPanels() {
        return laptopContent.getPanels();
    }

    public String getTitle() {
        return laptopContent.getTitle();
    }

    public boolean isPanelRead(String panelId) {
        LaptopPanel panel = laptopContent.findPanel(panelId);
        return panel != null && readPanelIds.contains(panel.getPanelId());
    }
}
