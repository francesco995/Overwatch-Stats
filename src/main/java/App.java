import java.io.*;

public class App {

    public static void main(String[] args) throws IOException {

        Player ps = new Player("TheWraith-2169");

        try{
            ps.loadData();
        }catch (NullPointerException e){
            System.out.println("Database empty");
        }

        System.out.println("Currently "+ ps.getStatsN() + " stats in db");

        for(int i = 0; i < 1000; i++){


            //System.out.println(i);

            ps.getNewStats();

            ps.saveData();

            System.out.println("Currently "+ ps.getStatsN() + " stats in db");


            try {
                Thread.sleep(1000*60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        ps.saveData();


        //save(ps);
        //System.out.println("P saved");

        //Player ps1 = load();
        //System.out.println("P loaded");

    }


    public static void save(Player db){
        try (
                FileOutputStream fos = new FileOutputStream("db.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(db);
        }catch(IOException ioe){
            System.out.println("Problem saving Treets");
            ioe.printStackTrace();
        }

    }


    public static Player load(){
        Player load = new Player("TheWraith-2169");
        try(
                FileInputStream fis = new FileInputStream("db.ser");
                ObjectInputStream ois = new ObjectInputStream(fis);
        ){
            load = (Player) ois.readObject();
        }	catch(IOException ioe){
            System.out.println("Error reading file");
            ioe.printStackTrace();
        }	catch (ClassNotFoundException cnfe){
            System.out.println("Error loading stats");
            cnfe.printStackTrace();
        }
        return load;
    }


}
