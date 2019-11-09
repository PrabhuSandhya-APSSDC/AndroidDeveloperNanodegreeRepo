package sandhya.prabhu.in.ndbakingapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Recipe {

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("ingredients")
    List<Ingredients> ingredientsList;

    @SerializedName("steps")
    List<Steps> stepsList;

    @SerializedName("servings")
    private Integer servings;

    @SerializedName("image")
    private String image;

    public Recipe(Integer id, String name, List<Ingredients> ingredientsList, List<Steps> stepsList, Integer servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredientsList = ingredientsList;
        this.stepsList = stepsList;
        this.servings = servings;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredients> getIngredientsList() {
        return ingredientsList;
    }

    public void setIngredientsList(List<Ingredients> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public List<Steps> getStepsList() {
        return stepsList;
    }

    public void setStepsList(List<Steps> stepsList) {
        this.stepsList = stepsList;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
