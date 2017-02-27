package com.rent_it_app.rent_it;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;

import static android.support.test.espresso.Espresso.onView;
import android.support.test.espresso.contrib.DrawerActions;

import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import com.rent_it_app.rent_it.views.ListItemFragment;

/**
 * Created by Mimi on 2/14/17.
 */

@RunWith(AndroidJUnit4.class)
public class HomeActivityEspressoTest {

    //private String mString;


    @Rule
    public ActivityTestRule<HomeActivity> mHomeActivityRule =
            new ActivityTestRule<>(HomeActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        //mString = "Espresso";
    }

    /*@Test
    public void getToListItemFragment()
    {
        //get the text which the fragment shows
        ViewInteraction fragmentText = onView(withId(R.id.fli_tv_title));

        //check the fragment text does not exist on fresh activity start
        fragmentText.check(ViewAssertions.doesNotExist());

        // Open the drawer
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        // Click on the List Item option
        //onView(withId(R.id.nav_list)).perform(click());
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_list));

        //check the fragments text is now visible in the activity
        fragmentText.check(matches(isDisplayed()));
    }*/

    @Test
    public void listItemWithoutDescription()
    {
        // Open the drawer
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        // Click on the List Item option
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_list));

        //input value besides description
        onView(withId(R.id.title)).perform(typeText("8 People tent"), closeSoftKeyboard());
        onView(withId(R.id.condition)).perform(typeText("like new"), closeSoftKeyboard());
        onView(withId(R.id.city)).perform(typeText("Troy"), closeSoftKeyboard());
        onView(withId(R.id.zipcode)).perform(typeText("48084"), closeSoftKeyboard());
        onView(withId(R.id.tags)).perform(typeText("tent, outdoor"), closeSoftKeyboard());
        onView(withId(R.id.value)).perform(typeText("400"), closeSoftKeyboard());
        onView(withId(R.id.rate)).perform(typeText("2"), closeSoftKeyboard());

        //onView(withId(R.id.list_button)).perform(fo);
        //Click List it
        onView(withId(R.id.list_button)).perform(ViewActions.scrollTo()).perform(click());

        //check error is shown in description field
        onView(withId(R.id.description)).check(matches(hasErrorText("Description is required!")));
    }

    @Test
    public void listItem()
    {
        // Open the drawer
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        // Click on the List Item option
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_list));

        //input value besides description
        onView(withId(R.id.title)).perform(typeText("8 People Tent"), closeSoftKeyboard());
        onView(withId(R.id.description)).perform(typeText("Here's some description"), closeSoftKeyboard());
        onView(withId(R.id.condition)).perform(typeText("like new"), closeSoftKeyboard());
        onView(withId(R.id.city)).perform(typeText("Troy"), closeSoftKeyboard());
        onView(withId(R.id.zipcode)).perform(typeText("48084"), closeSoftKeyboard());
        onView(withId(R.id.tags)).perform(typeText("tent, outdoor"), closeSoftKeyboard());
        onView(withId(R.id.value)).perform(typeText("300"), closeSoftKeyboard());
        onView(withId(R.id.rate)).perform(typeText("2"), closeSoftKeyboard());

        //onView(withId(R.id.list_button)).perform(fo);
        //Click List it
        onView(withId(R.id.list_button)).perform(ViewActions.scrollTo()).perform(click());

        //check sucess toast message is displayed
        onView(withText("Sucessfully Created New Listing"))
                .inRoot(withDecorView(not(is(mHomeActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

}
