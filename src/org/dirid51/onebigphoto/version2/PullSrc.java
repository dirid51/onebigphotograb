package org.dirid51.onebigphoto.version2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PullSrc {
	Set<String> urls;

	public static void main(String[] args) {
//		Current valid category options are:
//		* action
//		* animal
//		* art
//		* black-and-white
//		* home
//		* landscape
//		* long-exposure
//		* machine
//		* macro
//		* nature
//		* people
//		* space
//		* urban
//		Always use lower case
		
		String category = "landscape";
		for (String s : new PullSrc().pull(category)) {
			System.out.println(s);
		}
	}
	
	public PullSrc() {
		urls = new HashSet<String>();
	}
	
	public void pullAll(int maxPage, String category) throws IOException, URISyntaxException {
		for (int i = 1; i <= maxPage; i++) {
			pullByPage(i, category);
		}
		System.out.println("Set size: " + urls.size());
		for (String s : urls) {
			System.out.println(s);
		}
	}
	
	public void pullByPage(int page, String category) throws IOException, URISyntaxException {
		getImgElems(getDocument(page, category))
			.stream()
			.map(e -> e.attr("src"))
			.map(s -> s.substring(73, s.length() - 12))
			.forEach(s -> urls.add(s));
		System.out.println("Page " + page + ": image URLs added to list. List size: " + urls.size());
	}
	
	private Document getDocument(int page, String category) throws IOException, URISyntaxException {
		Document doc = null;
        doc = Jsoup.parse(getHTMLPage("http://onebigphoto.com/category/" + category + "-photography/page/" + page + "/"));
        System.out.println("Getting DOCUMENT for page: " + page);
        return doc;
	}
	
	private Elements getImgElems(Document doc) {
		System.out.println("Getting image ELEMENTS...");
		return doc.select("img[src*=http://onebigphoto.com/wp-content/themes/onebigphotoNEW/timthumb.php?src=http://onebigphoto.com/uploads/]");
	}
	
//	private String extractURL(Element imgElem) {
//		String wholeSrc = imgElem.attr("src");
//        return wholeSrc.substring(73, wholeSrc.length() - 12);
//	}
	
	public List<String> pull(String category) {
		List<String> myUrls = new ArrayList<String>();
		for (int i = 1; i <= (new PullPageCount()).pageCount(category); i++) {
	        Document doc = null;
            try {
	            doc = Jsoup.parse(getHTMLPage("http://onebigphoto.com/category/" + category + "-photography/page/" + i + "/"));
            } catch (IOException | URISyntaxException e1) {
	            e1.printStackTrace();
	            System.exit(0);
            }
	        Elements images = doc.select("img[src*=http://onebigphoto.com/wp-content/themes/onebigphotoNEW/timthumb.php?src=http://onebigphoto.com/uploads/]");
	        for (Element e : images) {
		        String wholeSrc = e.attr("src");
		        myUrls.add(wholeSrc.substring(73, wholeSrc.length() - 12));
	        }
        }
		return myUrls;
	}
	
	public static String getHTMLPage(URL url) throws IOException, URISyntaxException {
		System.out.println("Reading page at: " + url.toString());
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        StringBuilder result = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
	        result.append(inputLine);
        }
        in.close();
        return result.toString();
	}
	
	public static String getHTMLPage(String url) throws IOException, URISyntaxException {
		return getHTMLPage(new URL(url));
	}

	public Set<String> getUrls() {
		return this.urls;
	}
}