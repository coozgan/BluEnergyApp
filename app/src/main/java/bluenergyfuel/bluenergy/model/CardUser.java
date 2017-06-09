package bluenergyfuel.bluenergy.model;

/**
 * Created by jockinjc0 on 6/9/17.
 */

public class CardUser {
    private String birthday;
    private String cardnumber;
    private String cardtype;
    private String firstname;
    private String lastname;
    private String phone;
    private String transactions;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTransactions() {
        return transactions;
    }

    public void setTransactions(String transactions) {
        this.transactions = transactions;
    }

    public CardUser(String birthday, String cardnumber, String cardType, String firstname, String lastname, String phone, String transactions) {

        this.birthday = birthday;
        this.cardnumber = cardnumber;
        this.cardtype = cardType;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.transactions = transactions;
    }

    public CardUser(){

    }
}
