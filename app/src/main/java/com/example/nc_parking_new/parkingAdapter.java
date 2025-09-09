package com.example.nc_parking_new;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class parkingAdapter extends ArrayAdapter<ParkingSlot> {  // Custom Adapter for displaying parking slot bookings
    /**
     * this adapter connects the model class and the custom layout file to allow for information from the database
     * to be displayed at runtime.
     *
     */

    public parkingAdapter(Context context, List<ParkingSlot> parkingSlotList) {
        super(context, 0, parkingSlotList); // this constructor initialises the adapter with a context and the data list.
    }

    @Override // Called for each row in the ListView to generate a view
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.parking_info_list_item, parent, false);
        }

        ParkingSlot info = getItem(position); //  Get the ParkingSlot object at the given position in the list
        // Bind UI components  from parking_info_list_item.xml
        TextView SlotNumber = convertView.findViewById(R.id.SlotNumber);
        TextView BookingStartTime = convertView.findViewById(R.id.BookingStartTime);
        TextView BookingEndTime = convertView.findViewById(R.id.BookingEndTime);
        TextView Status = convertView.findViewById(R.id.Status);
        TextView BookingDate = convertView.findViewById(R.id.BookingDate);
        TextView FirstName = convertView.findViewById(R.id.FirstName);
        TextView FinishTime = convertView.findViewById(R.id.FinishTime);

        if (info != null) {  // If `info` is not null, populate the TextViews with data from the `ParkingSlot` object

            SlotNumber.setText(info.getSlotNumber()); // Set the text of each TextView to the corresponding field in the `ParkingSlot` object
            BookingStartTime.setText(info.getBookingStartTime());
            BookingEndTime.setText(info.getBookingEndTime());
            Status.setText(info.getStatus());
            BookingDate.setText(info.getBookingDate());
            FirstName.setText(info.getFirstName());
            FinishTime.setText(info.getFinishedTime());
        }

        return convertView; // Return the completed view to be displayed
    }
}
