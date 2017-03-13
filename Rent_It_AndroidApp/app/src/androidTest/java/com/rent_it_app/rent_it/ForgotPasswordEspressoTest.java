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
public class ForgotPasswordEspressoTest {

    @Rule
    public ActivityTestRule<ResetPasswordActivity> mResetPasswordTestRule =
            new ActivityTestRule(ResetPasswordActivity.class);

    @Test
    //request a new password with invalid email
    public void ForgotPassword1() {
        //open home page
        onView(withId(R.layout.activity_home));
        //navigate to the forgot password by pressing forgot password button
        onView(withId(R.id.btn_forgot_password)).perform(navigateTo(R.layout.activity_reset_password));
        //Input Values for forgotten password
        onView(withId(R.id.email_field)).perform(typeText("welcome.oakland.edu"), closeSoftKeyboard());
        //Select reset password button
        onView(withId(R.id.btn_send_email)).perform(ViewActions.click());
        //check toast message
        onView(withText("Please enter valid email address"))
                .inRoot(withDecorView(not(is(mResetPasswordTestRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    //request a new password with valid email
    public void ForgotPassword() {
        onView(withId(R.layout.activity_reset_password));
        //Input Values for forgotten password
        onView(withId(R.id.email_field)).perform(typeText("prsande2@oakland.edu"), closeSoftKeyboard());
        //Select reset password button
        onView(withId(R.id.btn_send_email)).perform(ViewActions.click());
        //check toast message
        onView(withText("We have sent you instructions to reset your password!"))
                .inRoot(withDecorView(not(is(mResetPasswordTestRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }
}
