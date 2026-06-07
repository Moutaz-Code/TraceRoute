package motaz.pharaon.adu.ac.ae.traceroute.ui.laptop;

import android.graphics.Color;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.View;

import motaz.pharaon.adu.ac.ae.traceroute.R;
import motaz.pharaon.adu.ac.ae.traceroute.audio.AudioManager;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.laptop.LaptopPanelProcessor;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.laptop.LaptopSession;

public class LaptopViewBinder {
    /*
     * Purpose:
     * - Plain Java helper for wiring Android Views inside LaptopFragment.
     *
     * Responsibilities:
     * - Manage the laptop screen TextView and the authored panel buttons.
     * - Open a panel when its button is tapped and render the resulting screen.
     * - Keep the stylized laptop as real Android UI, with no command-line text entry.
     *
     * Depends on:
     * - LaptopFragment's inflated layout.
     * - LaptopSession for laptop screen state.
     *
     * Used by:
     * - LaptopFragment.
     */

    private final ScrollView scrollView;
    private final TextView titleText;
    private final TextView screenText;
    private final Button closeButton;
    private final Button messagesButton;
    private final Button activityButton;
    private final Button clearButton;
    private final Button filesButton;
    private final Button draftButton;

    public LaptopViewBinder(View rootView) {
        scrollView = rootView.findViewById(R.id.laptop_scroll);
        titleText = rootView.findViewById(R.id.laptop_title);
        screenText = rootView.findViewById(R.id.laptop_screen);
        closeButton = rootView.findViewById(R.id.laptop_close_button);
        messagesButton = rootView.findViewById(R.id.laptop_panel_messages);
        activityButton = rootView.findViewById(R.id.laptop_panel_activity);
        clearButton = rootView.findViewById(R.id.laptop_panel_clear);
        filesButton = rootView.findViewById(R.id.laptop_panel_files);
        draftButton = rootView.findViewById(R.id.laptop_panel_draft);
    }

    public void bind(
            LaptopSession laptopSession,
            LaptopPanelProcessor panelProcessor,
            Runnable closeCallback
    ) {
        titleText.setText(laptopSession.getTitle());
        closeButton.setAllCaps(false);
        closeButton.setOnClickListener(view -> closeCallback.run());
        render(laptopSession);

        messagesButton.setOnClickListener(view -> openPanel("messages", laptopSession, panelProcessor));
        activityButton.setOnClickListener(view -> openPanel("activity", laptopSession, panelProcessor));
        filesButton.setOnClickListener(view -> openPanel("files", laptopSession, panelProcessor));
        draftButton.setOnClickListener(view -> openPanel("draft", laptopSession, panelProcessor));
        clearButton.setOnClickListener(view -> {
            laptopSession.clearScreen();
            render(laptopSession);
        });
    }

    private void openPanel(
            String panelId,
            LaptopSession laptopSession,
            LaptopPanelProcessor panelProcessor
    ) {
        laptopSession.openPanel(panelId, panelProcessor);
        AudioManager.getInstance().playPanelRead();
        render(laptopSession);
    }

    private void render(LaptopSession laptopSession) {
        screenText.setText(laptopSession.getScreenText());
        renderPanelButton(messagesButton, laptopSession, "messages", "Messages");
        renderPanelButton(activityButton, laptopSession, "activity", "Activity");
        renderPanelButton(filesButton, laptopSession, "files", "Files");
        renderPanelButton(draftButton, laptopSession, "draft", "Draft");
        styleUtilityButton(clearButton);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    private void renderPanelButton(
            Button button,
            LaptopSession laptopSession,
            String panelId,
            String fallback
    ) {
        boolean read = laptopSession.isPanelRead(panelId);
        button.setAllCaps(false);
        button.setText(laptopSession.getPanelLabel(panelId, fallback) + (read ? " (read)" : ""));
        button.setTextColor(read ? Color.rgb(5, 8, 10) : Color.rgb(214, 245, 227));
        button.setBackgroundColor(read ? Color.rgb(245, 218, 122) : Color.rgb(18, 35, 39));
    }

    private void styleUtilityButton(Button button) {
        button.setAllCaps(false);
        button.setTextColor(Color.rgb(214, 245, 227));
        button.setBackgroundColor(Color.rgb(20, 39, 45));
    }
}
