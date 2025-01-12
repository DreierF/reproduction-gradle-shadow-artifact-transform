plugins {
	application
	id("com.gradleup.shadow") version "9.0.0-beta4"
}

application {
	mainClass.set("com.company.Main")
}

dependencies {
	implementation(project(":lib"))
}

val transformedAttribute = Attribute.of("custom-transformed", Boolean::class.javaObjectType)

dependencies.artifactTypes.maybeCreate("jar").attributes.attribute(transformedAttribute, false)

dependencies.registerTransform(CustomTransformAction::class) {
	from.attributes.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "jar").attribute(transformedAttribute, false)
	to.attributes.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "jar").attribute(transformedAttribute, true)
}

configurations.runtimeClasspath {
	attributes.attribute(transformedAttribute, true)
}

abstract class CustomTransformAction : TransformAction<TransformParameters.None> {

	@get:InputArtifact
	abstract val inputArtifact: Provider<FileSystemLocation>

	override fun transform(outputs: TransformOutputs) {
		val originalJar = inputArtifact.get().asFile

		// Omitted actual jar file transformation

		outputs.file(originalJar)
	}
}
