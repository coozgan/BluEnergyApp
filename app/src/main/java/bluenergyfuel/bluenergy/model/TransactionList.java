package bluenergyfuel.bluenergy.model;

/**
 * Created by jockinjc0 on 4/16/17.
 */

public class TransactionList {

    private String date;
    private String amount;
    private String time;
    private String station;

    public TransactionList() {
        //empty constructor
    }

    public TransactionList(String date, String amount, String time, String station) {
        this.date = date;
        this.amount = amount;
        this.time = time;
        this.station = station;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }
}
