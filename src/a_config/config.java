package a_config;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector; // Add this import at the top
import javax.swing.table.DefaultTableModel; // Add this import at the top
import java.sql.ResultSetMetaData; // Add this import at the top
import javax.swing.JTable;
import java.sql.*;
import javax.swing.*;
import java.text.SimpleDateFormat;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPTable;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileOutputStream;



public class config {

    
    public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC"); 
            con = DriverManager.getConnection("jdbc:sqlite:ebs.db"); 
            System.out.println("Connection Successful");
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e);
        }
        return con;
    }
    
    
    private static String currentUser;
    
    public static String getUser() {
        return currentUser;
    }

    public static void setUser(String user) {
        currentUser = user;
    }

    

    public static String getID() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    public static String hashPassword(String password) {
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes("UTF-8"));
        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    } catch (Exception ex) {
        throw new RuntimeException(ex);
    }
}

    public static String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   public String getFname() {
    return u_fname; 
}
    private String u_fname;

    // Method used by admin_dashboard to retrieve result sets
    public java.sql.ResultSet getData(String query) throws java.sql.SQLException {
        java.sql.Connection conn = connectDB();
        java.sql.Statement stmt = conn.createStatement();
        return stmt.executeQuery(query);
    
    }
    public Connection getConnection() {
        return connectDB();
    }

    public void addRecord(String sql, Object... values) {
        try (Connection conn = connectDB(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }
            pstmt.executeUpdate();
            System.out.println("Record added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding record: " + e.getMessage());
        }
    }

        // USE TRY-WITH-RESOURCES to ensure connections close automatically
    public void displayData(String sql, javax.swing.JTable table) {
        try (Connection conn = connectDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            Vector<String> columnNames = new Vector<>();
            for (int column = 1; column <= columnCount; column++) {
                columnNames.add(metaData.getColumnName(column));
            }

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> vector = new Vector<>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    vector.add(rs.getObject(columnIndex));
                }
                data.add(vector);
            }
            table.setModel(new DefaultTableModel(data, columnNames));

        } catch (SQLException e) {
            System.out.println("Error displaying data: " + e.getMessage());
        }
        // The "try (...)" block closes everything automatically here!
    }

