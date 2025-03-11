import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.script

version = "2024.03"

project {
    buildType(Test)
}

object Test : BuildType({
    id("Test")
    name = "test"

    artifactRules = "BuildOutput"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        script {
            name = "Build Project"
            id = "Build_Project"
            scriptContent = "build.sh"
            formatStderrAsError = true
        }

        step {
            val artifact = "BuildOutput/HelloWorld.exe"

            id = "SignPathRunner_2"
            type = "SignPathRunner"
            param("inputArtifactPath", artifact)
            param("outputArtifactPath", artifact)

            param("artifactConfigurationSlug", "initial")
            param("organizationId", "7cf15ea1-dc50-4d3d-b0c1-e88a5a7d951c")
            param("apiToken", "credentialsJSON:8d2abf33-2c18-49a8-94e2-10fd378478e7")

            param("waitForCompletion", "true")
            param("connectorUrl", "https://teamcity-connector-playground.customersimulation.int.signpath.io")
            param("signingPolicySlug", "test-signing")
            param("projectSlug", "Project")
        }
    }
})
