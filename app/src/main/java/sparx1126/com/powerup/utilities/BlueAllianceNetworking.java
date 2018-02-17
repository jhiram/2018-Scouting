package sparx1126.com.powerup.utilities;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import sparx1126.com.powerup.data_components.BlueAllianceEvent;
import sparx1126.com.powerup.data_components.BlueAllianceMatch;
import sparx1126.com.powerup.data_components.BlueAllianceTeam;

public class BlueAllianceNetworking {
    public interface Callback {
        abstract void handleFinishDownload();
    }

    private static final String TAG = "BlueAllianceNetworking ";
    private static final String BLUE_ALLIANCE_BASE_URL ="http://www.thebluealliance.com/api/v3/";
    // header for authetication
    private static final String BLUE_ALLIANCE_AUTH_HEADER = "X-TBA-Auth-Key";
    // Key generated in thebluealliance.com for access
    // This key is Hiram's key (expires in 30 days)
    private static final String BLUE_ALLIANCE_KEY = "0i1rgva3Y8G14rZS4dWDHcPNaw6EVMb9uSI9jW7diochnHpH8Y4nIhT0iHwj0hCq";
    private static final String YEAR = "2017";
    private static final String SPARX_TEAM_KEY = "frc1126";
    // intention is for {event_key} to be substituted
    private static String EVENT_TEAMS_URL_TAIL = "event/{event_key}/teams";
    // intention is for {team_key} to be substituted
    private static String TEAM_EVENTS_URL_TAIL = "team/{team_key}/events/" + YEAR;
     private static String EVENT_MATCHES_URL_TAIL = "event/{event_key}/matches/simple";

    private OkHttpClient regularHttpClient;
    private static BlueAllianceNetworking instance;
    private JSONParser jsonParser;
    private static FileIO fileIO;
    private static DataCollection dataCollection;

    // synchronized means that the method cannot be executed by two threads at the same time
    // hence protected so that it always returns the same instance
    public static synchronized BlueAllianceNetworking getInstance() {
        if (instance == null)
            instance = new BlueAllianceNetworking();
        return instance;
    }

    private BlueAllianceNetworking() {
        regularHttpClient = new OkHttpClient();
        jsonParser = JSONParser.getInstance();
        fileIO = FileIO.getInstance();
        dataCollection = DataCollection.getInstance();
    }

    public void downloadEventsSparxsIsIn(Context _context, Callback _callback) {
        downloadTeamEvents(SPARX_TEAM_KEY, _context, _callback);
    }

    // made this private because why do we care of other team events???
    private void downloadTeamEvents(String _key, final Context _context, final Callback _callback) {
        String url_tail = (TEAM_EVENTS_URL_TAIL).replace("{team_key}", _key);
        downloadBlueAllianceData(url_tail, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                throw new AssertionError(e.getMessage() + this);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    Map<String, BlueAllianceEvent> rtnMap = jsonParser.teamEventsStringIntoMap(data);
                    dataCollection.setEventsWeAreIn(rtnMap);
                    fileIO.storeTeamEvents(data);
                    _callback.handleFinishDownload();
                }
                else {
                    throw new AssertionError(response.message() + this);
                }
            }
        });
    }

    public void downloadEventTeams(String _key, final Context _context, final Callback _callback) {
        String url_tail = (EVENT_TEAMS_URL_TAIL).replace("{event_key}", _key);
        downloadBlueAllianceData(url_tail, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                throw new AssertionError(e.getMessage() + this);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    Map<String, BlueAllianceTeam> rtnMap = jsonParser.eventTeamsStringIntoMap(data);
                    fileIO.storeEventTeams(data);
                    dataCollection.setTeamsInEvent(rtnMap);
                    _callback.handleFinishDownload();
                }
                else {
                    throw new AssertionError(response.message() + this);
                }
            }
        });
    }

    public void downloadEventMatches(String _eventKey, final Context _context, final Callback _callback) {
        String url_tail = (EVENT_MATCHES_URL_TAIL).replace("{event_key}", _eventKey);
        downloadBlueAllianceData(url_tail, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                throw new AssertionError(e.getMessage() + this);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    Map<String, BlueAllianceMatch> rtnMap = jsonParser.eventMatchesStringIntoMap(data);
                    fileIO.storeEventMatches(data);
                    dataCollection.setEventMatches(rtnMap);
                    _callback.handleFinishDownload();
                }
                else {
                    throw new AssertionError(response.message() + this);
                }
            }
        });
    }

    private void downloadBlueAllianceData(String _url_tail, okhttp3.Callback _callback) {
        String url = BLUE_ALLIANCE_BASE_URL + _url_tail;
        Log.d(TAG, url);
        Request requestEvents = new Request.Builder()
                .addHeader(BLUE_ALLIANCE_AUTH_HEADER, BLUE_ALLIANCE_KEY)
                .url(url)
                .build();

        regularHttpClient.newCall(requestEvents).enqueue(_callback);
    }
}
