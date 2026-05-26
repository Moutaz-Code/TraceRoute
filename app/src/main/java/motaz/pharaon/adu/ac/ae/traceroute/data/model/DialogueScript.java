package motaz.pharaon.adu.ac.ae.traceroute.data.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DialogueScript {
    private final String id;
    private final String title;
    private final List<DialogueLine> lines;

    public DialogueScript(String id, String title, List<DialogueLine> lines) {
        this.id = id;
        this.title = title;
        this.lines = Collections.unmodifiableList(new ArrayList<>(lines));
    }

    public static DialogueScript empty() {
        return new DialogueScript(
                "EMPTY",
                "No Dialogue",
                Collections.singletonList(new DialogueLine(
                        "System",
                        "No dialogue script is available."
                ))
        );
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<DialogueLine> getLines() {
        return lines;
    }
}
