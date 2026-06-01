package motaz.pharaon.adu.ac.ae.traceroute.ui.laptop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import motaz.pharaon.adu.ac.ae.traceroute.R;
import motaz.pharaon.adu.ac.ae.traceroute.core.GameRuntime;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.laptop.LaptopPanelProcessor;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.laptop.LaptopSession;

public class LaptopFragment extends DialogFragment {
    /*
     * Purpose:
     * - Stylized victim-laptop popup the player opens on their phone.
     *
     * Responsibilities:
     * - Display the laptop screen and the authored panel buttons (Messages, Activity, Files, Draft).
     * - Open a panel when its button is tapped, which can unlock evidence and thread updates.
     * - Remain a DialogFragment because the laptop is a major required mechanic.
     *
     * Depends on:
     * - LaptopSession for laptop screen state.
     * - LaptopPanelProcessor for opening authored panels.
     * - GameRuntime for evidence/thread unlocks.
     *
     * Used by:
     * - GameShellFragment.
     * - HandheldFragment.
     */

    private LaptopSession laptopSession;
    private LaptopPanelProcessor panelProcessor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_laptop, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        laptopSession = new LaptopSession(GameRuntime.getInstance().getLaptopContent());
        panelProcessor = new LaptopPanelProcessor(GameRuntime.getInstance());

        LaptopViewBinder viewBinder = new LaptopViewBinder(view);
        viewBinder.bind(laptopSession, panelProcessor, this::dismiss);
    }
}
