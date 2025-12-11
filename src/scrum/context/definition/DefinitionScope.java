package scrum.context.definition;

import lombok.Getter;
import scrum.context.ExecutionContext;
import scrum.exception.ImpedimentCode;
import scrum.exception.ScrumRuntimeException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class DefinitionScope {
    private final Set<ClassDefinition> classes;
    private final Set<FunctionDefinition> functions;
    private final Set<ApiDefinition> apis;
    @Getter
    private final DefinitionScope parent;

    public DefinitionScope(DefinitionScope parent) {
        this.classes = new HashSet<>();
        this.functions = new HashSet<>();
        this.apis = new HashSet<>();
        this.parent = parent;
    }

    public ClassDefinition getClass(String name) {
        Optional<ClassDefinition> classDefinition = classes.stream()
                .filter(t -> t.getName().equals(name))
                .findAny();
        if (classDefinition.isPresent())
            return classDefinition.get();
        else if (parent != null)
            return parent.getClass(name);
        else
            throw buildNameException(String.format("EPIC (class) is not defined: %s", name));
    }

    public void addClass(ClassDefinition classDefinition) {
        classes.add(classDefinition);
    }

    public FunctionDefinition getFunction(String name) {
        Optional<FunctionDefinition> functionDefinition = functions.stream()
                .filter(t -> t.getName().equals(name))
                .findAny();
        if (functionDefinition.isPresent())
            return functionDefinition.get();
        else if (parent != null)
            return parent.getFunction(name);
        else
            throw buildNameException(String.format("USER STORY (function) is not defined: %s", name));
    }

    public void addFunction(FunctionDefinition functionDefinition) {
        functions.add(functionDefinition);
    }

    public ApiDefinition getApi(String name) {
        Optional<ApiDefinition> apiDefinition = apis.stream()
                .filter(t -> t.getName().equals(name))
                .findAny();
        if (apiDefinition.isPresent())
            return apiDefinition.get();
        else if (parent != null)
            return parent.getApi(name);
        else
            throw buildNameException(String.format("API is not defined: %s", name));
    }

    public void addApi(ApiDefinition apiDefinition) {
        apis.add(apiDefinition);
    }
    
    private ScrumRuntimeException buildNameException(String message) {
        ExecutionContext.Context ctx = ExecutionContext.get();
        return ScrumRuntimeException.builder()
            .message(message)
            .impedimentCode(ImpedimentCode.SCRUM_RUNTIME_NAME_001)
            .snippet("<definition lookup>")
            .epicName(ctx != null ? ctx.getEpicName() : null)
            .storyName(ctx != null ? ctx.getStoryName() : null)
            .fileName(ctx != null ? ctx.getFileName() : null)
            .build();
    }
}
