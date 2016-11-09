/*
 * Copyright 2016 Jan Hackel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jhunovis.umlauts

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith

/**
 * A unit-test for [GermanTransliteration].
 *
 * @author [Jan Hackel](mailto:jhunovis+idea@gmail.com)
 */
@RunWith(JUnitParamsRunner::class)
class GermanTransliterationTest {

    @Test
    @Parameters(
            "Ä, Ae",
            "Ö, Oe",
            "Ü, Ue",
            "ä, ae",
            "ö, oe",
            "ü, ue",
            "ß, ss")
    fun replaceAllGermanSpecialCharactersWithTheirTransliteration(word: String, expectedTransliteration: String) {
        assertIsTranslatedFromInto(expectedTransliteration, word)
    }

    @Test
    @Parameters(
            "Ae, Ä",
            "Oe, Ö",
            "Ue, Ü",
            "ae, ä",
            "oe, ö",
            "ue, ü",
            "ss, ß")
    fun replaceAllTransliterationsWithTheirNativeForm(word: String, expectedTransliteration: String) {
        assertIsTranslatedFromInto(expectedTransliteration, word)
    }

    @Test
    @Parameters(
            "Käsefondue, Kaesefondue",
            "Autoeinführung, Autoeinfuehrung")
    fun whenWordContainsBothNativeAndTransliteratedForms_PreferTheNativeFormAndLeaveTheTransliterationsAsTheyAre(word: String, expectedTransliteration: String) {
        assertIsTranslatedFromInto(expectedTransliteration, word)
    }

    fun replaceAllOccurrences(word: String, expectedTransliteration: String) {
        assertIsTranslatedFromInto("Käsefondueüberfluss", "Kaesefondueueberfluss")
    }

    @Test
    fun givenTransliteratedWritings_IncorrectMappingsMayResult() {
        assertIsTranslatedFromInto("Kaesefondueueberfluss", "Käsefondüüberfluß")
    }

    private fun assertIsTranslatedFromInto(expectedTransliteration: String, word: String) {
        assertThat(
                GermanTransliteration().transliterate(word), `is`(equalTo(expectedTransliteration))
        )
    }

}