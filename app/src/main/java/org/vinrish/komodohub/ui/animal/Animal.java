package org.vinrish.komodohub.ui.animal;

public class Animal {
    private String name;
    private int population;
    private String imageUrl;
    private String documentId;

    public Animal(String documentId, String name, int population, String imageUrl) {
        this.documentId = documentId;
        this.name = name;
        this.population = population;
        this.imageUrl = imageUrl;
    }

    public Animal(String name, int population, String imageUrl) {
        this.name = name;
        this.population = population;
        this.imageUrl = imageUrl;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

