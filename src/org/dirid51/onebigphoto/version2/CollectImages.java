package org.dirid51.onebigphoto.version2;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

public class CollectImages {
//	List<String> imageUrls = new ArrayList<>();
	
//	Before running, be sure that you set the correct category
//	Current valid category options are:
//	* action
//	* animal
//	* art
//	* black-and-white
//	* home
//	* landscape
//	* long-exposure
//	* machine
//	* macro
//	* nature
//	* people
//	* space
//	* urban
//	Always use lower case
	
	static final String[] CATEGORY = {"animal","landscape","nature","long-exposure","space","urban"};

	public static void main(String[] args) {
		CollectImages collector = new CollectImages();
		for (String cat : CATEGORY) {
			String saveTo = "C:\\Screensaver Photos\\OneBigPhoto\\" + CollectImages.capitalize(cat) + "\\";
			String regex = "^http://onebigphoto\\.com/uploads/\\d+/\\d+/(.+\\.jpg)$";
			collector.collect(saveTo, regex, cat);
		}
	}
	
	public static String capitalize(String str) {
		return Arrays.asList(str.split("[- ]"))
					.stream()
					.map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
					.reduce((t, u) -> (new StringBuilder(t + " " + u)).toString())
					.get();
//		return str.substring(0,1).toUpperCase() + str.substring(1);
	}

	public void collect(String saveToDirectory, String extractFileNameRegex, String category) {
		PullSrc puller = new PullSrc();
		int maxPage = (new PullPageCount()).pageCount(category);
		try {
			puller.pullAll(maxPage, category);
//	        for (int i = 1; i <= maxPage; i++) {
//	            puller.pullByPage(i);
//            }
//            imageUrls.addAll(puller.getUrls());
//			imageUrls
	        puller.getUrls()
	        	.stream()
	        	.forEach(s -> new DownloadFile().save(s, saveToDirectory, extractFileNameRegex));
//	        	.forEach(s -> System.out.println(s));
        } catch (IOException | URISyntaxException e) {
	        e.printStackTrace();
        }
		
    }
}
