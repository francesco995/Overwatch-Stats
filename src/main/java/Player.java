import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Player implements Serializable {

    private TreeMap<Long, PlayerStat> mPlayerStats;
    private String mUserName;


    Player(String userName){

        mPlayerStats = new TreeMap<>();
        mUserName = userName;

    }

    public void getNewStats(){

        try {
            PlayerStat temp = new PlayerStat(mUserName);
            mPlayerStats.put(temp.getDate().getTime(), temp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadData() throws NullPointerException{

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        List<String> stats = new ArrayList<>();
        File[] files = new File(mUserName).listFiles();

        for (File file : files) {
            if (file.isFile()) {
                stats.add(file.getName());
            }
        }

        stats.stream().forEach(s -> {

            try {
                String psString = loadStringFromFile(mUserName + "/" + s);
                PlayerStat ps = gson.fromJson(psString, PlayerStat.class);
                mPlayerStats.put(ps.getDate().getTime(), ps);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        });

    }

    /**
     * Loads a text-based file to a string
     *
     * @param fileName name of the file to load
     * @return returns the file as a string
     * @throws FileNotFoundException
     */
    private String loadStringFromFile(String fileName) throws FileNotFoundException {

        String mStringDeck;

        Scanner mScanner = new Scanner(new File(fileName));
        mStringDeck = mScanner.useDelimiter("\\A").next();

        mScanner.close();

        return mStringDeck;
    }



    public void saveData() {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            Files.createDirectories(Paths.get(mUserName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPlayerStats.values().stream().forEach(ps -> {
            try {

                String jsonString = gson.toJson(ps);

                FileWriter fw = new FileWriter(new File(mUserName + "/" + ps.getDate().getTime() + ".json"));
                BufferedWriter out = new BufferedWriter(fw);

                out.write(jsonString);
                out.flush();
                out.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public int getStatsN(){
        return mPlayerStats.size();
    }



}
