import com.yahoo.engine.YahooFantasyEngine;
import com.yahoo.objects.api.YahooApiInfo;
import com.yahoo.objects.league.*;
import com.yahoo.objects.league.transactions.LeagueScoreboard;
import com.yahoo.objects.league.transactions.LeagueTransaction;
import com.yahoo.objects.league.transactions.TransactionPlayersList;
import com.yahoo.objects.players.Player;
import com.yahoo.objects.stats.Stat;
import com.yahoo.objects.team.*;
import com.yahoo.services.LeagueService;
import com.yahoo.services.PlayerService;
import com.yahoo.services.TeamService;
import com.yahoo.services.YahooServiceFactory;
import com.yahoo.services.enums.ServiceType;
import com.yahoo.utils.Oceans;
import com.yahoo.utils.OceansMatchup;
import com.yahoo.utils.oauth.OAuthConnection;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cedric on 10/3/14.
 */
public class MatchupDemo
{
    public static void main( String[] args )
    {
        YahooApiInfo info =
                new YahooApiInfo("dj0yJmk9SG1OeGxuQUhCdWxKJmQ9WVdrOVdGWkJOSEJDTldFbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD1lNw--",
                        "8e6b6c1e5f2d1f4e3b4fd74d428ae38c7a02a8e9");

        YahooFantasyEngine engine = new YahooFantasyEngine(info);
        OAuthConnection oAuthConn = YahooFantasyEngine.getoAuthConn();
        YahooServiceFactory factory = YahooFantasyEngine.getServiceFactory();
        String requestUrl = oAuthConn.retrieveAuthorizationUrl();

        try {
            if (!oAuthConn.connect()) {
                URI uri = new URI(requestUrl);
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(uri);

                System.out.println("Please type in verifier code:");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String verifier = br.readLine();
                oAuthConn.retrieveAccessToken(verifier);
            }
            if (oAuthConn.isAuthorized()) {

                // Create services that talk to Yahoo
                LeagueService gameService = (LeagueService) factory.getService(ServiceType.LEAGUE);
                TeamService teamService = (TeamService) factory.getService(ServiceType.TEAM);

                Oceans oceans = new Oceans(gameService, teamService);
                List<Team> teams = oceans.getTeams();
                Map<Team, OceansMatchup> result = new HashMap<Team, OceansMatchup>(); //Result object of matchup data

                for (Team team : teams) {
                    OceansMatchup oceansMatchup = oceans.createHistoricalMatchup(team);
                    result.put(team, oceansMatchup);
                }

                // write result to text
                FileOutputStream fout = new FileOutputStream("/Users/vithushan/Documents/code/foo.txt");
                ObjectOutputStream oos = new ObjectOutputStream(fout);
                oos.writeObject(result);

                // read text to result object
                FileInputStream fit = new FileInputStream("/Users/vithushan/Documents/code/foo.txt");
                ObjectInputStream ois = new ObjectInputStream(fit);
                Map<Team, OceansMatchup> object = (Map<Team, OceansMatchup>) ois.readObject();

                System.out.println("DONE");

            }
        }
        catch (Exception e)
        {
            System.out.println("Problem with getting accessing url.");
            System.err.println(e);
        }
      }
}
