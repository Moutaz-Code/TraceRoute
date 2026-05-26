package motaz.pharaon.adu.ac.ae.traceroute.data.model;

public class CaseCompletion {
    private final String title;
    private final String body;

    public CaseCompletion(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public static CaseCompletion empty() {
        return new CaseCompletion(
                "Case Complete",
                "The investigation is complete."
        );
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
