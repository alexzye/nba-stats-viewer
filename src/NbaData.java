import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.util.*;

/*
   GUI Interface for the NBA Data Viewer

   Authors:
   Alex Ye, aye01
   Robert Franklin Mathews IV, rfmathew
   Esteban Ray Ramos, eramos04
*/
public class NbaData extends JPanel {
    private boolean DEBUG = true;
    private JTable table;
    private JTextField yearText;
    private JTextField playerText;
    private JTextField teamText;
    private TableRowSorter<DefaultTableModel> sorter;
    private static Connection conn;
    

    public NbaData() {
        super();
        // setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setLayout(new BorderLayout());
        

        makeConnection();
        
        ResultSet result = null;
        DefaultTableModel model = null;
        Statement s = null;
        
        
        try {
            s = conn.createStatement();
            result = s.executeQuery("SELECT * FROM Players");
        } catch (SQLException e) {};
        
        try {
            model = buildTableModel(result);    
        } catch(SQLException e) {}
        
        sorter = new TableRowSorter<DefaultTableModel>(model);
        table = new JTable(model);
        table.setRowSorter(sorter);
        Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println(screen.width);
        table.setPreferredScrollableViewportSize(new Dimension(screen.width-20, screen.height/2));
        table.setFillsViewportHeight(true);

        //For the purposes of this example, better to have a single
        //selection.
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel scroll = new JPanel(new BorderLayout());
        scroll.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        JPanel leftText = new JPanel();
        
        leftText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel centerText = new JPanel();
        
        centerText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel rightText = new JPanel();
        
        rightText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(table);
        scroll.add(scrollPane);
        JPanel textFields = new JPanel(new BorderLayout());
        textFields.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        // JPanel footerPanel = new JPanel();

        add(scroll, BorderLayout.NORTH);
        add(textFields, BorderLayout.WEST);
        // add(footerPanel, BorderLayout.SOUTH);

        
        // footerPanel.setPreferredSize(new Dimension(screen.width-20, 300));
        
        
        JLabel l1 = new JLabel("Player", JLabel.CENTER);
        JLabel l2 = new JLabel("Team", JLabel.CENTER);
        JLabel l3 = new JLabel("Year", JLabel.CENTER);
        
        playerText = new JTextField(10);
        teamText = new JTextField(10);
        yearText = new JTextField(10);
        
        leftText.add(l1, BorderLayout.NORTH);
        leftText.add(playerText, BorderLayout.CENTER);

        centerText.add(l2, BorderLayout.NORTH);
        centerText.add(teamText, BorderLayout.CENTER);

        rightText.add(l3, BorderLayout.NORTH);
        rightText.add(yearText, BorderLayout.CENTER);

        textFields.add(leftText, BorderLayout.WEST);
        textFields.add(centerText, BorderLayout.CENTER);
        textFields.add(rightText, BorderLayout.EAST);

        playerText.getDocument().addDocumentListener(
            new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    newFilter();
                }
                public void insertUpdate(DocumentEvent e) {
                    newFilter();
                }
                public void removeUpdate(DocumentEvent e) {
                    newFilter();
                }
            }
        );

        teamText.getDocument().addDocumentListener(
            new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    newFilter();
                }
                public void insertUpdate(DocumentEvent e) {
                    newFilter();
                }
                public void removeUpdate(DocumentEvent e) {
                    newFilter();
                }
            }
        );

        yearText.getDocument().addDocumentListener(
            new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    newFilter();
                }
                public void insertUpdate(DocumentEvent e) {
                    newFilter();
                }
                public void removeUpdate(DocumentEvent e) {
                    newFilter();
                }
            }
        );
    }

    /** 
     * Update the row filter regular expression from the expression in
     * the text box.
     */
    private void newFilter() {
        RowFilter<DefaultTableModel, Object> rf = null;
        List<RowFilter<Object,Object>> filters = new ArrayList<RowFilter<Object,Object>>(2);

        //If current expression doesn't parse, don't update.
        try {
            filters.add(RowFilter.regexFilter(playerText.getText(), 0));
            filters.add(RowFilter.regexFilter(teamText.getText(), 3));
            filters.add(RowFilter.regexFilter(yearText.getText(), 23));
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        rf = RowFilter.andFilter(filters);
        sorter.setRowFilter(rf);
    }

    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                try {
                    vector.add((int)rs.getObject(columnIndex));
                } catch(Exception e) {
                    vector.add(rs.getObject(columnIndex));
                }
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames) {
            @Override
            public Class getColumnClass(int colNum) {
               return getValueAt(0, colNum).getClass();
            }
        };
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("NBA Data Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //Create and set up the content pane.
        NbaData newContentPane = new NbaData();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void makeConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver Found");
        }
        catch (ClassNotFoundException ex) {
            System.out.println("Driver not found");
        };
        
        String url = "jdbc:mysql://nba.ciqzndzyzwah.us-west-1.rds.amazonaws.com:3306/";
        String user ="admin";
        String db = "nba";
        String password="password";

        try {
            conn = DriverManager.getConnection(url + db, user, password);
            System.out.println("Connection made");
        }
        catch (Exception ex) {
            System.out.println("Could not open connection");
        };
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
