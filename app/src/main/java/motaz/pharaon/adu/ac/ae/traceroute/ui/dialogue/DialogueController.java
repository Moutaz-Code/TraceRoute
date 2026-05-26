package motaz.pharaon.adu.ac.ae.traceroute.ui.dialogue;

import motaz.pharaon.adu.ac.ae.traceroute.data.model.DialogueLine;
import motaz.pharaon.adu.ac.ae.traceroute.data.model.DialogueScript;

public class DialogueController {
    private final DialogueScript dialogueScript;
    private int lineIndex;

    public DialogueController(DialogueScript dialogueScript) {
        this.dialogueScript = dialogueScript == null ? DialogueScript.empty() : dialogueScript;
        lineIndex = 0;
    }

    public String getTitle() {
        return dialogueScript.getTitle();
    }

    public DialogueLine getCurrentLine() {
        return dialogueScript.getLines().get(lineIndex);
    }

    public int getCurrentLineNumber() {
        return lineIndex + 1;
    }

    public int getTotalLineCount() {
        return dialogueScript.getLines().size();
    }

    public boolean isAtEnd() {
        return lineIndex >= dialogueScript.getLines().size() - 1;
    }

    public void advance() {
        if (!isAtEnd()) {
            lineIndex++;
        }
    }
}
