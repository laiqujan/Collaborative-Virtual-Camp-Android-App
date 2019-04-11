package com.example.mahnoor.CollaborativeVirtualCamp;


import java.util.ArrayList;

public class MyJoinedTeams {

    private String teamName;

    public void setTeamName(String teamName){

        this.teamName = teamName;
    }

    public String getTeamName(){

        return teamName;
    }

    public static ArrayList<MyJoinedTeams> getData(){

        ArrayList<MyJoinedTeams> dataList = new ArrayList<>();

        for(int i = 0; i < 10; i++){

            MyJoinedTeams myJoinedTeams = new MyJoinedTeams();
            myJoinedTeams.setTeamName("Laiq");
            dataList.add(myJoinedTeams);
        }

        return dataList;
    }
}
