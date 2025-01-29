package com.traveloffersystem.utils;

import java.io.*;

public class FileTextUtils {

    /**
     * 将文本内容写入指定文件
     * @param filePath 文件完整路径
     * @param content 要写入的文本
     * @throws IOException
     */
    public static void writeTextFile(String filePath, String content) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(content);
        }
    }

    /**
     * 从指定文件读取文本
     * @param filePath 文件完整路径
     * @return 文本内容
     * @throws IOException
     */
    public static String readTextFile(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString().trim();
    }
}
