# Opt
This is an offline first recipe android app. Lists different recipes from the [MealDB API](https://www.themealdb.com/api.php). One can document their
daily meals in the app and use AI to generate meal suggestions.

## Demo/screenshots
<p align="center">
 <img src = "https://github.com/user-attachments/assets/7d65f31e-9090-4179-b5db-8f5103c7a742" height = "20%" width ="30%" >
 <img src = "https://github.com/user-attachments/assets/e6935e24-6cf6-42bf-8469-f42b1a754f39" height = "20%" width ="30%" >
 <img src = "https://github.com/user-attachments/assets/382ca8d7-1fae-4f92-9a8f-c6c7e1f08074" height = "20%" width ="30%" >
 <img src = "https://github.com/user-attachments/assets/0644bf62-2938-4136-9169-4a0f80a520f7" height = "20%" width ="30%" >
 <img src = "https://github.com/user-attachments/assets/c4e8db80-53bd-464f-a41a-1e4b6dc4a6d7" height = "20%" width ="30%" >
 <img src = "https://github.com/user-attachments/assets/4325c2cd-303f-4c03-a184-81f359999398" height = "20%" width ="30%" >
 <img src = "https://github.com/user-attachments/assets/e0028bb9-4238-4e7c-9d64-4ea51d1c011e" height = "20%" width ="30%" >
 <img src = "https://github.com/user-attachments/assets/690801f7-a1c7-437b-9799-d91ecbda0bbe" height = "20%" width ="30%" >
</p>

## Technologies

The app uses these technologies;

- Kotlin - App is built with the language.
- Jetpack compose - Ui uses compose with material3
- Jetpack components;
    - Datastore - for data persistence in the app.
    - Room - for storing schedules.
    - worker - for managing app data.
- Coroutines - Used to make asynchronous calls.
- Splash screen Api - For creating a splash screen on app starting.
- [RichEditorCompose](https://mohamedrejeb.github.io/compose-rich-editor/) - Library to display different text formats.
- Reftrofit - For networking
- GeminiAI
- Coil - For loading Images
- Firebase - For authentication, storage

## Upcoming Features
- Add filters when searching.
- Sync with firestore for meal documents.
- Add Chat history for the AI.
- Improve on meal documentation.

## Want to clone/contribute?
Setup your own firebase and provide `google-services.json`.
- Add these variables to your **local.properties** file.
```
geminiApiKey=  ## This your retrive from google gemini
serverId=  ## This your retrieve frome your firebase console
```

## Known Bugs


If the app has any bug. Please make contact below or open an issue
> lsheldon645@gmail.com