// Update insertData to close the connection properly
   // Sa config.java
     public int insertData(String sql) {
    int result = 0;
    try {
        Connection conn = connectDB();
        PreparedStatement pst = conn.prepareStatement(sql);
        result = pst.executeUpdate();
        System.out.println("Data inserted successfully!");
        pst.close();
        conn.close();
    } catch (SQLException e) {
        System.out.println("Database Error: " + e.getMessage());
    }
    return result; // Mobalik og 1 kung success, 0 kung failed
}

 public boolean updateData(String sql){
    try (Connection conn = connectDB(); 
         PreparedStatement pst = conn.prepareStatement(sql)) {
        
        int rowsUpdated = pst.executeUpdate();
        return rowsUpdated > 0;
        
    } catch (SQLException ex) {
        System.out.println("Update Error: " + ex.getMessage());
        return false;
    }
}
// Kini nga version modawat og custom SQL query
    public void populateTable(String query, javax.swing.JTable table) {
        try (Connection conn = connectDB();
             java.sql.Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery(query)) {

            java.sql.ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // 1. Kuhaon ang mga Column Names gikan sa database
            java.util.Vector<String> columnNames = new java.util.Vector<>();
            for (int column = 1; column <= columnCount; column++) {
                columnNames.add(metaData.getColumnName(column));
            }

            // 2. Kuhaon ang tanang Data sa mga rows
            java.util.Vector<java.util.Vector<Object>> data = new java.util.Vector<>();
            while (rs.next()) {
                java.util.Vector<Object> vector = new java.util.Vector<>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    vector.add(rs.getObject(columnIndex));
                }
                data.add(vector);
            }

            // 3. I-set ang model sa table aron makita ang data sa screen
            table.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));

        } catch (java.sql.SQLException e) {
            System.out.println("Error populating table: " + e.getMessage());
        }
    }

    // Kini nga version naggamit og default query (pananglitan, tanang users)
    public void populateTable(javax.swing.JTable usertable) {
        // Tawgon ang function sa ibabaw gamit ang default nga query
        String defaultQuery = "SELECT * FROM users"; 
        populateTable(defaultQuery, usertable);
    }
    public static class usersession {
        private static usersession instance;
        private int id;
        private String fname, lname, role, accNum;
        private String image;
        private String accnum;
        private String address;
        private String email;

        private usersession() {}

        public static usersession getInstance() {
            if (instance == null) {
                instance = new usersession();
            }
            return instance;
        }

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getFirstname() { return fname; }
        public void setFirstname(String firstname) { this.fname = firstname; }

        public String getLastname() { return lname; }
        public void setLastname(String lastname) { this.lname = lastname; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public String getAccNum() { return accNum; }
        public void setAccNum(String accNum) { this.accNum = accNum; }

        // SETTERS - Kini ang mag-save sa data sa memory
        public void setImage(String destination) {
            this.image = destination;
        }

        public void setPassword(String newPassHashed) {
            // Kung naa kay variable para sa password, i-assign diri
        }

        public void setFname(String fn) {
            this.fname = fn;
        }

        public void setLname(String ln) {
            this.lname = ln;
        }

        public void setEmail(String em) {
            this.email = em;
        }

        public void setAddress(String ad) {
            this.address = ad;
        }

        public void setAccnum(String an) {
            this.accnum = an;
        }

        // GETTERS - Kini ang mokuha sa data para i-display sa Profile.java
        public String getFname() {
            return this.fname;
        }

        public String getLname() {
            return this.lname;
        }

        public String getEmail() {
            return this.email;
        }

        public String getAddress() {
            return this.address;
        }

        public String getAccnum() {
            return this.accnum;
        }

        public String getImage() { // Gi-change nako gikan Object ngadto sa String
            return this.image;
        }
    public static class billsmodel {

        public billsmodel() {
        }
    }
    public void populateTable(String query, javax.swing.JTable table) {
    try (Connection conn = connectDB();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // 1. Extract Column Names from ebs.db
        Vector<String> columnNames = new Vector<>();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // 2. Extract Row Data
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        // 3. Apply to your JTable
        table.setModel(new DefaultTableModel(data, columnNames));

    } catch (SQLException e) {
        System.out.println("Error populating table: " + e.getMessage());
    }
}
    }
    public String generateAccountNumber() {
    String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder sb = new StringBuilder(10); 
    sb.append("EBS");

    for (int i = 0; i < 7; i++) {
        int index = (int)(AlphaNumericString.length() * Math.random());
        sb.append(AlphaNumericString.charAt(index));
    }

    return sb.toString(); 
}
    public boolean updateData(String sql, Object... params) {
    try (Connection conn = connectDB();
         PreparedStatement pst = conn.prepareStatement(sql)) {
        
        for (int i = 0; i < params.length; i++) {
            pst.setObject(i + 1, params[i]);
        }
        
        int rowsAffected = pst.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        System.out.println("Update Error: " + e.getMessage());
        return false;
    }
}
    public String getAccountNumber(int userId) {
    String accNum = "";
    try {
        Connection conn = this.connectDB(); // or db.connectDB() if calling from another class
        String sql = "SELECT u_accnum FROM users WHERE u_id = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, userId);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            accNum = rs.getString("u_accnum");
        }
        
        rs.close();
        pst.close();
        conn.close();
    } catch (SQLException e) {
        System.out.println("Error fetching account number: " + e.getMessage());
    }
    return accNum;
}
    
    public void printReceipt(javax.swing.JTable table, String userId) {
        String today = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date());
        String fullName = "Customer";
        String address = "N/A";
        String accNum = "N/A";

        try (Connection conn = connectDB()) {
            // We find the account number, name, and address using the u_id!
            String sql = "SELECT u_accnum, u_fname, u_lname, u_address FROM users WHERE u_id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, userId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                accNum = rs.getString("u_accnum");
                fullName = rs.getString("u_fname") + " " + rs.getString("u_lname");
                address = rs.getString("u_address");
            } else {
                // If u_id search fails, we show what ID we were looking for
                System.out.println("Could not find user with ID: " + userId);
            }
        } catch (Exception e) { 
            System.out.println("Database Error: " + e.getMessage()); 
        }

        StringBuilder sb = new StringBuilder();
        sb.append("==================================================\n");
        sb.append("           ELECTRIC BILLING SYSTEM              \n");
        sb.append("               OFFICIAL RECEIPT                 \n");
        sb.append("==================================================\n\n");
        sb.append(" NAME:    ").append(fullName.toUpperCase()).append("\n");
        sb.append(" ACCT #:  ").append(accNum.toUpperCase()).append("\n");
        sb.append(" ADDR:    ").append(address.toUpperCase()).append("\n");
        sb.append(" DATE:    ").append(today).append("\n");
        sb.append("--------------------------------------------------\n");
        sb.append(String.format(" %-12s %-12s %-15s\n", "ID", "AMOUNT", "DATE"));
        sb.append("--------------------------------------------------\n");

        for (int i = 0; i < table.getRowCount(); i++) {
            String id = table.getValueAt(i, 0).toString();
            String amt = table.getValueAt(i, 2).toString();
            String dte = table.getValueAt(i, 3).toString();
            sb.append(String.format(" %-12s %-12s %-15s\n", id, amt, dte));
        }

        sb.append("--------------------------------------------------\n");
        sb.append("       THANK YOU FOR YOUR PAYMENT!        \n");
        sb.append("==================================================");

        javax.swing.JTextArea area = new javax.swing.JTextArea();
        area.setText(sb.toString());
        area.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 9));
        try {
            area.print();
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Printer Error: " + e.getMessage());
        }
    }

    // ===================== UI STYLING =====================

