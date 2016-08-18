package org.tools.hqlbuilder.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.swingeasy.ETable;
import org.swingeasy.ETableConfig;
import org.swingeasy.ETableHeaders;
import org.swingeasy.ETableRecord;
import org.swingeasy.ETableRecordBean;

/**
 * @author Jurgen
 */
public class FavoritesDialog extends JDialog {
    private static final long serialVersionUID = -1905240688562741597L;

    private QueryFavorite selection = null;

    private ETable<QueryFavorite> table = new ETable<>(new ETableConfig(true));

    public FavoritesDialog(JFrame frame, final LinkedList<QueryFavorite> favorites) {
        super(frame);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());
        ETableHeaders<QueryFavorite> headers = new ETableHeaders<>();
        headers.add(HqlResourceBundle.getMessage("name"));
        headers.add(HqlResourceBundle.getMessage("parametersPreview"));
        headers.add(HqlResourceBundle.getMessage("hqlPreview"));
        List<String> orderedFields = new ArrayList<>();
        orderedFields.add("name");
        orderedFields.add("parametersPreview");
        orderedFields.add("hqlPreview");
        for (QueryFavorite favorite : favorites) {
            table.addRecord(new ETableRecordBean<>(orderedFields, favorite));
        }
        table.setHeaders(headers);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (e.getClickCount() == 2) {
                        QueryFavorite favorite = table.getRecordAtVisualRow(table.getSelectedRow()).getBean();
                        selection = favorite;
                        FavoritesDialog.this.dispose();
                    }
                } catch (Exception ex) {
                    // TODO: handle exception
                    ex.printStackTrace();
                }
            }
        });
        getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel actions = new JPanel(new FlowLayout());
        JButton importSelected = new JButton(HqlResourceBundle.getMessage("restore selected"));
        importSelected.addActionListener(e -> {
            try {
                QueryFavorite favorite = table.getRecordAtVisualRow(table.getSelectedRow()).getBean();
                selection = favorite;
                FavoritesDialog.this.dispose();
            } catch (Exception ex) {
                // TODO: handle exception
                ex.printStackTrace();
            }
        });
        actions.add(importSelected);
        JButton deleteSelected = new JButton(HqlResourceBundle.getMessage("delete selected"));
        deleteSelected.addActionListener(e -> {
            try {
                ETableRecord<QueryFavorite> recordAtVisualRow = table.getRecordAtVisualRow(table.getSelectedRow());
                QueryFavorite favorite = recordAtVisualRow.getBean();
                new File("favorites", favorite.getName() + ".xml").delete();
                favorites.remove(favorite);
                table.removeRecord(recordAtVisualRow);
            } catch (Exception ex) {
                // TODO: handle exception
                ex.printStackTrace();
            }
        });
        actions.add(deleteSelected);
        JButton close = new JButton(HqlResourceBundle.getMessage("cancel"));
        close.addActionListener(e -> FavoritesDialog.this.dispose());
        actions.add(close);
        getContentPane().add(actions, BorderLayout.SOUTH);
    }

    public QueryFavorite getSelection() {
        return selection;
    }
}
