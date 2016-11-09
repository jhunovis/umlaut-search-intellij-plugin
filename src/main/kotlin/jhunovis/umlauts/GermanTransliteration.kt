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

import org.apache.commons.lang3.StringUtils.containsAny
import org.apache.commons.lang3.StringUtils.replaceEach

/**
 * Maps word with German native characters (umlauts and the ß-ligature) to their ASCII-transliterations and back.
 *
 * @author <a href="mailto:jhunovis+idea@gmail.com">Jan Hackel</a>
 */
class GermanTransliteration {

    val native = arrayOf("Ä", "ä", "Ö", "ö", "Ü", "ü", "ß")
    val transliterated = arrayOf("Ae", "ae", "Oe", "oe", "Ue", "ue", "ss")

    fun transliterate(word: String): String {
        return when {
            containsAny(word, *native) ->
                    replaceEach(word, native, transliterated)
            containsAny(word, *transliterated) ->
                replaceEach(word, transliterated, native)
            else -> word
        }
    }
}