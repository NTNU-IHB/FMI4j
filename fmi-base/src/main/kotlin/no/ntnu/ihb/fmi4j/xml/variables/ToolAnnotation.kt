package no.ntnu.ihb.fmi4j.xml.variables

typealias Annotations = List<ToolAnnotation>

data class ToolAnnotation(
        val name: String,
        val `object`: Any
)