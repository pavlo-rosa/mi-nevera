package com.minevera;
/*
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.minevera.activities.JoinGHActivity;
import com.minevera.activities.NavigationDrawerActivity;
import com.minevera.activities.SignInActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
*/
/**
 * Created by Pablo on 28/03/2016.
 *//*
@RunWith(value = AndroidJUnit4.class)
@LargeTest*/
public class RegisterActivity_JoinGHTest {
/*
    public static final String STRING_TO_BE_TYPED = "9999999";
    public static final String PASSWORD = "keyMal";
    public static final String ID_GH = "5689413791121408";

    @Rule
    public IntentsTestRule<SignInActivity> mActivityRule = new IntentsTestRule <>(SignInActivity.class);
  */  /*Necesitamos iniciar sesion (no registrado aun) sin establecer el grupo de hogar -- Al iniciar RegisterActivity tiene que apareccer*/
    /*Tenemos que tener creado ya un grupo de hogar previamente*/

    /****
     * PS - 005
     * Test01
     * Descripción: Esta prueba va a consistir de verificar que salta el Toast Corresponiente en caso de no intoducir en el formulario
     */
   /* @Test
    public void testJoinGH01() {*/
        /*Click para unirse un grupo de hogar*/
   //     onView(withId(R.id.buttonRegJoinGH)).perform(click());
        /*Comprobamos que hemos pasado de actividad*/
  //      intended(hasComponent(hasClassName(JoinGHActivity.class.getName())));
        /*Sino introducimos nada*/
   //     onView(withId(R.id.buttonJoinGH)).perform(click());
  //      onView(withText(R.string.ToastFieldsAreEmpty)).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
  //  }

    /****
     * PS - 005
     * Test02
     * Descripción: Esta prueba va a consistir de verificar que salta el Toast Corresponiente en caso de no intoducir algun valor del formulario
     */
  //  @Test
  //  public void testJoinGH02() {
        /*Click para unirse un grupo de hogar*/
//        onView(withId(R.id.buttonRegJoinGH)).perform(click());
        /*Comprobamos que hemos pasado de actividad*/
 ///       intended(hasComponent(hasClassName(JoinGHActivity.class.getName())));
        /*Si rellenamos alguno de los campos pero no los dos esperamos el mismo resultado*/
        /*A) Solo ID GH*/
  //      onView(withId(R.id.IDgh)).perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());
 //       onView(withId(R.id.buttonJoinGH)).perform(click());
  //      onView(withText(R.string.ToastFieldsAreEmpty)).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        /*B) Solo password*/
    //    onView(withId(R.id.IDgh)).perform(clearText());
  //      onView(withId(R.id.passwordIDgh)).perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());
  //      onView(withId(R.id.buttonJoinGH)).perform(click());
 //       onView(withText(R.string.ToastFieldsAreEmpty)).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

 //   }
    /****
     * PS - 005
     * Test03
     * Descripción: Esta prueba va a consistir de verificar que salta el Toast Corresponiente en caso de  intoroducir mal los valores del formulario
     */
 //   @Test
 //   public void testJoinGH03() {
        /*Click para unirse un grupo de hogar*/
    //    onView(withId(R.id.buttonRegJoinGH)).perform(click());
        /*Comprobamos que hemos pasado de actividad*/
   //     intended(hasComponent(hasClassName(JoinGHActivity.class.getName())));
         /*Si rellenamos los dos campos y no coinciden con los valores de la base de datos*/
  //      onView(withId(R.id.IDgh)).perform(typeText(STRING_TO_BE_TYPED));
 //       onView(withId(R.id.passwordIDgh)).perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());
  //      onView(withId(R.id.buttonJoinGH)).perform(click());
       /*Da error pero porque se llama desde la clase Controller. Pero funciona correctamente*/
 //       onView(withText(R.string.ToastJoinGHIncorrect)).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

//    }
    /****
     * PS - 005
     * Test04
     * Descripción: Prueba verifica el comportamiento correcto de la actividad
     */
 //   @Test
 //   public void testJoinGH04() {
        /*Click para unirse un grupo de hogar*/
//        onView(withId(R.id.buttonRegJoinGH)).perform(click());
        /*Comprobamos que hemos pasado de actividad*/
////        intended(hasComponent(hasClassName(JoinGHActivity.class.getName())));;
        /*Rellenamos los parametros*/
 //       onView(withId(R.id.IDgh)).perform(typeText(ID_GH));
        /*Cambiar Password = key*/
  //      onView(withId(R.id.passwordIDgh)).perform(typeText(PASSWORD));
  //      onView(withId(R.id.buttonJoinGH)).perform(click());
       /*Comprobamos que hemos pasado de actividad*/
 //       intended(hasComponent(hasClassName(NavigationDrawerActivity.class.getName())));
  //  }
}
