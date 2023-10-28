package com.piwew.storyapp.ui.login

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.piwew.storyapp.R
import com.piwew.storyapp.helper.EspressoIdlingResource
import com.piwew.storyapp.ui.WelcomeActivity
import com.piwew.storyapp.ui.main.MainActivity
import com.piwew.storyapp.ui.story.AddStoryActivity
import com.piwew.storyapp.ui.story.CameraActivity
import org.hamcrest.Matchers.anyOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {

    @get:Rule
    val activity = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        Intents.release()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun loginAndLogout_Success() {
        onView(withId(R.id.ed_login_email)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_login_password)).check(matches(isDisplayed()))

        onView(withId(R.id.ed_login_email)).perform(typeText("gam@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.ed_login_password)).perform(typeText("12345678"), closeSoftKeyboard())

        onView(withId(R.id.btn_login)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_login)).perform(click())

        val btnNext = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.continue_title)
        onView(withText(btnNext)).inRoot(isDialog()).check(matches(isDisplayed())).perform(click())

        Intents.intended(IntentMatchers.hasComponent(MainActivity::class.java.name))

        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_stories)).check(matches(isDisplayed()))

        onView(withId(R.id.action_logout)).perform(click())
        val btnYes = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.yes_title)
        onView(withText(btnYes)).inRoot(isDialog()).check(matches(isDisplayed())).perform(click())

        Intents.intended(IntentMatchers.hasComponent(WelcomeActivity::class.java.name))
    }

    @Test
    fun loginValidation() {
        onView(withId(R.id.ed_login_email)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_login_password)).check(matches(isDisplayed()))

        onView(withId(R.id.ed_login_email)).perform(typeText("rafi"), closeSoftKeyboard())
        val invalidEmailMessage = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.invalid_email)
        onView(withId(R.id.ed_login_email)).check(matches(hasErrorText(invalidEmailMessage)))
        onView(withId(R.id.ed_login_email)).perform(typeText("rafi@gmail.com"), closeSoftKeyboard())

        onView(withId(R.id.ed_login_password)).perform(typeText("1234567"), closeSoftKeyboard())
        val invalidPasswordMessage = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.error_short_password)
        onView(withId(R.id.ed_login_password)).check(matches(hasErrorText(invalidPasswordMessage)))
        onView(withId(R.id.ed_login_password)).perform(typeText("12345678"), closeSoftKeyboard())
    }

    @Test
    fun loginUserNotFound_Failed() {
        onView(withId(R.id.ed_login_email)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_login_password)).check(matches(isDisplayed()))

        onView(withId(R.id.ed_login_email)).perform(typeText("z1x2c3v4b@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.ed_login_password)).perform(typeText("12345678"), closeSoftKeyboard())

        onView(withId(R.id.btn_login)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_login)).perform(click())

        val btnTryAgain = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.try_again)
        onView(withText(btnTryAgain)).inRoot(isDialog()).check(matches(isDisplayed())).perform(click())
    }

    @Test
    fun addStory_Success() {
        onView(withId(R.id.ed_login_email)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_login_password)).check(matches(isDisplayed()))

        onView(withId(R.id.ed_login_email)).perform(typeText("gam@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.ed_login_password)).perform(typeText("12345678"), closeSoftKeyboard())

        onView(withId(R.id.btn_login)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_login)).perform(click())

        val btnNext = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.continue_title)
        onView(withText(btnNext)).inRoot(isDialog()).check(matches(isDisplayed())).perform(click())
        Intents.intended(IntentMatchers.hasComponent(MainActivity::class.java.name))
        onView(withId(R.id.rv_stories)).check(matches(isDisplayed()))

        onView(withId(R.id.fab_add_story)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_add_story)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(AddStoryActivity::class.java.name))

        onView(withId(R.id.cameraXButton)).check(matches(isDisplayed()))
        onView(withId(R.id.cameraXButton)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(CameraActivity::class.java.name))
        onView(withId(R.id.captureImage)).perform(click())

        onView(withId(R.id.ed_add_description)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_add_description)).perform(typeText("Muhammad Rafi"), closeSoftKeyboard())

        onView(withId(R.id.checkbox_location)).check(matches(isDisplayed()))
        onView(withId(R.id.checkbox_location)).perform(click())
        onView(withId(R.id.checkbox_location)).check(matches(ViewMatchers.isChecked()))

        onView(withId(R.id.button_add)).check(matches(isDisplayed()))
        onView(withId(R.id.button_add)).perform(click())

        val btnClose = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.close_title)
        onView(withText("Story created successfully")).check(matches(isDisplayed()))
        onView(withText(btnClose)).inRoot(isDialog()).check(matches(isDisplayed())).perform(click())

        onView(withId(R.id.rv_stories)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_stories)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
        onView(anyOf(withText("Muhammad Rafi"), isDescendantOfA(withId(R.id.rv_stories))))
    }
}