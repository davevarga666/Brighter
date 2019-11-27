package com.android.example.autobrightness

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withId
import org.junit.After
import org.junit.Before
import org.junit.Rule
import android.support.test.rule.ActivityTestRule
import androidx.test.core.app.ActivityScenario


class MainActivityTest {

    @Rule
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)


    @Before
    fun setUp() {

    }

    fun testUSerInputScenario() {
        Espresso.pressBack()
        Espresso.pressBack()
        Espresso.onView(withId(R.id.brightSwitch)).perform(click())
        Espresso.onView(withId(R.id.startButton)).perform(click())
    }

    @After
    fun tearDown() {
    }
}