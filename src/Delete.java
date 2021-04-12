import java.util.Vector;
import java.util.concurrent.*;

public class Delete extends Thread{
    private String name;
    private Vector<Music> musics;
    private String textArea;

    public Delete(String name, Vector<Music> $musicsList, String $textArea) {
        this.name = name;
        this.musics = $musicsList;
        this.textArea = $textArea;
    }

    @Override
    public void run() {
        this.textArea = "";
        for(int i = 0; i < musics.size(); i++) {
            if(this.musics.get(i).getName().equals(this.name)){
                this.musics.remove(i);
                i--;
            } else {
                this.textArea += this.musics.get(i) + "\n";
            }
        }
        System.out.println("Removed");
    }
}
