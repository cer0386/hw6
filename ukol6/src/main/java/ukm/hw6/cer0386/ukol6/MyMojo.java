package ukm.hw6.cer0386.ukol6;


import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.model.Build;
import org.apache.maven.model.BuildBase;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.print.attribute.standard.DateTimeAtCompleted;

/**
 * Goal which touches a timestamp file.
 */
@Mojo( name = "logIt", defaultPhase = LifecyclePhase.PROCESS_SOURCES )
public class MyMojo
    extends AbstractMojo
{
    /**
     * Location of the file.
     */
    @Parameter( defaultValue = "${project.build.directory}", property = "outputDir", required = true )
    private File outputDirectory;
    
    @Parameter( defaultValue = "${project}")
    private MavenProject project;
   
    
    @Parameter
    private Date myDate;

    public void execute()
        throws MojoExecutionException
    {
    	getLog().info("Output directory " + outputDirectory);    	
    	
    	final File folder = new File(project.getBasedir().toString());

        List<String> result = new ArrayList<>();

        search(".*\\.java", folder, result);

        for (String s : result) {
        	getLog().info(s);
        }
        
        File f = outputDirectory;

        if ( !f.exists() )
        {
            f.mkdirs();
        }
        File touch = new File( f, "logfile.log");
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");

        FileWriter w = null;
        try
        {
            w = new FileWriter( touch, true );

            w.append("Number of files " + result.size() +"; Date&Time "+ formatter.format(new Date(System.currentTimeMillis()))+" \n");
        }
        catch ( IOException e )
        {
            throw new MojoExecutionException( "Error creating file " + touch, e );
        }
        finally
        {
            if ( w != null )
            {
                try
                {
                    w.close();
                }
                catch ( IOException e )
                {
                    // ignore
                }
            }
        }
    }
    public static void search(final String pattern, final File folder, List<String> result) {
        for (final File f : folder.listFiles()) {

            if (f.isDirectory()) {
                search(pattern, f, result);
            }

            if (f.isFile()) {
                if (f.getName().matches(pattern)) {
                    result.add(f.getName());
                }
            }

        }
    }
}
