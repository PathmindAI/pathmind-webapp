package io.skymind.pathmind.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
@RunWith(MockitoJUnitRunner.class)
public class FileUtilsTest {

    private String validFile ="./src/test/resources/static/Test_Class_Files/model";

    @InjectMocks
    FileUtils fileUtils;

    @Test
    public void listFiles_Success() {
        List<String> fileList = new ArrayList<>();
        List<String> expectedList = new ArrayList<>();
        expectedList.add("./src/test/resources/static/Test_Class_Files/model/coffeeshop/Chair.class");
        expectedList.add("./src/test/resources/static/Test_Class_Files/model/coffeeshop/Simulation.class");
        fileList = fileUtils.listFiles(validFile);
        assertThat(fileList, is(equalTo(expectedList)));
    }
}