import com.yahoo.engine.YahooFantasyEngine;
import com.yahoo.objects.api.YahooApiInfo;
import com.yahoo.objects.league.*;
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

import java.util.List;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Map;

/**
 * Created by cedric on 10/3/14.
 */
public class Demo
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
                URI uri = new java.net.URI(requestUrl);
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
                PlayerService playerService = (PlayerService)factory.getService(ServiceType.PLAYER);

                List<League> leagues = gameService.getUserLeagues("nba");
                for(League league : leagues)
                {
                    System.out.println(league.toString());
                }
                League testLeauge = leagues.get(0);

                List<Team> teamList = teamService.getLeagueTeams(testLeauge.getLeague_key());
                Map<String, TeamStandings> standingsMap = gameService.getLeagueStandings(testLeauge.getLeague_key());
                LeagueSettings demoLeagueSettings = gameService.getLeagueSettings(testLeauge.getLeague_key());

                for(Team team : teamList)
                {
                    TeamStandings standings = standingsMap.get(team.getTeam_key());
                    System.out.println(team.getName() + "\t Rank:" + standings.getRank());
                }

                Team demoTeam = teamList.get(0);

                Roster demoRoster = teamService.getTeamRoster(demoTeam.getTeam_key(), 1);
                WeekRosterPlayers demoRosterPlayers = demoRoster.getPlayers();
                List<Player> demoRosterPlayersList = demoRosterPlayers.getPlayer();
                System.out.println("Roster of "+ demoTeam.getName());
                for (Player p : demoRosterPlayersList)
                {
                    System.out.println(p.getName().getFull());
                    System.out.println("Stats for "+ p.getName().getFull());
                    System.out.println();
                }

                List<RosterStats> rosterStatsList = teamService.getWeeklyTeamRosterPoints(demoTeam.getTeam_key(), 1);
                for(RosterStats rosterStat : rosterStatsList)
                {
                    System.out.println("Week 1 Actual pts. for "+rosterStat.getPlayerKey()+ "("+
                            rosterStat.getSelectedPosition() +") : "+
                            rosterStat.getPlayerPoints());
                }
                List<LeagueTransaction> transactions = gameService.getLeagueTransactions(testLeauge.getLeague_key());
                for(LeagueTransaction transaction : transactions)
                {

                    System.out.println("Transaction "+ transaction.getTransaction_id()+ " is of " + transaction.getType()+" type that was "+ transaction.getStatus());
                    if (transaction.getPlayers() != null && transaction.getPlayers().getPlayer().size()>0)
                    {
                        System.out.println("Players Involved in Transaction");
                        TransactionPlayersList trxPlayers = transaction.getPlayers();
                        for (Player p : trxPlayers.getPlayer())
                        {
                            System.out.println(p.getName().getFull());
                        }
                    }
                }

            }
        }
        catch (Exception e)
        {
            System.out.println("Problem with getting accessing url.");
        }

    }
}
