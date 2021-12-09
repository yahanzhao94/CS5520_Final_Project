package edu.neu.madcourse.cs5520_a7.postService;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.cs5520_a7.R;

public class PostRviewHolder extends RecyclerView.ViewHolder {

  public ImageView photoIcon;
  public TextView postUser;
  public TextView postText;
  public TextView time;
  public TextView location;
  public ImageView likeIcon;


  public PostRviewHolder(View itemView, final ItemClickListener listener) {
    super(itemView);
    photoIcon = itemView.findViewById(R.id.item_icon);
    postUser = itemView.findViewById(R.id.post_user);
    postText = itemView.findViewById(R.id.post_text);
    time = itemView.findViewById(R.id.event_time);
    location = itemView.findViewById(R.id.location);
    likeIcon = itemView.findViewById(R.id.like_icon);

    likeIcon.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (listener != null) {
          int position = getLayoutPosition();
          if (position != RecyclerView.NO_POSITION) {
            listener.onItemClick(position);
          }
        }
      }
    });
  }
}

