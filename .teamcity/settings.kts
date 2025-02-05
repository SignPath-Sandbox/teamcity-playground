import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.powerShell

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2024.03"

project {

    buildType(TahasTestsProject_VersionedSettings_Test)
}

object TahasTestsProject_VersionedSettings_Test : BuildType({
    id("Test")
    name = "test"

    artifactRules = "signed.dll"

    params {
        param("SignPathApiToken", "ABj3ppzgBQ0Lq1s8LSadGZYIpikXelEK+W3OBVGqoe9u")
        param("param1", "value1")
        param("param2", "value2")
    }

    vcs {
        root(AbsoluteId("TahasTestsProject_HttpsGithubComCycloneDXCyclonedxCliGit"))
    }

    steps {
        powerShell {
            name = "Copy Artifact (DEBUG)"
            id = "Copy_Artifact_DEBUG"
            scriptMode = script {
                content = """
                    Copy-Item "C:\vfcompat.dll" .
                    "Hello world" | Out-File "test.txt"
                    ls
                """.trimIndent()
            }
        }
        step {
            id = "SignPathRunner_2"
            type = "SignPathRunner"
            param("inputArtifactPath", "C:\vfcompat.dll")
            param("artifactConfigurationSlug", "initial")
            param("organizationId", "e7509335-e491-4309-8e45-af0d7c1a8db6")
            param("apiToken", "credentialsJSON:8d2abf33-2c18-49a8-94e2-10fd378478e7")
            param("outputArtifactPath", "signed.dll")
            param("waitForCompletion", "true")
            param("connectorUrl", "https://teamcity-dev6.connectors.dev.signpath.io:15201")
            param("signingPolicySlug", "test-signing")
            param("projectSlug", "project")
        }
    }
})
