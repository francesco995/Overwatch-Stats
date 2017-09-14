import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class PStat implements Serializable {

    private static final long serialVersionUID = 000000000000000001L;

    //Date
    private Date mDate = new Date();

    //Original HTML
    private String mOriginalHTML;

    private ModeStats mCompetitiveStats;
    private ModeStats mQuickPlayStats;

    //              CATEGORY        ACHIEV  VALUE TODO: implement
    //private HashMap<String, HashMap<String, String>> mAchievements;

    private int mPlayerLevel;
    private int mPlayerRank;
    private int mGamesWon;



    PStat(String userName) throws IOException {

        //Load web page
        Document doc = Jsoup.connect("https://playoverwatch.com/en-us/career/pc/eu/" + userName).get();

        //Initialize maps
        mCompetitiveStats = new ModeStats("competitive", doc);
        mQuickPlayStats = new ModeStats("quickplay", doc);

        //mOriginalHTML = doc.toString();

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




    public Date getDate() {
        return mDate;
    }


}
