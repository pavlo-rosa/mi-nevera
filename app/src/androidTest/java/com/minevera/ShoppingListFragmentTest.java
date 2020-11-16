package com.minevera;
/*
import android.support.annotation.NonNull;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import com.minevera.activities.SignInActivity;
import com.minevera.fragments.ShoppingListFragment;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Checks.checkNotNull;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;*/

/**
 * Created by Pablo on 29/03/2016.
 */
//@RunWith(value = AndroidJUnit4.class)
//@LargeTest
public class ShoppingListFragmentTest {

  //  @Rule
  //  public IntentsTestRule<SignInActivity> mActivityRule = new IntentsTestRule <>(SignInActivity.class);
    /*Para hacer estas pruebas, partiremos de un usuario registrado pero con ls lista de la compra vacia*/

    /****
     * PS - 003
     * Test01
     * Descripción: Usuario recien registrado, comprobar que la lista está vacia
     */
  //  @Test
 //   public void testShoppingList01() {
        /*Click para unirse abrir el navigation drawer*/
  //      onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        /*Pulsamos sobre Lista de la compra*/
 //       onView(withId(R.id.nav_shoppinglist)).perform(click());
        /*Comprobamos que hemos pasado al fragmento correspondiente*/
  //      intended(hasComponent(hasClassName(ShoppingListFragment.class.getName())));
        /*Comprobamos que no hay nada*/
 //       onView(withId(R.id.listRecyclerView)).check(matches(atPosition(0, withText("Test Text"))));
 //       onView(withText(R.id.listRecyclerView)).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
 //   }

 /*   public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }
*/
}
