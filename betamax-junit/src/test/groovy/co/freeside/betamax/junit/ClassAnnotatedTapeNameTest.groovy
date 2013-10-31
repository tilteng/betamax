/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.freeside.betamax.junit

import co.freeside.betamax.Configuration
import com.google.common.io.Files
import groovy.transform.CompileStatic
import org.junit.*
import spock.lang.Issue

@Issue("https://github.com/robfletcher/betamax/issues/36")
@Betamax
@CompileStatic
class ClassAnnotatedTapeNameTest {

    static final File TAPE_ROOT = Files.createTempDir()
    static Configuration configuration = Configuration.builder().tapeRoot(TAPE_ROOT).build()
    @ClassRule public static RecorderRule recorder = new RecorderRule(configuration)

    @AfterClass
    static void deleteTempDir() {
        TAPE_ROOT.deleteDir()
    }

    @Test
    void tapeNameDefaultsToClassName() {
        assert recorder.tape.name == "class annotated tape name test"
    }

}
