package com.mobileposse.maven.kiji.schema.migrations;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;

/**
 * Generate a new scripts
 *
 * @goal generate
 */
public class GenerateMojo extends AbstractMojo {

    /**
     * Base directory of the scanning process
     *
     * @parameter default-value="${project.basedir}/src/main/resources/schema/migrate"
     */
    public String migrateFolder;

    /**
     * Name of the migration
     *
     * @parameter default-value="TBD"
     */
    public String migrationName = "TBD";

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Log log = getLog();
        log.info("");
        log.info("Generating new migration file");
        log.info("Migrate Folder:  " + this.migrateFolder);
        log.info("Migration Name: " + this.migrationName);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        dateFormat.format(new Date());
        String migrationFileName = String.format("%s_%s.ddl", dateFormat.format(new Date()),
                migrationName.replace(" ", "_"));

        try {
            File file = new File(migrateFolder, migrationFileName);
            file.getParentFile().mkdirs();
            file.createNewFile();
            log.info("Created " + file.getCanonicalPath());
        } catch (IOException ex) {
            throw new MojoExecutionException("Could not create new migration file", ex);
        }
    }
}
