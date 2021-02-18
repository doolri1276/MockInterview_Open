package com.ub_hackers.mockinterview.adapters;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ub_hackers.mockinterview.R;
import com.ub_hackers.mockinterview.activities.other.AnswersActivity;
import com.ub_hackers.mockinterview.models.Attempts;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AttemptAdapter extends RecyclerView.Adapter<AttemptAdapter.ViewHolder> {

    List<Attempts> attemptsList;
    AnswersActivity.AudioPlayer audioPlayer;
    AnswerAdapter.ClickInterface clickInterface;

    public AttemptAdapter(List<Attempts> attemptList, AnswersActivity.AudioPlayer audioPlayer, AnswerAdapter.ClickInterface clickInterface){
        this.attemptsList = attemptList;
        this.audioPlayer = audioPlayer;
        this.clickInterface = clickInterface;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer_attempt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.tvNum.setText((position+1)+"");
        holder.tvDateTime.setText(attemptsList.get(position).getDatetime());
    }

    @Override
    public int getItemCount() {
        return attemptsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNum, tvDateTime;
        ImageView ivPlay, ivTrash;

        View prevPlaying;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvNum = itemView.findViewById(R.id.tv_num);
            tvDateTime = itemView.findViewById(R.id.tv_datetime);
            ivPlay = itemView.findViewById(R.id.iv_play);
            ivTrash = itemView.findViewById(R.id.iv_trash);

            setListeners();
        }

        private void setListeners(){
            ivPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(prevPlaying!=null){
                        boolean same=prevPlaying == v;

                        prevPlaying.setBackground(v.getContext().getDrawable(R.drawable.ic_play_default));
                        audioPlayer.stopPlaying();
                        if(same)return;
                    }

                    prevPlaying = v;
                    audioPlayer.startPlaying(attemptsList.get(getLayoutPosition()).getAudioUrl(), v);
                    v.setBackground(v.getContext().getDrawable(R.drawable.ic_play_selected));

                }
            });

            ivTrash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickInterface.deleteAttempt(getLayoutPosition()+"");
                }
            });
        }

    }
}
