package Regression_Analysis.pkg;

import org.sqlite.JDBC;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;

public class Regression_Analysis extends JFrame {
    public final String Connection_String = "jdbc:sqlite:C:/Users/Diego Angulo/Documents/Java Projects/Personal Projects/Projects/com/Regression Analysis/Databases/Database.db";
    private Connection conn;

    public static Regression_Analysis Regression_Analysis;
    private JButton btnGenerate;
    private JButton buttonCancel;
    public JTable tblDataset;
    private JFormattedTextField tfXaxis;
    private JFormattedTextField tfYaxis;
    private JButton btnAdd_Entry;

    //panels
    private JPanel contentPane;
    private JPanel mainPanel;


    private JComboBox<String> cmbDatasetList;
    private JComboBox<String> cmbSeriesList;

    private JButton btnAddSeries;
    private DefaultTableModel model;
    private String Combo_Option;
    //    public static Vector<Vector<Object>> list;
//    public Object[][] table;
    public Vector<String> Datasetlist;
    public ArrayList<String> List_Series_Name;

    private int counter;
    private String[] Series_arr;
    private String Series_N;


    public Regression_Analysis() {
        super("Regression Analysis");
        this.conn = null;

        setMinimumSize(new Dimension(318, 595));


//        setSize(new Dimension(318, 595));
        setContentPane(mainPanel);
        setResizable(true);
        tblDataset.getTableHeader().setReorderingAllowed(false);
        createtable();
        getRootPane().setDefaultButton(btnAdd_Entry);
        btnAdd_Entry.setEnabled(false);

        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("Resources/Image/007-boar.png")));
        setIconImage(icon.getImage());

        //ACTION LISTENERS
        btnGenerate.addActionListener(e -> onGenerate());
        buttonCancel.addActionListener(e -> onCancel());
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        btnAdd_Entry.addActionListener(e -> onAdd_Entry());


        cmbDatasetList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cmbDataset_x = (JComboBox) e.getSource();
                Combo_Option = (String) cmbDataset_x.getSelectedItem();

