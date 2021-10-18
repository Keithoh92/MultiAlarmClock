//package com.example.multialarmclock
//
//import androidx.lifecycle.Lifecycle
//import androidx.test.core.app.ActivityScenario
//import androidx.test.core.app.launchActivity
//import androidx.test.espresso.Espresso
//import androidx.test.espresso.Espresso.onData
//import androidx.test.espresso.Espresso.onView
//import androidx.test.espresso.action.ViewActions.click
//import androidx.test.espresso.matcher.ViewMatchers.assertThat
//import com.google.common.truth.Truth.assertThat
//import androidx.test.espresso.matcher.ViewMatchers.withId
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import androidx.test.filters.LargeTest
//import androidx.test.rule.ActivityTestRule
//import org.junit.After
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@LargeTest
//@RunWith(AndroidJUnit4::class)
//class BuildNewAlarmTest{
//
//    private lateinit var scenario:ActivityScenario<BuildNewAlarm>
//    @get:Rule
//    var activityTestRule = ActivityTestRule(BuildNewAlarm::class.java)
//
//    private var buildNewAlarm = BuildNewAlarm()
//    @Before
//    fun setup(){
////        scenario = launchActivity()
////        scenario.moveToState(Lifecycle.State.STARTED)
//        buildNewAlarm = activityTestRule.activity
//    }
//
//    @Test
//    fun testCheckboxDaysSelectedArray()
//    {
//        onView(withId(R.id.cb_day1)).perform(click())
//        onView(withId(R.id.cb_day3)).perform(click())
//        onView(withId(R.id.cb_day4)).perform(click())
//        onView(withId(R.id.save_button)).perform(click())
//        val str = activityTestRule.runOnUiThread{activityTestRule.activity.getCheckedDays()}
//
//        assertThat(str).isEqualTo("Mon,Wed,Thu")
//    }
//
////    @After
////    fun testThatArrayHasCorrectSelectedDays(){
////        val str = buildNewAlarm.getCheckedDays()
////        assertThat(str).isEqualTo("Mon,Wed,Thu")
////    }
//
//}