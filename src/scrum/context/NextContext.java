package scrum.context;

public class NextContext {
    private static NextScope scope = new NextScope();

    public static NextScope getScope() {
        return scope;
    }

    public static void reset() {
        NextContext.scope = new NextScope();
    }
}