// For JButton (admin_dashboard, Bills, Payment1, Setting, Logs)
public static void styleButton(javax.swing.JButton btn, String type) {
    java.awt.Color bg;
    switch (type.toLowerCase()) {
        case "pay":     bg = new java.awt.Color(57, 122, 0);   break; // Dark Green
        case "soa":     bg = new java.awt.Color(41, 128, 185); break; // Blue
        case "receipt": bg = new java.awt.Color(26, 26, 46);   break; // Navy
        case "pending": bg = new java.awt.Color(243, 156, 18); break; // Orange
        case "paid":    bg = new java.awt.Color(39, 174, 96);  break; // Green
        case "back":    bg = new java.awt.Color(26, 26, 46);   break; // Navy
        case "update":  bg = new java.awt.Color(57, 122, 0);   break; // Dark Green
        case "select":  bg = new java.awt.Color(41, 128, 185); break; // Blue
        case "print":   bg = new java.awt.Color(26, 26, 46);   break; // Navy
        case "close":   bg = new java.awt.Color(192, 57, 43);  break; // Red
        case "add":      bg = new java.awt.Color(39, 174, 96);  break; // Green
        case "edit":     bg = new java.awt.Color(41, 128, 185); break; // Blue
        case "delete":   bg = new java.awt.Color(192, 57, 43);  break; // Red
        case "search":   bg = new java.awt.Color(44, 62, 80);   break; // Dark
        case "save":     bg = new java.awt.Color(57, 122, 0);   break; // Dark Green
        case "cancel":   bg = new java.awt.Color(255, 144, 0);  break; // Orange
        case "logout":   bg = new java.awt.Color(192, 57, 43);  break; // Red
        case "refresh":  bg = new java.awt.Color(44, 62, 80);   break; // Dark
        case "activate": bg = new java.awt.Color(39, 174, 96);  break; // Green
        default:         bg = new java.awt.Color(44, 62, 80);   break;
    }
    btn.setBackground(bg);
    btn.setForeground(java.awt.Color.WHITE);
    btn.setFocusPainted(false);
    btn.setBorderPainted(false);
    btn.setOpaque(true);
    btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
    final java.awt.Color finalBg = bg;
    btn.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(finalBg.darker()); }
        public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(finalBg); }
    });
}
public void printPanel(JPanel panel) {
    PrinterJob job = PrinterJob.getPrinterJob();
    job.setJobName("Print Receipt");

    job.setPrintable(new Printable() {
        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
            if (pageIndex > 0) return Printable.NO_SUCH_PAGE;
            Graphics2D g2 = (Graphics2D) graphics;
            g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            panel.printAll(g2);
            return Printable.PAGE_EXISTS;
        }
    });

    if (job.printDialog()) {
        try { job.print(); } catch (PrinterException e) { e.printStackTrace(); }
    }
}

// For JPanel used as a button (SAVE, CANCEL, logoutbtn, HOME, BILLS, PROFILE, SETTINGS)
public static void stylePanelButton(javax.swing.JPanel panel, String type) {
    java.awt.Color bg;
    switch (type.toLowerCase()) {
        case "save":     bg = new java.awt.Color(57, 122, 0);   break; // Dark Green
        case "cancel":   bg = new java.awt.Color(255, 144, 0);  break; // Orange
        case "logout":   bg = new java.awt.Color(192, 57, 43);  break; // Red
        case "nav":      bg = new java.awt.Color(0, 153, 153);  break; // Teal (HOME,BILLS,etc)
        case "nav-active": bg = new java.awt.Color(0, 204, 204); break; // Light Teal (active nav)
        case "print":    bg = new java.awt.Color(26, 26, 46);   break; // Navy
        case "login":    bg = new java.awt.Color(26, 26, 46);  break; // Teal
        case "register": bg = new java.awt.Color(26, 26, 46);   break; // Navy
        
        default:         bg = new java.awt.Color(0, 153, 153);  break;
    }
    panel.setBackground(bg);
    panel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    panel.setOpaque(true);
    final java.awt.Color finalBg = bg;
    panel.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent e) { panel.setBackground(finalBg.darker()); }
        public void mouseExited(java.awt.event.MouseEvent e)  { panel.setBackground(finalBg); }
    });
}

    // ===================== BILL RECEIPT PRINTER =====================
