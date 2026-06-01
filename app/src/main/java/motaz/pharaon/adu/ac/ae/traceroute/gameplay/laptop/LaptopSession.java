package motaz.pharaon.adu.ac.ae.traceroute.gameplay.laptop;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import motaz.pharaon.adu.ac.ae.traceroute.data.model.LaptopContent;
import motaz.pharaon.adu.ac.ae.traceroute.data.model.LaptopPanel;

public class LaptopSession {
    /*
     * Purpose:
     * - Runtime state for the stylized victim laptop popup.
     *
     * Responsibilities:
     * - Track what the laptop screen currently shows and which panels have been opened.
     * - Keep that state out of the LaptopFragment UI widgets.
     * - Open authored panels through LaptopPanelProcessor, which may unlock evidence.
     *
     * Depends on:
     * - LaptopPanelProcessor for opening a panel's content.
     * - LaptopContent for the title, starting screen, and panels.
     *
     * Used by:
     * - LaptopFragment / LaptopViewBinder.
     */

    private final List<String> screenLines;
    private final Set<String> readPanelIds;
    private final LaptopContent laptopContent;

    public LaptopSession(LaptopContent laptopContent) {
        this.laptopContent = laptopContent == null
                ? LaptopContent.empty()
                : laptopContent;
        screenLines = new ArrayList<>();
        readPanelIds = new LinkedHashSet<>();
        showHomeScreen();
    }

    public void openPanel(String panelId, LaptopPanelProcessor panelProcessor) {
        LaptopPanel laptopPanel = laptopContent.findPanel(panelId);
        if (laptopPanel == null) {
            return;
        }

        screenLines.add(panelProcessor.openPanel(panelId));
        readPanelIds.add(laptopPanel.getPanelId());
    }

    public void clearScreen() {
        showHomeScreen();
    }

    public String getScreenText() {
        StringBuilder builder = new StringBuilder();

        for (String line : screenLines) {
            builder.append(line).append('\n');
        }

        return builder.toString().trim();
    }

    public String getTitle() {
        return laptopContent.getTitle();
    }

    public String getPanelLabel(String panelId, String fallback) {
        LaptopPanel panel = laptopContent.findPanel(panelId);
        return panel == null ? fallback : panel.getLabel();
    }

    public boolean isPanelRead(String panelId) {
        LaptopPanel panel = laptopContent.findPanel(panelId);
        return panel != null && readPanelIds.contains(panel.getPanelId());
    }

    private void showHomeScreen() {
        screenLines.clear();
        screenLines.addAll(laptopContent.getIntroLines());
    }
}
