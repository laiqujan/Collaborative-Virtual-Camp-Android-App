package com.example.mahnoor.CollaborativeVirtualCamp;

import java.util.ArrayList;

public class MyAllTeams {

    private String teamName;

    public String getTeamName(){

        return teamName;
    }
    public void setTeamName(String teamName){

        this.teamName = teamName;
    }

    public static ArrayList<MyAllTeams> getData(){

        ArrayList<MyAllTeams> dataList = new ArrayList<>();

        for(int i = 0; i < 10; i++){

            MyAllTeams myAllTeams = new MyAllTeams();
            myAllTeams.setTeamName("Laiq");
            dataList.add(myAllTeams);
        }

        return dataList;
    }
}
