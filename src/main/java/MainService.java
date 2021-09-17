import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author hellodk
 * @date 2021-09-12 14:20
 */
public class MainService {

    private static final String apiUrl = "http://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=zh-CN";
    private static final String baseUrl = "http://bing.com";

    public static String getTodayString() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String now = date.format(formatter1);
        return now;
    }

    public static void savePic2Disk(String saveFolder) {
        HttpURLConnection con;
        BufferedReader bufferedReader;
        String finalImageUrl = "";
        try {
            URL url = new URL(apiUrl);
            // 得到连接对象
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            while ((finalImageUrl = bufferedReader.readLine()) != null) {
                sb.append(finalImageUrl);
            }
            JSONObject jsonObject = JSONObject.parseObject(sb.toString());
            if (jsonObject != null) {
                JSONArray images = jsonObject.getJSONArray("images");
                if (images != null && images.size() > 0) {
                    JSONObject theImage = images.getJSONObject(0);
                    String imageUrl = theImage.getString("url");
                    finalImageUrl = baseUrl.concat(imageUrl);
                }
            }

            bufferedReader.close();
            con.disconnect();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (finalImageUrl != "" && finalImageUrl != null) {
            try {
                Path path = Paths.get(getRealSaveFolder(saveFolder));
                String todayString = getTodayString();
                StringBuilder sb = new StringBuilder();
                sb.append(path.toString()).append("/").append(todayString).append(".jpg");
                File file = new File(sb.toString());
                URL url = new URL(finalImageUrl);
                URLConnection urlConnection = url.openConnection();
                InputStream is = urlConnection.getInputStream();
                // 构造 1KB 的数据缓冲字节数组
                byte[] bs = new byte[1024];
                int len;
                // 如果已经存在文件则将其删除
                if (file.exists()) {
                    file.delete();
                }
                FileOutputStream os = new FileOutputStream(file, true);
                // 不断读取输入流
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
                // 关闭连接
                is.close();
                os.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private static String getRealSaveFolder(String saveFolder) {
        return saveFolder.replace("--saveFolder=", "");
    }

    public static void main(String[] args) {
        int length = args.length;
        if (length < 1 || length > 1) {
            System.out.println("usage: `java -jar xxx.jar --saveFolder=xxx`, please use correct parameter to run this program.");
            return;
        }
        String saveFolder = args[0];
        if (!saveFolder.startsWith("--saveFolder=")) {
            System.out.println("usage: `java -jar xxx.jar --saveFolder=xxx`, please use correct parameter to run this program.");
            return;
        }
        savePic2Disk(saveFolder);
    }

}
