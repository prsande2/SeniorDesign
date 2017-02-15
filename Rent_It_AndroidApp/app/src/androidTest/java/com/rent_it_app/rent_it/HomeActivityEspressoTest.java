package com.rent_it_app.rent_it;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.action.ViewActions.click;

import static android.support.test.espresso.Espresso.onView;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import com.rent_it_app.rent_it.views.ListItemFragment;

/**
 * Created by Nagoya on 2/14/17.
 */

@RunWith(AndroidJUnit4.class)
public class HomeActivityEspressoTest {

    @Rule
    public ActivityTestRule<HomeActivity> mHomeActivityRule =
            new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void getToListItemFragment()
    {
        //get the text which the fragment shows
        ViewInteraction fragmentText = onView(withId(R.id.fli_tv_title));

        //check the fragment text does not exist on fresh activity start
        fragmentText.check(ViewAssertions.doesNotExist());

        // Click on List it menu
        onView(withId(R.id.nav_list))
                .perform(click());

        //check the fragments text is now visible in the activity
        fragmentText.check(ViewAssertions.matches(isDisplayed()));
    }

}
