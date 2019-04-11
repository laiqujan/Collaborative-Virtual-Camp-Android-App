package com.example.mahnoor.CollaborativeVirtualCamp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by adilr on 6/12/2017.
 */

public class MyAllTeamsAdaptor extends RecyclerView.Adapter<MyAllTeamsAdaptor.CardItemViewHolder> {

    private List<MyAllTeams> data;
    private LayoutInflater layoutInflater;

    public MyAllTeamsAdaptor(Context context, List<MyAllTeams> data){

        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public CardItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.my_teams_card, parent, false);
        MyAllTeamsAdaptor.CardItemViewHolder holder = new MyAllTeamsAdaptor.CardItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CardItemViewHolder holder, int position) {

        MyAllTeams currentObj = data.get(position);
        holder.setData(currentObj, position);
        holder.setListeners();

    }

    @Override
    public int getItemCount() {

        return data.size();
    }

    public void removeItem(int position){

        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,data.size());
    }

    public class CardItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView teamName, delete, files, team;
        int position;
        MyAllTeams current;

        public CardItemViewHolder(View itemView) {
            super(itemView);

            teamName = (TextView) itemView.findViewById(R.id.team_name);
            delete = (TextView) itemView.findViewById(R.id.delete);
            files = (TextView) itemView.findViewById(R.id.files);
            team = (TextView) itemView.findViewById(R.id.team);


        }

        public void setData(MyAllTeams current, int position) {

            this.teamName.setText(current.getTeamName());
            this.current = current;
            this.position = position;
        }

        public void setListeners() {

            delete.setOnClickListener(MyAllTeamsAdaptor.CardItemViewHolder.this);
            files.setOnClickListener(MyAllTeamsAdaptor.CardItemViewHolder.this);
            team.setOnClickListener(MyAllTeamsAdaptor.CardItemViewHolder.this);

        }

        @Override
        public void onClick(View v) {

            switch (v.getId()){

                case R.id.files:

                    break;

                case R.id.team:

                    break;

                case R.id.delete:
                    removeItem(position);
                    break;
            }
        }
    }
}
