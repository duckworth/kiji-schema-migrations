package com.mobileposse.maven.kiji.schema.migrations;

import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.kiji.schema.Kiji;
import org.kiji.schema.KijiURI;
import org.kiji.schema.util.ResourceUtils;

/**
 * Resets migration version to specified value
 *
 * @goal reset
 */
public class ResetMojo extends AbstractMojo {

    /**
     * The kiji uri
     *
     * @parameter expression="${kijiURI}" default-value="kiji://.env/default"
     */
    public String kijiURI;

    /**
     * Key to be used in kiji system table to keep track of latest migration version
     *
     * @parameter expression="${migrationKey}" default-value="user.schema.migration.version"
     */
    public String migrationKey = "user.schema.migration.version";

    /**
     * Value to reset the migration version to
     *
     * @parameter expression="${migrationVersion}" default-value=""
     */
    public String migrationVersion = "";

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Log log = getLog();
        log.info("");
        log.info("Resetting kiji schema migration version");
        log.info("Kiji URI: " + this.kijiURI);
        log.info("Migration Version: '" + this.migrationVersion + "'");

        try {
            Kiji kiji = Kiji.Factory.open(KijiURI.newBuilder(kijiURI).build());
            try {
                setMigration(kiji, migrationVersion);
            } finally {
                ResourceUtils.releaseOrLog(kiji);
            }
        } catch (Exception ex) {
            throw new MojoExecutionException("Error while resetting migration version", ex);
        }
        log.info("Reset complete");
    }

    private void setMigration(Kiji kiji, String migration) throws IOException {
        kiji.getSystemTable().putValue(migrationKey, migration.getBytes("UTF-8"));
    }
}
