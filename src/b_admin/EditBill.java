/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package b_admin;

import a_config.config;
import a_config.session;
import d_main.login;
import javax.swing.JOptionPane;

/**
 *
 * @author juls
 */
public class EditBill extends javax.swing.JFrame {

    /**
     * Creates new form EditBill
     */
    // Add this variable at the top of your class
    int selectedBillID;

    public EditBill(int billID) {
        initComponents();
        this.selectedBillID = billID;
        loadAccountNumbers();
        fetchBillData();

        config.stylePanelButton(PROFILE, "nav");
        config.stylePanelButton(SAVE,      "save");
        config.stylePanelButton(CANCEL,    "cancel");
        config.stylePanelButton(logoutbtn, "logout");
        config.stylePanelButton(HOME,      "nav");
        config.stylePanelButton(BILLS,     "nav-active");
        config.stylePanelButton(PROFILE,   "nav");
        config.stylePanelButton(SETTINGS,  "nav");
    }
    
    public EditBill() {
        initComponents();
        loadAccountNumbers(); 
    }
    
    private void fetchBillData() {
        try {
            a_config.config conf = new a_config.config();
            java.sql.Connection conn = conf.connectDB();

            String sql = "SELECT b.*, u.u_fname, u.u_lname, u.u_address, u.u_accnum " +
                         "FROM bills b JOIN users u ON b.u_id = u.u_id " +
                         "WHERE b.b_id = ?";

            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, selectedBillID);
            java.sql.ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                accnum_drop.setSelectedItem(rs.getString("u_accnum"));
                month.setSelectedItem(rs.getString("b_month"));

                prev_read.setText(rs.getString("b_prev_reading"));
                curr_read.setText(rs.getString("b_curr_reading"));

                String fullName = rs.getString("u_fname") + " " + rs.getString("u_lname");
                name.setText(fullName.toUpperCase());
                address.setText(rs.getString("u_address").toUpperCase());

                // ✅ FIXED: was "b_duedate", correct column is "b_due_date"
                String dateValue = rs.getString("b_due_date");
                java.util.Date date = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateValue);
                duedate.setDate(date);
            }
            rs.close();
            pst.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Error loading existing bill data: " + e.getMessage());
            e.printStackTrace(); // ← add this so you can see errors in the console
        }
    }
    private void loadAccountNumbers() {
        accnum_drop.removeAllItems();
        
        try {
            config conf = new config();
            java.sql.Connection conn = conf.connectDB();
            String sql = "SELECT u_accnum FROM users WHERE u_role = 'User'";
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            java.sql.ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                accnum_drop.addItem(rs.getString("u_accnum"));
            }

            rs.close();
            pst.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        }
        }
    private void fetchUserDataByAccNum(String selectedAcc) {
    
    if (selectedAcc == null || selectedAcc.isEmpty()) {
        return;
    }

    try {
        a_config.config conf = new a_config.config();
        java.sql.Connection conn = conf.connectDB();

        String sql = "SELECT u.u_fname, u.u_lname, u.u_address, " +
                     "b.b_prev_reading, b.b_curr_reading, b.b_month, b.b_due_date " +
                     "FROM users u " +
                     "LEFT JOIN bills b ON u.u_id = b.u_id " +
                     "WHERE u.u_accnum = ? " +
                     "ORDER BY b.b_id DESC LIMIT 1";
        
        java.sql.PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, selectedAcc);
        java.sql.ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            String fullName = rs.getString("u_fname") + " " + rs.getString("u_lname");
            String addr = rs.getString("u_address");
            String prev = rs.getString("b_prev_reading") != null ? rs.getString("b_prev_reading") : "0";
            String curr = rs.getString("b_curr_reading") != null ? rs.getString("b_curr_reading") : "0";

            name.setText(fullName.toUpperCase());
            address.setText(addr.toUpperCase());
            prev_read.setText(prev);
            curr_read.setText(curr);

            String dbMonth = rs.getString("b_month");
            if (dbMonth != null && !dbMonth.isEmpty()) {
                boolean found = false;
                for (int i = 0; i < month.getItemCount(); i++) {
                    if (month.getItemAt(i).toString().equalsIgnoreCase(dbMonth)) {
                        month.setSelectedIndex(i);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.println("Warning: Month '" + dbMonth + "' not found in ComboBox items.");
                }
            }
            String dbDate = rs.getString("b_due_date");
            if (dbDate != null && !dbDate.isEmpty()) {
                try {
                    java.util.Date date = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dbDate);
                    duedate.setDate(date);
                } catch (java.text.ParseException e) {
                    System.out.println("Date Parse Error: " + e.getMessage());
                }
            }

            System.out.println("Loaded details for: " + fullName); 
            
        } else {
            name.setText("NOT FOUND");
            address.setText("");
            prev_read.setText("0");
            curr_read.setText("0");
            duedate.setDate(null);
        }

        rs.close();
        pst.close();
        conn.close();

    } catch (Exception e) {
        System.out.println("Error fetching details: " + e.getMessage());
        e.printStackTrace();
    }
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        month = new javax.swing.JComboBox<>();
        curr_read = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        accnum_drop = new javax.swing.JComboBox<>();
        duedate = new com.toedter.calendar.JDateChooser();
        prev_read = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        SAVE = new javax.swing.JPanel();
        save = new javax.swing.JLabel();
        CANCEL = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        logoutbtn = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        HOME = new javax.swing.JPanel();
        home = new javax.swing.JLabel();
        BILLS = new javax.swing.JPanel();
        bills = new javax.swing.JLabel();
        PROFILE = new javax.swing.JPanel();
        profile = new javax.swing.JLabel();
        SETTINGS = new javax.swing.JPanel();
        settings = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        PROFILE1 = new javax.swing.JPanel();
        profile2 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        name = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        address = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(0, 204, 204));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(0, 153, 153));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Lucida Sans", 3, 24)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Edit Bill");
        jPanel6.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 550, 60));

        jPanel5.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 570, 60));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel6.setText("Due Date:");
        jPanel5.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 180, 210, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel9.setText("Previous kWh:");
        jPanel5.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 260, 140, -1));

        month.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        month.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Month", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "December" }));
        month.setPreferredSize(new java.awt.Dimension(350, 40));
        month.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthActionPerformed(evt);
            }
        });
        jPanel5.add(month, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 210, 200, 40));

        curr_read.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        curr_read.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        curr_read.setPreferredSize(new java.awt.Dimension(350, 40));
        curr_read.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                curr_readKeyTyped(evt);
            }
        });
        jPanel5.add(curr_read, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 290, 200, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel10.setText("Account Number:");
        jPanel5.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 100, 160, -1));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel11.setText("Month:");
        jPanel5.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 180, 110, -1));

        accnum_drop.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        accnum_drop.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Account Number" }));
        accnum_drop.setPreferredSize(new java.awt.Dimension(350, 40));
        accnum_drop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accnum_dropActionPerformed(evt);
            }
        });
        jPanel5.add(accnum_drop, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 130, 420, 40));
        jPanel5.add(duedate, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 210, 200, 40));

        prev_read.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        prev_read.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        prev_read.setPreferredSize(new java.awt.Dimension(350, 40));
        prev_read.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                prev_readKeyTyped(evt);
            }
        });
        jPanel5.add(prev_read, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 290, 200, 40));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel13.setText("Current kWh:");
        jPanel5.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 260, 130, -1));

        SAVE.setBackground(new java.awt.Color(57, 122, 0));
        SAVE.setForeground(new java.awt.Color(0, 153, 153));
        SAVE.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SAVEMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SAVEMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                SAVEMouseExited(evt);
            }
        });
        SAVE.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        save.setBackground(new java.awt.Color(255, 255, 255));
        save.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        save.setForeground(new java.awt.Color(255, 255, 255));
        save.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        save.setText("UPDATE");
        save.setMaximumSize(new java.awt.Dimension(62, 22));
        save.setMinimumSize(new java.awt.Dimension(62, 22));
        save.setPreferredSize(new java.awt.Dimension(62, 22));
        save.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveMouseClicked(evt);
            }
        });
        SAVE.add(save, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 110, 30));

        jPanel5.add(SAVE, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 420, 110, 30));

        CANCEL.setBackground(new java.awt.Color(255, 144, 0));
        CANCEL.setForeground(new java.awt.Color(0, 153, 153));
        CANCEL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CANCELMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CANCELMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                CANCELMouseExited(evt);
            }
        });
        CANCEL.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setBackground(new java.awt.Color(255, 255, 255));
        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("CANCEL");
        jLabel14.setMaximumSize(new java.awt.Dimension(62, 22));
        jLabel14.setMinimumSize(new java.awt.Dimension(62, 22));
        jLabel14.setPreferredSize(new java.awt.Dimension(62, 22));
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel14MouseClicked(evt);
            }
        });
        CANCEL.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 110, 30));

        jPanel5.add(CANCEL, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 420, 110, 30));

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));
        jPanel1.setPreferredSize(new java.awt.Dimension(200, 100));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logoutbtn.setBackground(new java.awt.Color(255, 0, 0));
        logoutbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutbtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutbtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutbtnMouseExited(evt);
            }
        });
        logoutbtn.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("LOGOUT");
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });
        logoutbtn.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 150, -1));
        logoutbtn.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, -1));

        jPanel1.add(logoutbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 420, 170, 40));

        HOME.setBackground(new java.awt.Color(0, 153, 153));
        HOME.setForeground(new java.awt.Color(0, 153, 153));
        HOME.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                HOMEMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                HOMEMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                HOMEMouseExited(evt);
            }
        });
        HOME.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        home.setBackground(new java.awt.Color(255, 255, 255));
        home.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        home.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        home.setText("USERS");
        home.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                homeMouseClicked(evt);
            }
        });
        HOME.add(home, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, 10, 160, -1));

        jPanel1.add(HOME, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 150, 40));

        BILLS.setBackground(new java.awt.Color(0, 204, 204));
        BILLS.setForeground(new java.awt.Color(0, 153, 153));
        BILLS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BILLSMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BILLSMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                BILLSMouseExited(evt);
            }
        });
        BILLS.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bills.setBackground(new java.awt.Color(0, 153, 153));
        bills.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        bills.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bills.setText("BILLS");
        bills.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                billsMouseClicked(evt);
            }
        });
        BILLS.add(bills, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, 10, 150, 20));

        jPanel1.add(BILLS, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 170, 40));

        PROFILE.setBackground(new java.awt.Color(0, 153, 153));
        PROFILE.setForeground(new java.awt.Color(0, 153, 153));
        PROFILE.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PROFILEMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                PROFILEMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                PROFILEMouseExited(evt);
            }
        });
        PROFILE.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        profile.setBackground(new java.awt.Color(255, 255, 255));
        profile.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        profile.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        profile.setText("PAYMENTS");
        profile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                profileMouseClicked(evt);
            }
        });
        PROFILE.add(profile, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 150, -1));

        jPanel1.add(PROFILE, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 170, 40));

        SETTINGS.setBackground(new java.awt.Color(0, 153, 153));
        SETTINGS.setForeground(new java.awt.Color(0, 153, 153));
        SETTINGS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SETTINGSMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SETTINGSMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                SETTINGSMouseExited(evt);
            }
        });
        SETTINGS.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        settings.setBackground(new java.awt.Color(255, 255, 255));
        settings.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        settings.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        settings.setText("SETTINGS");
        settings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                settingsMouseClicked(evt);
            }
        });
        SETTINGS.add(settings, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 140, -1));

        jPanel1.add(SETTINGS, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 150, 40));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/powerr (1).png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 110, 90));

        PROFILE1.setBackground(new java.awt.Color(0, 153, 153));
        PROFILE1.setForeground(new java.awt.Color(0, 153, 153));
        PROFILE1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PROFILE1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                PROFILE1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                PROFILE1MouseExited(evt);
            }
        });
        PROFILE1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        profile2.setBackground(new java.awt.Color(255, 255, 255));
        profile2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        profile2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        profile2.setText("PROFILE");
        profile2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                profile2MouseClicked(evt);
            }
        });
        PROFILE1.add(profile2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 130, -1));

        jPanel1.add(PROFILE1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 350, 170, 40));

        jPanel5.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 150, 500));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel12.setText("Name:");
        jPanel5.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 340, 140, -1));

        name.setEditable(false);
        name.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        name.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        name.setPreferredSize(new java.awt.Dimension(350, 40));
        name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nameKeyTyped(evt);
            }
        });
        jPanel5.add(name, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 370, 200, 40));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel15.setText("Address:");
        jPanel5.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 340, 130, -1));

        address.setEditable(false);
        address.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        address.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        address.setPreferredSize(new java.awt.Dimension(350, 40));
        address.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                addressKeyTyped(evt);
            }
        });
        jPanel5.add(address, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 370, 200, -1));

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void monthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_monthActionPerformed

    private void curr_readKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_curr_readKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != '.') {
            evt.consume(); // This "eats" the keystroke if it's a letter
        }
    }//GEN-LAST:event_curr_readKeyTyped

    private void accnum_dropActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accnum_dropActionPerformed
    Object selected = accnum_drop.getSelectedItem();
        if (selected != null) {
            fetchUserDataByAccNum(selected.toString());
        }
    }//GEN-LAST:event_accnum_dropActionPerformed

    private void prev_readKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_prev_readKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_prev_readKeyTyped

    private void saveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveMouseClicked
    try {
        if (prev_read.getText().isEmpty() || curr_read.getText().isEmpty() || duedate.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Please fill all fields!");
            return;
        }

        double pRead = Double.parseDouble(prev_read.getText());
        double cRead = Double.parseDouble(curr_read.getText());
        double consumption = cRead - pRead;
        double rate = 12.50;
        double total = consumption * rate;

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(duedate.getDate());

        int actualUserID = 0;
        a_config.config conf = new a_config.config();
        java.sql.Connection conn = conf.connectDB();
        String findUser = "SELECT u_id FROM users WHERE u_accnum = ?";
        java.sql.PreparedStatement pst = conn.prepareStatement(findUser);
        pst.setString(1, accnum_drop.getSelectedItem().toString());
        java.sql.ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            actualUserID = rs.getInt("u_id");
        }
        conn.close();

        String sql = "UPDATE bills SET u_id = ?, b_month = ?, b_prev_reading = ?, " +
                     "b_curr_reading = ?, b_amount = ?, b_due_date = ? " +
                     "WHERE b_id = ?";

        boolean success = conf.updateData(sql,
            actualUserID,
            month.getSelectedItem().toString(),
            pRead,
            cRead,
            total,
            formattedDate,
            selectedBillID
        );

        if (success) {
            JOptionPane.showMessageDialog(null, "Bill Updated Successfully!");
            Bills bp = new Bills();
            bp.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Update failed. b_id was: " + selectedBillID);
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Please enter valid numbers for readings!");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Update Failed: " + e.getMessage());
        e.printStackTrace();
    }
    }//GEN-LAST:event_saveMouseClicked

    private void SAVEMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SAVEMouseClicked
        
    }//GEN-LAST:event_SAVEMouseClicked

    private void SAVEMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SAVEMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_SAVEMouseEntered

    private void SAVEMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SAVEMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_SAVEMouseExited

    private void jLabel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseClicked
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to cancel? Any unsaved progress will be lost.",
            "Cancel Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        // 2. If the user clicks 'Yes' (YES_OPTION), then proceed to close and return to dashboard
        if (confirm == JOptionPane.YES_OPTION) {
            admin_dashboard ad = new admin_dashboard();
            ad.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_jLabel14MouseClicked

    private void CANCELMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CANCELMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_CANCELMouseClicked

    private void CANCELMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CANCELMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_CANCELMouseEntered

    private void CANCELMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CANCELMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_CANCELMouseExited

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        session.getInstance().logout(this);
    }//GEN-LAST:event_jLabel7MouseClicked

    private void logoutbtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutbtnMouseClicked

        int confirm = javax.swing.JOptionPane.showConfirmDialog(null,
            "Do you really want to log out?",
            "Logout Confirmation",
            javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            config conf = new config();
            //conf.logEvent("User Logged Out");
            login loginFrame = new login();
            loginFrame.setVisible(true);
            this.dispose();
        } else {
            System.out.println("Logout cancelled by user.");
        }
    }//GEN-LAST:event_logoutbtnMouseClicked

    private void logoutbtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutbtnMouseEntered

    }//GEN-LAST:event_logoutbtnMouseEntered

    private void logoutbtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutbtnMouseExited

    }//GEN-LAST:event_logoutbtnMouseExited

    private void homeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeMouseClicked
        admin_dashboard ud = new admin_dashboard(); // I-pasa ang name
        ud.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_homeMouseClicked

    private void HOMEMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HOMEMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_HOMEMouseClicked

    private void HOMEMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HOMEMouseEntered

    }//GEN-LAST:event_HOMEMouseEntered

    private void HOMEMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HOMEMouseExited

    }//GEN-LAST:event_HOMEMouseExited

    private void billsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_billsMouseClicked
        Bills bp = new Bills(); // Ang page diin makita ang "Add" button
        bp.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_billsMouseClicked

    private void BILLSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BILLSMouseClicked

    }//GEN-LAST:event_BILLSMouseClicked

    private void BILLSMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BILLSMouseEntered

    }//GEN-LAST:event_BILLSMouseEntered

    private void BILLSMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BILLSMouseExited

    }//GEN-LAST:event_BILLSMouseExited

    private void profileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_profileMouseClicked
        Payment1 py = new Payment1();
        py.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_profileMouseClicked

    private void PROFILEMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PROFILEMouseClicked

    }//GEN-LAST:event_PROFILEMouseClicked

    private void PROFILEMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PROFILEMouseEntered

    }//GEN-LAST:event_PROFILEMouseEntered

    private void PROFILEMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PROFILEMouseExited

    }//GEN-LAST:event_PROFILEMouseExited

    private void settingsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsMouseClicked
        Setting st = new Setting();
        st.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_settingsMouseClicked

    private void SETTINGSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SETTINGSMouseClicked

    }//GEN-LAST:event_SETTINGSMouseClicked

    private void SETTINGSMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SETTINGSMouseEntered

    }//GEN-LAST:event_SETTINGSMouseEntered

    private void SETTINGSMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SETTINGSMouseExited

    }//GEN-LAST:event_SETTINGSMouseExited

    private void nameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_nameKeyTyped

    private void addressKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_addressKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_addressKeyTyped

    private void profile2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_profile2MouseClicked
        Profile prf = new Profile();
    prf.setVisible(true);
    this.dispose();
    }//GEN-LAST:event_profile2MouseClicked

    private void PROFILE1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PROFILE1MouseClicked

    }//GEN-LAST:event_PROFILE1MouseClicked

    private void PROFILE1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PROFILE1MouseEntered

    }//GEN-LAST:event_PROFILE1MouseEntered

    private void PROFILE1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PROFILE1MouseExited

    }//GEN-LAST:event_PROFILE1MouseExited

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EditBill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditBill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditBill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditBill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EditBill().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BILLS;
    private javax.swing.JPanel CANCEL;
    private javax.swing.JPanel HOME;
    private javax.swing.JPanel PROFILE;
    private javax.swing.JPanel PROFILE1;
    private javax.swing.JPanel SAVE;
    public javax.swing.JPanel SETTINGS;
    private javax.swing.JComboBox<String> accnum_drop;
    private javax.swing.JTextField address;
    private javax.swing.JLabel bills;
    private javax.swing.JTextField curr_read;
    private com.toedter.calendar.JDateChooser duedate;
    private javax.swing.JLabel home;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel logoutbtn;
    private javax.swing.JComboBox<String> month;
    private javax.swing.JTextField name;
    private javax.swing.JTextField prev_read;
    private javax.swing.JLabel profile;
    private javax.swing.JLabel profile2;
    private javax.swing.JLabel save;
    private javax.swing.JLabel settings;
    // End of variables declaration//GEN-END:variables
}
