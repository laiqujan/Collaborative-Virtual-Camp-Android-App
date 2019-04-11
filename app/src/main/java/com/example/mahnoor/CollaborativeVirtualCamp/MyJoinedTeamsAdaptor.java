package com.example.mahnoor.CollaborativeVirtualCamp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class MyJoinedTeamsAdaptor extends RecyclerView.Adapter<MyJoinedTeamsAdaptor.CardItemViewHolder> {

    private List<MyJoinedTeams> data;
    private LayoutInflater layoutInflater;

    public MyJoinedTeamsAdaptor(Context context, List<MyJoinedTeams> data){

        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public CardItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.joined_teams_card, parent, false);
        CardItemViewHolder holder = new CardItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CardItemViewHolder holder, int position) {

        MyJoinedTeams currentObj = data.get(position);
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

        TextView teamName;
        ImageView leave;
        int position;
        MyJoinedTeams current;

        public CardItemViewHolder(View itemView) {
            super(itemView);

            teamName = (TextView) itemView.findViewById(R.id.textView_team_tittle);
            leave = (ImageView) itemView.findViewById(R.id.image_leave);

        }

        public void setData(MyJoinedTeams current, int position) {

            this.teamName.setText(current.getTeamName());
            this.current = current;
            this.position = position;
        }

        public void setListeners() {

            leave.setOnClickListener(CardItemViewHolder.this);

        }

        @Override
        public void onClick(View v) {

                    removeItem(position);
        }
    }
}
