package org.dirid51.onebigphoto.version2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PullPageCount {

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
		System.out.println("Page Count: " + (new PullPageCount()).pageCount(category));
	}
	
	public int pageCount(String category) {
		int totalPages = 0;
		Document doc;
        try {
	        doc = Jsoup.parse(getHTMLPage(new URL("http://onebigphoto.com/category/" + category + "-photography/")));
			Elements links = doc.select("div.paginationitem");
			for (Element e : links) {
				try {
	                Integer pageNumber = Integer.parseInt(e.text());
	                if (pageNumber > totalPages) {
	                	totalPages = pageNumber;
	                }
                } catch (NumberFormatException e1) {
	                System.out.println("Ignored: " + e.text());
                }
			}
        } catch (IOException | URISyntaxException e1) {
	        e1.printStackTrace();
        }
        System.out.println("Max Page Number: " + totalPages);
        return totalPages;
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

}
