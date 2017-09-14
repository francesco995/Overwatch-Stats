import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;

public class ModeStats {

    private String mModeName;

    //              HERO            CATERGORY       STAT    VALUE
    private HashMap<String, HashMap<String, HashMap<String, String>>> mStats;





    public ModeStats(String mModeName, Document doc) {

        mModeName = mModeName;

        mStats = new HashMap<>();

        //Get mode data from doc
        Elements mode = doc
                .getElementById(mModeName)
                .getElementsByClass("career-stats-section")
                .get(0).child(0).children();

        //Remove empty first heroes (placeholders)
        mode.remove(0);
        mode.remove(0);

        //Add each hero to competitive stats
        //Hero # depends on played heroes
        mode.stream().forEach(hero -> {
            //single hero data (first all heroes)
            addHeroData(hero, mStats);
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


}
