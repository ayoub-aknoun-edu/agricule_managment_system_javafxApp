package com.app.engineerapp.FunctionUtil;

import java.io.*;

public class JavaMailUtil {
    public static void sendmail(String recepteur, String verificationcode){
        String command = "python \"C:\\Program Files\\AGRICU\\email_test.py\" "+ recepteur +" "+verificationcode;
        try {
            Process p = Runtime.getRuntime().exec(command);
            String cmdOutput = null;
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            // read the output from the command
            while ((cmdOutput = stdInput.readLine()) != null) {
                System.out.println(cmdOutput);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
