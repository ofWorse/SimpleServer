package namesPkg;

import java.io.*;
import java.util.LinkedList;
import java.util.List;


public class NamesHolder {
    public static List<String> userNames = new LinkedList<>();
    public static boolean addUser(String name) throws IOException {

        if(name.startsWith("\n")) return false;
        else {
            var fw = new FileWriter("usernames.txt", true);
            updateList();
            if (userNames.contains(name))
                return false;
            else {
                fw.write(name + "\n");
                fw.flush();
                fw.close();
                return true;
            }
        }
    }

    public static void removeUser(String tcpConnectionNumber) throws IOException {
        userNames.removeIf(elem -> elem.contains(tcpConnectionNumber));
        var nfw = new FileWriter("usernames.txt", true);

        for(var names : userNames)
            nfw.write(names);
        nfw.flush();
        nfw.close();
    }

    private static void updateList() throws IOException {
        var fr = new FileReader("usernames.txt");
        var bf = new BufferedReader(fr);
        var line = bf.readLine();
        while(line!=null) {
            userNames.add(line);
            line = bf.readLine();
        }
    }
}
