package us.libertybaptist.calenderapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    // textview for the month and year at top of calender
    private TextView monthYearText;

    // recycler view for the days in each month
    private RecyclerView calenderRecyclerView;

    //local date will be the selected date when clicked.
    private LocalDate selectedDate;

    // the following is for onclick of date
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView tvSelectedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();
    }

    //initialize the calender recycler view and textview for month/year at top of calender
    private void initWidgets() {
        calenderRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);

    }

    //sets the view for the month, creates array for days in currently selected month and calls
    //adapter. Invokes the layout manager and sets to gridlayout for the calender. sets the layout
    // manager and the adapter
    private void setMonthView() {
        //sets the text of the month/year at top of calender
        monthYearText.setText(monthYearFromDate(selectedDate));

        //method call to get the days in current month tp fill array
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);


        CalendarAdapter calenderViewAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calenderRecyclerView.setLayoutManager(layoutManager);
        calenderRecyclerView.setAdapter(calenderViewAdapter);
    }

    //helper method to fill daysInMonth array. takes in the local date.
    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        //setting the text in each grid location for the days of the month by filling the array
        for(int i = 1; i <= 42; i++ ){
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek){
                daysInMonthArray.add("");

            }
            else{
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    //formatter for the date
    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    //button click event for the previous month arrow
    public void PreviousMonthAction(View view) {
        //sets selectedDate to previous month
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();

    }

    //button click event for the next month arrow
    public void NextMonthAction(View view) {
        //sets selectedDate to next month
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    //onItemClick handles the click event for each day of the month
    @Override
    public void onItemClick(int position, String dayText) {
        //toast for empty squares
        if(dayText.equals("")){
            String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
        //calls onClickEvents for each real day of the month
        else{

            onClickEvents(dayText);
        }
    }

    //launches the layout for options on each day for event adding and viewing
    public void onClickEvents(String dayText) {


        String message =  dayText + " " + monthYearFromDate(selectedDate);


        dialogBuilder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
        final View optionsView = getLayoutInflater().inflate(R.layout.date_event_controls, null);

        dialogBuilder.setTitle(message);
        dialogBuilder.setView(optionsView);
        dialog = dialogBuilder.create();
        dialog.show();


    }

    // takes user to layout for adding events for that day
    public void AddEventClickListener(View view) {
        Toast.makeText(MainActivity.this, "add Event", Toast.LENGTH_SHORT).show();

        dialog.cancel();

    }

    // takes user to layout for viewing all events for that day
    public void RemoveAllEventsClickListener(View view) {
        Toast.makeText(MainActivity.this, "View All Events", Toast.LENGTH_SHORT).show();
        dialog.cancel();
    }
}