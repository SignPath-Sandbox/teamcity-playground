import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.powerShell

version = "2024.03"

project {
    buildType(TahasTestsProject_VersionedSettings_Test)
}

object TahasTestsProject_VersionedSettings_Test : BuildType({
    id("Test")
    name = "test"

    artifactRules = "signed.dll"

    params {

    }

    vcs {
        root(DslContext.settingsRoot)
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
            param("inputArtifactPath", "vfcompat.dll")
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
