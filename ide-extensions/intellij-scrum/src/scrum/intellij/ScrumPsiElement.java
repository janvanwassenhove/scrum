package scrum.intellij;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

/**
 * Base PSI element for SCRUM language.
 */
public class ScrumPsiElement extends ASTWrapperPsiElement {
    public ScrumPsiElement(@NotNull ASTNode node) {
        super(node);
    }
}
