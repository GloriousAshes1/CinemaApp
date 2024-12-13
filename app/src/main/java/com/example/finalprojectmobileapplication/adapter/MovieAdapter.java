package com.example.finalprojectmobileapplication.adapter;

import com.example.finalprojectmobileapplication.model.Movie;

import java.util.List;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectmobileapplication.databinding.ItemMovieBinding;
import com.example.finalprojectmobileapplication.model.Movie;
import com.example.finalprojectmobileapplication.util.GlideUtils;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{
    private final List<Movie> mListMovies;
    private final IManagerMovieListener iManagerMovieListener;

    public interface IManagerMovieListener {
        void clickItemMovie(Movie movie);
    }

    public MovieAdapter(List<Movie> mListMovies, IManagerMovieListener iManagerMovieListener) {
        this.mListMovies = mListMovies;
        this.iManagerMovieListener = iManagerMovieListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMovieBinding itemMovieBinding = ItemMovieBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MovieViewHolder(itemMovieBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = mListMovies.get(position);
        if (movie == null) {
            return;
        }
        GlideUtils.loadUrl(movie.getImage(), holder.mItemMovieBinding.imgMovie);
        holder.mItemMovieBinding.tvName.setText(movie.getName());
        holder.mItemMovieBinding.tvBooked.setText(String.valueOf(movie.getBooked()));

        holder.mItemMovieBinding.layoutItem.setOnClickListener(v -> iManagerMovieListener.clickItemMovie(movie));
    }

    @Override
    public int getItemCount() {
        if (mListMovies != null) {
            return mListMovies.size();
        }
        return 0;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        private final ItemMovieBinding mItemMovieBinding;

        public MovieViewHolder(@NonNull ItemMovieBinding itemMovieBinding) {
            super(itemMovieBinding.getRoot());
            this.mItemMovieBinding = itemMovieBinding;
        }
    }
}
