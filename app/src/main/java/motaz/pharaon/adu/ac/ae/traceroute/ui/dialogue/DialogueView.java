package motaz.pharaon.adu.ac.ae.traceroute.ui.dialogue;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import motaz.pharaon.adu.ac.ae.traceroute.R;
import motaz.pharaon.adu.ac.ae.traceroute.data.model.DialogueLine;

public class DialogueView {
    private final TextView titleText;
    private final TextView speakerText;
    private final TextView lineText;
    private final TextView progressText;
    private final Button nextButton;

    public DialogueView(View rootView) {
        titleText = rootView.findViewById(R.id.dialogue_title);
        speakerText = rootView.findViewById(R.id.dialogue_speaker);
        lineText = rootView.findViewById(R.id.dialogue_line);
        progressText = rootView.findViewById(R.id.dialogue_progress);
        nextButton = rootView.findViewById(R.id.dialogue_next_button);
    }

    public void bind(DialogueController controller, Runnable finishCallback) {
        render(controller);
        nextButton.setOnClickListener(view -> {
            if (controller.isAtEnd()) {
                finishCallback.run();
                return;
            }

            controller.advance();
            render(controller);
        });
    }

    private void render(DialogueController controller) {
        DialogueLine line = controller.getCurrentLine();
        titleText.setText(controller.getTitle());
        speakerText.setText(line.getSpeaker());
        lineText.setText(line.getText());
        progressText.setText(controller.getCurrentLineNumber() + " / " + controller.getTotalLineCount());
        nextButton.setText(controller.isAtEnd() ? "Close" : "Next");
    }
}
