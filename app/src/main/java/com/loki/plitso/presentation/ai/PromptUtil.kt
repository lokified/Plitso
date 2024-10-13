package com.loki.plitso.presentation.ai

object PromptUtil {
    val OUT_OF_CONTEXT_WARNING =
        """
            Stick to content around nutrition, food, recipes and any relation to it.
             If asked about other things tell one to stick to prompts about, food, nutrition etc
              and don't return a response outside it 
        """.trimIndent()

    fun generativePrompt(
        recipeData: List<RecipeData>,
        pastMeal: List<PastMeal>,
        parameters: GenerativeParameters
    ): String {
        return """
                Using this data $recipeData,
                I want to generate a recipe base on the below parameters.
                Make sure to provide ingredients and instructions too.
                I will be having ${parameters.mealType} from the cuisine ${parameters.cuisine}
                My mood is ${parameters.mood} food. Don't forget I am a ${parameters.dietary}.
                I need a ${if (parameters.isQuick) "Quick" else "normal"} Meal.
                
                To add on, which is important to note consider my past meals which are as provided
                 below $pastMeal.
                 Use the trend to generate the best meal with the data provided.
                If my past meal, reply that there is no data to suggest.
                Tell one to document meals to get better suggestions that fits their eating habits.
                
                If there are no options to recommend from the provided data, Use your data. Don't tell
                 that you don't have the data, be creative when responding.
            """.trimIndent()
    }
}