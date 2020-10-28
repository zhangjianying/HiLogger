package com.talkweb.logger.imp;

import com.talkweb.logger.LogStrategy;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.talkweb.logger.Utils.*;

public class DiskLogStrategy implements LogStrategy {
    private final WriteHandler handler;

    public DiskLogStrategy(WriteHandler handler) {
        this.handler = requireNonNull(handler);
    }

    @Override
    public void log(int level, String tag, String message) {
        requireNonNull(message);
        //发送给后台线程 写文件
        Object object = message;
        InnerEvent writeFileInnerEvent = InnerEvent.get(0, 0, object);
        handler.sendEvent(writeFileInnerEvent, 2, EventHandler.Priority.IMMEDIATE);
    }

    static class WriteHandler extends EventHandler {

        private String folder;
        private int maxFileSize;

        public WriteHandler(EventRunner runner, String folder, int maxFileSize) throws IllegalArgumentException {
            super(runner);
            this.folder = requireNonNull(folder);
            this.maxFileSize = requireNonNull(maxFileSize);
        }

        @Override
        public void processEvent(InnerEvent event) {
            super.processEvent(event);
            if (isNull(event)) {
                return;
            }

            int eventId = event.eventId;
            String message = (String) event.object;
            handleMessage(message);
        }

        public void handleMessage(String content) {
            FileWriter fileWriter = null;
            File logFile = getLogFile(folder, "logs");
            try {
                fileWriter = new FileWriter(logFile, true);

                writeLog(fileWriter, content);

                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (nonNull(fileWriter)) {
                    try {
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }

        private void writeLog(FileWriter fileWriter, String content) throws IOException {
            requireNonNull(fileWriter);
            requireNonNull(content);

            fileWriter.append(content);
        }


        private File getLogFile(String folderName, String fileName) {
            requireNonNull(folderName);
            requireNonNull(fileName);

            File folder = new File(folderName);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            int newFileCount = 0;
            File newFile;
            File existingFile = null;

            newFile = new File(folder, String.format("%s_%s.csv", fileName, newFileCount));
            while (newFile.exists()) {
                existingFile = newFile;
                newFileCount++;
                newFile = new File(folder, String.format("%s_%s.csv", fileName, newFileCount));
            }

            if (nonNull(existingFile)) {
                if (existingFile.length() >= maxFileSize) {
                    return newFile;
                }
                return existingFile;
            }

            return newFile;
        }

    }

}