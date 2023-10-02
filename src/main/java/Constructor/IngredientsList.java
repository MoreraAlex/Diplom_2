package Constructor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class IngredientsList {

    public static String ingredients() {
        List<String> ingredients = Arrays.asList(
                "61c0c5a71d1f82001bdaaa6d",
                "61c0c5a71d1f82001bdaaa70",
                "61c0c5a71d1f82001bdaaa71",
                "61c0c5a71d1f82001bdaaa72",
                "61c0c5a71d1f82001bdaaa6e",
                "61c0c5a71d1f82001bdaaa73",
                "61c0c5a71d1f82001bdaaa74",
                "61c0c5a71d1f82001bdaaa6c",
                "61c0c5a71d1f82001bdaaa75",
                "61c0c5a71d1f82001bdaaa76",
                "61c0c5a71d1f82001bdaaa77",
                "61c0c5a71d1f82001bdaaa78",
                "61c0c5a71d1f82001bdaaa79",
                "61c0c5a71d1f82001bdaaa7a"
        );

        Random random = new Random();

        int numberOfIngredients = random.nextInt(10) + 1;

        List<String> randomIngredients = new ArrayList<>();

        for (int i = 0; i < numberOfIngredients; i++) {
            String randomIngredient = ingredients.get(random.nextInt(ingredients.size()));
            randomIngredients.add(randomIngredient);
        }

        JsonObject jsonIngredients = new JsonObject();
        jsonIngredients.add("ingredients", new Gson().toJsonTree(randomIngredients));

        return jsonIngredients.toString();

    }


}
