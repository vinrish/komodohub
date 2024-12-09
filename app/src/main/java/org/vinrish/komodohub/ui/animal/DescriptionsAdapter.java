package org.vinrish.komodohub.ui.animal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.vinrish.komodohub.R;

import java.util.List;

public class DescriptionsAdapter extends RecyclerView.Adapter<DescriptionsAdapter.DescriptionViewHolder>{
    private List<String> descriptions;

    public DescriptionsAdapter(List<String> descriptions) {
        this.descriptions = descriptions;
    }

    @NonNull
    @Override
    public DescriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_description, parent, false);
        return new DescriptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DescriptionViewHolder holder, int position) {
        holder.bind(descriptions.get(position));
    }

    @Override
    public int getItemCount() {
        return descriptions.size();
    }

    static class DescriptionViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionText;

        DescriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            descriptionText = itemView.findViewById(R.id.description_text);
        }

        void bind(String description) {
            descriptionText.setText(description);
        }
    }
}
