package org.vinrish.komodohub.ui.animal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.vinrish.komodohub.databinding.FragmentAnimalBinding;

import java.util.ArrayList;
import java.util.List;

public class AnimalFragment extends Fragment implements AnimalAdapter.OnAnimalClickListener {

    private AnimalAdapter adapter;
    private FragmentAnimalBinding binding;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAnimalBinding.inflate(inflater, container, false);

        db = FirebaseFirestore.getInstance();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Fetch animals from Firestore
        fetchAnimalsFromFirestore();
    }

    private void fetchAnimalsFromFirestore() {
        db.collection("animals")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Animal> animalList = new ArrayList<>();
                        QuerySnapshot documents = task.getResult();

                        for (DocumentSnapshot document : documents) {
                            String documentId = document.getId();
                            String name = document.getString("name");
                            int population = document.getLong("population").intValue();
                            String imageUrl = document.getString("imageUrl");

                            // Pass documentId to the Animal object
                            Animal animal = new Animal(documentId, name, population, imageUrl);
                            animalList.add(animal);
                        }

                        adapter = new AnimalAdapter(animalList, this);
                        binding.recyclerViewAnimals.setAdapter(adapter);
                    } else {
                        // Handle failure
                    }
                });
    }

    @Override
    public void onAnimalClick(Animal animal) {
        AnimalFragmentDirections.ActionAnimalFragmentToAnimalDetailFragment action =
                AnimalFragmentDirections.actionAnimalFragmentToAnimalDetailFragment(
                        animal.getDocumentId(),
                        animal.getName(),
                        animal.getImageUrl(),
                        animal.getPopulation()
                );
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}