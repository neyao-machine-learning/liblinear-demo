package de.bwaldvogel.liblinear.util;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by yaonengjun on 2017/6/29 下午3:10.
 */
public class FileHelper {



  private static final int BUFFER_SIZE = 16 * 1024;

  private static final String BOM_UTF32_BE = "0000FEFF";
  private static final String BOM_UTF32_LE = "FFFE0000";
  private static final String BOM_UTF16_BE = "FEFF"; // utf-16 big-endian
  private static final String BOM_UTF16_LE = "FFFE";
  private static final String BOM_UTF8 = "EFBBBF";

  public static boolean copyFile(File src, File dst) {
    int size = BUFFER_SIZE;

    InputStream in = null;
    OutputStream out = null;
    try {
      in = new BufferedInputStream(new FileInputStream(src), size);
      out = new BufferedOutputStream(new FileOutputStream(dst), size);
      byte[] buffer = new byte[size];
      while (in.read(buffer) > 0) {
        out.write(buffer);
      }
    } catch (Exception e) {
      return false;
    } finally {
      try {
        if (in != null) {
          in.close();
        }
        if (out != null) {
          out.close();
        }
      } catch (IOException e) {
        // ignore the exception
      }
    }

    return true;
  }

  public static boolean writeFile(File dst, byte[] buffer) {
    if (buffer == null) {
      return false;
    }
    OutputStream out = null;
    try {
      out = new BufferedOutputStream(new FileOutputStream(dst));
      out.write(buffer);
    } catch (Exception e) {
      return false;
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (IOException e) {
          // ignore the exception
        }
      }
    }

    return true;
  }

  /**
   * 读取一个文件的每一行到List
   *
   * @return
   * @throws IOException
   */
//  public static List<String> readFileToLines(String filePath) throws IOException {
//    ClassPathResource cpr = new ClassPathResource(filePath);
//    BufferedReader reader = null;
//    List<String> lines = new ArrayList<String>();
//    try {
//      reader = new BufferedReader(new InputStreamReader(cpr.getInputStream()));
//
//      String line;
//      while ((line = reader.readLine()) != null) {
//        lines.add(line);
//      }
//    } finally {
//      if (reader != null) {
//        try {
//          reader.close();
//        } catch (IOException e) {
//          // ignore the exception
//        }
//      }
//    }
//
//    return lines;
//  }

//  /**
//   * 读取一个文件，并整合到一个String对象
//   *
//   * @param filePath
//   * @return
//   * @throws IOException
//   */
//  public static String readFileToLine(String filePath) throws IOException {
//    ClassPathResource cpr = new ClassPathResource(filePath);
//    StringBuilder builder = new StringBuilder();
//    BufferedReader reader = null;
//    try {
//      reader = new BufferedReader(new InputStreamReader(cpr.getInputStream()));
//
//      String line;
//      while ((line = reader.readLine()) != null) {
//        builder.append(line);
//      }
//    } finally {
//      if (reader != null) {
//        try {
//          reader.close();
//        } catch (IOException e) {
//          // ignore the exception
//        }
//      }
//    }
//    return builder.toString();
//  }

  public static LinkedHashMap<String, String> readDict(BufferedReader reader) {
    LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
    String s;
    String[] array;
    try {
      while ((s = reader.readLine()) != null) {
        array = s.split("\t");
        if (array == null || array.length < 2)
          continue;
        map.put(array[0], array[1].split(" ")[0]);
      }
    } catch (IOException e) {
      return null;
    }
    return map;
  }

  // check whether the file the url points to exists
  public static boolean exist(String urlStr) {
    try {
      URL url = new URL(urlStr);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("HEAD");
      return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
    } catch (Exception e) {
      // ignore the exception
    }

    return false;
  }

  public static String getFileExtension(String fileName) {
    String extensionName;
    String[] flds = fileName.split("\\.");
    if (flds.length >= 2) {
      extensionName = flds[flds.length - 1];
    } else {
      extensionName = "";
    }
    return extensionName.toLowerCase();
  }

  /**
   * 合并两个文件
   *
   * @param destFilePath
   * @param srcFilePath
   * @return created by wangyu on 2015年11月4日 下午2:28:04
   */
  public static boolean mergeFile(String destFilePath, String srcFilePath) {
    BufferedOutputStream bos = null;
    FileInputStream input = null;
    try {
      File destFile = new File(destFilePath);
      File srcFile = new File(srcFilePath);
      if (!destFile.exists()) {
        return false;
      }
      if (!srcFile.exists()) {
        return true;
      }
      input = new FileInputStream(srcFilePath);
      bos = new BufferedOutputStream(new FileOutputStream(destFile, true));
      byte[] buffer = new byte[8192];
      int length = -1;
      while (true) {
        length = input.read(buffer);
        if (length == -1)
          break;
        bos.write(buffer, 0, length);
      }
      return true;
    } catch (Exception e) {
      return false;
    } finally {
      if (bos != null) {
        try {
          bos.close();
        } catch (IOException e) {
          return false;
        }
      }
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          return false;
        }
      }
    }
  }

  /**
   * 删除文件/文件夹
   *
   * @param filePath
   */
  public static void delete(String filePath) {
    File file = new File(filePath);
    if (file.exists()) {
      if (file.isFile()) {
        deleteFile(file);
      } else if (file.isDirectory()) {
        deleteDir(file);
      }
    }
  }

  private static void deleteFile(File file) {
    if (file.exists() && file.isFile()) {
      file.delete();
    }
  }

  private static void deleteDir(File dir) {
    if (dir.exists() && dir.isDirectory()) {
      File[] files = dir.listFiles();
      if (null != files) {
        for (File file : files) {
          if (file.exists() && file.isFile()) {
            deleteFile(file);
          } else if (file.exists() && file.isDirectory()) {
            deleteDir(file);
          }
        }
      }
      dir.delete();
    }
  }


  public static void appendToFile(String fileName, String line) {
    if(StringUtils.isBlank(line))
      return;

    appendToFile(fileName, Arrays.asList(line));
  }

  /**
   * @param fileName 文件名全路径,如果文件对应的文件名不存在会创建
   * @param lines
   * @throws IOException
   */
  public static void appendToFile(String fileName, List<String> lines) {
    if (fileName == null || "".equals(fileName))
      return;

    if (lines == null || lines.size() == 0) {
      return;
    }

    File file = new File(fileName);
    BufferedWriter bw = null;
    FileWriter fw = null;
    try {
      if (file.exists() == false) {
        boolean result = file.createNewFile();
        if (result)
          file = new File(fileName);
      }

      fw = new FileWriter(file.getAbsoluteFile(), true);
      bw = new BufferedWriter(fw);
      for (String line : lines) {
        bw.write(line);
        bw.write("\r\n");
      }

//      bw.flush();
//      fw.flush();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {


      try {

        if (bw != null)
          bw.close();
        if (fw != null)
          fw.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }
}
