package org.dirid51.onebigphoto.version2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadFile {
	
	public String save(String url, String directoryPath, String regex) {
		if (Files.exists(Paths.get(directoryPath))) {
	        System.out.print("Starting download of " + url + " to " + directoryPath + "...");
	        if (directoryPath != null && directoryPath.length() > 0) {
		        return guts(url, directoryPath, regex);
	        } else {
		        System.out.println(" Fail!");
		        return "Invalid or missing directory path";
	        }
        } else {
        	try {
	            Files.createDirectories(Paths.get(directoryPath));
	        	return "Created directory path.\n\n" + guts(url, directoryPath, regex);
            } catch (IOException e) {
	            e.printStackTrace();
	            return "No such directory path. Attempt to create directory path failed.";
            }
        }
	}
	
	private String guts(String url, String directoryPath, String regex) {
		Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(url);
        if (m.matches()) {
	        //			    System.out.println("Match found: " + m.group(1));
	        String fullPath = directoryPath + m.group(1);
	        if (!Files.exists(Paths.get(fullPath))) {
		        long bytesWritten = 0;
		        try (ReadableByteChannel rbc = Channels.newChannel(new URL("http://onebigphoto.com/wp-content/themes/onebigphotoNEW/timthumb.php?src="
		                        + url + "&w=1500&h=1000").openStream());
		                        FileOutputStream fos = new FileOutputStream(fullPath);) {
			        bytesWritten = fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		        } catch (IOException e) {
			        e.printStackTrace();
		        }
		        System.out.println(" Complete!");
		        return Long.toString(bytesWritten) + " @ " + fullPath;
	        } else {
		        System.out.println(" Already exists");
		        return "0 @ " + fullPath;
	        }
        } else {
	        System.out.println(" Fail!");
	        return "Invalid URL: No file name found";
        }
	}
}
