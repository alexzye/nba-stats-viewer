import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.table.*;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.util.*;
import java.awt.event.KeyListener;
import java.io.*;

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
    private static JFrame frame;
    private JTextField addYearText;
    private JTextField addPlayerText;
    private JTextField addTeamText;
    private JPanel srpanel;
    private JPanel stats;
    private JLabel stat1result;
    private JLabel stat2result;
    private JLabel stat3result;
    private JLabel stat4result;
    private JLabel stat5result;
    private JLabel stat6result;
    private JScrollPane scrollPane;

    private TableRowSorter<DefaultTableModel> sorter;
    private static Connection conn;
    private Statement s;
    private DefaultTableModel model;
    private ArrayList<String> teams;
    private String query;
    private String[] cols = {
        "Player", "Pos", "Age", "Team", "GP", "GS", 
        "MIN", "FGM", "FGA", "3PM", "3PA", "2PM", 
        "2PA", "FTM", "FTA", "ORB", "DRB", "AST", 
        "STL", "BLK", "TOV", "PF", "PTS", "Year"
    };

    private String[] setupFiles = {
        "../script/table-cleanup.sql",
        "../script/table-setup.sql",
        "../script/create-teams.sql",
        "../script/create-players.sql",
        "../script/create-mvp.sql",
        "../script/create-champions.sql"
    };

    public NbaData() {
        super();
        setLayout(new BorderLayout());        
        makeConnection();
        
        ResultSet result = null;
        query = "SELECT Abbrev FROM Teams";
        teams = new ArrayList<>();

        try {
            s = conn.createStatement();
            ResultSet r = s.executeQuery(query);

            while (r.next()) {
               teams.add((String)r.getObject(1));
            }    
        } catch(SQLException ee) {
            ee.printStackTrace();
        }
  
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

        table.setPreferredScrollableViewportSize(new Dimension(screen.width-20, screen.height/2));
        table.setFillsViewportHeight(true);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel scroll = new JPanel(new BorderLayout());
        scroll.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        
        JPanel leftText = new JPanel();
        leftText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel centerText = new JPanel();
        centerText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel rightText = new JPanel();
        rightText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel addLeftText = new JPanel();
        addLeftText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel addCenterText = new JPanel();
        addCenterText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel addRightText = new JPanel();
        addRightText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel statleft = new JPanel();

        JPanel stat1 = new JPanel(new BorderLayout());
        stat1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel stat2 = new JPanel(new BorderLayout());
        stat2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel stat1header = new JLabel("Most Total Points");
        stat1result = new JLabel("");
        stat1.add(stat1header, BorderLayout.NORTH);
        stat1.add(stat1result, BorderLayout.SOUTH);

        JLabel stat2header = new JLabel("MVP with Most Total Points");
        stat2result = new JLabel("");
        stat2.add(stat2header, BorderLayout.NORTH);
        stat2.add(stat2result, BorderLayout.SOUTH);

        statleft.add(stat1, BorderLayout.WEST);
        statleft.add(stat2, BorderLayout.EAST);
        
        JPanel statright = new JPanel();

        JPanel stat3 = new JPanel(new BorderLayout());
        stat3.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel stat4 = new JPanel(new BorderLayout());
        stat4.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel stat3header = new JLabel("Team with Worst Shooting %");
        stat3result = new JLabel("");
        stat3.add(stat3header, BorderLayout.NORTH);
        stat3.add(stat3result, BorderLayout.SOUTH);

        JLabel stat4header = new JLabel("Team with Best Shooting %");
        stat4result = new JLabel("");
        stat4.add(stat4header, BorderLayout.NORTH);
        stat4.add(stat4result, BorderLayout.SOUTH);

        statright.add(stat3, BorderLayout.WEST);
        statright.add(stat4, BorderLayout.EAST);

        JPanel statcenter = new JPanel();
        statcenter.setBorder(BorderFactory.createEmptyBorder(1, 10, 10, 10));

        JPanel stat5 = new JPanel(new BorderLayout());
        stat5.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel stat6 = new JPanel(new BorderLayout());
        stat6.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel stat5header = new JLabel("Team with Most Titles");
        stat5result = new JLabel("");
        stat5.add(stat5header, BorderLayout.NORTH);
        stat5.add(stat5result, BorderLayout.SOUTH);

        JLabel stat6header = new JLabel("Worst Non-Zero Free Throw %");
        stat6result = new JLabel("");
        stat6.add(stat6header, BorderLayout.NORTH);
        stat6.add(stat6result, BorderLayout.SOUTH);

        statcenter.add(stat5, BorderLayout.WEST);
        statcenter.add(stat6, BorderLayout.EAST);

        JButton addButton = new JButton("ADD");

        srpanel = new JPanel(new BorderLayout());
        stats = new JPanel(new BorderLayout());
        JLabel statlabel = new JLabel("Notable Statistics(Last 10 Years)");

        stats.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        stats.add(statlabel, BorderLayout.NORTH);
        stats.add(statleft, BorderLayout.WEST);
        stats.add(statright, BorderLayout.EAST);
        stats.add(statcenter, BorderLayout.CENTER);

        scrollPane = new JScrollPane(table);
        scroll.add(scrollPane);

        JPanel textFields = new JPanel(new BorderLayout());
        textFields.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        JPanel addPlayer = new JPanel(new BorderLayout());
        addPlayer.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        JPanel searchAdd = new JPanel();

        JButton resetButton = new JButton("RESET DATA");

        srpanel.add(stats, BorderLayout.NORTH);
        srpanel.add(resetButton, BorderLayout.SOUTH);

        add(scroll, BorderLayout.NORTH);
        add(searchAdd, BorderLayout.CENTER);
        add(srpanel, BorderLayout.SOUTH);

        JLabel label = new JLabel("Search by player name, team name, year, or all 3! Case sensitive. Example: Name: Stephen Curry, Team: GSW, Year: 2015");
        JLabel l1 = new JLabel("Name", JLabel.CENTER);
        JLabel l2 = new JLabel("Team", JLabel.CENTER);
        JLabel l3 = new JLabel("Year", JLabel.CENTER);

        JLabel plabel = new JLabel("Enter a Name, Team, and Year to add a player to the database");
        JLabel pl1 = new JLabel("Name", JLabel.CENTER);
        JLabel pl2 = new JLabel("Team", JLabel.CENTER);
        JLabel pl3 = new JLabel("Year", JLabel.CENTER);
        
        label.setBorder(BorderFactory.createEmptyBorder(10, 13, 0, 10));
        plabel.setBorder(BorderFactory.createEmptyBorder(10, 13, 0, 10));

        addPlayerText = new JTextField(10);
        addTeamText = new JTextField(10);
        addYearText = new JTextField(10);

        playerText = new JTextField(10);
        teamText = new JTextField(10);
        yearText = new JTextField(10);

        leftText.add(l1, BorderLayout.NORTH);
        leftText.add(playerText, BorderLayout.CENTER);

        centerText.add(l2, BorderLayout.NORTH);
        centerText.add(teamText, BorderLayout.CENTER);

        rightText.add(l3, BorderLayout.NORTH);
        rightText.add(yearText, BorderLayout.CENTER);

        addLeftText.add(pl1, BorderLayout.NORTH);
        addLeftText.add(addPlayerText, BorderLayout.CENTER);

        addCenterText.add(pl2, BorderLayout.NORTH);
        addCenterText.add(addTeamText, BorderLayout.CENTER);

        addRightText.add(pl3, BorderLayout.NORTH);
        addRightText.add(addYearText, BorderLayout.CENTER);

        textFields.add(label, BorderLayout.NORTH);
        textFields.add(leftText, BorderLayout.WEST);
        textFields.add(centerText, BorderLayout.CENTER);
        textFields.add(rightText, BorderLayout.EAST);
        
        addPlayer.add(plabel, BorderLayout.NORTH);
        addPlayer.add(addLeftText, BorderLayout.WEST);
        addPlayer.add(addCenterText, BorderLayout.CENTER);

        JPanel ok = new JPanel(new BorderLayout());
        ok.add(addRightText, BorderLayout.WEST);
        ok.add(addButton, BorderLayout.EAST);

        addPlayer.add(ok, BorderLayout.EAST);
    
        searchAdd.add(textFields, BorderLayout.WEST);
        searchAdd.add(addPlayer, BorderLayout.EAST);

        addPlayer.setBorder(BorderFactory.createLineBorder(Color.black));
        textFields.setBorder(BorderFactory.createLineBorder(Color.black));
        stats.setBorder(BorderFactory.createLineBorder(Color.black));

        updateStatistics();

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

        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Warning: This will take a while.");
                resetDatabases();

                ResultSet res = null;
                try {
                    res = s.executeQuery("SELECT * FROM Players");
                } catch (SQLException eql) {};
                
                try {
                    model = buildTableModel(res);    
                } catch(SQLException eql) {}
                table = new JTable(model);

                scrollPane.setViewportView(table);
                table.getModel().addTableModelListener(new TableModelListener() {
                    public void tableChanged(TableModelEvent evt) {
                        int colnum = evt.getColumn();
                        int row = evt.getFirstRow();

                        String pName = (String)table.getModel().getValueAt(row, 0);
                        String pYear = String.valueOf(table.getModel().getValueAt(row, 23));
                        String pTeam = (String)table.getModel().getValueAt(row, 3);
                        
                        try {
                            if(colnum >= 0) {
                                if(colnum != 0 && colnum != 1 && colnum != 3) {
                                    query = "UPDATE Players SET " + cols[colnum] + "=" + 
                                    table.getModel().getValueAt(row, colnum) + 
                                    " WHERE Player='" + pName + "' AND Team='" + 
                                    pTeam + "' AND Year=" + pYear;
                                    s.executeUpdate(query);
                                } else {
                                    if(colnum == 3) {
                                        if(teams.contains(pTeam)) {
                                            System.out.println("Team is found for entry");
                                        } else {
                                            try {
                                                query = "INSERT INTO Teams(Abbrev, Name) VALUES('" + pTeam + "', 'User Added')";
                                                System.out.println(query);
                                                s.executeUpdate(query);
                                                
                                                try {
                                                    query = "SELECT Abbrev FROM Teams";
                                                    s = conn.createStatement();
                                                    ResultSet r = s.executeQuery(query);

                                                    while (r.next()) {
                                                       teams.add((String)r.getObject(1));
                                                    }    
                                                } catch(SQLException ee) {
                                                    ee.printStackTrace();
                                                }
                                            } catch(SQLException ex) {
                                                JOptionPane.showMessageDialog(frame, "Please enter a 3 character team code.");
                                                ex.printStackTrace();
                                            }
                                        }
                                    }

                                    query = "UPDATE Players SET " + cols[colnum] + "='" + 
                                    table.getModel().getValueAt(row, colnum) + 
                                    "' WHERE Player='" + pName + "' AND Team='" + 
                                    pTeam + "' AND Year=" + pYear;
                                    s.executeUpdate(query);
                                }
                                System.out.println(query);
                            }
                        } catch(SQLException col) {
                            JOptionPane.showMessageDialog(frame, "Invalid update");
                            col.printStackTrace();
                        }
                    }
                });
            }
        });

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String player = addPlayerText.getText();
                String team = addTeamText.getText();
                String year = addYearText.getText();

                if(player.length() == 0) {
                    JOptionPane.showMessageDialog(frame, "Player name must be a valid string");
                    return;
                }

                if(year.length() == 0) {
                    JOptionPane.showMessageDialog(frame, "Year name must be a valid integer");
                    return;
                } else {
                    try {
                        Integer.parseInt(year);
                    } catch(Exception e1) {
                        JOptionPane.showMessageDialog(frame, "Year must be a valid integer");
                        return;
                    }
                }
                
                if(team.length() == 3) {
                    try {
                        if(teams.contains(team)) {
                            System.out.println("Team is found for entry");
                        } else {
                            try {
                                query = "INSERT INTO Teams(Abbrev, Name) VALUES('" + addTeamText.getText() + "', 'User Added')";
                                System.out.println(query);
                                s.executeUpdate(query);
                                query = "SELECT Abbrev FROM Teams";
                                teams = new ArrayList<>();

                                try {
                                    s = conn.createStatement();
                                    ResultSet r1 = s.executeQuery(query);

                                    while (r1.next()) {
                                       teams.add((String)r1.getObject(1));
                                    }    
                                } catch(SQLException ee1) {
                                    ee1.printStackTrace();
                                }
                            } catch(SQLException ex) {
                                ex.printStackTrace();
                            }
                        }

                        query = "INSERT INTO Players(Player, Team, Year) VALUES('"
                            + player + "','"
                            + team + "',"
                            + year + ");";
                        System.out.println(query);
                        s.executeUpdate(query);
                        
                        Vector<String> row = new Vector<String>();
                        row.add(player);
                        row.add("");
                        row.add("0");
                        row.add(team);
                        for(int i = 0; i < 19; i++) {
                            row.add("0");
                        }
                        row.add(year);
                        model.addRow(row);
                    } catch(SQLException a) {
                        a.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Team abbrev must be a 3 character string");
                    return;
                }
                addYearText.setText("");
                addTeamText.setText("");
                addPlayerText.setText("");
            }
        });

        table.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent evt) {
                int colnum = evt.getColumn();
                int row = evt.getFirstRow();

                String pName = (String)table.getModel().getValueAt(row, 0);
                String pYear = String.valueOf(table.getModel().getValueAt(row, 23));
                String pTeam = (String)table.getModel().getValueAt(row, 3);
                
                try {
                    if(colnum >= 0) {
                        if(colnum != 0 && colnum != 1 && colnum != 3) {
                            query = "UPDATE Players SET " + cols[colnum] + "=" + 
                            table.getModel().getValueAt(row, colnum) + 
                            " WHERE Player='" + pName + "' AND Team='" + 
                            pTeam + "' AND Year=" + pYear;
                            s.executeUpdate(query);
                        } else {
                            if(colnum == 3) {
                                if(teams.contains(pTeam)) {
                                    System.out.println("Team is found for entry");
                                } else {
                                    try {
                                        query = "INSERT INTO Teams(Abbrev, Name) VALUES('" + pTeam + "', 'User Added')";
                                        System.out.println(query);
                                        s.executeUpdate(query);
                                        
                                        try {
                                            query = "SELECT Abbrev FROM Teams";
                                            s = conn.createStatement();
                                            ResultSet r = s.executeQuery(query);

                                            while (r.next()) {
                                               teams.add((String)r.getObject(1));
                                            }    
                                        } catch(SQLException ee) {
                                            ee.printStackTrace();
                                        }
                                    } catch(SQLException ex) {
                                        JOptionPane.showMessageDialog(frame, "Please enter a 3 character team code.");
                                        ex.printStackTrace();
                                    }
                                }
                            }

                            query = "UPDATE Players SET " + cols[colnum] + "='" + 
                            table.getModel().getValueAt(row, colnum) + 
                            "' WHERE Player='" + pName + "' AND Team='" + 
                            pTeam + "' AND Year=" + pYear;
                            s.executeUpdate(query);
                        }
                        System.out.println(query);
                    }
                } catch(SQLException col) {
                    JOptionPane.showMessageDialog(frame, "Invalid update");
                    col.printStackTrace();
                }
                updateStatistics();
            }

        });
    }

    private void resetDatabases() {
        for(String file : setupFiles) {
            String content = null;
            try {
                content = new Scanner(new File(file)).useDelimiter("\\Z").next();
            } catch(FileNotFoundException fnf) {
                System.out.println("Database loading scripts not found: " + file);
            }
            System.out.println(content);
            if(content != null) {
                for(String q : content.split(";\n")) {
                    try {
                        s.executeUpdate(q);    
                    } catch(SQLException sqle) {
                        sqle.printStackTrace();
                    }
                }
            }
        }
        updateStatistics();
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
         frame = new JFrame("NBA Data Viewer");
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

    private void updateStatistics() {
        String content = null;
        File fil;
        Scanner scanner;
        try {
            fil = new File("../script/queries.sql");
            scanner = new Scanner(fil).useDelimiter("\\Z");
            content = scanner.next();
        } catch(FileNotFoundException fnf) {
            System.out.println("Cannot find query script");
        }    
        if(content != null) {
            int i = 1;
            for(String q : content.split(";\n")) {
                System.out.println(q);
                try {
                    ResultSet r = s.executeQuery(q);
                    r.next();
                    String result = (String)r.getObject(1);
                    switch (i) {
                        case 1:  stat1result.setText(result + " (" + r.getInt("total") + ")");
                                 break;
                        case 2:  stat2result.setText(result + " (" + r.getInt("total") + ")");
                                 break;
                        case 3:  stat3result.setText(result + " (" + r.getFloat("total") + ")");
                                 break;
                        case 4:  stat4result.setText(result + " (" + r.getFloat("total") + ")");
                                 break;
                        case 5:  stat5result.setText(result + " (" + r.getInt("total") + ")");
                                 break; 
                        case 6:  stat6result.setText(result + " (" + r.getFloat("total") + ")");
                                 break;
                    }
                } catch(SQLException ee) {
                    ee.printStackTrace();
                }
                i++;
            }
        }

    }

    public static void makeConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver Found");
        }
        catch (ClassNotFoundException ex) {
            System.out.println("Driver not found");
            System.exit(1);
        };
        
        // String url = "jdbc:mysql://nba.ciqzndzyzwah.us-west-1.rds.amazonaws.com:3306/";
        // String user ="admin";
        // String db = "nba";
        // String password="password";

        String url = "jdbc:mysql@cslvm74.csc.calpoly.edu/nba";
        String user ="aye01";
        String password="4207731";

        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection made");
        }
        catch (Exception ex) {
            System.out.println("Could not open connection");
            System.exit(1);
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
