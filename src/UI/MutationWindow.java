package UI;

import Virus.VirusManagement;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MutationWindow extends JDialog {
    private final String[] namesOfVariants = {"Chinese Variant","British Variant","African Variant"};
    private final JPanel mainPanel = new JPanel();
    private static Object[][] checkboxOfVariants;
    public MutationWindow(JFrame window) {      //opens dialog mutation frame
        super(window, "Mutations", true);
        setPreferredSize(new Dimension(500, 150));
        setResizable(false);
        int SIZEOFMUTATIONS = 3;
        checkboxOfVariants = new Boolean[SIZEOFMUTATIONS][SIZEOFMUTATIONS]; //boolean values for optional mutations
        for (int i = 0; i < SIZEOFMUTATIONS; i++) {        //when we open frame we get current mutation values
            for (int j = 0; j < SIZEOFMUTATIONS; j++) {
                checkboxOfVariants[i][j]=VirusManagement.getVariantsBooleanTable()[i][j];
            }
        }
        String[] columnNamesOfVariants = {"Chinese Variant","British Variant","African Variant"};
        JTable mainTable = new JTable(checkboxOfVariants, columnNamesOfVariants) {    //set them to boolean values
            public Class getColumnClass(int column) {
                return Boolean.class;
            }

        };
        mainTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                for (int i = 0; i < mainTable.getModel().getRowCount(); i++) {
                    //if ((Boolean) mainTable.getModel().getValueAt(i, 0)) {
                        Boolean flag = (Boolean) checkboxOfVariants[mainTable.getSelectedRow()][mainTable.getSelectedColumn()];
                        toggleMutation(mainTable.getSelectedRow(), mainTable.getSelectedColumn(), flag);
                        break;
                    //}
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(mainTable); //add all to frame
        JTable rowTable = new TableOfRow(mainTable);
        scrollPane.setRowHeaderView(rowTable);
        scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, rowTable.getTableHeader());
        this.add(scrollPane);
        pack();
        setVisible(true);
    }
    private void toggleMutation(int row, int col, Boolean bool){
        if(VirusManagement.getVariantsBooleanTable()[row][col] != bool)
            VirusManagement.setVariantTable(row,col);
    }
    private static class TableOfRow extends JTable implements ChangeListener, PropertyChangeListener, TableModelListener {
        private static final String[] columnNames = {"Chinese Variant","British Variant","African Variant"};
        private JTable mainTable;
	    public TableOfRow(JTable table) {
            mainTable = table;
            mainTable.addPropertyChangeListener( this );
            mainTable.getModel().addTableModelListener( this );
            setFocusable( false );
            setAutoCreateColumnsFromModel( false );
            setSelectionModel( mainTable.getSelectionModel() );
            TableColumn column = new TableColumn();
            column.setHeaderValue(" ");
            addColumn( column );
            column.setCellRenderer(new RowNumberRenderer());

            getColumnModel().getColumn(0).setPreferredWidth(50);
            setPreferredScrollableViewportSize(getPreferredSize());
        }
        public void addNotify() {
            super.addNotify();
            Component c = getParent();
            if (c instanceof JViewport) {
                JViewport viewport = (JViewport)c;
                viewport.addChangeListener( this );
            }
        }
        public int getRowCount() {
            return mainTable.getRowCount();
        }
        public int getRowHeight(int row) {
            int rowHeight = mainTable.getRowHeight(row);
            if (rowHeight != super.getRowHeight(row)) {
                super.setRowHeight(row, rowHeight);
            }
            return rowHeight;
        }
        public Object getValueAt(int row, int column) {
            return Integer.toString(row + 1);
        }
        public boolean isCellEditable(int row, int column) {
            return false;
        }
        public void setValueAt(Object value, int row, int column) {}
        public void stateChanged(ChangeEvent e) {
            JViewport viewport = (JViewport) e.getSource();
            JScrollPane scrollPane = (JScrollPane)viewport.getParent();
            scrollPane.getVerticalScrollBar().setValue(viewport.getViewPosition().y);
        }
        public void propertyChange(PropertyChangeEvent e) {
            if ("selectionModel".equals(e.getPropertyName())) {
                setSelectionModel( mainTable.getSelectionModel() );
            }
            if ("rowHeight".equals(e.getPropertyName())) {
                repaint();
            }
            if ("model".equals(e.getPropertyName())) {
                mainTable.getModel().addTableModelListener( this );
                revalidate();
            }
        }
        @Override
        public void tableChanged(TableModelEvent e) {
            revalidate();
        }
        private static class RowNumberRenderer extends DefaultTableCellRenderer {
            public RowNumberRenderer() {
                setHorizontalAlignment(JLabel.CENTER);
            }
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (table != null) {
                    JTableHeader header = table.getTableHeader();
                    if (header != null) {
                        setForeground(header.getForeground());
                        setBackground(header.getBackground());
                        setFont(header.getFont());
                    }
                }
                if (isSelected) {
                    setFont( getFont().deriveFont(Font.BOLD) );
                }
                setText((value == null) ? "" : columnNames[(Integer.parseInt(value.toString()))-1]);
                setBorder(UIManager.getBorder("TableHeader.cellBorder"));
                return this;
            }
        }
    }
}
