package service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import model.FileMessage;
import util.FileUtils;

public class SplitService {

    public ArrayList<FileMessage> split(FileMessage fileMessage) {
        byte[] content = fileMessage.getContent();
        int fileSize = fileMessage.getFileSize();
        String filename = fileMessage.getFilename();
        Date createdDate = fileMessage.getCreatedDate();
        String fileType = fileMessage.getFileType();
        String originPath = fileMessage.getOriginPath();
        String owner = fileMessage.getOwner();

        byte[] c1 = Arrays.copyOfRange(content, 0, (fileSize + 1) / 2);
        byte[] c2 = Arrays.copyOfRange(content, (fileSize + 1) / 2, fileSize);

        FileMessage f1 = new FileMessage();
        f1.setContent(c1);
        f1.setFilename(filename + "_1");
        f1.setCreatedDate(createdDate);
        f1.setFileType(fileType);
        f1.setOriginPath(originPath);
        f1.setFileSize(fileSize / 2);
        f1.setOwner(owner);

        FileMessage f2 = new FileMessage();
        f2.setContent(c2);
        f2.setFilename(filename + "_2");
        f2.setCreatedDate(createdDate);
        f2.setFileType(fileType);
        f2.setOriginPath(originPath);
        f2.setFileSize(fileSize / 2);
        f2.setOwner(owner);

        ArrayList<FileMessage> files = new ArrayList<>();
        files.add(f1);
        files.add(f2);

        return files;
    }

    public FileMessage merge(FileMessage f1, FileMessage f2) throws IOException {
        int fileSize = f1.getFileSize() + f2.getFileSize();
        String filename = FileUtils.getFilenameWithoutServerNum(f1.getFilename());
        Date createdDate = f1.getCreatedDate();
        String fileType = f1.getFileType();
        String originPath = FileUtils.getOriginPathWithoutServerNum(f1.getOriginPath(), fileType);
        String owner = f1.getOwner();

        byte[] c1 = f1.getContent();
        byte[] c2 = f2.getContent();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(c1);
        output.write(c2);
        byte[] content = output.toByteArray();

        FileMessage fm = new FileMessage();
        fm.setContent(content);
        fm.setFilename(filename);
        fm.setCreatedDate(createdDate);
        fm.setFileType(fileType);
        fm.setOriginPath(originPath);
        fm.setFileSize(fileSize);
        fm.setOwner(owner);

        return fm;
    }
}
