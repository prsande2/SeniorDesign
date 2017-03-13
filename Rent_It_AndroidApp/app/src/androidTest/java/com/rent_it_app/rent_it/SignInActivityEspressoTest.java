package com.rent_it_app.rent_it;

import android.nfc.Tag;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
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
 * Created by prsande2 on 3/7/17.
 */

@RunWith(AndroidJUnit4.class)
public class SignInActivityEspressoTest {

    @Rule
    public ActivityTestRule<SignInActivity> mSignInTestRule =
            new ActivityTestRule(SignInActivity.class);

    @Test
    //sign in with correct email and  empty password
    public void SignIn() {
        onView(withId(R.layout.activity_home));
        //Input Values for Sign In
        onView(withId(R.id.field_email)).perform(typeText("Prsande2@oakland.edu"), closeSoftKeyboard());
        onView(withId(R.id.field_password)).perform(typeText(""), closeSoftKeyboard());
        // Click Sign In Button
        onView(withId(R.id.email_sign_in_button)).perform(ViewActions.click());
        //display error for required password
        onView(withId(R.id.field_password)).check(matches(hasErrorText("Required")));
    }


    @Test
    //sign in with incorrect email and password
    public void SignInC() {
        onView(withId(R.layout.activity_home));
        //Input Values for Sign In
        onView(withId(R.id.field_email)).perform(typeText("Prsande2@oakland.edu"), closeSoftKeyboard());
        onView(withId(R.id.field_password)).perform(typeText("hello"), closeSoftKeyboard());
        // Click Sign In Button
        onView(withId(R.id.email_sign_in_button)).perform(ViewActions.click());
        //Check toast message
        onView(withText(R.string.auth_failed))
                .inRoot(withDecorView(not(is(mSignInTestRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

    }

    @Test
    //request a new password with invalid email
    public void ForgotPassword1() {
        onView(withId(R.layout.activity_home));
        //select button for forgot password
        onView(withId(R.id.btn_forgot_password)).perform(ViewActions.click());
        //open layout for reset password
        onView(withId(R.layout.activity_reset_password));
        //Input Values for forgotten password
        onView(withId(R.id.email_field)).perform(typeText("welcome.oakland.edu"), closeSoftKeyboard());
        //Select reset password button
        onView(withId(R.id.btn_send_email)).perform(ViewActions.click());
        //check toast message
        onView(withText("Please enter valid email address"))
                .inRoot(withDecorView(not(is(mSignInTestRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

    }

    @Test
    //create account with an exsiting email
    public void SignUp1() {
        onView(withId(R.layout.activity_home));
        //select button for creating an account
        onView(withId(R.id.btn_sign_up)).perform(ViewActions.click());
        onView(withId(R.layout.activity_sign_up));
        //Input Values for account creation
        onView(withId(R.id.field_first_name)).perform(typeText("Precious"), closeSoftKeyboard());
        onView(withId(R.id.field_last_name)).perform(typeText("sanders"), closeSoftKeyboard());
        onView(withId(R.id.field_display_name)).perform(typeText("Prsande2"), closeSoftKeyboard());
        onView(withId(R.id.field_email)).perform(typeText("Prsande2@oakland.edu"), closeSoftKeyboard());
        onView(withId(R.id.field_password)).perform(typeText("temp1234"), closeSoftKeyboard());
        //click create account button
        onView(withId(R.id.email_create_account_button)).perform(ViewActions.click());
        //check toast message
        onView(withText("Email Address already registered"))
                .inRoot(withDecorView(not(is(mSignInTestRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }
}