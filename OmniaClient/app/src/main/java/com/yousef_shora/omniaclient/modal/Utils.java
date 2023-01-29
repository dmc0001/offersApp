package com.yousef_shora.omniaclient.modal;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.yousef_shora.omniaclient.R;
import com.yousef_shora.omniaclient.pojo.Filter;

import java.util.ArrayList;

public class Utils {
    private enum Categories {
        All,
        Fashion,
        Cab,
        Restaurant,
        Electronics,
        Services
    }

    private static ArrayList<Integer> get_filter_icons(){
        ArrayList<Integer> icons = new ArrayList<>();
        icons.add(R.drawable.all_icon);
        icons.add(R.drawable.fashion_icon);
        icons.add(R.drawable.cab_icon);
        icons.add(R.drawable.restaurant_icon);
        icons.add(R.drawable.electronics_icon);
        icons.add(R.drawable.services_icon);
        return icons;
    }
    public static void subscribe_to_notifications() {
        FirebaseMessaging.getInstance().subscribeToTopic("all")
                .addOnCompleteListener(task -> {
                    String msg = "Subscribed";
                    if (!task.isSuccessful()) {
                        msg = "Subscribe failed";
                    }
                    Log.d("Firestore", msg);
                });
    }

    public static ArrayList<String> get_categories() {
        ArrayList<String> categories_array = new ArrayList<>();
        for (Categories category : Categories.values()) {
            categories_array.add(category.name());
        }
        return categories_array;
    }

    public static ArrayList<Filter> get_filters() {
        ArrayList<Filter> filters = new ArrayList<>();
        ArrayList<String> categories = get_categories();
        ArrayList<Integer> icons = get_filter_icons();
        for (int i = 0; i < categories.size();i++) {
                filters.add(new Filter(icons.get(i),categories.get(i)));
        }
        return filters;
    }

    public static boolean is_category(String check_against) {
        Categories[] arr = Categories.values();
        for (Categories category : arr) {
            if (category.name().equalsIgnoreCase(check_against)) {
                return true;
            }
        }
        return false;
    }
}
