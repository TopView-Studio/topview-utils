package file.upload;

import com.topview.utils.file.upload.impl.LocalUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * @author Albumen
 * @date 2020/1/25
 */
@SpringBootTest
@SpringBootApplication
@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = "com.topview.utils")
public class TestFileUtil {
    @Autowired
    private LocalUpload localUpload;

    @Test
    public void testSave() throws IOException {
        File file = new File("test.ttt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        IOUtils.write(RandomStringUtils.randomAscii(10000), fileOutputStream, StandardCharsets.UTF_8);
        fileOutputStream.close();

        MultipartFile multipartFile = new MockMultipartFile("test.ttt", "test.ttt", "ttt", new FileInputStream(file));
        String uploadOnly = localUpload.upload(multipartFile);
        Assert.assertTrue(StringUtils.isNotEmpty(uploadOnly));

        String uploadWithFolder = localUpload.upload(multipartFile, System.getProperty("java.io.tmpdir"));
        Assert.assertTrue(uploadWithFolder.contains(System.getProperty("java.io.tmpdir")));

        String filename = System.getProperty("java.io.tmpdir") + "test.test";
        String uploadWithFilename = localUpload.upload(multipartFile, filename);
        Assert.assertEquals(uploadWithFilename, filename);

        MultipartFile[] multipartFiles = new MultipartFile[2];
        multipartFiles[0] = multipartFile;
        multipartFiles[1] = multipartFile;

        String[] uploadArrayOnly = localUpload.upload(multipartFiles);
        Assert.assertTrue(StringUtils.isNotEmpty(uploadArrayOnly[0]));
        Assert.assertTrue(StringUtils.isNotEmpty(uploadArrayOnly[1]));

        String[] uploadArrayFolder = localUpload.upload(multipartFiles, System.getProperty("java.io.tmpdir"));
        Assert.assertTrue(uploadArrayFolder[0].contains(System.getProperty("java.io.tmpdir")));
        Assert.assertTrue(uploadArrayFolder[1].contains(System.getProperty("java.io.tmpdir")));

        new File(uploadOnly).delete();
        new File(uploadWithFolder).delete();
        new File(uploadWithFilename).delete();
        new File(uploadArrayOnly[0]).delete();
        new File(uploadArrayOnly[1]).delete();
        new File(uploadArrayFolder[0]).delete();
        new File(uploadArrayFolder[1]).delete();
        file.delete();
    }
}
