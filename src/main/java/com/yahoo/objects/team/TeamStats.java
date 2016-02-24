package com.yahoo.objects.team;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by cedric on 10/29/14.
 */
public class TeamStats implements Serializable
{
    private String coverage_type;
    private String week;
    private String season;

    @JsonProperty("stats")
    private MatchupStatsList stats;

    public String getCoverage_type() {
        return coverage_type;
    }

    public void setCoverage_type(String coverage_type) {
        this.coverage_type = coverage_type;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public MatchupStatsList getStats() {
        return stats;
    }

    public void setStats(MatchupStatsList stats) {
        this.stats = stats;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }
}
