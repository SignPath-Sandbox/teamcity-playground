import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.script

version = "2024.03"

project {
    buildType(Test)
}

object Test : BuildType({
    id("Test")
    name = "test"

    artifactRules = "BuildOutput.zip"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        script {
            name = "Build Project"
            id = "Build_Project"
            scriptContent = "./build.sh"
            formatStderrAsError = true
        }

        step {
            val artifact = "BuildOutput.zip"

            id = "SignPathRunner_2"
            type = "SignPathRunner"
            param("inputArtifactPath", artifact)
            param("outputArtifactPath", artifact)

            param("artifactConfigurationSlug", "initial")
            param("organizationId", "d0bc0910-ab91-4b74-bcdd-52d983196a4d")
            param("apiToken", "credentialsJSON:24fb3992-424d-4a9d-ab99-068eccb8e3fe")

            param("waitForCompletion", "true")
            param("connectorUrl", "https://teamcity-connector-playground.customersimulation.int.signpath.io")
            param("signingPolicySlug", "test-signing")
            param("projectSlug", "Project")
        }
    }
})
