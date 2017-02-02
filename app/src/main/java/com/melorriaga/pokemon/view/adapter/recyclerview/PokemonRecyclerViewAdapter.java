package com.melorriaga.pokemon.view.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.melorriaga.pokemon.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by melorriaga on 2/2/17.
 */
public class PokemonRecyclerViewAdapter extends RecyclerView.Adapter<PokemonRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {

    private List<String> mPokemonNames;
    private View.OnClickListener mOnClickListener;

    public List<String> getPokemonNames() {
        return mPokemonNames;
    }

    public void setPokemonNames(List<String> pokemonNames) {
        mPokemonNames = pokemonNames;
        notifyDataSetChanged();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_pokemon, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String pokemonName = mPokemonNames.get(position);
        holder.update(pokemonName);
    }

    @Override
    public int getItemCount() {
        return mPokemonNames == null ? 0 : mPokemonNames.size();
    }

    @Override
    public void onClick(View v) {
        mOnClickListener.onClick(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pokemon_name)
        TextView pokemonNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void update(String pokemonName) {
            pokemonNameTextView.setText(pokemonName);
        }

    }

}
