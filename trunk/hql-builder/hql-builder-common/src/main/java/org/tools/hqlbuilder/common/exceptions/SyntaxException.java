package org.tools.hqlbuilder.common.exceptions;

public class SyntaxException extends ServiceException {
    private static final long serialVersionUID = -3751758369552573295L;

    public static enum SyntaxExceptionType {
        unexpected_token, invalid_path, could_not_resolve_property, not_mapped;
    }

    private final int line;

    private final int col;

    private final SyntaxExceptionType type;

    private final String wrong;

    public SyntaxException(SyntaxExceptionType type, String message, String wrong, int line, int col) {
        super(message);
        this.wrong = wrong;
        this.line = line;
        this.col = col;
        this.type = type;
    }

    public SyntaxException(SyntaxExceptionType type, String message, String wrong) {
        super(message);
        this.wrong = wrong;
        this.line = -1;
        this.col = -1;
        this.type = type;
    }

    public int getLine() {
        return this.line;
    }

    public int getCol() {
        return this.col;
    }

    public String getWrong() {
        return this.wrong;
    }

    public SyntaxExceptionType getType() {
        return this.type;
    }
}
