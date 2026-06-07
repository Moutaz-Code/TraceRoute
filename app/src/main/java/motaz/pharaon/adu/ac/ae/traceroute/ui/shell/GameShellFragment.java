package motaz.pharaon.adu.ac.ae.traceroute.ui.shell;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import motaz.pharaon.adu.ac.ae.traceroute.R;
import motaz.pharaon.adu.ac.ae.traceroute.core.GameUiEventListener;
import motaz.pharaon.adu.ac.ae.traceroute.core.GameRuntime;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceItem;
import motaz.pharaon.adu.ac.ae.traceroute.ui.handheld.HandheldFragment;
import motaz.pharaon.adu.ac.ae.traceroute.ui.laptop.LaptopFragment;
import motaz.pharaon.adu.ac.ae.traceroute.ui.world.WorldInvestigationFragment;

public class GameShellFragment extends Fragment implements GameUiEventListener {
    /*
     * Purpose:
     * - Root fragment for the playable game shell.
     *
     * Responsibilities:
     * - Host the world investigation fragment.
     * - Host overlay fragments such as handheld screens.
     * - Open the laptop popup as a DialogFragment.
     * - Keep MainActivity thin and fragment-mandate compliant.
     *
     * Depends on:
     * - WorldInvestigationFragment for OpenGL apartment interaction.
     * - HandheldFragment for evidence/threads/timeline UI.
     * - LaptopFragment for the stylized victim-laptop popup.
     *
     * Used by:
     * - MainActivity.
     */

    private static final String HANDHELD_TAG = "handheld";
    private static final String LAPTOP_TAG = "laptop";
    private static final long NOTIFICATION_VISIBLE_MS = 2600L;

    private final Handler notificationHandler = new Handler(Looper.getMainLooper());
    private View notificationPanel;
    private TextView notificationTitle;
    private TextView notificationBody;
    private final Runnable hideNotificationRunnable = new Runnable() {
        @Override
        public void run() {
            hideNotification();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_shell, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notificationPanel = view.findViewById(R.id.game_notification_panel);
        notificationTitle = view.findViewById(R.id.game_notification_title);
        notificationBody = view.findViewById(R.id.game_notification_body);

        if (savedInstanceState == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.world_fragment_container, new WorldInvestigationFragment())
                    .commit();
        }
    }

    public void openHandheld() {
        openHandheld(null);
    }

    private void openHandheld(String focusedEvidenceId) {
        FragmentManager fragmentManager = getChildFragmentManager();
        if (fragmentManager.findFragmentByTag(HANDHELD_TAG) != null) {
            fragmentManager.popBackStack(HANDHELD_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        fragmentManager
                .beginTransaction()
                .replace(
                        R.id.overlay_fragment_container,
                        HandheldFragment.newInstance(focusedEvidenceId),
                        HANDHELD_TAG
                )
                .addToBackStack(HANDHELD_TAG)
                .commit();
    }

    public void openLaptop() {
        if (getChildFragmentManager().findFragmentByTag(LAPTOP_TAG) != null) {
            return;
        }

        new LaptopFragment().show(getChildFragmentManager(), LAPTOP_TAG);
    }

    @Override
    public void onOpenHandheldRequested() {
        openHandheld();
    }

    @Override
    public void onOpenLaptopRequested() {
        openLaptop();
    }

    @Override
    public void onEvidenceInspectionRequested(String evidenceId, boolean alreadyCollected) {
        showEvidenceNotification(evidenceId, alreadyCollected);
    }

    @Override
    public void onRoomContextChanged(String roomName, String contextText, String hotspotHint) {
        // Room context labels are owned by WorldInvestigationFragment.
    }

    @Override
    public void onDestroyView() {
        notificationHandler.removeCallbacks(hideNotificationRunnable);
        notificationPanel = null;
        notificationTitle = null;
        notificationBody = null;
        super.onDestroyView();
    }

    private void showEvidenceNotification(String evidenceId, boolean alreadyCollected) {
        if (notificationPanel == null || notificationTitle == null || notificationBody == null) {
            return;
        }

        EvidenceItem evidenceItem = GameRuntime.getInstance().getEvidenceSystem().getEvidence(evidenceId);
        String evidenceTitle = evidenceItem != null ? evidenceItem.getTitle() : evidenceId;
        notificationTitle.setText(alreadyCollected ? "Already in handheld" : "Evidence acquired");
        notificationBody.setText(evidenceTitle);

        notificationHandler.removeCallbacks(hideNotificationRunnable);
        notificationPanel.setVisibility(View.VISIBLE);
        notificationPanel.animate().cancel();
        notificationPanel.animate()
                .alpha(1.0f)
                .setDuration(120L)
                .start();
        notificationHandler.postDelayed(hideNotificationRunnable, NOTIFICATION_VISIBLE_MS);
    }

    private void hideNotification() {
        if (notificationPanel == null) {
            return;
        }

        notificationPanel.animate().cancel();
        notificationPanel.animate()
                .alpha(0.0f)
                .setDuration(180L)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        if (notificationPanel != null) {
                            notificationPanel.setVisibility(View.GONE);
                        }
                    }
                })
                .start();
    }
}
