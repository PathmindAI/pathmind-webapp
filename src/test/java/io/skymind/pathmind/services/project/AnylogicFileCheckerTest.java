package io.skymind.pathmind.services.project;

import io.skymind.pathmind.ui.components.status.StatusUpdater;
import io.skymind.pathmind.ui.views.project.NewProjectView;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.io.FileMatchers.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;


@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AnylogicFileCheckerTest {

    private File validFile =new File("./src/test/resources/static/CoffeeShopAnylogicExported.zip");
    private File inValidFile =new File("./src/test/resources/static/CoffeeShop.zip");
    private File invalidFormat = new File("./src/test/resources/static/Deployments_Screen.PNG");
    private static ThreadLocal<File> jarFile = new ThreadLocal<File>();


    AnylogicFileCheckResult anylogicFileCheckResult = new AnylogicFileCheckResult();

    StatusUpdater statusUpdater = new NewProjectView();


    @InjectMocks
    AnylogicFileChecker anylogicFileChecker;


    @Test
    public void performFileCheck_Success() {
        AnylogicFileCheckResult testFileCheckResult = new AnylogicFileCheckResult();
        List<String> definedHelpers = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();

        definedHelpers.add("coffeeshop/Main##pathmindHelper");

        fileNameList.add("database/");
        fileNameList.add("database/db.properties");
        fileNameList.add("database/db.script");
        fileNameList.add("model.jar");

        testFileCheckResult.setFileCheckComplete(true);
        testFileCheckResult.setCorrectFileType(true);
        testFileCheckResult.setModelJarFilePresent(true);
        testFileCheckResult.setDefinedHelpers(definedHelpers);
        testFileCheckResult.setZipContentFileNames(fileNameList);

        FileCheckResult fileCheckResult = anylogicFileChecker.performFileCheck(statusUpdater, validFile);
        assertThat(fileCheckResult.isFileCheckComplete(), is(equalTo(testFileCheckResult.isFileCheckComplete())));
        assertThat(fileCheckResult.isCorrectFileType(), is(equalTo(testFileCheckResult.isCorrectFileType())));
        assertThat(fileCheckResult.isModelJarFilePresent(), is(equalTo(testFileCheckResult.isModelJarFilePresent())));
        assertThat(fileCheckResult.getDefinedHelpers(), is(equalTo(testFileCheckResult.getDefinedHelpers())));
        assertThat(fileCheckResult.getZipContentFileNames(), is(equalTo(testFileCheckResult.getZipContentFileNames())));
    }

    @Test
    public void performFileCheck_Fail() {
        FileCheckResult fileCheckResult = anylogicFileChecker.performFileCheck(statusUpdater,inValidFile);
        assertThat(fileCheckResult.isFileCheckComplete(), is(equalTo(true)));
        assertThat(fileCheckResult.isCorrectFileType(), is(equalTo(false)));
    }

    @Test
    public void TestA_checkZipFile_Success() throws IOException{
        File unZippedJar = anylogicFileChecker.checkZipFile(validFile, anylogicFileCheckResult);
        jarFile.set(unZippedJar);
        System.out.println(jarFile);
        assertThat(unZippedJar,anExistingFileOrDirectory());
        assertThat(unZippedJar, aFileWithCanonicalPath(containsString("model.jar")));
    }

    @Test
    public void TestB_checkZipFile_Fail() throws IOException{
        Logger fileLogger = (Logger) LoggerFactory.getLogger(AnylogicFileChecker.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        fileLogger.addAppender(listAppender);
        anylogicFileChecker.checkZipFile(invalidFormat, anylogicFileCheckResult);
        List<ILoggingEvent> logsList = listAppender.list;
        assertThat(logsList.get(1).getLevel(),is(equalTo(Level.ERROR)));
        assertThat(logsList.get(1).getMessage(),is(equalTo("Invalid input file format :")));
    }

    @Test
    public void TestC_checkJarFile_Success(){
        anylogicFileChecker.checkJarFile(jarFile.get(), anylogicFileCheckResult);
        assertThat(anylogicFileCheckResult.isModelJarFilePresent(), is(equalTo(true)));
    }

    @Test
    public void TestD_checkJarFile_Fail(){
        Logger fileLogger = (Logger) LoggerFactory.getLogger(AnylogicFileChecker.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        fileLogger.addAppender(listAppender);
        anylogicFileChecker.checkJarFile(invalidFormat, anylogicFileCheckResult);
        assertThat(anylogicFileCheckResult.isModelJarFilePresent(), is(equalTo(false)));
        List<ILoggingEvent> logsList = listAppender.list;
        assertThat(logsList.get(1).getLevel(),is(equalTo(Level.ERROR)));
        assertThat(logsList.get(1).getMessage(),is(equalTo("Error opening jar file")));
    }

    @Test
    public void TestE_checkHelpers_Success(){
        AnylogicFileCheckResult testFileCheckResult = new AnylogicFileCheckResult();
        List<String> definedHelpers = new ArrayList<>();
        definedHelpers.add("coffeeshop/Main##pathmindHelper");
        testFileCheckResult.setDefinedHelpers(definedHelpers);

        anylogicFileChecker.checkHelpers(jarFile.get(), anylogicFileCheckResult);
        assertThat(anylogicFileCheckResult.getDefinedHelpers(), is(equalTo(testFileCheckResult.getDefinedHelpers())));
    }

    @Test
    public void TestF_checkHelpers_Fail(){
        Logger fileLogger = (Logger) LoggerFactory.getLogger(AnylogicFileChecker.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        List<String> definedHelpers = new ArrayList<>();
        listAppender.start();
        fileLogger.addAppender(listAppender);
        anylogicFileChecker.checkHelpers(invalidFormat, anylogicFileCheckResult);
        List<ILoggingEvent> logsList = listAppender.list;
        assertThat(anylogicFileCheckResult.getDefinedHelpers(), is(equalTo(definedHelpers)));
        assertThat(logsList.get(2).getLevel(),is(equalTo(Level.ERROR)));
        assertThat(logsList.get(2).getMessage(),is(equalTo("error while extract jar files")));
    }
}