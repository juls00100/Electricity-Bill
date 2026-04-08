package a_config;



public class session {
    private static session instance;

    // Data we want to carry across the session
    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String role;
    private String accnum;
    private String address;
    private String image;
    private String path;
    
    private session() {}

    public static synchronized session getInstance() {
        if (instance == null) {
            instance = new session();
        }
        return instance;
    }

    // --- GETTERS & SETTERS ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getPath() {return path;}

    public void setPath(String path) { this.path = path; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFname() { return firstname; }
    public void setFname(String firstname) { this.firstname = firstname; }

    public String getLname() { return lastname; }
    public void setLname(String lastname) { this.lastname = lastname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAccnum() { return accnum; }
    public void setAccnum(String accnum) { this.accnum = accnum; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    // Call this when the user clicks Logout
    // Inside session.java
    public void logout(javax.swing.JFrame currentFrame) {
        int confirm = javax.swing.JOptionPane.showConfirmDialog(
            currentFrame, 
            "Are you sure you want to logout?", 
            "Logout Confirmation", 
            javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            // Clear all session data
            this.id = 0;
            this.username = null;
            this.firstname = null;
            this.lastname = null;
            this.email = null;
            this.role = null;
            this.accnum = null;

            new d_main.login().setVisible(true);
            currentFrame.dispose();
        }
    }
    
    
}