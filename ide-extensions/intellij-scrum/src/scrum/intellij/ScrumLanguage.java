package scrum.intellij;

import com.intellij.lang.Language;

/**
 * SCRUM language definition for IntelliJ IDEA.
 */
public class ScrumLanguage extends Language {
    public static final ScrumLanguage INSTANCE = new ScrumLanguage();

    private ScrumLanguage() {
        super("SCRUM");
    }

    @Override
    public String getDisplayName() {
        return "SCRUM";
    }

    @Override
    public boolean isCaseSensitive() {
        return false; // SCRUM keywords are case-insensitive
    }
}
