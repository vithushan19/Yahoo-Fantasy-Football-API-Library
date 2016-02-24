/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yahoo.objects.stats;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

/**
 *
 * @author DMDD
 */

@JsonIgnoreProperties(ignoreUnknown = true)

public class Stat implements Serializable
{
    private int table_stat_id;
    String stat_id;
    private String value;

    

    public int getTable_stat_id() {
        return table_stat_id;
    }

    public void setTable_stat_id(int table_stat_id) {
        this.table_stat_id = table_stat_id;
    }

 

    public String getStat_id() {
        return stat_id;
    }

    public void setStat_id(String stat_id) {
        this.stat_id = stat_id;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
