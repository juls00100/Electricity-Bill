package a_config;

import java.sql.Date;

public class billsmodel {
   private int b_id;
    private int u_id;
    private double b_amount;
    private String b_status;
    private String b_month;
    private String b_due_date;
    private double b_prev_reading;
    private double b_curr_reading;
    private double b_kwh_used;
    private String b_date_paid;


    public billsmodel(int b_id, int u_id, double b_amount, String b_status, 
                     String b_month, String b_due_date, double b_prev_reading, 
                     double b_curr_reading, double b_kwh_used, String b_date_paid) {
        this.b_id = b_id;
        this.u_id = u_id;
        this.b_amount = b_amount;
        this.b_status = b_status;
        this.b_month = b_month;
        this.b_due_date = b_due_date;
        this.b_prev_reading = b_prev_reading;
        this.b_curr_reading = b_curr_reading;
        this.b_kwh_used = b_kwh_used;
        this.b_date_paid = b_date_paid;
    }
    public int getB_id() { return b_id; }
    public int getU_id() { return u_id; }
    public double getB_amount() { return b_amount; }
    public String getB_status() { return b_status; }
    public String getB_month() { return b_month; }
    public String getB_due_date() { return b_due_date; }
    public double getB_prev_reading() { return b_prev_reading; }
    public double getB_curr_reading() { return b_curr_reading; }
    public double getB_kwh_used() { return b_kwh_used; }
    public String getB_date_paid() { return b_date_paid; }
    
    public void setB_id(int b_id) { this.b_id = b_id; }
    public void setU_id(int u_id) { this.u_id = u_id; }
    public void setB_amount(double b_amount) { this.b_amount = b_amount; }
    public void setB_status(String b_status) { this.b_status = b_status; }
    public void setB_month(String b_month) { this.b_month = b_month; }
    public void setB_due_date(String b_due_date) { this.b_due_date = b_due_date; }
    public void setB_prev_reading(double b_prev_reading) { this.b_prev_reading = b_prev_reading; }
    public void setB_curr_reading(double b_curr_reading) { this.b_curr_reading = b_curr_reading; }
    public void setB_kwh_used(double b_kwh_used) { this.b_kwh_used = b_kwh_used; }
    public void setB_date_paid(String b_date_paid) { this.b_date_paid = b_date_paid; }
    
    @Override
    public String toString() {
        return "billsmodel{" +
                "b_id=" + b_id +
                ", u_id=" + u_id +
                ", b_amount=" + b_amount +
                ", b_status='" + b_status + '\'' +
                ", b_month='" + b_month + '\'' +
                ", b_due_date='" + b_due_date + '\'' +
                ", b_prev_reading=" + b_prev_reading +
                ", b_curr_reading=" + b_curr_reading +
                ", b_kwh_used=" + b_kwh_used +
                ", b_date_paid='" + b_date_paid + '\'' +
                '}';
    }
    
}

