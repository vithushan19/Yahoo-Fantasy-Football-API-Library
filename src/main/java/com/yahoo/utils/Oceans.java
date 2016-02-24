package com.yahoo.utils;

import com.yahoo.engine.YahooFantasyEngine;
import com.yahoo.objects.api.YahooApiInfo;
import com.yahoo.objects.league.League;
import com.yahoo.objects.league.LeagueMatchup;
import com.yahoo.objects.league.StatCategory;
import com.yahoo.objects.league.transactions.LeagueScoreboard;
import com.yahoo.objects.stats.Stat;
import com.yahoo.objects.team.Team;
import com.yahoo.objects.team.TeamStandings;
import com.yahoo.services.LeagueService;
import com.yahoo.services.TeamService;
import com.yahoo.services.YahooServiceFactory;
import com.yahoo.services.enums.ServiceType;
import com.yahoo.utils.oauth.OAuthConnection;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.*;
import java.util.List;

/**
 * Created by vithushan on 2/24/16.
 */
public class Oceans {

    private static final String OCEANS_LEAGUE_KEY = "353.l.630";
    private static final String GOTTA_BE_KD = "353.l.630.t.7";
    private List<Team> mTeams;
    private Map<String, TeamStandings> mStandings;
    private Map<String, StatCategory> mStatCategories;
    private Map<Integer, LeagueScoreboard> mScoreboard;
    private LeagueService gameService;
    private TeamService teamService;

    public Oceans (LeagueService leagueService, TeamService teamService) {
        this.gameService = leagueService;
        this.teamService = teamService;

        initialize();
    }

    private void initialize() {
        League league = gameService.getLeague(OCEANS_LEAGUE_KEY);
        List<Team> teamList = teamService.getLeagueTeams(league.getLeague_key());

        Map<String, TeamStandings> standingsMap = gameService.getLeagueStandings(OCEANS_LEAGUE_KEY);
        Map<String, StatCategory> statCategoryMap = gameService.getLeagueCategories(OCEANS_LEAGUE_KEY);
        Map<Integer, LeagueScoreboard> leagueScoreboard = new LinkedHashMap<Integer, LeagueScoreboard>();

        int currentWeek = Integer.valueOf(league.getCurrent_week());
        int startWeek = Integer.valueOf(league.getStart_week());


        for (int i = startWeek; i <= currentWeek; i++) {
            leagueScoreboard.put(i, gameService.getWeeklyScoreBoard(OCEANS_LEAGUE_KEY, i));
        }

        mTeams = Collections.unmodifiableList(teamList);
        mStandings = Collections.unmodifiableMap(standingsMap);
        mStatCategories = Collections.unmodifiableMap(statCategoryMap);
        mScoreboard = Collections.unmodifiableMap(leagueScoreboard);

    }

    private Team getGottaBeKD() {

        for (Team team : mTeams) {
            if (GOTTA_BE_KD.equals(team.getTeam_key())) {
                return team;
            }
        }
        return null;
    }

    public List<Team> getTeams() {
        return mTeams;
    }

    public OceansMatchup createHistoricalMatchup(Team oppTeam) {
        Team myTeam = getGottaBeKD();
        return createHistoricalMatchup(myTeam, oppTeam);
    }

    public OceansMatchup createHistoricalMatchup(Team myTeam, Team oppTeam) {

        Map<String, List<Stat>> myStats = new HashMap<String, List<Stat>>();
        Map<String, List<Stat>> theirStats = new HashMap<String, List<Stat>>();

        for (Map.Entry<Integer, LeagueScoreboard> entry : mScoreboard.entrySet()) {
            LeagueScoreboard leagueScoreboard = entry.getValue();
            List<LeagueMatchup> leagueMatchups = leagueScoreboard.getMatchups().getMatchup();
            String week = leagueScoreboard.getWeek();

            for (LeagueMatchup matchup : leagueMatchups) {
                Team matchupTeam1 = matchup.getTeams().getTeam().get(0);
                Team matchupTeam2 = matchup.getTeams().getTeam().get(1);

                if (matchupTeam1.getTeam_key().equals(myTeam.getTeam_key())) {
                    List<Stat> stats = matchupTeam1.getTeam_stats().getStats().getStats();
                    theirStats.put(week, stats);
                }

                if (matchupTeam2.getTeam_key().equals(myTeam.getTeam_key())) {
                    List<Stat> stats = matchupTeam2.getTeam_stats().getStats().getStats();
                    theirStats.put(week, stats);
                }

                if (matchupTeam1.getTeam_key().equals(oppTeam.getTeam_key())) {
                    List<Stat> stats = matchupTeam1.getTeam_stats().getStats().getStats();
                    myStats.put(week, stats);
                }

                if (matchupTeam2.getTeam_key().equals(oppTeam.getTeam_key())) {
                    List<Stat> stats = matchupTeam2.getTeam_stats().getStats().getStats();
                    myStats.put(week, stats);
                }
            }
        }

        OceansMatchup result = new OceansMatchup(myTeam,oppTeam,myStats,theirStats);
        return result;
    }
}
