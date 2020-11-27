package com.droukos.cdnservice.util;

import java.io.File;

import static com.droukos.cdnservice.environment.security.HttpExceptionFactory.badRequest;
import static com.droukos.cdnservice.environment.security.HttpExceptionFactory.internalError;

public class FilesUtil {
    private FilesUtil() {
    }

    private static String checkWhetherFolderExist(String folderPath) {
        File directory = new File(folderPath);
        if (!directory.exists() && !directory.mkdir()) throw internalError();
        return folderPath;
    }

    public static String tempPathForAedDevicePic() {
        String folderPath = System.getProperty("user.dir") + "\\tmp\\device\\";
        return checkWhetherFolderExist(folderPath);
    }

    public static String tempPathForAedAddressPic() {
        String folderPath = System.getProperty("user.dir") + "\\tmp\\address\\";
        return checkWhetherFolderExist(folderPath);
    }

    public static String tempPathForAvatarPic() {
        String folderPath = System.getProperty("user.dir") + "\\tmp\\avatar\\";
        return checkWhetherFolderExist(folderPath);
    }
}
