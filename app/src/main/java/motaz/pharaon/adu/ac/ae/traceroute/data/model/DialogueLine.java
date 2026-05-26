package motaz.pharaon.adu.ac.ae.traceroute.data.model;

public class DialogueLine {
    private final String speaker;
    private final String text;

    public DialogueLine(String speaker, String text) {
        this.speaker = speaker;
        this.text = text;
    }

    public String getSpeaker() {
        return speaker;
    }

    public String getText() {
        return text;
    }
}
