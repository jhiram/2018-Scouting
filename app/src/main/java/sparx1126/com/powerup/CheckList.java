package sparx1126.com.powerup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;

import sparx1126.com.powerup.utilities.DataCollection;

public class CheckList extends AppCompatActivity {

    private TableLayout masterTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checklist);

        DataCollection data = DataCollection.getInstance();

        /*
           .-"""""""-.
         .'       __  \_
        /        /  \/  \
       |         \_0/\_0/______
       |:.          .'       oo`\
       |:.         /             \
       |' ;        |             |
       |:..   .     \_______     |
       |::.|'     ,  \,_____\   /
       |:::.; ' | .  '|  ====)_/===;===========;()
       |::; | | ; ; | |            # # # #::::::
      /::::.|-| |_|-|, \           # # # #::::::
     /'-=-'`  '-'   '--'\          # # # #::::::
    /                    \         # # # #::::::
                                   # # # # # # #
                                   # # # # # # #
                                   # # # # # # #
                                   # # # # # # #
                                   # # # # # # #
                                   # # # # # # #

                ALL OF THIS IS JUST PLACEHOLDER, NEED TO UPDATE WITH NEW DATA STRUCTURES
                ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         */

//        Map<Integer, List<ScoutingData>> scoutingData = RETRIEVE FROM NEW DATA;
//        Map<String, BlueAllianceTeam> teamsInEvent = RETRIEVE FROM NEW DATA;
//
//        masterTable = findViewById(R.id.masterTable);
//
//        if (teamsInEvent != null) {
//            for (String key : teamsInEvent.keySet()) {
//                TableRow child = new TableRow(this);
//                TextView teamNumber = new TextView(this);
//                teamNumber.setText(key.substring(3));
//                teamNumber.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                child.addView(teamNumber);
//                masterTable.addView(child);
//            }
//        }
//
//
//        if (scoutingData != null && scoutingData.keySet().size() > 5) {
//            for (int teamNumber : scoutingData.keySet()) {
//                int numTimesScouted = 0;
//                for (int teamNum2 : scoutingData.keySet()) {
//                    if (teamNum2 == teamNumber) {
//                        numTimesScouted++;
//                    }
//                }
//                TableRow child = new TableRow(this);
//                TextView scoutedInfo = new TextView(this);
//                scoutedInfo.setText("Scouted " + numTimesScouted + " Times");
//                if (numTimesScouted == 0) {
//                    scoutedInfo.setTextColor(Color.RED);
//                }
//                child.addView(scoutedInfo);
//                masterTable.addView(child);
//            }
//        }
//
//        if (scoutingData != null && scoutingData.keySet().size() > 5) {
//            for (int teamNumber : scoutingData.keySet()) {
//                int numTimesScouted = 0;
//                for (int teamNum2: scoutingData.keySet()) {
//                    if(teamNum2 == teamNumber) {
//                        numTimesScouted++;
//                    }
//                }
//                TableRow child = new TableRow(this);
//                TextView scoutedInfo = new TextView(this);
//                scoutedInfo.setText("Scouted " + numTimesScouted + " Times");
//                if(numTimesScouted == 0) {
//                    scoutedInfo.setTextColor(Color.RED);
//                }
//                child.addView(scoutedInfo);
//                masterTable.addView(child);
//            }
//        }


    }


}

