package org.vinrish.komodohub.ui.animal;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.vinrish.komodohub.R;

import java.util.List;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder> {
    private List<Animal> animalList;
    private OnAnimalClickListener listener;

    public interface OnAnimalClickListener {
        void onAnimalClick(Animal animal);
    }

    public AnimalAdapter(List<Animal> animalList, OnAnimalClickListener listener) {
        this.animalList = animalList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_animal_list, parent, false);
        return new AnimalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalViewHolder holder, int position) {
        Animal animal = animalList.get(position);
        Log.d("AnimalAdapter", "Image URL: " + animal.getImageUrl());
        holder.bind(animal, listener);
    }

    @Override
    public int getItemCount() {
        return animalList.size();
    }

    static class AnimalViewHolder extends RecyclerView.ViewHolder {
        ImageView animalImage;
        TextView animalName;
        TextView animalPopulation;

        AnimalViewHolder(@NonNull View itemView) {
            super(itemView);
            animalImage = itemView.findViewById(R.id.animalImageView);
            animalName = itemView.findViewById(R.id.animal_name);
//            animalPopulation = itemView.findViewById(R.id.animal_population);
        }

        void bind(final Animal animal, final OnAnimalClickListener listener) {
            // Use Glide or similar image loading library to load images from URL or resource ID
            Glide.with(itemView.getContext())
                    .load(animal.getImageUrl())
                    .error(R.drawable.tiger)
                    .into(animalImage);

            animalName.setText(animal.getName());
//            animalPopulation.setText("Population: " + animal.getPopulation());

            itemView.setOnClickListener(v -> listener.onAnimalClick(animal));
        }
    }
}