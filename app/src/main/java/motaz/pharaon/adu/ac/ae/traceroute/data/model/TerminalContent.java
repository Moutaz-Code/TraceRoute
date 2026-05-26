package motaz.pharaon.adu.ac.ae.traceroute.data.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TerminalContent {
    private final String title;
    private final List<String> introLines;
    private final List<TerminalPanel> panels;

    public TerminalContent(String title, List<String> introLines, List<TerminalPanel> panels) {
        this.title = title;
        this.introLines = immutableCopy(introLines);
        this.panels = immutableCopy(panels);
    }

    public static TerminalContent empty() {
        return new TerminalContent(
                "VICTIM LAPTOP",
                Collections.singletonList("No terminal content is available."),
                Collections.emptyList()
        );
    }

    public TerminalPanel findPanel(String command) {
        for (TerminalPanel panel : panels) {
            if (panel.matches(command)) {
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

    public List<TerminalPanel> getPanels() {
        return panels;
    }

    private static <T> List<T> immutableCopy(List<T> values) {
        if (values == null || values.isEmpty()) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableList(new ArrayList<>(values));
    }
}
