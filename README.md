# MealMate

## Description
MealMate is a meal planning and calendar management app built with Java and Android. It allows users to discover new meals, plan their weekly meals, save favorites, and synchronize data for seamless access across devices.

## Technologies Used
- Java
- Android SDK
- MVP (Model-View-Presenter) Architecture
- RxJava
- Room Database
- Firebase 

## App Features

### Meal Exploration
- **Meal of the Day:** Users can view an arbitrary meal for inspiration.
- **Search for Meals:** Users can search for meals based on country, ingredient, or category.
- **Categories List:** Displays a list of meal categories for easy selection.
- **Countries List:** Shows available countries so users can explore popular meals by region.

### Favorites & Meal Planning
- **Favorite Meals:** Users can add meals to favorites or remove them. (Stored locally using Room; Firebase is NOT used for this part.)
- **Meal Planning:** Users can add meals to their weekly plan and view their planned meals for the current week.
- **Offline Support:** If there is no network connection, users can still access their favorite meals and weekly meal plans.
- **Data Backup & Sync:** Users can back up their meals and restore them upon login using Firebase.

### Authentication
- **User Authentication:**
    - Standard email/password login.
    - Social login options (Google, Facebook, Twitter) via Firebase Authentication.
    - Guest mode: Allows browsing categories, searching meals, and viewing the Meal of the Day.
- **Persistent Login:** Once logged in, users remain authenticated using Firebase and SharedPreferences.

### UI & Experience
- **Splash Screen:** Animated splash screen using Lottie.
- **Smooth Navigation:** Utilizes the Navigation Component for seamless user experience.

## Installation
1. Clone the repository:
   ```sh
   git clone https://github.com/DaTaj-ai/MealMate.git
   ```
2. Open the project in Android Studio.
3. Sync Gradle dependencies.
4. Run the app on an emulator or physical device.

## Usage
1. Launch the app and log in or use guest mode.
2. Browse meals, search by ingredients, categories, or countries.
3. Save meals to favorites or plan meals for the week.
4. Backup data and access it across devices.



## Contact
For questions or feedback, reach out via [Dataj ](https://github.com/DaTaj-ai/).

