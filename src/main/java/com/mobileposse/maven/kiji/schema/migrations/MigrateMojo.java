package com.mobileposse.maven.kiji.schema.migrations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.DirectoryScanner;
import org.kiji.schema.Kiji;
import org.kiji.schema.KijiURI;
import org.kiji.schema.shell.api.Client;
import org.kiji.schema.util.ResourceUtils;

/**
 * Run the migration scripts
 *
 * @goal migrate
 */
public class MigrateMojo extends AbstractMojo {

    /**
     * The kiji uri
     *
     * @parameter property="kijiURI" default-value="kiji://.env/default"
     */
    public String kijiURI;

    /**
     * Base directory of the scanning process
     *
     * @parameter property="migrateFolder" default-value="${project.basedir}/src/main/resources/schema/migrate"
     */
    public String migrateFolder;

    /**
     * Key to be used in kiji system table to keep track of latest migration version
     *
     * @parameter property="migrationKey" default-value="user.schema.migration.version"
     */
    public String migrationKey = "user.schema.migration.version";

    /**
     * Ant-style include pattern.
     *
     * For example **.* is all files
     *
     * @parameter
     */
    public String[] includes = new String[] { "*_*.ddl", "*_*.sql" };

    /**
     * Ant-style exclude pattern.
     *
     * For example **.* is all files
     *
     * @parameter
     */
    public String[] excludes;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Log log = getLog();
        log.info("");
        log.info("Migration kiji schema");
        log.info("Kiji URI: " + this.kijiURI);
        log.info("Migrate Folder:  " + this.migrateFolder);
        log.info("Includes: " + Arrays.toString(this.includes));
        log.info("Exludes:  " + Arrays.toString(this.excludes));

        Client client = Client.newInstance(KijiURI.newBuilder(kijiURI).build());
        try {
            // One or more calls to executeUpdate(). These will throw DDLException
            // if there's a problem with your DDL statement.

            DirectoryScanner scanner = new DirectoryScanner();
            scanner.setBasedir(this.migrateFolder);
            scanner.setIncludes(this.includes);
            scanner.setExcludes(this.excludes);
            scanner.setCaseSensitive(true);
            scanner.scan();

            String[] includedFiles = scanner.getIncludedFiles();

            log.info("File list contains " + includedFiles.length + " files");

            Arrays.sort(includedFiles);
            try {
                Kiji kiji = Kiji.Factory.open(client.kijiUri());

                String migration = getMigration(kiji);

                try {

                    for (String ddl : includedFiles) {
                        File file = new File(migrateFolder, ddl);
                        String fileName = file.getName();
                        String filePrefix = fileName.split("_")[0];
                        if (filePrefix.compareTo(migration) > 0) {
                            try {
                                log.info("Executing " + file.getCanonicalPath() + " ...");
                                InputStream in = new FileInputStream(file);
                                client.executeStream(in);
                                log.info(client.getLastOutput());
                            } catch (FileNotFoundException fnfex) {
                                new MojoExecutionException("Could not find ddl file", fnfex);
                            }
                            migration = filePrefix;
                            setMigration(kiji, migration);
                        }

                    }
                } finally {
                    ResourceUtils.releaseOrLog(kiji);
                }
            } catch (IOException ex) {
                throw new MojoExecutionException("Could not execute migration", ex);
            }

        } finally {
            // Always call close when you're done.
            client.close();
        }

    }

    private String getMigration(Kiji kiji) throws IOException {
        byte[] bytes = kiji.getSystemTable().getValue(migrationKey);
        if (bytes == null) {
            return "";
        }
        return new String(bytes, "UTF-8");
    }

    private void setMigration(Kiji kiji, String migration) throws IOException {
        kiji.getSystemTable().putValue(migrationKey, migration.getBytes("UTF-8"));
    }
}
