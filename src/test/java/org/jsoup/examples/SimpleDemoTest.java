package org.jsoup.examples;

import junit.framework.TestCase;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;

public class SimpleDemoTest extends TestCase {

    // I'm so sorry for not checking the equals-function.
    public void testEquals() throws Exception {
        String data = ParallelParserTest.readFile("src/test/extern/hs.html");

        Document d1 = Parser.parse(data, "");
        Document d2 = Parser.parse(data, "");
        assertTrue(d1.equals(d2));
    }

    public void testSimpleDemo() throws Exception {
        loadOneFile("src/test/extern/hs.html");
    }

    private void loadOneFile(String path) throws IOException {
        String data = ParallelParserTest.readFile(path);

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
        if (!dd.equals(d)) {
            System.out.println("出现异常！");
        }

        System.out.println("expect:");
        System.out.println(dd);
        System.out.println("now:");
        System.out.println(d);
    }
}