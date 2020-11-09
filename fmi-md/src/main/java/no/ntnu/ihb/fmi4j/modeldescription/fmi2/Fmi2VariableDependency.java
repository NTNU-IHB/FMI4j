
package no.ntnu.ihb.fmi4j.modeldescription.fmi2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Fmi2VariableDependency {

    public Fmi2VariableDependency() {
    }

    protected Fmi2VariableDependency(String dummy) {
    }

    @JsonProperty(value = "Unknown", required = true)
    @JacksonXmlElementWrapper(useWrapping = false)
    protected List<Fmi2VariableDependency.Unknown> unknown;

    public List<Fmi2VariableDependency.Unknown> getUnknown() {
        if (unknown == null) {
            unknown = new ArrayList<>();
        }
        return this.unknown;
    }


    public static class Unknown {

        @JacksonXmlProperty(isAttribute = true)
        private int index;
        @JsonIgnore
        @JacksonXmlProperty(isAttribute = true)
        private String dependencies;
        @JacksonXmlProperty(isAttribute = true)
        private String dependenciesKind;

        public int getIndex() {
            return index;
        }

        public void setIndex(int value) {
            this.index = value;
        }

        @JsonIgnore
        public List<Integer> getDependencies() {
            if (dependencies == null || dependencies.isEmpty()) {
                return new ArrayList<>();
            } else {
                String[] split = dependencies.trim().split(" ");
                return Arrays.stream(split).map(Integer::parseInt).collect(Collectors.toList());
            }
        }

        @JsonProperty
        public void setDependencies(String dependencies) {
            this.dependencies = dependencies;
        }

        @JsonIgnore
        public List<String> getDependenciesKind() {
            if (dependenciesKind == null || dependencies.isEmpty()) {
                return new ArrayList<>();
            } else {
                return Arrays.asList(dependenciesKind.split(" "));
            }
        }

        @JsonProperty
        public void setDependenciesKind(String dependenciesKind) {
            this.dependenciesKind = dependenciesKind;
        }
    }

}
