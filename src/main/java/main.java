import jexer.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.*;
import java.util.Scanner;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


public class main{
    public static boolean fs(int a){
        boolean first = true;
        if (a == 1){
            first = false;
        }
        return first;
    }

    //POST Method
    public static void sender(String user, String message) throws IOException{

        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:3000/api/tasks/").openConnection();

        connection.setRequestMethod("POST");

        String number = "1";

        String postData = "user=" + URLEncoder.encode(user);
        postData += "&message=" + URLEncoder.encode(message);
        postData += "&number=" + number;

        connection.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
        wr.write(postData);
        wr.flush();

        int responseCode = connection.getResponseCode();
        if(responseCode == 200){
            System.out.println("POST was successful.");
        }
        else if(responseCode == 401){
            System.out.println("Pe");
        }
    }

    //GET Method
    public static String watcher() throws IOException {

        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:3000/api/tasks/").openConnection();

        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        String response = "";
        Scanner scanner = new Scanner(connection.getInputStream());
        while(scanner.hasNextLine()){
            response += scanner.nextLine();
            response += "\n";
        }
        scanner.close();
        String messages = "";
        connection.getResponseMessage();
        System.out.println(response);

        JSONArray arra = new JSONArray(response);
        System.out.println(arra.length());
        for (int i = 0; i <= arra.length() - 1; i++) {
            JSONObject temp = (JSONObject) arra.get(i);
            System.out.println(temp.get("user") + ": " + temp.get("message"));
            messages = messages + "\n" +temp.get("user") + ": " + temp.get("message");
        }
        return messages;
    }

    public static void main(String[] args) throws Exception {


        TApplication app = new TApplication(TApplication.BackendType.SWING);



        //Login
        TWindow login_window = app.addWindow("Verzach3's Messenger", 50, 25);
        TLabel login_label = login_window.addLabel("Enter your username:",15,8);
        final  TField login_textfield = login_window.addField(15,10,20,false,"");
        final TButton login_button = login_window.addButton("Login", 22, 12,
                new TAction() {
                    @Override
                    public void DO() {
                        String username = login_textfield.getText();
                        login_window.close();
                        //Main Screen
                        TWindow chat_main_screen = app.addWindow("Verzach3's Messenger", 50, 25);
                        TLabel chat_username_label = chat_main_screen.addLabel("Tu nickname es: "+ username,6,3);
                        final TProgressBar progress = chat_main_screen.addProgressBar(6,4,30,1);
                        final TText chat_message_container = chat_main_screen.addText("",6,6,30,10);
                        final  TField chat_message_textfield = chat_main_screen.addField(15,17,15,false,"");

                        final TButton button = chat_main_screen.addButton("Enviar", 5, 17,
                                new TAction() {
                                    public void DO() {
                                        try {
                                            sender(username, chat_message_textfield.getText());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        chat_message_textfield.setText("");
                                        chat_message_container.toEnd();


                                    }
                                }
                        );

                        TTimer timer = app.addTimer(5, true,
                                new TAction() {
                                    @Override
                                    public void DO() {
                                        int a = progress.getValue();
                                        if (a >= 100){
                                            progress.setValue(0);
                                            try {
                                                chat_message_container.setText(watcher());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        } 
                                        else {
                                            progress.setValue(progress.getValue() + 10);
                                        }
                                        if (fs(0) == true){
                                            chat_message_container.toEnd();
                                            fs(1);
                                        }
                                    }
                                }
                        );
                        //End mainscreen
                    }
                }
        );

        //End Login



        app.run();


    }

}
