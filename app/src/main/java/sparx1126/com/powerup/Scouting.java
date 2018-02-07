package sparx1126.com.powerup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import sparx1126.com.powerup.custom_layouts.PlusMinusEditTextLinearLayout;
import sparx1126.com.powerup.data_components.ScoutingData;

public class Scouting extends AppCompatActivity {
    private ScoutingData scoutingData;
    private EditText teamnum;
    private RadioButton blueAlliancecolor;
    private RadioButton redAlliancecolor;
    private EditText matchnum;
    private Button submitbutton;
    private CheckBox autolinecheck;
    private CheckBox scoreswitchcheck;
    private CheckBox scorescalecheck;
    private CheckBox pickupcubecheck;
    private CheckBox cubexchangecheck;
    private RadioButton startLeftbtn ;
    private RadioButton startCenterbtn;
    private RadioButton startRightbtn;
    private PlusMinusEditTextLinearLayout timeScoreswitch;
    private PlusMinusEditTextLinearLayout timeScorescale;
    private PlusMinusEditTextLinearLayout timesPlacedexhange;
    private PlusMinusEditTextLinearLayout timesPickedfromFloor;
    private PlusMinusEditTextLinearLayout cubesfromplayers;
    private CheckBox playeddefense;
    private RadioButton climbRung;
    private RadioButton climbRobot;
    private RadioButton climbDoesnt;
    private CheckBox climbOn;
    private RadioButton hold1;
    private RadioButton hold2;
    private CheckBox climbunder15secs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scouting);

        scoutingData = new ScoutingData();
        teamnum = findViewById(R.id.scouteamnuminput);
        redAlliancecolor = findViewById(R.id.redAlliancebtn);
        blueAlliancecolor = findViewById(R.id.blueAlliancebtn);
        autolinecheck = findViewById(R.id.autolinecheck);
        scorescalecheck = findViewById(R.id.scorescalecheck);
        scoreswitchcheck =findViewById(R.id.scoreswitchcheck);
        pickupcubecheck =findViewById(R.id.pickupcubecheck);
        cubexchangecheck =findViewById(R.id.cubexchangecheck);
        matchnum = findViewById(R.id.matchnumimput);
        timeScoreswitch = findViewById(R.id.timesscoredswitchpicker);
        timeScorescale =findViewById(R.id.timesscoredscalepicker);
        timesPlacedexhange =findViewById(R.id.timesplacedexchangepicker);
        timesPickedfromFloor =findViewById(R.id.cubesfromfloorpicker);
        cubesfromplayers =findViewById(R.id.cubesfromfloorpicker);
        startLeftbtn =findViewById(R.id.startLeftbtn);
        startCenterbtn =findViewById(R.id.startCenterbtn);
        startRightbtn = findViewById(R.id.startRightbtn);
        playeddefense=findViewById(R.id.playeddefensecheck);
        climbRung=findViewById(R.id.climbRung);
        climbRobot=findViewById(R.id.climbRobot);
        climbDoesnt=findViewById(R.id.climbDoesnt);
        climbOn=findViewById(R.id.climbOn);
        hold1=findViewById(R.id.ClimbOn1);
        hold2=findViewById(R.id.ClimbOn2);
        climbunder15secs=findViewById(R.id.Climb15secs);
        submitbutton=findViewById(R.id.submitbutton);

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!teamnum.getText().toString().isEmpty()) {
                    scoutingData.setTeamnumber(Integer.parseInt(teamnum.getText().toString()));
                }
                if(redAlliancecolor.isChecked()){
                    scoutingData.setAllianceColor("Red");
                }
                else if(blueAlliancecolor.isChecked()){
                    scoutingData.setAllianceColor("Blue");

                }
                if(!teamnum.getText().toString().isEmpty()) {
                    scoutingData.setMatchnum(Integer.parseInt(matchnum.getText().toString()));
                }
                scoutingData.setAutolinecheck(autolinecheck.isChecked());
                scoutingData.setScorescalecheck(scorescalecheck.isChecked());
                scoutingData.setScoreswitchcheck(scoreswitchcheck.isChecked());
                scoutingData.setPickedupcubecheck(pickupcubecheck.isChecked());
                scoutingData.setCubexhangecheck(cubexchangecheck.isChecked());
                if(startLeftbtn.isChecked()){
                    scoutingData.setStartingPosition("Left");
                }
                else if(startCenterbtn.isChecked()){
                    scoutingData.setStartingPosition("Center");
                }
                else if(startRightbtn.isChecked()){
                    scoutingData.setStartingPosition("Right");
                }
                scoutingData.setTimesscoredswitch(timeScoreswitch.getValue());
                scoutingData.setTimescoredscale(timeScorescale.getValue());
                scoutingData.setTimesplacedexchange(timesPlacedexhange.getValue());
                scoutingData.setTimesPickedfromfloor(timesPickedfromFloor.getValue());
                scoutingData.setCubesfromplayers(cubesfromplayers.getValue());
                scoutingData.setPlayeddefense(playeddefense.isChecked());
                if(climbRung.isChecked()){
                    scoutingData.setClimbmethod(" Climbed on Rung");
                }
                else if(climbRobot.isChecked()){
                    scoutingData.setClimbmethod(" Climbed onOther Robot");
                }
                else if(climbDoesnt.isChecked()){
                    scoutingData.setClimbmethod("Didn't Climb");
                }
                scoutingData.setClimbOn(climbOn.isChecked());
                if(hold1.isChecked()){
                    scoutingData.setCanHold(" Holds 1 Robot");
                }
                else if(hold2.isChecked()){
                    scoutingData.setCanHold(" Holds 2 Robots");
                }
                scoutingData.setClimbunder15secs(climbunder15secs.isChecked());

                Log.d("scoutingdata", scoutingData.toString());

            }

        });

}}