package be.ugent.oasis.tools.hqlbuilder;

import javax.swing.SwingUtilities;

public class DefaultHqlBuilder {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                HqlBuilderFrame.start(args);
            }
        });
    }
}
