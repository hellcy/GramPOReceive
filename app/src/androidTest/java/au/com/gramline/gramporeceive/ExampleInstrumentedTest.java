package au.com.gramline.gramporeceive;

import android.app.Activity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    public static final String Username = "Charles Cheng";
    public static final String Password = "12345678";

    @Rule public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

//    @Test
//    public void changeText_sameActivity() {
//        // Type text and then press the button.
//        onView(withId(R.id.userName))
//                .perform(typeText(Username), closeSoftKeyboard());
//        onView(withId(R.id.password))
//                .perform(typeText(Password), closeSoftKeyboard());
//        onView(withId(R.id.loginBtn)).perform(click());
//
//        // Check that the text was changed.
//        //onView(withId(R.id.textToBeChanged)).check(matches(withText(STRING_TO_BE_TYPED)));
//    }

    @Test
    public void changeText_newActivity() {
        // Type text and then press the button.
        onView(withId(R.id.userName))
                .perform(typeText(Username), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText(Password), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());

        onView(withId(R.id.enterOrderNumberText)).perform(typeText("11986"),
                closeSoftKeyboard());
        onView(withId(R.id.getOrdersButton)).perform(click());

        // This view is in a different Activity, no need to tell Espresso.
        //onView(withId(R.id.show_text_view)).check(matches(withText(STRING_TO_BE_TYPED)));
    }
}
