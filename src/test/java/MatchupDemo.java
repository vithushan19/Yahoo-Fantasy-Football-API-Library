import com.yahoo.engine.YahooFantasyEngine;
import com.yahoo.objects.api.YahooApiInfo;
import com.yahoo.objects.league.League;
import com.yahoo.objects.league.LeagueSettings;
import com.yahoo.objects.league.transactions.LeagueTransaction;
import com.yahoo.objects.league.transactions.TransactionPlayersList;
import com.yahoo.objects.players.Player;
import com.yahoo.objects.team.*;
import com.yahoo.services.LeagueService;
import com.yahoo.services.PlayerService;
import com.yahoo.services.TeamService;
import com.yahoo.services.YahooServiceFactory;
import com.yahoo.services.enums.ServiceType;
import com.yahoo.utils.oauth.OAuthConnection;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Created by cedric on 10/3/14.
 */
public class MatchupDemo
{
    public static void main( String[] args )
    {
        // TODO Probably want to config this out
        YahooApiInfo info =
                new YahooApiInfo("dj0yJmk9SG1OeGxuQUhCdWxKJmQ9WVdrOVdGWkJOSEJDTldFbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD1lNw--",
                        "8e6b6c1e5f2d1f4e3b4fd74d428ae38c7a02a8e9");

        //OAuthConnection oAuthConn =  new OAuthConnection();
        //oAuthConn.initService(info);
        YahooFantasyEngine engine = new YahooFantasyEngine(info);
        OAuthConnection oAuthConn = YahooFantasyEngine.getoAuthConn();
        YahooServiceFactory factory = YahooFantasyEngine.getServiceFactory();
        String requestUrl = oAuthConn.retrieveAuthorizationUrl();

        try
        {
            if(!oAuthConn.connect())
            {
                URI uri = new URI(requestUrl);
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(uri);

                System.out.println("Please type in verifier code:");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String verifier = br.readLine();
                oAuthConn.retrieveAccessToken(verifier);
            }
            if(oAuthConn.isAuthorized())
            {


                LeagueService gameService = (LeagueService)factory.getService(ServiceType.LEAGUE);
                TeamService teamService = (TeamService)factory.getService(ServiceType.TEAM);

                List<League> leagues = gameService.getUserLeagues("nba");
                for(League league : leagues)
                {
                    System.out.println(league.toString());
                }
                League testLeauge = leagues.get(0);

                List<Team> teamList = teamService.getLeagueTeams(testLeauge.getLeague_key());
                Map<String, TeamStandings> standingsMap = gameService.getLeagueStandings(testLeauge.getLeague_key());

                for(Team team : teamList)
                {
                    // Print ranks
                    TeamStandings standings = standingsMap.get(team.getTeam_key());
                    //System.out.println(team.getName() + "\t Rank:" + standings.getRank());
                }

                Team demoTeam = teamList.get(0);

                List<TeamStat> teamStats = teamService.getWeeklyTeamPointsForSeason(demoTeam.getTeam_key());
                System.out.println("Team weekly points are:");
                for(TeamStat teamStat : teamStats)
                {
                    System.out.println("Week : " + teamStat.getTeam_points().getWeek() + " Actual pts. : "+ teamStat.getTeam_points().getTotal());
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("Problem with getting accessing url.");
        }

    }
}
