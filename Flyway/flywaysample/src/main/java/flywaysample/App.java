package flywaysample;

import org.flywaydb.core.Flyway;

/**
 * https://flywaydb.org/getstarted/firststeps/api
 *
 * mvn archetype:generate -B -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.1 -DgroupId=robloxro -DartifactId=flywaysample -Dversion=1.0-SNAPSHOT -Dpackage=flywaysample
 *
 * https://www.baeldung.com/database-migrations-with-flyway
 *
 * flyway.properties
 *
 */
public class App
{
    public static void main( String[] args )
    {
        System.out.println( "Starting ..." );

        // Create the Flyway instance and point it to the database
        Flyway flyway = Flyway.configure().dataSource("jdbc:h2:file:./target/flywaysample",
                "sa", null).load();

        // Start the migration
        flyway.migrate();
    }
}
