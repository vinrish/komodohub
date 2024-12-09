package org.vinrish.komodohub.ui.animal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.vinrish.komodohub.R;
import org.vinrish.komodohub.databinding.FragmentAddAnimalBinding;

public class AddAnimalFragment extends Fragment {

    private FragmentAddAnimalBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddAnimalBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

//        if (mAuth.getCurrentUser() == null) {
//            NavController navController = Navigation.findNavController(view);
//            navController.navigate(R.id.loginFragment);
//            return;
//        }

        binding.btnAddAnimal.setOnClickListener(v -> addAnimal());
    }

    private void addAnimal() {
        String animalName = binding.etAnimalName.getText().toString().trim();
        String populationStr = binding.etAnimalPopulation.getText().toString().trim();
        String imageUrl = binding.etImageUrl.getText().toString().trim();
        String description = binding.etDescription.getText().toString().trim();

        if (TextUtils.isEmpty(animalName)) {
            Toast.makeText(getContext(), "Animal name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(populationStr)) {
            Toast.makeText(getContext(), "Population is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(imageUrl)) { // Validate image URL
            Toast.makeText(getContext(), "Image URL is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(description)) { // Validate description
            Toast.makeText(getContext(), "Description is required", Toast.LENGTH_SHORT).show();
            return;
        }

        int population = Integer.parseInt(populationStr);

        // Create Animal object with the image URL
        Animal animal = new Animal(animalName, population, imageUrl);

        // Add animal document to Firestore
        db.collection("animals")
                .add(animal)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Animal added successfully", Toast.LENGTH_SHORT).show();
                    // You can clear the fields here
                    addDescription(documentReference.getId(), description);

                    binding.etAnimalName.setText("");
                    binding.etAnimalPopulation.setText("");
                    binding.etImageUrl.setText("");
                    binding.etDescription.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error adding animal", Toast.LENGTH_SHORT).show();
                });
    }

    private void addDescription(String animalId, String description) {
        // Reference to the sub-collection "descriptions" under the specific animal document
        CollectionReference descriptionsRef = db.collection("animals")
                .document(animalId)
                .collection("descriptions");

        // Create a new description document
        descriptionsRef.add(new Description(description))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Description added successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error adding description", Toast.LENGTH_SHORT).show();
                });
    }

    private String getAnimalIdFromArgs() {
        // Logic to get the animal ID, either from arguments or saved state.
        return "some_animal_id";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}