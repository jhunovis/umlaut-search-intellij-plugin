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

import com.intellij.navigation.ChooseByNameContributorEx
import com.intellij.navigation.NavigationItem
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.Processor
import com.intellij.util.indexing.FindSymbolParameters
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.then

class TransliteratingNavigationContributorTest {

    private val nameProcessor: Processor<NavigationItem> = mock()
    private val ideaDefaultNameContributor: ChooseByNameContributorEx = mock()

    private val chooseByTransliterationNameContributor = TransliteratingNavigationContributor(ideaDefaultNameContributor)

    @Test
    fun whenSearchingForPatternWithNativeCharactersOrTheirTransliterations_ShouldForwardMappedPatternToRealImplementation() {
        val findSymbolParametersCapture: ArgumentCaptor<FindSymbolParameters> = captor()

        chooseByTransliterationNameContributor.processElementsWithName(
                "Hällo", nameProcessor, findSymbolParameters("Hällo", "Hä")
        )

        then(ideaDefaultNameContributor).should()
                .processElementsWithName(eq("Haello"), eq(nameProcessor), findSymbolParametersCapture.capture())
        assertThat(findSymbolParametersCapture.value.completePattern, `is`(equalTo("Hae")))
        assertThat(findSymbolParametersCapture.value.localPatternName, `is`(equalTo("Haello")))
    }

    @Test
    fun whenSearchingForPatternWithoutNativeCharactersOrTheirTransliterations_ShouldNotCallRealImplementation() {
        chooseByTransliterationNameContributor.processElementsWithName(
                "Hallo", nameProcessor, findSymbolParameters("Hallo", "Ha")
        )

        then(ideaDefaultNameContributor).shouldHaveZeroInteractions()
    }

    private fun findSymbolParameters(nativeName: String, nativePattern: String) =
            FindSymbolParameters(nativePattern, nativeName, GlobalSearchScope.EMPTY_SCOPE, null)

    inline fun <reified T : Any> captor(): ArgumentCaptor<T> = ArgumentCaptor.forClass(T::class.java)
}


