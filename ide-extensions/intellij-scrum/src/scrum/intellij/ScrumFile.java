package scrum.intellij;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

/**
 * PSI file representation for SCRUM files.
 */
public class ScrumFile extends PsiFileBase {
    public ScrumFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, ScrumLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return ScrumFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "SCRUM File";
    }
}
