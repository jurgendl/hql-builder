package org.tools.hqlbuilder.common.exceptions;

public class SyntaxException extends ServiceException {
    private static final long serialVersionUID = -3751758369552573295L;

    private final int line;

    private final int col;

    private final String wrong;

    public SyntaxException(String message, String wrong, int line, int col) {
        super(message);
        this.wrong = wrong;
        this.line = line;
        this.col = col;
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
}
