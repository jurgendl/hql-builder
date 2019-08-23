package org.tools.hqlbuilder.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swingeasy.CustomizableOptionPane;
import org.swingeasy.ETable;
import org.swingeasy.ETableConfig;
import org.swingeasy.ETableHeaders;
import org.swingeasy.ETableRecord;
import org.swingeasy.ETableRecordBean;
import org.swingeasy.MessageType;
import org.tools.hqlbuilder.client.PathResolver.End;
import org.tools.hqlbuilder.client.PathResolver.Result;
import org.tools.hqlbuilder.client.PathResolver.TreeNode;
import org.tools.hqlbuilder.common.HibernateWebResolver;
import org.tools.hqlbuilder.common.HibernateWebResolver.ClassNode;

/**
 * @author Jurgen
 */
public class HqlWizard {
    protected static final Logger logger = LoggerFactory.getLogger(HqlWizard.class);

    protected static class Stop extends RuntimeException {
        private static final long serialVersionUID = -8660143360351472707L;
    }

    public static interface HqlWizardListener {
        void query(String query);
    }

    public static class Option<T> implements Comparable<Option<T>> {
        public Option(String label, T option) {
            super();
            this.label = label;
            this.option = option;
        }

        final String label;

        final T option;

        @Override
        public String toString() {
            return label;
        }

        @Override
        public int compareTo(final Option<T> other) {
            return new CompareToBuilder().append(label, other.label).toComparison();
        }
    }

    protected ETable<Row> table;

    protected final ETableHeaders<Row> headers = new ETableHeaders<>();

    protected final HqlWizardListener listener;

    protected final JFrame parent;

    protected final Map<String, Integer> aliasUses = new HashMap<>();

    protected final List<String> orderedFields = new ArrayList<>();

    protected final HibernateWebResolver meta;

    protected int lastSelectedParentRow = -1;

    protected final JFrame queryBuilder = new JFrame();

    public HqlWizard(HqlWizardListener listener, JFrame parent, HibernateWebResolver info) {
        this.listener = listener;
        this.parent = parent;
        this.meta = info;
        initComponents();
    }

