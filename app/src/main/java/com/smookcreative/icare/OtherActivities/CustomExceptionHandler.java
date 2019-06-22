package com.smookcreative.icare.OtherActivities;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.security.Timestamp;

public class CustomExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler defaultUEH;
    private String localPath;
    private String url;

    public CustomExceptionHandler(String localPath, String url) {
        this.localPath = localPath;
        this.url = url;
        this.defaultUEH =Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        //String timestamp = Timestamp.getInstance().getTimestamp();
        final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            e.printStackTrace(printWriter);
            String stactrace = result.toString();
            printWriter.close();
            //String filename = timestamp+".stacktrace";

    }



}
