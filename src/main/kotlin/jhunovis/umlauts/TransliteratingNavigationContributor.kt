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

import com.intellij.ide.util.gotoByName.DefaultClassNavigationContributor
import com.intellij.ide.util.gotoByName.DefaultFileNavigationContributor
import com.intellij.ide.util.gotoByName.DefaultSymbolNavigationContributor
import com.intellij.navigation.ChooseByNameContributorEx
import com.intellij.navigation.NavigationItem
import com.intellij.util.Processor
import com.intellij.util.indexing.FindSymbolParameters

/**
 * Search for variations of the class name patterned entered by the IDEA user by mapping German native characters to
 * their ASCII-transliteration and vice versa.
 *
 * @author <a href="mailto:jhunovis+idea@gmail.com">Jan Hackel</a>
 */
open class TransliteratingNavigationContributor
constructor(val delegate: ChooseByNameContributorEx) : ChooseByNameContributorEx by delegate {

    private val germanTransliteration = GermanTransliteration()

    final override fun processElementsWithName(name: String, processor: Processor<NavigationItem>, parameters: FindSymbolParameters) {
        val completePattern = parameters.completePattern
        val transliteratedPattern = transliterate(completePattern)
        if (transliteratedPattern != completePattern) {
            println("Looking for $transliteratedPattern")
            val transliteratedName = transliterate(name)
            delegate.processElementsWithName(
                    transliteratedName, processor, newParametersWith(parameters, transliteratedPattern, transliteratedName)
            )
        }
    }

    private fun newParametersWith(parameters: FindSymbolParameters, pattern: String, name: String) =
            FindSymbolParameters(pattern, name, parameters.searchScope, parameters.idFilter)

    private fun transliterate(symbol: String) =
        germanTransliteration.transliterate(symbol)
}

class TransliteratingClassNavigationContributor : TransliteratingNavigationContributor(DefaultClassNavigationContributor())
class TransliteratingFileNavigationContributor : TransliteratingNavigationContributor(DefaultFileNavigationContributor())
class TransliteratingSymbolNavigationContributor : TransliteratingNavigationContributor(DefaultSymbolNavigationContributor())