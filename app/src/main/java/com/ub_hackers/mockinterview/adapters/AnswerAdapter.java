package com.ub_hackers.mockinterview.adapters;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.ub_hackers.mockinterview.Go;
import com.ub_hackers.mockinterview.R;
import com.ub_hackers.mockinterview.activities.other.AnswersActivity;
import com.ub_hackers.mockinterview.custom.CustomDialog;
import com.ub_hackers.mockinterview.models.Question;
import com.ub_hackers.mockinterview.singletons.LoginedUser;
import org.jetbrains.annotations.NotNull;


public class AnswerAdapter extends FirebaseRecyclerAdapter<Question, AnswerAdapter.AnswerViewHolder> {

    AnswersActivity.AudioPlayer audioPlayer;
    DatabaseReference mReference;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AnswerAdapter(@NonNull @NotNull FirebaseRecyclerOptions<Question> options) {
        super(options);
    }

    public void setAudioPlayer(AnswersActivity.AudioPlayer audioPlayer){
        this.audioPlayer = audioPlayer;
        return;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull final AnswerViewHolder holder, int position, @NonNull @NotNull Question model) {
        holder.tvQuestion.setText(model.getQuestionText());

        if(model.getAttempts().size()>0){
            holder.rvAttempts.setVisibility(View.VISIBLE);
            holder.adapter = new AttemptAdapter( model.getAttempts(), audioPlayer, new ClickInterface(){
                @Override
                public void deleteAttempt(String attemptIdx) {
                    holder.deleteAttempt(attemptIdx);
                }
            });
            holder.rvAttempts.setAdapter(holder.adapter);
        }else{
            holder.rvAttempts.setVisibility(View.GONE);
        }

    }

    @NonNull
    @NotNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer_q, parent, false);
        return new AnswerViewHolder(view);
    }

    class AnswerViewHolder extends RecyclerView.ViewHolder{
        ToggleButton tbBookmark, tbOpen;
        TextView tvQuestion, tvRetry;
        LinearLayout llOpen;
        RecyclerView rvAttempts;
        AttemptAdapter adapter;

        public AnswerViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tbBookmark = itemView.findViewById(R.id.tb_bookmark);
            tbBookmark.setVisibility(View.INVISIBLE);
            tbOpen = itemView.findViewById(R.id.tb_open);
            tbOpen.setVisibility(View.INVISIBLE);
            tvQuestion = itemView.findViewById(R.id.tv_question);
            tvRetry = itemView.findViewById(R.id.tv_retry);
            llOpen = itemView.findViewById(R.id.ll_open);
            rvAttempts = itemView.findViewById(R.id.rv_attempts);

            setListners();
        }

        public void setListners(){
            tvRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("questionPk", Integer.parseInt(getItem(getLayoutPosition()).getQuestionPk()));

                    Go.goToInterviewActivity(itemView.getContext(), bundle);
                }
            });
        }

        public void deleteAttempt(final String attemptsIdx){
            final CustomDialog dialog = new CustomDialog(itemView.getContext());
            dialog.setTitle("Are you sure you want to delete?")
                    .setMessage("This action cannot be undone.")
                    .setCanceledOnTouchOutside(true)
                    .setRight("Accept", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mReference.child("users").child(LoginedUser.getInstance().getUserPk()).child("questions").child(getItem(getLayoutPosition()).getQuestionPk()).child("attempts").child(attemptsIdx).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialog.dialog.dismiss();
                                }
                            });
                        }
                    }).setLeft("Cancel", null).create().show();

        }
    }

    public interface ClickInterface{
        void deleteAttempt(String attemptIdx);
    }
}