    protected void initComponents() {
        queryBuilder.setTitle(HqlResourceBundle.getMessage("find path between classes"));
        if (parent != null) {
            queryBuilder.setIconImage(parent.getIconImage());
        }
        ETableConfig cfg = new ETableConfig(true);
        table = new ETable<>(cfg);
        headers.add("collection", Boolean.class, false);
        headers.add("class", String.class, false);
        headers.add("parent", String.class, false);
        headers.add("path", String.class, false);
        headers.add("inner join", Boolean.class, true);
        headers.add("select", Boolean.class, true);
        orderedFields.add("collection");
        orderedFields.add("simpleClassName");
        orderedFields.add("parent");
        orderedFields.add("path");
        orderedFields.add("innerjoin");
        orderedFields.add("select");
        table.setHeaders(headers);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                        int selectedRow = table.getSelectedRow();
                        if (selectedRow != -1) {
                            Row row = table.getRecordAtVisualRow(selectedRow).getBean();
                            selectTargetAfterSource(row, false);
                        }
                    } else if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
                        int selectedRow = table.getSelectedRow();
                        lastSelectedParentRow = -1;
                        if (selectedRow != -1) {
                            Row parentRef = table.getRecordAtVisualRow(selectedRow).getBean().getParentRef();
                            if (parentRef != null) {
                                lastSelectedParentRow = getRows().indexOf(parentRef);
                            }
                        }
                        table.repaint();
                    }

                } catch (Stop ex) {
                    logger.error("{}", ex);
                }
            }
        });

        queryBuilder.getContentPane().add(new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS),
                BorderLayout.CENTER);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.add(new JButton(new AbstractAction("done") {
            private static final long serialVersionUID = -4588826583392694461L;

            @Override
            public void actionPerformed(ActionEvent e) {
                queryBuilder.dispose();
                fini();
            }
        }));
        queryBuilder.getContentPane().add(actions, BorderLayout.SOUTH);
        queryBuilder.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        Dimension size = new Dimension(1000, 600);
        queryBuilder.setPreferredSize(size);
        queryBuilder.setSize(size);

        try {
            table.packColumn(0, 3);
            table.packColumn(4, 3);
            table.packColumn(5, 3);
        } catch (Exception ex) {
            //
        }

        table = table.stsi();

        SwingUtilities.invokeLater(() -> queryBuilder.setVisible(true));

        selectPoints();
    }

    protected void fini() {
        StringBuilder q1 = new StringBuilder();
        StringBuilder q2 = new StringBuilder();
        Iterator<Row> iterator = getRows().iterator();

        if (!iterator.hasNext()) {
            return;
        }

        Row firstRow = iterator.next();
        if (firstRow.select) {
            q1.append(" ").append(firstRow.getAlias()).append(getNewline());
        }
        q2.append("from ").append(firstRow.getPath()).append(" ").append(getAlias(firstRow.getAlias())).append(getNewline());
        while (iterator.hasNext()) {
            Row row = iterator.next();
            if (row.getPath().startsWith("SUB")) {
                // TODO "SUB"
                continue;
            }
            if (row.getInnerjoin()) {
                q2.append("inner join ");
            } else {
                q2.append("left outer join ");
            }
            q2.append(row.getParent()).append(".").append(row.getPath()).append(" ").append(getAlias(row.getAlias())).append(getNewline());
            if (row.select) {
                q1.append(" , ").append(row.getAlias()).append(getNewline());
            }
        }
        listener.query("select" + getNewline() + q1 + q2);
    }

    private String getNewline() {
        // return HqlBuilderFrame.getNewline();
        return "\n"; // FIXME newline
    }

    public List<Row> getRows() {
        List<Row> rows = new ArrayList<>();
        for (int j = 0; j < table.getRowCount(); j++) {
            ETableRecord<Row> record = table.getRecordAtVisualRow(j);
            rows.add(record.getBean());
        }

        return Collections.unmodifiableList(rows);
    }

    protected String getAlias(String base) {
        Integer count = aliasUses.get(base);
        if (count == null) {
            count = 1;
        } else {
            count++;
            base += count;
        }
        aliasUses.put(base, count);
        return base;
    }

    protected void selectPoints() {
        Row sourceRow = selectSource();

        if (sourceRow == null) {
            queryBuilder.dispose();
            return;
        }

        selectTargetAfterSource(sourceRow, true);
    }

    protected void selectTargetAfterSource(Row sourceRow, boolean addSingleRow) {
        Option<ClassNode> targetNode = selectTarget();

        if (targetNode == null) {
            if (addSingleRow) {
                table.addRecord(new ETableRecordBean<>(orderedFields, sourceRow));
            }
            return;
        }

        List<TreeNode> resolvedPath = resolvePath(sourceRow, targetNode);

        if (resolvedPath != null) {
            showAutoWizardResult(resolvedPath);
        }
    }

    protected List<TreeNode> resolvePath(Row sourceRow, Option<ClassNode> targetNode) {
        try {
            ClassNode source = meta.getNodes().get(sourceRow.type.getName());
            PathResolver pathResolver = new PathResolver(source, targetNode.option);
            while (true) {
                try {
                    pathResolver.findPath();
                } catch (Result ex) {
                    StringBuilder message = new StringBuilder();
                    for (TreeNode o : ex.resolvePath) {
                        message.append(o.value).append("<br>");
                    }
                    String[] options = {
                            HqlResourceBundle.getMessage("accept"),
                            HqlResourceBundle.getMessage("continue search"),
                            HqlResourceBundle.getMessage("stop") };
                    String rv = CustomizableOptionPane.showCustomDialog(parent, new JLabel("<html>" + message.toString() + "</html>"),
                            HqlResourceBundle.getMessage("find path between classes"), MessageType.QUESTION, options, options[1], null, null);
                    if (options[0].equals(rv)) {
                        return ex.resolvePath;
                    } else if (options[1].equals(rv)) {
                        continue;
                    } else {
                        break;
                    }
                }
            }
        } catch (End ex) {
            JOptionPane.showMessageDialog(parent, HqlResourceBundle.getMessage("no path found"));
        }
        return null;
    }

    protected Option<ClassNode> selectTarget() {
        Option<ClassNode> initialTarget;
        Collection<ClassNode> tmp = meta.getNodes().values();
        @SuppressWarnings("unchecked")
        Option<ClassNode>[] options = new Option[tmp.size()];
        int i = 0;
        for (ClassNode cn : tmp) {
            options[i++] = new Option<>(cn.getType().getSimpleName(), cn);
        }
        Arrays.sort(options);
        initialTarget = ClientUtils.showDialog(parent, HqlResourceBundle.getMessage("target class"), options);
        return initialTarget;
    }

    protected Row selectSource() {
        Row initialSelected;
        {
            final SortedSet<Row> options = new TreeSet<>();
            for (ClassNode node : meta.getClasses()) {
                String classname = node.getId();
                try {
                    Row row = new Row(classname.substring(classname.lastIndexOf('.') + 1));
                    row.alias = Character.toLowerCase(row.path.charAt(0)) + row.path.substring(1);
                    row.type = Class.forName(classname);
                    options.add(row);
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
            initialSelected = ClientUtils.showDialog(parent, HqlResourceBundle.getMessage("source class"), options.toArray(new Row[options.size()]));
        }
        return initialSelected;
    }

    protected void showAutoWizardResult(List<TreeNode> resolvePath) {
        Iterator<TreeNode> iterator = resolvePath.iterator();
        TreeNode first = iterator.next();

        Row row = new Row(first.value.getType().getSimpleName());
        row.alias = Character.toLowerCase(row.path.charAt(0)) + row.path.substring(1);
        row.type = first.value.getType();
        table.addRecord(new ETableRecordBean<>(orderedFields, row));

        while (iterator.hasNext()) {
            TreeNode next = iterator.next();
            Row row2 = new Row(next.property.getId());
            row2.alias = next.property.getId();
            row2.type = next.value.getType();
            row2.collection = next.property.isCollection();
            row2.setParentRef(row);
            table.addRecord(new ETableRecordBean<>(orderedFields, row2));
            row = row2;
        }
    }
}
