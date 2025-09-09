package com.example.nc_parking_new;


// Model class representing a parking slot booking. uses a custom layout file to display each object in the list view.
public class ParkingSlot { // Private fields to store booking details
    private String SlotNumber;
    private String FirstName;
    private String BookingStartTime;
    private String BookingEndTime;
    private String Status;
    private String BookingDate;
    private String UserID;
    private String FinishedTime;



    public ParkingSlot(){ // constructor needed for firebase. used to convert documents to java objects

    }



// getter methods which retrieve the values from the booking detials.

    public String getSlotNumber() { return SlotNumber; }
    public String getFirstName() { return FirstName; }
    public String getStatus() { return Status; }
    public String getBookingStartTime() { return BookingStartTime; }
    public String getBookingEndTime() { return BookingEndTime; }
    public String getBookingDate() { return BookingDate; }
    public String getUserID(){return UserID;}
    public String getFinishedTime(){return FinishedTime;}


// setters allow for the modification of the booking details.
    public void setSlotNumber(String SlotNumber) {
        this.SlotNumber = SlotNumber;
    }

    public void setBookingDate(String BookingDate){
        this.BookingDate =  BookingDate;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public void setBookingStartTime(String BookingStartTime) {
        this.BookingStartTime = BookingStartTime;
    }

    public void setBookingEndTime(String BookingEndTime) {
        this.BookingEndTime = BookingEndTime;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public void setUserID(String UserID){this.UserID = UserID;}

    public void setFinishedTime(String FinishTime){this.FinishedTime=FinishTime;}




}

