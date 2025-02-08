package Main;

import AddTransaction.AddTransaction;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class MainFrame extends javax.swing.JFrame {

    Connection con;
    PreparedStatement pst;

    public MainFrame() {
        initComponents();
        connect();
        loadExpensesData();
        customizeTable();
        customizeTable2();
        loadMembersData();

    }

    public void connect() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  //register
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AddTransaction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/family_expense", "root", ""); //connection
            System.out.println("hello");
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(AddTransaction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    public void loadExpensesData() {
        try {
            String query = "SELECT * FROM expenses"; // SQL query to fetch data
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            // Create a DefaultTableModel to hold the data
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Clear existing data

            // Initialize totals
            double totalExpense = 0;
            double totalIncome = 0;
            double overallTotal = 0;

            // Iterate through the ResultSet and add rows to the model
            while (rs.next()) {
                String date = rs.getString("Date");
                String member = rs.getString("Member");
                String type = rs.getString("Type");
                String description = rs.getString("Description");
                double amount = rs.getDouble("Amount");

                // Add a row to the model
                model.addRow(new Object[]{date, member, type, description, amount});

                // Calculate totals based on type
                if (type.equalsIgnoreCase("Expense")) {
                    totalExpense += amount;
                } else if (type.equalsIgnoreCase("Income")) {
                    totalIncome += amount;
                }
            }

            // Calculate the overall total
            overallTotal = totalIncome - totalExpense;
            // Update the labels with the calculated totals
            texpense.setText(String.format("%.2f", totalExpense));
            tincome.setText(String.format("%.2f", totalIncome));
            ttotal.setText(String.format("%.2f", overallTotal));

        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    private void customizeTable() {
        table.setRowHeight(40);
        // Customize the table header (column titles)
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setSize(WIDTH, 40);// Larger and bold font
        header.setBackground(new Color(232, 223, 223)); // Steel blue background
        header.setForeground(Color.BLACK); // White text color
        header.setOpaque(false); // Make the header non-opaque for better rendering
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Set font size and padding
                c.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Larger font size
                ((DefaultTableCellRenderer) c).setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

                // Get the type of the transaction from the "Type" column
                String type = (String) table.getModel().getValueAt(row, 2); // Assuming "Type" is the 3rd column (index 2)

                // Set background and foreground colors based on the type
                if (type.equalsIgnoreCase("Expense")) {
                    c.setBackground(new Color(255, 200, 200)); // Light red
                    c.setForeground(Color.RED); // Red text
                } else if (type.equalsIgnoreCase("Income")) {
                    c.setBackground(new Color(200, 255, 200)); // Light green
                    c.setForeground(Color.GREEN.darker()); // Dark green text
                } else {
                    c.setBackground(table.getBackground()); // Default background
                    c.setForeground(table.getForeground()); // Default foreground
                }
                // Highlight selected row
                if (isSelected) {
                    c.setBackground(new Color(173, 216, 230)); // Light blue for selected row
                    c.setForeground(Color.BLACK); // Black text for selected row
                }
                return c;
            }
        });
    }

    private void customizeTable2() {
        table2.setRowHeight(30);
        // Customize the table header (column titles)
        JTableHeader header = table2.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setSize(WIDTH, 40);// Larger and bold font
        header.setBackground(new Color(232, 223, 223)); // Steel blue background
        header.setForeground(Color.BLACK); // White text color
        header.setOpaque(false); // Make the header non-opaque for better rendering
        table2.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Set font size and padding
                c.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Larger font size
                ((DefaultTableCellRenderer) c).setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

                // Get the type of the transaction from the "Type" column
                String type = (String) table.getModel().getValueAt(row, 2); // Assuming "Type" is the 3rd column (index 2)

                // Set background and foreground colors based on the type
                if (type.equalsIgnoreCase("Expense")) {
                    c.setBackground(new Color(255, 200, 200)); // Light red
                    c.setForeground(Color.RED); // Red text
                } else if (type.equalsIgnoreCase("Income")) {
                    c.setBackground(new Color(200, 255, 200)); // Light green
                    c.setForeground(Color.GREEN.darker()); // Dark green text
                } else {
                    c.setBackground(table.getBackground()); // Default background
                    c.setForeground(table.getForeground()); // Default foreground
                }
                // Highlight selected row
                if (isSelected) {
                    c.setBackground(new Color(173, 216, 230)); // Light blue for selected row
                    c.setForeground(Color.BLACK); // Black text for selected row
                }
                return c;
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        texpense = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        tincome = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        ttotal = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        date2 = new com.toedter.calendar.JDateChooser();
        date3 = new com.toedter.calendar.JDateChooser();
        jButton3 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table2 = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        mem = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        table3 = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        savingsAmnt = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        savingsMonth = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        savingsYear = new javax.swing.JComboBox<>();
        savingsPalen = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setBackground(new java.awt.Color(204, 0, 204));
        jTabbedPane1.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(153, 204, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Expense");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(97, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 14, Short.MAX_VALUE))
        );

        texpense.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(texpense, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(texpense)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 180, 110));

        jPanel3.setBackground(new java.awt.Color(102, 0, 204));
        jPanel3.setForeground(new java.awt.Color(102, 0, 204));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Family Expense Manager");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, 360, -1));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1180, 50));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Income");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(106, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        tincome.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(tincome, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tincome)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 70, 190, 110));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("Total");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(99, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        ttotal.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(ttotal, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ttotal)
                .addGap(0, 56, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 70, 180, 110));

        jButton1.setBackground(new java.awt.Color(255, 51, 51));
        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Remove Transaction");
        jButton1.setBorder(null);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 140, 220, 40));

        jButton2.setBackground(new java.awt.Color(0, 153, 51));
        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Add Transaction");
        jButton2.setBorder(null);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 80, 220, 40));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, -1, -1));

        jPanel10.setBackground(new java.awt.Color(153, 153, 255));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Date", "Member", "Type", "Description", "Amount"
            }
        ));
        jScrollPane1.setViewportView(table);

        jPanel10.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1180, 390));

        jPanel1.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 1180, 390));

        jTabbedPane1.addTab("Home", jPanel1);

        jPanel12.setBackground(new java.awt.Color(102, 0, 204));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Choose a Date - ");
        jPanel12.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 16, 145, 24));
        jPanel12.add(date2, new org.netbeans.lib.awtextra.AbsoluteConstraints(292, 16, 134, -1));
        jPanel12.add(date3, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 16, 122, -1));

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setText("show");
        jButton3.setBorder(null);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 18, 56, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("---");
        jPanel12.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 20, -1, 10));

        jButton4.setText("Print");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 20, -1, -1));

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        table2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Member", "Type", "Description", "Amount"
            }
        ));
        jScrollPane2.setViewportView(table2);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 561, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 619, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 1182, Short.MAX_VALUE)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, 1182, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Statistics", jPanel11);

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        jPanel17.setBackground(new java.awt.Color(204, 204, 255));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 0, 102));
        jLabel7.setText("Add Member");

        jButton5.setBackground(new java.awt.Color(51, 153, 0));
        jButton5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Add");
        jButton5.setBorder(null);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        table3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Members"
            }
        ));
        jScrollPane3.setViewportView(table3);

        jButton6.setBackground(new java.awt.Color(255, 51, 0));
        jButton6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Remove");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(mem, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(13, 13, 13))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel7)
                .addGap(64, 64, 64)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mem, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addGap(56, 56, 56)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel18.setBackground(new java.awt.Color(204, 204, 255));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(102, 0, 102));
        jLabel8.setText("Year:");

        jButton7.setText("Ok");
        jButton7.setBorder(null);
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(0, 153, 0));
        jButton8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Show Stat");
        jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(102, 0, 102));
        jLabel9.setText(" Savings Amount : ");

        savingsMonth.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "January", " February", " March", " April ", "May", " June ", "July", " August", " September", " October ", "November", " December" }));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(102, 0, 102));
        jLabel10.setText("Month:");

        savingsYear.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2024", "2025", "2026", "2027", "2028", "2029", "2030" }));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(savingsAmnt, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(savingsMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(savingsYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(jButton8)
                .addContainerGap(87, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(savingsAmnt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(savingsMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(savingsYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        savingsPalen.setBackground(new java.awt.Color(204, 204, 255));

        javax.swing.GroupLayout savingsPalenLayout = new javax.swing.GroupLayout(savingsPalen);
        savingsPalen.setLayout(savingsPalenLayout);
        savingsPalenLayout.setHorizontalGroup(
            savingsPalenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 829, Short.MAX_VALUE)
        );
        savingsPalenLayout.setVerticalGroup(
            savingsPalenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(savingsPalen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, 583, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 5, Short.MAX_VALUE))
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(savingsPalen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, 1182, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Manage", jPanel15);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 623, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        AddTransaction tr = new AddTransaction(this);
        tr.show();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int selectedRow = table.getSelectedRow();

        if (selectedRow != -1) {
            // Display confirmation dialog
            int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this transaction?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                // Get data from the selected row to use for the DELETE query
                String date = (String) table.getValueAt(selectedRow, 0);
                String member = (String) table.getValueAt(selectedRow, 1);
                String description = (String) table.getValueAt(selectedRow, 3);

                // Prepare and execute SQL query to delete the record from the database
                String deleteQuery = "DELETE FROM expenses WHERE Date = ? AND Member = ? AND Description = ?";

                try {
                    PreparedStatement pst = con.prepareStatement(deleteQuery);
                    pst.setString(1, date);
                    pst.setString(2, member);
                    pst.setString(3, description);

                    int result = pst.executeUpdate();

                    if (result > 0) {
                        // If deletion is successful, remove the row from the table
                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        model.removeRow(selectedRow);
                        loadExpensesData();  // Refresh data if needed
                        JOptionPane.showMessageDialog(null, "Transaction deleted successfully.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Transaction not found or could not be deleted.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error while deleting the transaction.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "No row selected.");
        }
    }//GEN-LAST:event_jButton1ActionPerformed
public void loadMembersData() {
    try {
    
        String sql = "SELECT * FROM members";
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        
        DefaultTableModel model = (DefaultTableModel) table3.getModel();
        model.setRowCount(0); 

        while (rs.next()) {
         
            String name = rs.getString("member");
           
            model.addRow(new Object[]{name});
        }

        // Step 5: Close resources
        rs.close();
        
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error loading data: " + e.getMessage());
    }
}
    private void showPieChart() {
        // Retrieve the selected dates from the date choosers
        java.util.Date startDate = date2.getDate();
        java.util.Date endDate = date3.getDate();

        if (startDate == null || endDate == null) {
            JOptionPane.showMessageDialog(null, "Please select both start and end dates.");
            return;
        }

        // Convert java.util.Date to java.sql.Date
        java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
        java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());

        try {
            double totalExpense = 0;
            double totalIncome = 0;

            // Query to fetch data within the specified date range
            String query = "SELECT Type, SUM(Amount) AS Total FROM expenses WHERE Date BETWEEN ? AND ? GROUP BY Type";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setDate(1, sqlStartDate);
            pst.setDate(2, sqlEndDate);

            ResultSet rs = pst.executeQuery();

            // Fetch totals
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                String type = rs.getString("Type");
                double total = rs.getDouble("Total");

                if (type.equalsIgnoreCase("Expense")) {
                    totalExpense = total;
                } else if (type.equalsIgnoreCase("Income")) {
                    totalIncome = total;
                }
            }

            // Close resources
            rs.close();
            pst.close();

            if (!hasData) {
                JOptionPane.showMessageDialog(null, "No data found for the selected date range.");
                return;
            }

            // Create a dataset for the pie chart
            DefaultPieDataset dataset = new DefaultPieDataset();
            dataset.setValue("Total Expense", totalExpense);
            dataset.setValue("Total Income", totalIncome);

            // Create the pie chart
            JFreeChart chart = ChartFactory.createPieChart(
                    "Expense and Income Overview", // Chart title
                    dataset, // Dataset
                    true, // Include legend
                    true,
                    false
            );

            // Customize the chart to display values
            PiePlot plot = (PiePlot) chart.getPlot();
            plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} ({2})"));
            // {0} = key, {1} = value, {2} = percentage

            // Create a ChartPanel to display the chart
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(550, 350));

            // Clear any existing chart and add the new chart to the panel
            jPanel14.removeAll();
            jPanel14.setLayout(new BorderLayout());
            jPanel14.add(chartPanel, BorderLayout.CENTER);
            jPanel14.revalidate();
            jPanel14.repaint();

        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error while fetching data.");
        }
    }


    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Retrieve the selected dates from the date choosers
        showPieChart();
        java.util.Date startDate = date2.getDate();
        java.util.Date endDate = date3.getDate();

        if (startDate == null || endDate == null) {
            JOptionPane.showMessageDialog(null, "Please select both start and end dates.");
            return;
        }

        // Convert java.util.Date to java.sql.Date
        java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
        java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());

        try {
            // Construct the SQL query to fetch data within the specified date range
            String query = "SELECT * FROM expenses WHERE Date BETWEEN ? AND ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setDate(1, sqlStartDate);
            pst.setDate(2, sqlEndDate);

            ResultSet rs = pst.executeQuery();

            // Create a DefaultTableModel to hold the data
            DefaultTableModel model = (DefaultTableModel) table2.getModel();
            model.setRowCount(0); // Clear existing data

            // Iterate through the ResultSet and add rows to the model
            while (rs.next()) {
                String date = rs.getString("Date");
                String member = rs.getString("Member");
                String type = rs.getString("Type");
                String description = rs.getString("Description");
                double amount = rs.getDouble("Amount");

                // Add a row to the model
                model.addRow(new Object[]{date, member, type, description, amount});
            }

        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error while fetching data.");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        java.util.Date startDate = date2.getDate();
        java.util.Date endDate = date3.getDate();
        try {
            // Check if the table has data
            if (table2.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No data available to print!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create a PrinterJob instance
            PrinterJob printerJob = PrinterJob.getPrinterJob();

            // Set the printable content to the JTable with a dynamic title
            printerJob.setPrintable(new Printable() {
                @Override
                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                    if (pageIndex > 0) {
                        return NO_SUCH_PAGE; // Only one page is printed
                    }

                    // Cast Graphics to Graphics2D
                    Graphics2D g2d = (Graphics2D) graphics;

                    // Translate the graphics context to fit the page format
                    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                    // Define the title text with start and end dates
                    String title = "Expense Report from " + startDate + " to " + endDate; // Dynamic title
                    Font titleFont = new Font("Arial", Font.BOLD, 13);
                    Font tableFont = new Font("Arial", Font.PLAIN, 12);

                    // Draw the title
                    g2d.setFont(titleFont);
                    int titleWidth = g2d.getFontMetrics().stringWidth(title);
                    int x = (int) (pageFormat.getImageableWidth() - titleWidth) / 2; // Center the title
                    int y = 20; // Vertical position of the title
                    g2d.drawString(title, x, y);

                    // Adjust the y-coordinate for the table to avoid overlapping with the title
                    y += 30; // Add some space between the title and the table

                    // Translate the graphics context to account for the title
                    g2d.translate(0, y);

                    // Print the table
                    table2.printAll(g2d);

                    return PAGE_EXISTS; // Page exists and is printed
                }
            });

            // Display the print dialog to the user
            if (printerJob.printDialog()) {
                // Print the table if the user confirms
                printerJob.print();
            } else {
                JOptionPane.showMessageDialog(this, "Printing cancelled.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error while printing: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        int selectedRow = table3.getSelectedRow();

    if (selectedRow != -1) {
        // Display confirmation dialog
        int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this member and their transactions?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            // Get data from the selected row to use for the DELETE query
            String member = (String) table3.getValueAt(selectedRow, 0);

            try {
                // Step 1: Delete the member from the members table
                String deleteMemberQuery = "DELETE FROM members WHERE member = ?";
                PreparedStatement pstMember = con.prepareStatement(deleteMemberQuery);
                pstMember.setString(1, member);

                int memberResult = pstMember.executeUpdate();

                if (memberResult > 0) {
                    // Step 2: Delete the corresponding rows from the expenses table
                    String deleteExpensesQuery = "DELETE FROM expenses WHERE Member = ?";
                    PreparedStatement pstExpenses = con.prepareStatement(deleteExpensesQuery);
                    pstExpenses.setString(1, member);

                    int expensesResult = pstExpenses.executeUpdate();

                    if (expensesResult >= 0) {
                        // If deletion is successful, remove the row from the table
                        DefaultTableModel model = (DefaultTableModel) table3.getModel();
                        model.removeRow(selectedRow);

                        // Refresh data in table1 (expenses table)
                        loadExpensesData();

                        JOptionPane.showMessageDialog(null, "Member and their transactions deleted successfully.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Member deleted, but no transactions found for this member.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Member not found or could not be deleted.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error while deleting the member or their transactions.");
            }
        }
    } else {
        JOptionPane.showMessageDialog(null, "No row selected.");
    }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        String memmmm = mem.getText();
        try {
            pst = con.prepareStatement("INSERT INTO members (member) VALUES (?)");
            pst.setString(1, memmmm);

            pst.executeUpdate();

            // Show success message
            JOptionPane.showMessageDialog(this, "Saved");

            // Clear input fieldstype
            mem.setText("");

            loadMembersData();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(AddTransaction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage());
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        String savingsAmountText = savingsAmnt.getText();
    String selectedMonth = (String) savingsMonth.getSelectedItem();

    if (savingsAmountText.isEmpty() || selectedMonth == null) {
        JOptionPane.showMessageDialog(this, "Please enter the savings amount and select a month.", "Input Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        double savingsAmount = Double.parseDouble(savingsAmountText);
        int currentYear = java.time.Year.now().getValue();
        String yearDate = currentYear + "-" + String.format("%02d", savingsMonth.getSelectedIndex() + 1); 

        // Check if year_date already exists
        String checkQuery = "SELECT COUNT(*) FROM savings WHERE month = ?";
        PreparedStatement checkPst = con.prepareStatement(checkQuery);
        checkPst.setString(1, yearDate);
        ResultSet rs = checkPst.executeQuery();

        rs.next(); // Move to the first row
        if (rs.getInt(1) > 0) { 
            JOptionPane.showMessageDialog(this, "Entry for " + yearDate + " already exists!", "Duplicate Entry", JOptionPane.WARNING_MESSAGE);
            return; // Stop execution
        }

        // Insert new record
        String query = "INSERT INTO savings (month, amount) VALUES (?, ?)";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, yearDate);
        pst.setDouble(2, savingsAmount);

        int rowsAffected = pst.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "Savings data saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            savingsAmnt.setText("");
            savingsMonth.setSelectedIndex(0);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save savings data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Please enter a valid number for the savings amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error while saving data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
 DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    try {
        String query = "SELECT month, amount FROM savings";
        pst = con.prepareStatement(query);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            String month = rs.getString("month");
            double amount = rs.getDouble("amount");
            dataset.setValue(amount, "Savings", month);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error fetching data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Create Bar Chart
    JFreeChart barChart = ChartFactory.createBarChart(
            "Monthly Savings",
            "Month",
            "Amount",
            dataset
    );

    // Customize plot
    CategoryPlot plot = (CategoryPlot) barChart.getPlot();
    plot.setBackgroundPaint(Color.white);
    
    BarRenderer renderer = (BarRenderer) plot.getRenderer();
    Color customGreen = new Color(111,84,149);
    renderer.setSeriesPaint(0, customGreen);

    // Create ChartPanel and set it to jPanel1
    ChartPanel chartPanel = new ChartPanel(barChart);
    chartPanel.setPreferredSize(new Dimension(savingsPalen.getWidth(), savingsPalen.getHeight()));

    savingsPalen.removeAll();  // Remove old chart if any
    savingsPalen.setLayout(new BorderLayout());
    savingsPalen.add(chartPanel, BorderLayout.CENTER);
    savingsPalen.revalidate();
    savingsPalen.repaint();
    }//GEN-LAST:event_jButton8ActionPerformed

    public double getTotalBalance() {
    // Parse the text of ttotal to a double and return it
    return Double.parseDouble(ttotal.getText());
}
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser date2;
    private com.toedter.calendar.JDateChooser date3;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField mem;
    private javax.swing.JTextField savingsAmnt;
    private javax.swing.JComboBox<String> savingsMonth;
    private javax.swing.JPanel savingsPalen;
    private javax.swing.JComboBox<String> savingsYear;
    private javax.swing.JTable table;
    private javax.swing.JTable table2;
    private javax.swing.JTable table3;
    private javax.swing.JLabel texpense;
    private javax.swing.JLabel tincome;
    private javax.swing.JLabel ttotal;
    // End of variables declaration//GEN-END:variables
}
