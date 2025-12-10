package scrum.intellij;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * File type definition for SCRUM files (.scrum).
 */
public class ScrumFileType extends LanguageFileType {
    public static final ScrumFileType INSTANCE = new ScrumFileType();

    private ScrumFileType() {
        super(ScrumLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "SCRUM";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "SCRUM Language File";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "scrum";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        try {
            return IconLoader.getIcon("/icons/scrum-icon.png", ScrumFileType.class);
        } catch (Exception e) {
            return null; // Fallback to default icon
        }
    }
}
