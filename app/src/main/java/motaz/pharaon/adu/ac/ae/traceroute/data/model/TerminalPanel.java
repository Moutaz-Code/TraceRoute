package motaz.pharaon.adu.ac.ae.traceroute.data.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class TerminalPanel {
    private final String command;
    private final String label;
    private final String evidenceId;
    private final List<String> aliases;
    private final List<String> outputLines;

    public TerminalPanel(
            String command,
            String label,
            String evidenceId,
            List<String> aliases,
            List<String> outputLines
    ) {
        this.command = normalize(command);
        this.label = label;
        this.evidenceId = evidenceId;
        this.aliases = immutableNormalizedCopy(aliases);
        this.outputLines = immutableCopy(outputLines);
    }

    public boolean matches(String candidate) {
        String normalizedCandidate = normalize(candidate);
        return command.equals(normalizedCandidate) || aliases.contains(normalizedCandidate);
    }

    public String getCommand() {
        return command;
    }

    public String getLabel() {
        return label;
    }

    public String getEvidenceId() {
        return evidenceId;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public List<String> getOutputLines() {
        return outputLines;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.US);
    }

    private static List<String> immutableNormalizedCopy(List<String> values) {
        if (values == null || values.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> normalizedValues = new ArrayList<>();
        for (String value : values) {
            String normalizedValue = normalize(value);
            if (!normalizedValue.isEmpty()) {
                normalizedValues.add(normalizedValue);
            }
        }

        return Collections.unmodifiableList(normalizedValues);
    }

    private static List<String> immutableCopy(List<String> values) {
        if (values == null || values.isEmpty()) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableList(new ArrayList<>(values));
    }
}
