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

    private TextView monthYearText;
    private RecyclerView calenderRecyclerView;
    private LocalDate selectedDate;

    // the following is for onclick of date
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView tv_AddEvent, tv_ClearAllEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();
    }

    private void initWidgets() {
        calenderRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);

    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);


        CalendarAdapter calenderViewAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calenderRecyclerView.setLayoutManager(layoutManager);
        calenderRecyclerView.setAdapter(calenderViewAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

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

    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public void PreviousMonthAction(View view) {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();

    }

    public void NextMonthAction(View view) {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if(dayText.equals("")){
            String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
        else{
            onClickEvents();
        }
    }

    public void onClickEvents() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View optionsView = getLayoutInflater().inflate(R.layout.date_event_controls, null);
        tv_AddEvent = findViewById(R.id.tv_AddEvent);
        tv_ClearAllEvents = findViewById(R.id.tv_RemoveAllEvents);

        dialogBuilder.setView(optionsView);
        dialog = dialogBuilder.create();
        dialog.show();

    }


    public void AddEventClickListener(View view) {
        Toast.makeText(MainActivity.this, "add Event", Toast.LENGTH_SHORT).show();
        dialog.cancel();

    }

    public void RemoveAllEventsClickListener(View view) {
        Toast.makeText(MainActivity.this, "Delete All Event", Toast.LENGTH_SHORT).show();
        dialog.cancel();
    }
}