package sparx1126.com.powerup;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sparx1126.com.powerup.custom_layouts.CustomExpandableListAdapter;
import sparx1126.com.powerup.data_components.BenchmarkData;
import sparx1126.com.powerup.data_components.ScoutingData;
import sparx1126.com.powerup.utilities.DataCollection;

import static java.lang.Character.isUpperCase;

public class View extends AppCompatActivity {
    private static final String TAG = "View ";
    private static DataCollection dataCollection;
    private List<Integer> teamsInEvent;

    private AutoCompleteTextView teamnumber;
    private ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view);

        dataCollection = DataCollection.getInstance();
        teamsInEvent = dataCollection.getTeamsInEvent();

        teamnumber = findViewById(R.id.team_number);
        teamnumber.setTransformationMethod(null);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, R.layout.custom_list_item, teamsInEvent);
        teamnumber.setAdapter(adapter);
        teamnumber.setThreshold(1);
        teamnumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String teamNumberStringg = teamnumber.getText().toString();
                    int teamNumber = Integer.valueOf(teamNumberStringg);
                    boolean teamNumberFound = teamsInEvent.contains(teamNumber);
                    HashMap<String, List<String>> expandableListDetail = new HashMap<>();
                    if (teamNumberFound) {
                        Log.d(TAG, teamNumberStringg);
                        expandableListDetail = getData(teamNumber);
                        android.view.View view = findViewById(android.R.id.content).getRootView();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    }
                    List<String> expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
                    ExpandableListAdapter expandableListAdapter = new CustomExpandableListAdapter(View.this, expandableListTitle, expandableListDetail);
                    expandableListView.setAdapter(expandableListAdapter);

                } catch (Exception e) {
                    Log.e("Problem w/ team # txt", e.toString());
                }
            }
        });

        expandableListView = findViewById(R.id.expandableListView);
    }

    private HashMap<String, List<String>> getData(int _teamNumber) {
        HashMap<String, List<String>> expandableListDetail = new HashMap<>();

        expandableListDetail.put("Benchmark", getBenchmarkData(_teamNumber));
        expandableListDetail.put("Scouting", getScoutingData(_teamNumber));

        return expandableListDetail;
    }

    private List<String> getScoutingData(int _teamNumber) {
        List<String> rtnList = new ArrayList<>();

        Map<Integer, ScoutingData> datas = dataCollection.getScoutingDatasForTeam(_teamNumber);

        float numberOfDatas = datas.size();

        rtnList.add("<b>\tMatches scouted: </b>" + numberOfDatas);
        // Notice no String Map. It would look wrong to show ALL strings for ALL scouting in one field
        Map<String, Integer> countValueSumsMap = new HashMap<>();
        Map<String, Integer> averagesValueSumsMap = new HashMap<>();
        for (ScoutingData data : datas.values()) {
            Map<String, Boolean> booleanValuesMap = data.getBooleanValuesMap();
            for (String key : booleanValuesMap.keySet()) {
                int increment = 0;
                if (booleanValuesMap.get(key)) {
                    if (countValueSumsMap.containsKey(key)) {
                        increment = countValueSumsMap.get(key) + 1;
                    } else {
                        increment = 1;
                    }
                }
                countValueSumsMap.put(key, increment);
            }

            Map<String, Integer> intValuesMap = data.getIntValuesMap();
            for (String key : intValuesMap.keySet()) {
                // excluding team number and match number
                if (!key.equals(ScoutingData.TEAM_NUMBER) && !key.equals(ScoutingData.MATCH_NUMBER)) {
                    int total = intValuesMap.get(key);
                    if (averagesValueSumsMap.containsKey(key)) {
                        total += averagesValueSumsMap.get(key);
                    }
                    averagesValueSumsMap.put(key, total);
                }
            }
        }

        Map<String, Map<String, Object>> categoriesMap = new HashMap<>();

        for (Map.Entry<String, Integer> entry : countValueSumsMap.entrySet()) {
            String[] nameParts = entry.getKey().split("_");
            String category = nameParts[0];
            String name = nameParts[1];
            Map<String, Object> dataMap = new HashMap<>();
            if(categoriesMap.containsKey(category)) {
                dataMap = categoriesMap.get(category);
            }
            dataMap.put(name, entry.getValue());
            categoriesMap.put(category, dataMap);
        }
        for (Map.Entry<String, Integer> entry : averagesValueSumsMap.entrySet()) {
            String[] nameParts = entry.getKey().split("_");
            String category = nameParts[0];
            String name = nameParts[1];
            float average = entry.getValue() / numberOfDatas;
            Map<String, Object> dataMap = new HashMap<>();
            if(categoriesMap.containsKey(category)) {
                dataMap = categoriesMap.get(category);
            }
            dataMap.put(name, average);
            categoriesMap.put(category, dataMap);
        }

        for (Map.Entry<String, Map<String, Object>> entry : categoriesMap.entrySet()) {
            rtnList.add("<font color=\"yellow\"><b>\t" + fixKey(entry.getKey()) + ":</b></font>");
            for (Map.Entry<String, Object> entryData : entry.getValue().entrySet()) {
                if(entryData.getValue() instanceof Integer) {
                    Integer value = (Integer)entryData.getValue();
                    rtnList.add("<font color=\"yellow\">\t\t" + fixKey(entryData.getKey()) + ":  </font>" + value + " times");
                } else if (entryData.getValue() instanceof Float){
                    Float value = (Float)entryData.getValue();
                    String valueStrg = String.format("%.2f", value);
                    rtnList.add("<font color=\"yellow\">\t\t" + fixKey(entryData.getKey()) + ":  </font>" + valueStrg + " ave");
                }
            }
        }

        return rtnList;
    }

    private List<String> getBenchmarkData(int _teamNumber) {
        List<String> rtnList = new ArrayList<>();

        BenchmarkData data = dataCollection.getBenchmarkData(_teamNumber);

        if (data == null) {
            rtnList.add("<b>\tHas not been benchmarked!</b>");
        } else {
            Map<String, Map<String, Object>> categoriesMap = new HashMap<>();

            for (Map.Entry<String, String> entry : data.getStringValuesMap().entrySet()) {
                String[] nameParts = entry.getKey().split("_");
                String category = nameParts[0];
                String name = nameParts[1];
                Map<String, Object> dataMap = new HashMap<>();
                if(categoriesMap.containsKey(category)) {
                    dataMap = categoriesMap.get(category);
                }
                dataMap.put(name, entry.getValue());
                categoriesMap.put(category, dataMap);
            }
            for (Map.Entry<String, Boolean> entry : data.getBooleanValuesMap().entrySet()) {
                String[] nameParts = entry.getKey().split("_");
                String category = nameParts[0];
                String name = nameParts[1];
                Map<String, Object> dataMap = new HashMap<>();
                if(categoriesMap.containsKey(category)) {
                    dataMap = categoriesMap.get(category);
                }
                dataMap.put(name, trimBoolean(entry.getValue()));
                categoriesMap.put(category, dataMap);
            }
            for (Map.Entry<String, Integer> entry : data.getIntValuesMap().entrySet()) {
                String[] nameParts = entry.getKey().split("_");
                String category = nameParts[0];
                String name = nameParts[1];
                Map<String, Object> dataMap = new HashMap<>();
                if(categoriesMap.containsKey(category)) {
                    dataMap = categoriesMap.get(category);
                }
                dataMap.put(name, entry.getValue());
                categoriesMap.put(category, dataMap);
            }
            for (Map.Entry<String, Float> entry : data.getFloatValuesMap().entrySet()) {
                String[] nameParts = entry.getKey().split("_");
                String category = nameParts[0];
                String name = nameParts[1];
                Map<String, Object> dataMap = new HashMap<>();
                if(categoriesMap.containsKey(category)) {
                    dataMap = categoriesMap.get(category);
                }
                String valueStrg = String.format("%.2f", entry.getValue());
                dataMap.put(name, valueStrg);
                categoriesMap.put(category, dataMap);
            }

            for (Map.Entry<String, Map<String, Object>> entry : categoriesMap.entrySet()) {
                rtnList.add("<font color=\"yellow\"><b>\t" + fixKey(entry.getKey()) + ":</b></font>");
                for (Map.Entry<String, Object> entryData : entry.getValue().entrySet()) {
                    rtnList.add("<font color=\"yellow\">\t\t" + fixKey(entryData.getKey()) + ":  </font>" + entryData.getValue());
                }
            }
        }

        return rtnList;
    }

    public String trimBoolean(Boolean b) {
        if(b) {
            return "Yes";
        } else {
            return "No ";
        }
    }

    public String fixKey(String key) {
        String fixedKey = "";
        for(int i = 0; i < key.length(); i++) {
            if(isUpperCase(key.charAt(i))) {
                fixedKey += " ";
                fixedKey += key.charAt(i);
            } else if(key.charAt(i) == '_') {
                fixedKey += " ";
            } else {
                fixedKey += key.charAt(i);
            }
        }
        fixedKey = Character.toUpperCase((fixedKey.charAt(0))) + fixedKey.substring(1);
        return fixedKey;
    }
}
