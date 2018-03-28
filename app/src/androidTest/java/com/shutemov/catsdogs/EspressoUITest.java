package com.shutemov.catsdogs;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.filters.LargeTest;
import android.support.test.internal.util.Checks;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.shutemov.catsdogs.data.FlickrContent;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EspressoUITest {

    @Inject

    @Rule
    public ActivityTestRule<ScrollingActivity> mActivityRule =
            new ActivityTestRule(ScrollingActivity.class);

    @Test
    public void updateTest() {

        onView(withText("Kitten 0")).check(matches(isDisplayed()));
        onView(withText("Kitten 1")).check(matches(isDisplayed()));
        onView(withText("Kitten 2")).check(matches(isDisplayed()));

        onView(withParent(withId(R.id.CatsFragment)))
                .perform(RecyclerViewActions.scrollToPosition (0));
        onView(withParent(withId(R.id.CatsFragment)))
                .perform(RecyclerViewActions.actionOnItemAtPosition (3, click()));
        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
        onView(withId(R.id.imageView)).check(
                matches(DrawableMatcher.imageWithAssetFile("cat4.jpg")));
        onView(withId(R.id.detailsLayout)).check(matches(not(isDisplayed())));
        onView(withId(R.id.imageView)).perform(click());
        onView(withId(R.id.detailsLayout)).check(matches(isDisplayed()));
        onView(withText("Date: 2018/05/12 10:40")).check(matches(isDisplayed()));

        pressBack();

        onView(withId(android.R.id.content)).perform(ViewActions.swipeUp());

        onView(withText("Puppy 0")).check(matches(isDisplayed()));
        onView(withText("Puppy 1")).check(matches(isDisplayed()));
        onView(withText("Puppy 2")).check(matches(isDisplayed()));

        onView(withParent(withId(R.id.DogsFragment)))
                .perform(RecyclerViewActions.scrollToPosition (19));

        onView(withParent(withId(R.id.DogsFragment)))
                .perform(RecyclerViewActions.actionOnItemAtPosition (18, click()));
        onView(withParent(withId(R.id.DogsFragment)))
                .perform(RecyclerViewActions.actionOnItemAtPosition (18, click()));

        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
        onView(withId(R.id.imageView)).check(
                matches(DrawableMatcher.imageWithAssetFile("dog3.jpg")));
        pressBack();
        onView(withText("Puppy 18")).check(matches(isDisplayed()));
        onView(withText("Puppy 17")).check(matches(isDisplayed()));
        onView(withText("Puppy 16")).check(matches(isDisplayed()));

        onView(withParent(withId(R.id.DogsFragment)))
                .perform(RecyclerViewActions.actionOnItemAtPosition (17, click()));
        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
        onView(withId(R.id.imageView)).check(
                matches(DrawableMatcher.imageWithAssetFile("dog2.jpg")));

        onView(withId(R.id.detailsLayout)).check(matches(not(isDisplayed())));
        onView(withId(R.id.imageView)).perform(click());
        onView(withId(R.id.detailsLayout)).check(matches(isDisplayed()));
        onView(withText("Date: 2018/05/12 10:40")).check(matches(isDisplayed()));
        pressBack();

        onView(withParent(withId(R.id.UnfilteredFragment)))
                .perform(RecyclerViewActions.scrollToPosition (19));
        onView(withParent(withId(R.id.UnfilteredFragment)))
                .perform(RecyclerViewActions.actionOnItemAtPosition (19, click()));


    }

    public static Matcher<Object> pictureWithTitle(String expectedTitle) {
        Checks.checkNotNull(expectedTitle);
        return pictureWithTitle(equalTo(expectedTitle));
    }

    public static Matcher<Object> pictureWithTitle(final Matcher<String> itemMatcher) {
        Checks.checkNotNull(itemMatcher);

        return new BoundedMatcher<Object, FlickrContent.FlickrItem>(FlickrContent.FlickrItem.class) {
            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("Picture with title: ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(FlickrContent.FlickrItem flickrItem) {
                return itemMatcher.matches(flickrItem.title);
            }
        };
    }
}