//                JOptionPane.showMessageDialog(Regression_Analysis,"Whathis "+option);
            }
        });


        btnAddSeries.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                int x = JOptionPane.showConfirmDialog(Regression_Analysis, "Create Series?", "Series", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (x == 0) {
                    ++counter;
                    String Series_Name = JOptionPane.showInputDialog(Regression_Analysis, "Enter Series", "Series Name", JOptionPane.QUESTION_MESSAGE);
                    cmbSeriesList.addItem(Series_Name);
                    model.setRowCount(0);
                    btnAdd_Entry.setEnabled(true);
                }
            }
        });
        //
        WindowListener listener = new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {

//                Frame frame = (Frame) evt.getSource();
//                System.out.println("Closing = " + frame.getTitle());
            }
        };
        addWindowListener(listener);
    }

    public void OnAddtable() {


    }

    public void SQLDatabaseCreateConnection() {

        try {
            DriverManager.registerDriver(new JDBC());
            conn = DriverManager.getConnection(Connection_String);
            Statement statement = conn.createStatement();
//            JOptionPane.showMessageDialog(null, "CONNECTION HAS BEEN ESTABLISHED");
//            statement.execute("Create table   All_Series(   _id INT default 1,  _X  DOUBLE,   _Y  DOUBLE) ");
//            statement.execute("Create table   Datalist ( Dataset_Name text not null, 'Group Name' INTEGER primary key autoincrement)");
//            statement.execute("Create table  Series_Information ( _id   INTEGER default 1   primary key autoincrement, Column1_Name INTEGER, Column2_Name INTEGER, Series_Name  text,'Group Name' INTEGER)");

            DatabaseMetaData dmd = conn.getMetaData();
            System.out.println(dmd.getDatabaseProductName());
            System.out.println(dmd.getDatabaseProductVersion());
            System.out.println(dmd.getDriverName());
            System.out.println(dmd.getDriverVersion());

            ResultSet resultSet = dmd.getTables(null, null, null, new String[]{"TABLE"});
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                String remarks = resultSet.getString("REMARKS");

                System.out.println(tableName);
                System.out.println(remarks);
            }


//            statement.execute("INSERT INTO main.All_Series values ()")
//            String query = "insert into (X) values(?)";

//            PreparedStatement statement1 = conn.prepareStatement(query);

//            statement1.setString(1, x);
//            statement1.execute();
            statement.close();
            conn.close();


        } catch (SQLException e) {
            JOptionPane.showMessageDialog(Regression_Analysis, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }


    private void createtable() {

        String[] column = new String[]{"X", "Y"};
        this.model = new DefaultTableModel(null, column) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblDataset.setModel(model);

        TableColumnModel columns = tblDataset.getColumnModel();
        columns.getColumn(0).setMinWidth(100);
        columns.getColumn(1).setMinWidth(100);

        new DefaultTableCellRenderer();
        DefaultTableCellRenderer centerRenderer;
        centerRenderer = (DefaultTableCellRenderer) tblDataset.getTableHeader().getDefaultRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setHorizontalAlignment(JLabel.HORIZONTAL);

        columns.getColumn(0).setCellRenderer(centerRenderer);
        columns.getColumn(1).setCellRenderer(centerRenderer);


    }

    private void onGenerate() {
       /*
        try {
            DriverManager.registerDriver(new JDBC());
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Databases/Database.db");
//            Statement statement = conn.createStatement();

//            statement.execute("Create table IF NOT EXISTS (?)" +
//                    "(_id INTEGER PRIMARY KEY,  Column_1 INTEGER not null, Column_2 INTEGER not null)");


//IF NOT EXISTS
            *//*for (String i : Datasetlist) {
                String query = "Create table " + i + " (_id INTEGER PRIMARY KEY,  Column_1 INTEGER not null, Column_2 INTEGER not null)";
                Statement stmt = conn.createStatement();
                stmt.execute(query);
            }*//*


            DefaultTableModel tblMODEL = (DefaultTableModel) tblDataset.getModel();

            if (tblMODEL.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Table must not be empty", "Empty Table", JOptionPane.ERROR_MESSAGE);
            } else {
                String x, y, z;

                for (int i = 0; i < tblMODEL.getRowCount(); i++) {

                    x = tblMODEL.getValueAt(i, 0).toString();
                    y = tblMODEL.getValueAt(i, 1).toString();

                    String query = "insert into Series(Series_Name,Column_1,Column_2) values(?,?,?)";

                    PreparedStatement prepareStatement = conn.prepareStatement(query);
                    prepareStatement.setString(2, x);
                    prepareStatement.setString(3, y);
                    prepareStatement.execute();
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        seems like everything is normal}*/
        Chart_Title chart_title = new Chart_Title(Regression_Analysis);
        chart_title.pack();
        chart_title.setLocationRelativeTo(Regression_Analysis);
        chart_title.setVisible(true);
    }

    private void onCancel() {
        int choice = JOptionPane.showConfirmDialog(Regression_Analysis, "Delete Table?", "Erase Table", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == 0) {
            model.setRowCount(0);
        }

    }

    private Connection connect() {
        Connection conn = null;
        try {
            DriverManager.registerDriver(new JDBC());
            conn = DriverManager.getConnection(Connection_String);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }


    public void insert(double _X, double _Y) {
        String sql = "INSERT INTO All_Series VALUES(?,?,?);";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String x, y;

            pstmt.setDouble(1, counter);
            pstmt.setDouble(2, _X);
            pstmt.setDouble(3, _Y);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void dumptables() {
        try (Connection conn = this.connect()) {
            Statement stmt = conn.createStatement();
            stmt.execute("TRUNCATE  table  All_Series");
            stmt.execute("TRUNCATE  table  Series_Information");
            stmt.execute("TRUNCATE  table  Datalist");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void onAdd_Entry() {

        if (tfXaxis.getText().isEmpty() || tfYaxis.getText().isEmpty()) {
            JOptionPane.showMessageDialog(Regression_Analysis, "Enter number in fields", "Enter Data", JOptionPane.ERROR_MESSAGE);
        } else {
            insert(Double.parseDouble(tfXaxis.getText()), Double.parseDouble(tfYaxis.getText()));

            Object[] row = new Object[]{Double.parseDouble(tfXaxis.getText()), Double.parseDouble(tfYaxis.getText())};
            model.addRow(row);
            tfXaxis.setText("0");
            tfYaxis.setText("0");
        }


    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        JDialog.setDefaultLookAndFeelDecorated(true);
        Regression_Analysis = new Regression_Analysis();
        Regression_Analysis.pack();
        Regression_Analysis.setLocationRelativeTo(null);
        Regression_Analysis.setDefaultCloseOperation(EXIT_ON_CLOSE);
        Regression_Analysis.setVisible(true);
    }


    public void createUIComponents() {
        contentPane = new JPanel();
        mainPanel = new JPanel();

        mainPanel.add(contentPane);
//        mainPanel.setMinimumSize(new Dimension(318, 595));
//        mainPanel.setMaximumSize(new Dimension(318, 595));
//        mainPanel.setPreferredSize(new Dimension(318, 595));

        contentPane.setMinimumSize(new Dimension(318, 595));
        contentPane.setPreferredSize(new Dimension(318, 595));
        contentPane.setMaximumSize(new Dimension(318, 595));




//        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        contentPane = new JPanel();


        cmbSeriesList = new JComboBox<>();
        cmbDatasetList = new JComboBox<>();


        NumberFormat amountFormat = NumberFormat.getNumberInstance();
        amountFormat.setGroupingUsed(false);
        tfXaxis = new JFormattedTextField(amountFormat);
        tfXaxis.setHorizontalAlignment(JTextField.CENTER);
        tfYaxis = new JFormattedTextField(amountFormat);
        tfYaxis.setHorizontalAlignment(JTextField.CENTER);
    }

//        cmbDatasetList.setModel(new DefaultComboBoxModel<String>());


//        cmbDatasetList.setSelectedIndex(1);
//        JOptionPane.showMessageDialog(Regression_Analysis,"what does this do"+ obj[cmbDatasetList.getSelectedIndex()]);


//        cmbDatasetList = new JComboBox(Series_information.Chart_Information.toArray());
//        cmbDatasetList.setSelectedIndex(4);


}











