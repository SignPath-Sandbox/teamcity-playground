import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.buildSteps.signPathSubmitSigningRequest

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

        // https://teamcity-playground.customersimulation.int.signpath.io/app/dsl-documentation/buildSteps/signpath-step/index.html
        signPathSubmitSigningRequest {
             val artifact = "BuildOutput.zip"
             name = "Sign Package"

             inputArtifactPath = artifact
             outputArtifactPath = artifact
             artifactConfigurationSlug = "With_User_Defined_Parameters"
             organizationId = "d0bc0910-ab91-4b74-bcdd-52d983196a4d"
             apiToken = "credentialsJSON:795ac364-d1c2-46f6-9bc4-d9967e28ef8b"

             waitForCompletion = true
             connectorUrl = "https://teamcity-connector-playground.customersimulation.int.signpath.io"
             signingPolicySlug = "test-signing"
             projectSlug = "TeamCityConnector"
            userDefinedParameters = """
                
                
                fileName: fromUserDefinedParams.sig
                
            """.trimIndent()

         }

//        step {
//            val artifact = "BuildOutput.zip"
//
//            id = "SignPathRunner_2"
//            type = "SignPathRunner"
//            param("inputArtifactPath", artifact)
//            param("outputArtifactPath", artifact)
//
//            //param("artifactConfigurationSlug", "initial")
//            param("organizationId", "d0bc0910-ab91-4b74-bcdd-52d983196a4d")
//            param("apiToken", "credentialsJSON:795ac364-d1c2-46f6-9bc4-d9967e28ef8b")
//
//            param("waitForCompletion", "true")
//            param("connectorUrl", "https://teamcity-connector-playground.customersimulation.int.signpath.io")
//            param("signingPolicySlug", "test-signing")
//            param("projectSlug", "TeamCityConnector")
//        }
    }
})
