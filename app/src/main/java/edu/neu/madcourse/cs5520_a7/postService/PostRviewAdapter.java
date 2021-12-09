package edu.neu.madcourse.cs5520_a7.postService;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import edu.neu.madcourse.cs5520_a7.R;
import edu.neu.madcourse.cs5520_a7.stickerService.models.Post;

public class PostRviewAdapter extends RecyclerView.Adapter<PostRviewHolder> {

  private final List<Post> events;
  private final Set<String> likePostIds;

  private ItemClickListener listener;

  //Constructor
  public PostRviewAdapter(List<Post> itemList) {
    this(itemList, null);
  }

  public PostRviewAdapter(List<Post> itemList, Set<String> likePostIds) {
    this.events = itemList;
    this.likePostIds = likePostIds;
  }

  public void setOnItemClickListener(ItemClickListener listener) {
    this.listener = listener;
  }

  @Override
  public PostRviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
    return new PostRviewHolder(view, listener);
  }

  @Override
  public void onBindViewHolder(PostRviewHolder holder, int position) {
    Post currentItem = events.get(position);

    holder.photoIcon.setImageResource(Integer.parseInt(currentItem.photoId));
    holder.postText.setText(currentItem.text);
    holder.location.setText(currentItem.location);
    holder.postUser.setText(currentItem.username);
    holder.time.setText(Instant.ofEpochMilli(currentItem.timestampInMillis).toString());
    if (likePostIds != null) {
      if (likePostIds.contains(currentItem.postId)) {
        holder.likeIcon.setImageResource(R.drawable.like);
      } else {
        holder.likeIcon.setImageResource(R.drawable.like_empty);
      }
    }
  }

  @Override
  public int getItemCount() {
    return events.size();
  }
}