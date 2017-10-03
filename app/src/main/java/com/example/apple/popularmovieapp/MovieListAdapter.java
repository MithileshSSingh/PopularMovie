package com.example.apple.popularmovieapp;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by apple on 28/09/17.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieItemViewHolder> {

    public interface ListItemClickListener {
        void onListItemClick(int position);
    }

    private ListItemClickListener mListener;
    private ArrayList<BeanMovie> mList;

    MovieListAdapter(ListItemClickListener listener, ArrayList<BeanMovie> list) {
        this.mList = list;
        this.mListener = listener;
    }

    @Override
    public MovieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false);
        return new MovieItemViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(MovieItemViewHolder holder, int position) {
        BeanMovie beanMovie = mList.get(position);
        holder.apply(beanMovie,position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}

class MovieItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private View mView;
    private Integer mPosition;
    private BeanMovie mData;
    private MovieListAdapter.ListItemClickListener mListener;

    private ImageView ivMoviePoster;

    MovieItemViewHolder(View itemView, MovieListAdapter.ListItemClickListener listener) {
        super(itemView);

        mView = itemView;
        mListener = listener;

        init();
    }

    void init() {
        initView();
        initListener();
    }

    void initView() {
        ivMoviePoster = (ImageView) mView.findViewById(R.id.ivMoviePoster);
    }

    void initListener() {
        ivMoviePoster.setOnClickListener(this);
    }

    void apply(BeanMovie data, Integer position) {
        mData = data;
        mPosition = position;

        Resources r = mView.getContext().getResources();

        String posterPath = mData.getPosterPath();
        String imageSize = r.getString(R.string.size_w185);

        String imagePath = NetworkUtil.buildFetchMoviePosterURL(mView.getContext(), posterPath, imageSize).toString();
        Picasso.with(mView.getContext()).load(imagePath).into(ivMoviePoster);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivMoviePoster:

                mListener.onListItemClick(mPosition);

                break;
        }
    }
}
