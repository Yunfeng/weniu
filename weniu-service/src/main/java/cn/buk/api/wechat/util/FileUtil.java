package cn.buk.api.wechat.util;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: william
 * Date: 14-10-2
 * Time: 下午11:34
 * To change this template use File | Settings | File Templates.
 */
public class FileUtil {
    public static final String file2String(String filename, String charset) {
        BufferedReader br;
        StringBuilder strBlder = new StringBuilder("");
        File file = new File(filename);
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
            String line = "";
            while (null != (line = br.readLine())) {
                strBlder.append(line + "\n");
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return strBlder.toString();
    }
}
