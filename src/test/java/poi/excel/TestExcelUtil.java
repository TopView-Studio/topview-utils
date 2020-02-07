package poi.excel;

import com.topview.utils.poi.excel.ExcelUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import poi.excel.bean.Head;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

/**
 * @author Albumen
 * @date 2020/1/23
 */
public class TestExcelUtil {
    @BeforeAll
    public static void testWrite() throws IOException {
        File file = new File("Test-Excel.xlsx");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        List<Head> dataList = new LinkedList<>();
        for (int i = 0; i < 10000; i++) {
            String s = i + "";
            dataList.add(new Head(s, s, s));
        }
        ExcelUtil.writeToExcel("测试表名", fileOutputStream, Head.class, null, dataList, 1, 0);
    }

    @Test
    public void testSimpleRead() throws FileNotFoundException, InterruptedException {
        File file = new File("Test-Excel.xlsx");
        FileInputStream fileInputStream = new FileInputStream(file);
        List<Head> data = ExcelUtil.simpleReadFromExcel(fileInputStream, 0, Head.class);
        assertData(data);
    }

    @Test
    public void testSimpleReadWithHead() throws FileNotFoundException, InterruptedException {
        File file = new File("Test-Excel.xlsx");
        FileInputStream fileInputStream = new FileInputStream(file);
        List<Head> data = ExcelUtil.simpleReadFromExcel(fileInputStream, 0, 1, Head.class);
        assertData(data);
    }

    @Test
    public void testSaxRead() throws FileNotFoundException, InterruptedException {
        File file = new File("Test-Excel.xlsx");
        FileInputStream fileInputStream = new FileInputStream(file);
        CountDownLatch countDownLatch = new CountDownLatch(10000);
        ConcurrentLinkedQueue<Head> saxQueue = new ConcurrentLinkedQueue<>();
        ExcelUtil.saxReadFromExcel(fileInputStream, 0, 1, Head.class, (e) -> {
            e.forEach(l -> {
                saxQueue.add(l);
                countDownLatch.countDown();
            });
        });
        countDownLatch.await();
        List<Head> data = new LinkedList<>(saxQueue);
        assertData(data);
    }

    @Test
    public void testSaxReadWithHead() throws FileNotFoundException, InterruptedException {
        File file = new File("Test-Excel.xlsx");
        FileInputStream fileInputStream = new FileInputStream(file);
        CountDownLatch countDownLatch = new CountDownLatch(10000);
        ConcurrentLinkedQueue<Head> saxQueue = new ConcurrentLinkedQueue<>();
        ExcelUtil.saxReadFromExcel(fileInputStream, 0, Head.class, (e) -> {
            e.forEach(l -> {
                saxQueue.add(l);
                countDownLatch.countDown();
            });
        });
        countDownLatch.await();
        List<Head> data = new LinkedList<>(saxQueue);
        assertData(data);
    }

    private void assertData(List<Head> data) {
        Assertions.assertEquals(data.size(), 10000);
        for (int i = 0; i < data.size(); i++) {
            String s = i + "";
            Assertions.assertEquals(s, data.get(i).getFirstLine());
            Assertions.assertEquals(s, data.get(i).getSecondLine());
            Assertions.assertEquals(s, data.get(i).getThirdLine());
        }
        data = null;
    }

    @AfterAll
    public static void delete() {
        File file = new File("Test-Excel.xlsx");
        file.delete();
    }
}
