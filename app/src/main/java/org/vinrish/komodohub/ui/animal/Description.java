package org.vinrish.komodohub.ui.animal;

public class Description {
    private String description;

    public Description() {
        // Default constructor needed for Firestore
    }

    public Description(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
