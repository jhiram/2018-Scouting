package sparx1126.com.powerup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.ToggleButton;

import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sparx1126.com.powerup.data_components.BlueAllianceEvent;
import sparx1126.com.powerup.utilities.DataCollection;
import sparx1126.com.powerup.utilities.Logger;


public class Admin extends AppCompatActivity {
    private static final String TAG = "Admin";
    private Spinner eventSpinner;
    private List<String> eventsWeAreInList;
    private List<String> eventSpinnerList;
    private Map<String, String> eventNameToKeyMap;
    private boolean eventSelected = false;
    private boolean eventFilter = true;
    private ToggleButton blueSelectedToggle;
    private RadioButton teamNumberOneSelectedButton;
    private RadioButton teamNumber2SelectedButton;
    private RadioButton teamNumber3SelectedButton;
    private boolean teamNumberSelected = false;
    private Button stuffSelectedButton;
    private static DataCollection dataCollection;
    private SharedPreferences settings;
    private static Logger logger;
    private boolean blueSelected;
    private int teamNumberIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);
        logger = Logger.getInstance();
        settings = getSharedPreferences(getResources().getString(R.string.pref_name), 0);
        dataCollection = DataCollection.getInstance();


        settings = getSharedPreferences(getResources().getString(R.string.pref_name), 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.apply();


        eventSpinner = (Spinner) findViewById(R.id.eventSpinner);
        Map<String, BlueAllianceEvent> eventsWeAreIn = dataCollection.getEventsWeAreIn();
        eventSpinnerList = new ArrayList<>();
        eventSpinnerList.add("Select Event");
        for (String eventName : eventsWeAreIn.keySet()) {
            eventSpinnerList.add(eventName);
        }

        ArrayAdapter<String> eventAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, eventSpinnerList);
        eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSpinner.setAdapter(eventAdapter);



        eventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!getEventName().isEmpty()) {

                    if(!getEventName().contains("Select Event")) {
                        eventSelected = true;
                    }
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(getResources().getString(R.string.pref_event), getEventName());
                    editor.apply();


                    showButtons();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //eventsNearTodayList = new ArrayList<>();
        eventsWeAreInList = new ArrayList<>();

        eventNameToKeyMap = new HashMap<>();

        blueSelectedToggle = (ToggleButton) findViewById(R.id.blueSelectedToggle);

        teamNumberOneSelectedButton = (RadioButton) findViewById(R.id.team1);
        teamNumberOneSelectedButton.setOnClickListener(teamNumberSelectionFunction);

        teamNumber2SelectedButton = (RadioButton) findViewById(R.id.team2);
        teamNumber2SelectedButton.setOnClickListener(teamNumberSelectionFunction);
        teamNumber2SelectedButton.setVisibility(View.INVISIBLE);

        teamNumber3SelectedButton = (RadioButton) findViewById(R.id.team3);
        teamNumber3SelectedButton.setOnClickListener(teamNumberSelectionFunction);

        stuffSelectedButton = (Button) findViewById(R.id.selectStuff);
        stuffSelectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean blueSelected = blueSelectedToggle.isChecked();
                SharedPreferences.Editor editor = settings.edit();
                String selectedTeam = "red" + teamNumberIndex;
                if(blueSelected) {
                    selectedTeam = "blue" + teamNumberIndex;
                }
                editor.putString("selectedTeam", selectedTeam);
                editor.putString("selectedEvent", getEventName());
                editor.apply();
                finish();
            }
        });
        showButtons();
        restorePreferences();
    }

    private final View.OnClickListener teamNumberSelectionFunction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            teamNumberIndex = Integer.MAX_VALUE;
            if (teamNumberOneSelectedButton.isChecked()) {
                teamNumberIndex = 0;
            } else if (teamNumber2SelectedButton.isChecked()) {
                teamNumberIndex = 1;
            } else if (teamNumber3SelectedButton.isChecked()) {
                teamNumberIndex = 2;
            }

            if (teamNumberIndex != Integer.MAX_VALUE) {
                teamNumberSelected = true;
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt(getString(R.string.team_selected), teamNumberIndex);
                editor.apply();

            }
            else {
                teamNumberSelected = false;
            }
            showButtons();
        }
    };


    private String getEventName() {
        String eventName = "";
        if (eventSpinner.getSelectedItem() != null) {
            eventName = eventSpinner.getSelectedItem().toString();
        }
        return eventName;
    }

    private void restorePreferences() {
        String spinnerEvent = settings.getString("selectedEvent", "DNE");

//        // Initializing a String Array
//       final String[] EVENTCHOICES = new String[]{
//                "Select Event",
//                "2018ohcl",
//                "2018nyro",
//        };
//
//        if(spinnerEvent.toLowerCase().equals("2018ohcl")) {
//            eventSpinner.setSelection(1);
//        }
//        if(spinnerEvent.toLowerCase().equals("2018nyro")) {
//            eventSpinner.setSelection(2);
//        } else {
//            eventSpinner.setSelection(0);
//        }
        ArrayList<String> newEventSpinnerList = new ArrayList<>();

        for(String event: eventSpinnerList) {
            if(event.equals(spinnerEvent)) {
                newEventSpinnerList.add(0, event);
            } else {
                newEventSpinnerList.add(event);
            }
        }

        ArrayAdapter<String> eventAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, newEventSpinnerList);
        eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSpinner.setAdapter(eventAdapter);


        if(!spinnerEvent.equals("DNE")) {
            eventSelected = true;
            String eventName = settings.getString(getResources().getString(R.string.pref_event), "");
            if (!eventName.isEmpty() && (eventAdapter != null)) {
                setupEventSpinner(eventsWeAreInList);
                eventSpinner.setSelection(eventAdapter.getPosition(eventName));
            }
            showButtons();
            String teamStringIndex = settings.getString("selectedTeam", "could not get selected team");
            if (teamStringIndex.charAt(0) == 'b') {
                blueSelectedToggle.setChecked(true);
            } else {
                blueSelectedToggle.setChecked(false);
            }

            int teamIndex = Integer.parseInt(teamStringIndex.substring(teamStringIndex.length() - 1));

            if (teamIndex == 0) {
                teamNumberOneSelectedButton.setChecked(true);
            } else if (teamIndex == 1) {
                teamNumber2SelectedButton.setChecked(true);
            } else if (teamIndex == 2) {
                teamNumber3SelectedButton.setChecked(true);
            }
        }
    }


    private void setupEventSpinner(List<String> eventList) {
        eventSpinnerList.clear();
        for (String eventName : eventList) {
            eventSpinnerList.add(eventName);
        }

        ArrayAdapter<String> eventAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, eventSpinnerList);
        eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSpinner.setAdapter(eventAdapter);
    }



    private void showButtons() {
        if (eventSelected) {
            stuffSelectedButton.setVisibility(View.VISIBLE);
            blueSelectedToggle.setVisibility(View.VISIBLE);
            teamNumberOneSelectedButton.setVisibility(View.VISIBLE);
            teamNumber2SelectedButton.setVisibility(View.VISIBLE);
            teamNumber3SelectedButton.setVisibility(View.VISIBLE);
        } else {
            stuffSelectedButton.setVisibility(View.INVISIBLE);
            blueSelectedToggle.setVisibility(View.INVISIBLE);
            teamNumberOneSelectedButton.setVisibility(View.INVISIBLE);
            teamNumber2SelectedButton.setVisibility(View.INVISIBLE);
            teamNumber3SelectedButton.setVisibility(View.INVISIBLE);
        }
    }
}
