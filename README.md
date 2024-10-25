# Plitso
This is an offline first recipe android app. Lists different recipes from the [MealDB API](https://www.themealdb.com/api.php). One can document their
daily meals in the app and use AI to generate meal suggestions.

## Demo/screenshots
| **Home Screen**            | **Recipes Screen**           | **Recipe Detail Screen**       |
|:--------------------------:|:----------------------------:|:------------------------------:|
| ![Home Screen Image](https://github.com/user-attachments/assets/7d65f31e-9090-4179-b5db-8f5103c7a742) | ![Recipes Screen Image](https://github.com/user-attachments/assets/e6935e24-6cf6-42bf-8469-f42b1a754f39) | ![Recipe Detail Screen Image](https://github.com/user-attachments/assets/382ca8d7-1fae-4f92-9a8f-c6c7e1f08074) |
| **Document Screen**        | **Generative Screen**        | **Chat Screen**                |
| ![Document Screen Image](https://github.com/user-attachments/assets/0644bf62-2938-4136-9169-4a0f80a520f7) | ![Generative Screen Image](https://github.com/user-attachments/assets/c4e8db80-53bd-464f-a41a-1e4b6dc4a6d7) | ![Chat Screen Image](https://github.com/user-attachments/assets/8f8353d3-8b44-4c8d-9f1c-a623dfffaead) |
| **Chat History**           | **Search Screen**            | **Bookmark Screen**            |
| ![Chat History Image](https://github.com/user-attachments/assets/9853d1cf-784d-455a-b818-536bee4004fa) | ![Search Screen Image](https://github.com/user-attachments/assets/32f5a3b5-a536-4aeb-8254-2e550930ec14) | ![Bookmark Screen Image](https://github.com/user-attachments/assets/e0028bb9-4238-4e7c-9d64-4ea51d1c011e) |
| **Account Screen**         |
| ![Account Screen Image](https://github.com/user-attachments/assets/690801f7-a1c7-437b-9799-d91ecbda0bbe) |

## Technologies

The app uses these technologies;

- Kotlin - App is built with the language.
- Jetpack compose - Ui uses compose with material3
- Jetpack components;
    - Datastore - for data persistence in the app.
    - Room - for storing schedules.
    - worker - for managing app data.
    - navigation component
- Coroutines - Used to make asynchronous calls.
- Splash screen Api - For creating a splash screen on app starting.
- [RichEditorCompose](https://mohamedrejeb.github.io/compose-rich-editor/) - Library to display different text formats.
- Retrofit - For networking.
- Koin - A lightweight dependency injection framework.
- GeminiAI
- Coil - For loading Images
- Firebase - For authentication, storage

## Upcoming Features
- Sync with firestore for meal documents.
- Improve on meal documentation.

## Want to clone/contribute?
Setup your own firebase and provide `google-services.json`.
- Add these variables to your **local.properties** file.
```
geminiApiKey=  ## retrieve this from google gemini
serverID=  ## retrieve OAuth ClientID from your google console follow the link and replace projectname link= `https://console.cloud.google.com/apis/credentials?authuser=0&hl=en&project= ##projectname`
```

> Make sure to run this lint check before pushing
 `./gradlew ktlintFormat && ./gradlew ktlintCheck && ./gradlew detekt`

## Known Bugs


If the app has any bug. Please make contact below or open an issue
> lsheldon645@gmail.com
