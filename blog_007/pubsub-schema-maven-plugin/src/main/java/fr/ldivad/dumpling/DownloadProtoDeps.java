package fr.ldivad.dumpling;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

@Mojo(name = "proto-files", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class DownloadProtoDeps extends AbstractMojo {

  @Parameter(defaultValue = "${project.basedir}/src/main/proto/generated/", readonly = true)
  String path;

  @Parameter(readonly = true)
  String[] schemas;

  private static final Logger logger = LoggerFactory.getLogger(DownloadProtoDeps.class);

  private static Stream<String> command() {
    return Stream.of("gcloud", "pubsub", "schemas", "describe");
  }


  private void download(String schema) {
    String gcloudCommand = command().collect(Collectors.joining(" ")) + " " + schema;
    String fileName = schema.replace("-", "_") +  ".proto";

    Process process = null;
    Yaml yaml = new Yaml();

    try {
      process = Runtime.getRuntime().exec(gcloudCommand);
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      process.waitFor();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    Map<String, Object> result = yaml.load(process.getInputStream());

    logger.info(String.format("Writing the schema: %s in the file; %s", schema, fileName));

    try {
      final FileWriter writer = new FileWriter(path + "/" + fileName);
      writer.write(result.get("definition").toString());
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {

    logger.info("Start to download the consumer schema");

    logger.info(String.format("Download path: %s", path));

    final boolean created = new File(path).mkdirs();

    if (created) {
      logger.info("Download path successfully created");
    }

    logger.info("Number of schema to download: " + schemas.length);

    Arrays.stream(schemas).forEach(this::download);

    logger.info("Running the main method");
  }
}
