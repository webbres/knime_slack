# CDK Dependency materialisation


Supports the generation of the require lib folders for KNIME plugins

1. In the pom.xml specify the libraries of interest
2. Run mvn dependency:copy-dependencies
3. Copy the contents of ./target/depenency into the lib folder of the KNIME plugin

