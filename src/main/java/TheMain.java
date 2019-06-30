import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;
import java.util.*;

import javax.security.auth.login.LoginException;

public class TheMain extends ListenerAdapter {
    public static void main(String[] args) throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);

        builder.setToken("tokens");
        builder.addEventListener(new TheMain());
        builder.build();
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        String the_message = event.getMessage().getContentRaw().toLowerCase();
        System.out.println( "Message from: " +
                event.getAuthor().getName() + "#"+event.getAuthor().getDiscriminator()+": "+ event.getMessage().getContentDisplay()

        );

        boolean haveRespond = false;
        ArrayList<String> theRes = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    System.getProperty("user.dir")+"/learn.txt"));
            String line = reader.readLine();

            while (line != null) {
                line = reader.readLine();
                String question = "";
                String respond= "";
                try {
                    question = line.substring(line.indexOf("*") + 1, line.indexOf(":"));
                    respond = line.substring(line.indexOf(":") + 1);
                } catch(Exception e){System.out.println("Bot will not respond");}
                if(the_message.equals(question.trim())){
                    event.getChannel().sendMessage("typing...").queue();
                    haveRespond = true;
                    theRes.add(respond);

                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (haveRespond){
            if (theRes.size() > 1){
                int num = (int)(Math.random() * (theRes.size()));
                event.getChannel().sendMessage(theRes.get(num)).queue();
                theRes.clear();
            }
            else {
                event.getChannel().sendMessage(theRes.get(0)).queue();
            theRes.clear();
            }
        }

        if (the_message.substring(0,1).equals("*")){
            File checkifex = new File(System.getProperty("user.dir")+"/learn.txt");
            if(!checkifex.exists() && checkifex.isDirectory()) {
                try {
                    checkifex.createNewFile();
                }catch (Exception e){e.printStackTrace();}
            }
            String cheee = System.getProperty("user.dir")+"/learn.txt";
            BufferedWriter bw = null;
            FileWriter fw = null;
            try {
                fw = new FileWriter(cheee, true);
                bw = new BufferedWriter(fw);
                bw.write(the_message+"\n");
            } catch (IOException e2) {
                e2.printStackTrace();
            } finally {
                try {
                    if (bw != null)
                        bw.close();
                    if (fw != null)
                        fw.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            String question = the_message.substring(the_message.indexOf("*")+1, the_message.indexOf(":"));
            String respond = the_message.substring(the_message.indexOf(":")+1);
            event.getChannel().sendMessage("TIL when the question is: "+question+ "; I respond: "+ respond).queue();
        }

        if ( the_message.length() >= 11 && !(the_message.substring(0,1).equals("*")) && !haveRespond) {
            boolean found = false;
            while (!found) {
                if (the_message.substring(the_message.indexOf("u"), the_message.indexOf("u") + 11).equals("useless bot"))
                    found = true;
                else
                    the_message = the_message.substring(the_message.indexOf("u") + 1);
            }
            if (found) {
                event.getChannel().sendMessage("typing...").queue();
                //event.getMessage().delete().queue();
                found = false;
                double ran = Math.random();
                if (ran>0.5)
                    event.getChannel().sendMessage("no").queue();
                else event.getChannel().sendMessage("no u").queue();

            }
        }

    }
}
