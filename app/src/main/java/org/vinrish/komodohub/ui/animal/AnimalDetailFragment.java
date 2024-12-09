package org.vinrish.komodohub.ui.animal;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.vinrish.komodohub.R;
import org.vinrish.komodohub.databinding.FragmentAnimalDetailBinding;

import java.util.ArrayList;
import java.util.List;

public class AnimalDetailFragment extends Fragment {

    private FragmentAnimalDetailBinding binding;
    private FirebaseFirestore db;
    private DescriptionsAdapter descriptionAdapter;
    private List<String> descriptions;
    private String animalDocumentId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAnimalDetailBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();

        AnimalDetailFragmentArgs args = AnimalDetailFragmentArgs.fromBundle(getArguments());
        animalDocumentId = args.getAnimalDocumentId();

        binding.detailAnimalName.setText(args.getAnimalName());

        String imageUrl = args.getAnimalImageUrl();
        System.out.println("Image URL: " + imageUrl);

        Glide.with(requireContext())
                .asBitmap()
                .load(args.getAnimalImageUrl())
                .error(R.drawable.tiger)
                .into(binding.detailAnimalImage);

        binding.detailAnimalPopulation.setText("Population: " + args.getAnimalPopulation());

        binding.recyclerViewDescriptions.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewDescriptions.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int spacing = 8;
                outRect.top = spacing;
                outRect.bottom = spacing;
            }
        });
        descriptions = new ArrayList<>();
        descriptionAdapter = new DescriptionsAdapter(descriptions);
        binding.recyclerViewDescriptions.setAdapter(descriptionAdapter);

        // Fetch descriptions from Firestore
        fetchDescriptions(animalDocumentId);

        binding.buttonAddDescription.setOnClickListener(v -> {
            if (isUserLoggedIn()) {
                showAddDescriptionDialog();
            } else {
                // Show a message or redirect to login
                Toast.makeText(getContext(), "Please log in to add a description", Toast.LENGTH_SHORT).show();
                redirectToLogin();
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.nav_animal);
            }
        });

    }

    private boolean isUserLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    private void redirectToLogin() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.loginFragment);
    }

    private void showAddDescriptionDialog() {
        // Create an EditText input field for the dialog
        EditText input = new EditText(getContext());
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Add Description")
                .setMessage("Enter a new description for the animal")
                .setView(input)
                .setPositiveButton("Add", (dialogInterface, i) -> {
                    String descriptionText = input.getText().toString().trim();
                    if (!descriptionText.isEmpty()) {
                        addDescriptionToFirestore(descriptionText);
                    } else {
                        Toast.makeText(getContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void addDescriptionToFirestore(String descriptionText) {
        // Create a new description object
        Description description = new Description(descriptionText);

        // Add description to Firestore under the animal's subcollection "descriptions"
        db.collection("animals")
                .document(animalDocumentId)
                .collection("descriptions")
                .add(description)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Description added", Toast.LENGTH_SHORT).show();
                    descriptions.add(descriptionText);
                    descriptionAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to add description", Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchDescriptions(String documentId) {
        db.collection("animals")
                .document(documentId)
                .collection("descriptions")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> descriptionsList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Fetch and add each description to the list
                            Description description = document.toObject(Description.class);
                            descriptionsList.add(description.getDescription());
                        }
                        descriptions.clear();
                        descriptions.addAll(descriptionsList);
                        descriptionAdapter.notifyDataSetChanged();
                        System.out.println("Descriptions fetched: " + descriptionsList);
                    } else {
                        binding.detailAnimalPopulation.setText("Failed to load descriptions.");
                        System.err.println("Error fetching descriptions: " + task.getException());
                    }
                });
    }

    private void navigateBackToAnimalFragment() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_animalDetailFragment_to_nav_animal);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}