public void printReceipt(String name, String acc, String bId, String pId, String date, String method, String amount) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save Receipt PDF");
    fileChooser.setSelectedFile(new java.io.File("Receipt_" + bId + ".pdf"));
    
    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileChooser.getSelectedFile().getAbsolutePath()));
            document.open();

            // Header
            Paragraph header = new Paragraph("ELECTRIC BILLING SYSTEM - RECEIPT\n\n");
            header.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            document.add(header);

            // Create Table for alignment
            PdfPTable table = new PdfPTable(2);
            table.addCell("Customer Name:");   table.addCell(name);
            table.addCell("Account Number:"); table.addCell(acc);
            table.addCell("Bill ID:");        table.addCell(bId);
            table.addCell("Payment ID:");     table.addCell(pId);
            table.addCell("Date:");           table.addCell(date);
            table.addCell("Method:");         table.addCell(method);
            table.addCell("Total Paid:");     table.addCell("PHP " + amount);

            document.add(table);
            document.add(new Paragraph("\n\nThank you for your payment!"));
            document.close();
            
            JOptionPane.showMessageDialog(null, "Receipt Generated Successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Printing Failed: " + e.getMessage());
        }
    }
}


public static void printBillReceipt(String billId) {
    try {
        Connection conn = connectDB();

        // Fetch payment + user data in one query
        String sql = "SELECT p.p_id, p.p_amount, p.p_cash, p.p_change, p.p_date, p.p_method, p.u_accnum, " +
                     "u.u_fname, u.u_lname, u.u_address " +
                     "FROM payments p " +
                     "JOIN bills b ON p.b_id = b.b_id " +
                     "JOIN users u ON b.u_id = u.u_id " +
                     "WHERE p.b_id = ? " +
                     "ORDER BY p.p_id DESC LIMIT 1";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(billId.trim()));
        ResultSet rs = pst.executeQuery();

        if (!rs.next()) {
            JOptionPane.showMessageDialog(null, "No payment record found for Bill ID: " + billId);
            return;
        }

        String pid       = rs.getString("p_id");
        String amount    = rs.getString("p_amount");
        String cash      = rs.getString("p_cash");
        String change    = rs.getString("p_change");
        String date      = rs.getString("p_date");
        String method    = rs.getString("p_method");
        String accNum    = rs.getString("u_accnum");
        String fullName  = (rs.getString("u_fname") + " " + rs.getString("u_lname")).toUpperCase();
        String address   = rs.getString("u_address").toUpperCase();

        rs.close(); pst.close(); conn.close();

        // Build the receipt text
        String line1 = "==================================================";
        String line2 = "--------------------------------------------------";
        StringBuilder sb = new StringBuilder();
        sb.append(line1).append("\n");
        sb.append("        ELECTRIC BILLING SYSTEM             \n");
        sb.append("             OFFICIAL RECEIPT               \n");
        sb.append(line1).append("\n\n");
        sb.append(String.format(" %-14s %s%n", "NAME:",     fullName));
        sb.append(String.format(" %-14s %s%n", "ACCT #:",   accNum));
        sb.append(String.format(" %-14s %s%n", "ADDRESS:",  address));
        sb.append("\n").append(line2).append("\n");
        sb.append("  PAYMENT DETAILS\n");
        sb.append(line2).append("\n");
        sb.append(String.format(" %-14s %s%n",   "BILL ID:",    billId));
        sb.append(String.format(" %-14s %s%n",   "PAYMENT ID:", pid));
        sb.append(String.format(" %-14s %s%n",   "DATE:",       date));
        sb.append(String.format(" %-14s %s%n",   "METHOD:",     method));
        sb.append("\n").append(line2).append("\n");
        sb.append(String.format(" %-14s PHP %s%n", "AMOUNT DUE:", amount));
        sb.append(String.format(" %-14s PHP %s%n", "CASH:",       cash));
        sb.append(String.format(" %-14s PHP %s%n", "CHANGE:",     change));
        sb.append("\n").append(line1).append("\n");
        sb.append("      THANK YOU FOR YOUR PAYMENT!      \n");
        sb.append(line1);

        // Print directly
        javax.swing.JTextArea area = new javax.swing.JTextArea(sb.toString());
        area.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 10));
        area.print(); // ← triggers the system print dialog automatically

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Print Error: " + e.getMessage());
        e.printStackTrace();
    }
}
}