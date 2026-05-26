package motaz.pharaon.adu.ac.ae.traceroute.ui.dialogue;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import motaz.pharaon.adu.ac.ae.traceroute.R;
import motaz.pharaon.adu.ac.ae.traceroute.core.GameRuntime;
import motaz.pharaon.adu.ac.ae.traceroute.ui.completion.CaseCompletionFragment;

public class DialogueFragment extends DialogFragment {
    /*
     * Purpose:
     * - DialogFragment for the authored final confrontation sequence.
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_dialogue, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DialogueController controller = new DialogueController(
                GameRuntime.getInstance().getFinalConfrontationScript()
        );
        DialogueView dialogueView = new DialogueView(view);
        dialogueView.bind(controller, this::showCompletionAndDismiss);
    }

    private void showCompletionAndDismiss() {
        FragmentManager fragmentManager = getParentFragmentManager();
        dismiss();

        if (fragmentManager.findFragmentByTag(CaseCompletionFragment.TAG) == null) {
            new CaseCompletionFragment().show(fragmentManager, CaseCompletionFragment.TAG);
        }
    }
}
