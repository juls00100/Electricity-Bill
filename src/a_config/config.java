package a_config;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector; // Add this import at the top
import javax.swing.table.DefaultTableModel; // Add this import at the top
import java.sql.ResultSetMetaData; // Add this import at the top
import javax.swing.JTable;


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
}