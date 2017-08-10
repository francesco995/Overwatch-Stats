import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class PlayerStat implements Serializable {

    private static final long serialVersionUID = 000000000000000001L;

    //Date
    private Date mDate = new Date();

    //Original HTML
    private String mOriginalHTML;

    //              HERO            CATERGORY       STAT    VALUE
    private HashMap<String, HashMap<String, HashMap<String, String>>> mCompetitiveStats;
    private HashMap<String, HashMap<String, HashMap<String, String>>> mQuickPlayStats;

    //              CATEGORY        ACHIEV  VALUE TODO: implement
    //private HashMap<String, HashMap<String, String>> mAchievements;

    private int mPlayerLevel;
    private int mPlayerRank;
    private int mGamesWon;



    PlayerStat(String userName) throws IOException {

        //Initialize maps
        mCompetitiveStats = new HashMap<>();
        mQuickPlayStats = new HashMap<>();

        //Load web page
        Document doc = Jsoup.connect("https://playoverwatch.com/en-us/career/pc/eu/" + userName).get();

        //mOriginalHTML = doc.toString();

        addModeData("competitive", doc, mCompetitiveStats);
        addModeData("quickplay", doc, mQuickPlayStats);

        mPlayerRank = getSR(doc);
        mPlayerLevel = getLevel(doc);
        mGamesWon = getGamesWon(doc);

    }


    private int getGamesWon(Document doc){

        String dataString = (doc
                .getElementsByClass("masthead-detail h4")
                .get(0).child(0).html());

        dataString = dataString.replaceAll("\\D+","");

        return Integer.parseInt(dataString);

    }


    private int getLevel(Document doc){
        //TODO: handle exceptions
        return Integer.parseInt(
                doc.getElementsByClass("masthead-player-progression show-for-lg")
                        .get(0).child(0).child(0).html());

    }


    private int getSR(Document doc){
        //TODO: handle exceptions
        return Integer.parseInt(doc.getElementsByClass("competitive-rank").get(0).child(1).html());

    }


    private void addModeData(String modeName, Document doc, HashMap<String, HashMap<String, HashMap<String, String>>> heroData){

        //Get mode data from doc
        Elements mode = doc
                .getElementById(modeName)
                .getElementsByClass("career-stats-section")
                .get(0).child(0).children();

        //Remove empty first heroes (placeholders)
        mode.remove(0);
        mode.remove(0);

        //Add each hero to competitive stats
        //Hero # depends on played heroes
        mode.stream().forEach(hero -> {
            //single hero data (first all heroes)
            addHeroData(hero, heroData);
        });

    }


    private void addHeroData(Element hero, HashMap<String, HashMap<String, HashMap<String, String>>> heroData){

        String heroCode = hero.attr("data-category-id");

        heroData.put(heroCode, new HashMap<>());

        hero.childNodes().forEach(category -> {

            //Navigate to category kay name
            String categoryKey = category
                    .childNode(0)
                    .childNode(0)
                    .childNode(0)
                    .childNode(0)
                    .childNode(0)
                    .childNode(1)
                    .childNode(0)
                    .toString();

            //Add category key
            heroData.get(heroCode).put(
                    categoryKey , new HashMap<>());

            category.childNode(0).childNode(0).childNode(1).childNodes().forEach(stat -> {

                heroData.get(heroCode).get(categoryKey).put(
                        stat.childNode(0).childNode(0).toString(), stat.childNode(1).childNode(0).toString());

            });

        });

    }



    public Date getDate() {
        return mDate;
    }


}
