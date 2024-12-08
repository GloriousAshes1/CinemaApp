package com.example.finalprojectmobileapplication.adapter.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectmobileapplication.constant.ConstantKey;
import com.example.finalprojectmobileapplication.databinding.ItemMovieAdminBinding;
import com.example.finalprojectmobileapplication.model.Movie;
import com.example.finalprojectmobileapplication.util.GlideUtils;

import java.util.List;

public class AdminMovieAdapter extends RecyclerView.Adapter<AdminMovieAdapter.MovieViewHolder> {
    private Context context;
    private final List<Movie> movieList;
    private final IManagerMovieListener iManagerMovieListener;

    public interface IManagerMovieListener{
        void editMovie(Movie movie);

        void deleteMovie(Movie movie);

        void clickItemMovie(Movie movie);
    }

    public AdminMovieAdapter(Context context, List<Movie> movieList, IManagerMovieListener iManagerMovieListener){
        this.context = context;
        this.movieList = movieList;
        this.iManagerMovieListener = iManagerMovieListener;
    }

    public void release(){
        if(context != null){
            context = null;
        }
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMovieAdminBinding itemMovieAdminBinding = ItemMovieAdminBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MovieViewHolder(itemMovieAdminBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        if(movie == null){
            return;
        }

        GlideUtils.loadUrl(movie.getImage(), holder.itemMovieAdminBinding.imgMovie);
        holder.itemMovieAdminBinding.tvName.setText(movie.getName());
        holder.itemMovieAdminBinding.tvCategory.setText(movie.getCategoryName());
        holder.itemMovieAdminBinding.tvDescription.setText(movie.getDescription());

        String strPrice = movie.getPrice() + ConstantKey.UNIT_CURRENCY_MOVIE;
        holder.itemMovieAdminBinding.tvPrice.setText(strPrice);
        holder.itemMovieAdminBinding.tvDate.setText(movie.getDate());

        holder.itemMovieAdminBinding.imgEdit.setOnClickListener(view -> iManagerMovieListener.editMovie(movie));
        holder.itemMovieAdminBinding.imgDelete.setOnClickListener(view -> iManagerMovieListener.deleteMovie(movie));
        holder.itemMovieAdminBinding.layoutItem.setOnClickListener(view -> iManagerMovieListener.clickItemMovie(movie));
    }

    @Override
    public int getItemCount() {
        if(movieList != null){
            return movieList.size();
        }
        return 0;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder{
        private final ItemMovieAdminBinding itemMovieAdminBinding;

        public MovieViewHolder(ItemMovieAdminBinding itemMovieAdminBinding1){
            super(itemMovieAdminBinding1.getRoot());
            this.itemMovieAdminBinding = itemMovieAdminBinding1;
        }
    }
}
