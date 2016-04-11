package org.jsoup.examples;

import junit.framework.TestCase;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;

public class ParallelParserTest extends TestCase {

    int k = 0;
    int p = 0;
    int all = 0;
    public void testAll() {
        k = 0;
        loadAllFiles("src/test/extern/websites");
        System.out.println("正确率： " + k*100/all + "%");
        System.out.println("崩溃率： " + (all-p)*100/all + "%");
    }

    public void loadAllFiles(String filePath) {
        File f = new File(filePath);
        File[] files = f.listFiles(); // 得到f文件夹下面的所有文件。
        assert files != null;
        all = files.length;
        for(File file : files) {
            System.out.println(file.getAbsolutePath());
            try {
                loadOneFile(file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadOneFile(String path) throws IOException {
        String data = readFile(path);

        long begin = System.nanoTime();
        Document dd = Parser.parse(data, "");
        long end = System.nanoTime();
        System.out.println("normal time: " + (end - begin) / 1000000 + "ms");

        Document d = null;
        try {
            ParallelParser pp = new ParallelParser(data, 4);
            d = pp.parse();
        } catch (Exception e1) {
            e1.printStackTrace();
            return;
        }
        assertNotNull(d);
        assertTrue(d.getClass() == dd.getClass());
        p++;
        if (!dd.equals(d)) {
            System.out.println("出现异常！");
        } else {
            k++;
        }
    }


    public static String readFile(String fileName) throws IOException {
        StringBuilder buffer = new StringBuilder();
        InputStream is = new FileInputStream(fileName);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
        return buffer.toString();
    }
}