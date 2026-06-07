package motaz.pharaon.adu.ac.ae.traceroute;

import motaz.pharaon.adu.ac.ae.traceroute.data.repository.GameSaveState;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameSaveStateTest {
    @Test
    public void emptyStateStartsWithoutCaseCompletion() {
        GameSaveState saveState = GameSaveState.empty("LIVING_ROOM");

        assertFalse(saveState.isFinalTimelineCompleted());
        assertFalse(saveState.isCaseCompleted());
    }

    @Test
    public void saveStateCarriesCaseCompletionFlag() {
        GameSaveState saveState = new GameSaveState(
                Arrays.asList("E01_BODY_CONDITION_REPORT"),
                "LIVING_ROOM",
                Arrays.asList("F01"),
                true,
                true
        );

        assertTrue(saveState.isFinalTimelineCompleted());
        assertTrue(saveState.isCaseCompleted());
    }
}
