package com.yahoo.utils;

import com.yahoo.objects.stats.Stat;
import com.yahoo.objects.team.Team;
import com.yahoo.objects.team.TeamList;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by vithushan on 2/24/16.
 */
public class OceansMatchup implements Serializable {

    private Team team1;
    private Team team2;

    private Map<String, List<Stat>> stats1;
    private Map<String, List<Stat>> stats2;

    public OceansMatchup(Team team1, Team team2, Map<String, List<Stat>> stats1, Map<String, List<Stat>> stats2) {
        this.team1 = team1;
        this.team2 = team2;
        this.stats1 = stats1;
        this.stats2 = stats2;
    }

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public Map<String, List<Stat>> getStats1() {
        return stats1;
    }

    public void setStats1(Map<String, List<Stat>> stats1) {
        this.stats1 = stats1;
    }

    public Map<String, List<Stat>> getStats2() {
        return stats2;
    }

    public void setStats2(Map<String, List<Stat>> stats2) {
        this.stats2 = stats2;
    }
}
