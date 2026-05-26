package motaz.pharaon.adu.ac.ae.traceroute.ui.world;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

import motaz.pharaon.adu.ac.ae.traceroute.GameView;
import motaz.pharaon.adu.ac.ae.traceroute.R;
import motaz.pharaon.adu.ac.ae.traceroute.core.GameUiEventListener;

public class WorldInvestigationFragment extends Fragment {
    /*
     * Purpose:
     * - Fragment wrapper for the OpenGL apartment investigation view.
     *
     * Responsibilities:
     * - Own the GameView lifecycle.
     * - Place the GLSurfaceView inside a fragment so the project satisfies the fragment requirement.
     * - Stay focused on world rendering/input; handheld and terminal UI are separate fragments.
     *
     * Depends on:
     * - GameView for OpenGL rendering and touch capture.
     * - fragment_world_investigation.xml for the container.
     *
     * Used by:
     * - GameShellFragment.
     */

    private GameView gameView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_world_investigation, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FrameLayout gameViewContainer = view.findViewById(R.id.game_view_container);
        gameView = new GameView(requireContext());

        if (getParentFragment() instanceof GameUiEventListener) {
            gameView.setUiEventListener((GameUiEventListener) getParentFragment());
        }

        gameViewContainer.addView(
                gameView,
                new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        if (gameView != null) {
            gameView.onResume();
        }
    }

    @Override
    public void onPause() {
        if (gameView != null) {
            gameView.onPause();
        }
        super.onPause();
    }




}
