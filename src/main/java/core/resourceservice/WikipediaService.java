/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.resourceservice;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import structures.Keyword;
import structures.resources.Wikipedia;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author pmeladianos
 */
public class WikipediaService extends resourceService {

    public List<Wikipedia> createWikipediaArticles() {
        ArrayList<Wikipedia> list = new ArrayList<Wikipedia>();
        list.add(new Wikipedia("0"));
        return list;
    }

    //TODO fix or Remove
    public List<Wikipedia> getWikipediaArticles() {
        List<Wikipedia> items = new ArrayList<Wikipedia>();
        Collections.shuffle(getQueries());
        for (String key : getQueries()) {
            //String query = getWikipediaServiceQuery(key);
            String query = "";
            String response = callWIKIAPI(query);
            Document doc = Jsoup.parse(response, "", Parser.xmlParser());
            int intcc = 0;
            for (Element e : doc.select("p")) {
                String title = e.attr("title");
                if (title.contains("porn"))
                    continue;
                items.add(new Wikipedia(title));
                intcc++;
                if (intcc >= 1)
                    break;
            }
            if (items.size() > 9)
                break;
        }

        System.out.println("WIKI hits" + items.size());
        return items;
    }

    //TODO fix or Remove
    private String getWikipediaServiceQuery() {
        String q = "";
        String tags = "";
        for (String key : getQueries()) {
            String s = key.toString();
            tags += s + "%20";
        }
        q = "https://fr.wikipedia.org/w/api.php?action=query&list=search&srsearch=" + tags.substring(0, tags.length() - 1) + "&format=xml";
        System.out.println(q);

        return q;
    }

    private String getWikipediaServiceQuery(Keyword key) {
        String s = key.getKey().toString();
        String q = "https://fr.wikipedia.org/w/api.php?action=query&srwhat=text&list=search&srsearch=" + s + "&format=xml";
        return q;
    }

    private String callWIKIAPI(String query) {
        String output = "";
        try {
            output = callAPI(new URL(query).openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

}
