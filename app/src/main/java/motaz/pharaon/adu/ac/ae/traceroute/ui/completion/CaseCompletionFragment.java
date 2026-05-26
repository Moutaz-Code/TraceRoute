package motaz.pharaon.adu.ac.ae.traceroute.ui.completion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import motaz.pharaon.adu.ac.ae.traceroute.R;
import motaz.pharaon.adu.ac.ae.traceroute.core.GameRuntime;
import motaz.pharaon.adu.ac.ae.traceroute.data.model.CaseCompletion;

public class CaseCompletionFragment extends DialogFragment {
    public static final String TAG = "case_completion";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_case_completion, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CaseCompletion caseCompletion = GameRuntime.getInstance().getCaseCompletion();
        TextView titleText = view.findViewById(R.id.case_completion_title);
        TextView bodyText = view.findViewById(R.id.case_completion_body);

        titleText.setText(caseCompletion.getTitle());
        bodyText.setText(caseCompletion.getBody());

        view.findViewById(R.id.case_completion_return_button).setOnClickListener(button -> dismiss());
        view.findViewById(R.id.case_completion_reset_button).setOnClickListener(button -> {
            GameRuntime.resetPlaythrough(requireContext().getApplicationContext());
            requireActivity().recreate();
        });
    }
}
