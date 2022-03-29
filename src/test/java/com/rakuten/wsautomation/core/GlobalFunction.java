package com.rakuten.wsautomation.core;

import java.io.*;
import java.util.Properties;
import java.util.Random;

public class GlobalFunction {

public static Properties prop;

    public GlobalFunction() {
    }

    public static String GetEmailId() {
        String fichier = System.getProperty("user.dir").concat("\\src\\main\\resources\\jdd\\data.csv");
        int nbaleatoire = 0;
        String[] tableau = new String[0];
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fichier), "UTF-8"));
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] oldTableau = tableau;
                int numberligne = oldTableau.length;
                tableau = new String[numberligne + 1];
                System.arraycopy(oldTableau, 0, tableau, 0, numberligne);
                tableau[numberligne] = ligne;
            }
            nbaleatoire = new Random().nextInt(tableau.length);
            System.out.println(tableau[nbaleatoire]);
            br.close();

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }
        return tableau[nbaleatoire];
    }


}
