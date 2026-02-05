import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.buildSteps.signPathSubmitSigningRequest
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

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

version = "2025.11"

project {

    vcsRoot(TeamcityPlayground)
    vcsRoot(TeamcityPlaygroundVs)

    buildType(BuildAndSign)
    buildType(SignScript)

    params {
        param("SignPath.OrganizationID", "9ff791fc-c563-44e3-ab8c-86a33c910bbe")
    }
}

object BuildAndSign : BuildType({
    name = "Build and Sign"

    artifactRules = """
        BuildOutput.zip
        BuildOutput.signed.zip
    """.trimIndent()

    params {
        param("SignPath.OrganizationID", "d0bc0910-ab91-4b74-bcdd-52d983196a4d")
    }

    vcs {
        root(TeamcityPlayground)
    }

    steps {
        script {
            name = "Build Project"
            id = "Build_Project"
            scriptContent = "./build.sh"
            formatStderrAsError = true
            param("teamcity.kubernetes.executor.pull.policy", "IfNotPresent")
        }
        signPathSubmitSigningRequest {
            id = "SignPathRunner_2"
            connectorUrl = "https://teamcity-connector-playground.customersimulation.int.signpath.io/"
            apiToken = "credentialsJSON:795ac364-d1c2-46f6-9bc4-d9967e28ef8b"
            organizationId = "%SignPath.OrganizationID%"
            projectSlug = "TeamCityConnector"
            signingPolicySlug = "test-signing"
            artifactConfigurationSlug = "With_User_Defined_Parameters"
            inputArtifactPath = "BuildOutput.zip"
            outputArtifactPath = "BuildOutput.signed.zip"
            parameters = ""
            waitForCompletion = true
        }
    }
})

object SignScript : BuildType({
    name = "Sign Script"

    artifactRules = """
        script.ps1
        script.signed.ps1
    """.trimIndent()

    vcs {
        root(TeamcityPlayground)
    }

    steps {
        script {
            name = "Build Project"
            id = "Build_Project"
            scriptContent = """echo "hello world" > "script.ps1""""
            formatStderrAsError = true
        }
        signPathSubmitSigningRequest {
            id = "SignPathRunner_2"
            connectorUrl = "https://teamcity-connector-playground.customersimulation.int.signpath.io/"
            apiToken = "credentialsJSON:a65cfa1a-0cfd-4aa3-9b4a-301d2595440e"
            organizationId = "%SignPath.OrganizationID%"
            projectSlug = "Stefans_Teamcity_Project"
            signingPolicySlug = "test-signing"
            artifactConfigurationSlug = "initial"
            inputArtifactPath = "script.ps1"
            outputArtifactPath = "script.signed.ps1"
            parameters = ""
            waitForCompletion = true
        }
    }
})

object TeamcityPlayground : GitVcsRoot({
    name = "teamcity-playground"
    url = "https://github.com/Taha-cmd/teamcity-playground.git"
    branch = "refs/heads/main"
    branchSpec = "refs/heads/*"
})

object TeamcityPlaygroundVs : GitVcsRoot({
    name = "teamcity-playground-vs"
    url = "https://github.com/Taha-cmd/teamcity-playground.git"
    branch = "refs/heads/main"
    branchSpec = "refs/heads/*"
})