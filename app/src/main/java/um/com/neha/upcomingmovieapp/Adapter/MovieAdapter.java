package um.com.neha.upcomingmovieapp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import um.com.neha.upcomingmovieapp.Model.Movie;
import um.com.neha.upcomingmovieapp.Model.Result;
import um.com.neha.upcomingmovieapp.R;

/**
 * Created by Neha on 1/2/18.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private final List<Result> movie_list;
    private final Context context;
    OnItemClickListener mItemClickListener;
    private final String imageUrl;

    public MovieAdapter(Context context, List<Result> movie_list,String imageUrl) {
        this.movie_list = movie_list;
        this.context = context;
        this.imageUrl = imageUrl;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Result result = movie_list.get(position);
        holder.movie_title_textview.setText(result.getTitle());
        holder.release_date_textView.setText(result.getReleaseDate());
        boolean movie_category = result.getAdult();
        // checking condition for movie is adult or not
        if(movie_category){
            holder.category_textView.setText("(A)");
        }else{
            holder.category_textView.setText("(U/A)");
        }
        Picasso.with(context)
                .load(imageUrl+result.getPosterPath())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.movie_thumbnail_imageview);
    }

    @Override
    public int getItemCount() {
        return movie_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView movie_thumbnail_imageview;
        private final TextView movie_title_textview, release_date_textView,category_textView;


        public ViewHolder(View itemView) {
            super(itemView);
            movie_thumbnail_imageview = (ImageView) itemView.findViewById(R.id.movie_thumbnail_imageview);
            movie_title_textview = (TextView) itemView.findViewById(R.id.movie_title_textview);
            release_date_textView = (TextView) itemView.findViewById(R.id.release_date_textView);
            category_textView = (TextView) itemView.findViewById(R.id.category_textView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getPosition());
            }
        }
    }

    // ClickListener for RecyclerView
    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}

