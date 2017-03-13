package com.rent_it_app.rent_it;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by prsande2 on 3/11/17.
 */
@RunWith(AndroidJUnit4.class)
public class CAccountEspressoTest {


    @Rule
    public ActivityTestRule<SignUpActivity> mSignUpActivity =
            new ActivityTestRule(SignUpActivity.class);


    @Test
    //create account with an exsiting email
    public void SignIn() {
        onView(withId(R.layout.activity_sign_up));
        //Input Values for Sign up
        onView(withId(R.id.field_first_name)).perform(typeText("Precious"), closeSoftKeyboard());
        onView(withId(R.id.field_last_name)).perform(typeText("sanders"), closeSoftKeyboard());
        onView(withId(R.id.field_display_name)).perform(typeText("Prsande2"), closeSoftKeyboard());
        onView(withId(R.id.field_email)).perform(typeText("Prsande2@oakland.edu"), closeSoftKeyboard());
        onView(withId(R.id.field_password)).perform(typeText("temp1234"), closeSoftKeyboard());
        //select create account button
        onView(withId(R.id.email_create_account_button)).perform(ViewActions.click());
        //check toast message
        onView(withText("Email Address already registered"))
                .inRoot(withDecorView(not(is(mSignUpActivity.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }
        @Test
        //create account leaving a required field empty
        public void SignIn2() {
            onView(withId(R.layout.activity_sign_up));
            //Input Values for Sign up
            onView(withId(R.id.field_first_name)).perform(typeText("Precious"), closeSoftKeyboard());
            onView(withId(R.id.field_last_name)).perform(typeText("sanders"), closeSoftKeyboard());
            onView(withId(R.id.field_display_name)).perform(typeText("Prsande2"), closeSoftKeyboard());
            onView(withId(R.id.field_email)).perform(typeText("Prsande2@oakland.edu"), closeSoftKeyboard());
            onView(withId(R.id.field_password)).perform(typeText(""), closeSoftKeyboard());
            //select create account button
            onView(withId(R.id.email_create_account_button)).perform(ViewActions.click());
            //Check error message for empty password field
            onView(withId(R.id.field_password)).check(matches(hasErrorText("Required")));
    }
}