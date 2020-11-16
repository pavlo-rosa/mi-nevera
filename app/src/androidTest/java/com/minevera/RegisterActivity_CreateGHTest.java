package com.minevera;
/*
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.minevera.activities.SignInActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;*/
/**
 * Created by Pablo on 28/03/2016.
 */
//@RunWith(value = AndroidJUnit4.class)
//@LargeTest
public class RegisterActivity_CreateGHTest {
/*
    public static final String STRING_TO_BE_TYPED = "Espresso";

    @Rule
    public IntentsTestRule<SignInActivity> mActivityRule = new IntentsTestRule <>(SignInActivity.class);*/
    /*Necesitamos iniciar sesion (no registrado aun) sin establecer el grupo de hogar -- Al iniciar RegisterActivity tiene que apareccer*/


    /****
     * PS - 004
     * Test01
     * Descripción: Prueba de introducir la contraseña del grupo de hogar vacio salta un Toast
     * diciendo que la contraseña no puede ser nula.
     */
   /* @Test
    public void testCreateGH01() {
        /*Click para crear un grupo de hogar*/
       // onView(withId(R.id.buttonRegCreateGH)).perform(click());
        /*Comprobamos que una vez pulsado el boton aparece el dialog para crear la contraseña*/
     //   onView(withId(R.id.dpgh_textView)).check(matches(isDisplayed()));
        /*Pulsamos directamente al OK sin escribir nada*/
    //    onView(withText("OK")).perform(click());
    //    onView(withText(R.string.ToastPasswordIsEmpty)).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
 //   }
    /****
     * PS - 004
     * Test02
     * Descripción: Prueba de una vez introducida la contraseña, se crea el grupo de hogar.
     */
  //  @Test
  //  public void testCreateGH02() {
        /*Click para crear un grupo de hogar*/
  //      onView(withId(R.id.buttonRegCreateGH)).perform(click());
        /*Comprobamos que una vez pulsado el boton aparece el dialog para crear la contraseña*/
  //      onView(withId(R.id.dpgh_textView)).check(matches(isDisplayed()));
        /*Introducimos la contraseña*/
 //       onView(withId(R.id.dpedittext)).perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());
 //       onView(withText("OK")).perform(click());
  //      intended(hasComponent("com.minevera.activities.NavigationDrawerActivity"));
 //   }



}
