package io.skymind.pathmind.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@RunWith(MockitoJUnitRunner.class)
public class FileUtilsTest {

    private String validPath = FileUtils.getSystemIndependentFilePath("./src/test/resources/static/Test_Class_Files/model");
    private String inValidPath = FileUtils.getSystemIndependentFilePath("./src/test/resources/static/model");

    @InjectMocks
    FileUtils fileUtils;

    @Test
    public void testListFilesSuccess() {
        List<String> expectedList = new ArrayList<>();
        expectedList.add(FileUtils.getSystemIndependentFilePath("./src/test/resources/static/Test_Class_Files/model/coffeeshop/Simulation.class"));
        List<String> fileList = fileUtils.listFiles(validPath);
        assertThat(fileList, is(equalTo(expectedList)));
    }

    @Test
    public void testListFilesFail() {
        Logger fileLogger = (Logger) LoggerFactory.getLogger(FileUtils.class);
        List<String> expectedList = new ArrayList<>();
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        fileLogger.addAppender(listAppender);
        List<String> fileList = fileUtils.listFiles(inValidPath);
        List<ILoggingEvent> logsList = listAppender.list;
        assertThat(logsList.get(0).getLevel(),is(equalTo(Level.ERROR)));
        assertThat(logsList.get(0).getMessage(),is(equalTo("Invalid input file path")));
        assertThat(fileList, is(equalTo(expectedList)));
    }
}