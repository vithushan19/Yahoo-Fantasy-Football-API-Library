package com.yahoo.objects.team;

import com.yahoo.objects.stats.Stat;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cedric on 10/29/14.
 */

public class MatchupStatsList implements Serializable
{
    @JsonProperty("stat")
    private List<Stat> stat;

    public List<Stat> getStats() {
        return stat;
    }

    public void setStats(List<Stat> stats) {
        this.stat = stats;
    }
}
